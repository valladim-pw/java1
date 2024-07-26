package MavenEmailPlugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.google.api.client.http.FileContent;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;

@Mojo(name = "sendmail", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class SendmailMojo extends AbstractMojo {
	
	@Parameter(defaultValue = "${project.build.directory}", property = "outputDir", required = true)
	private File outputDirectory;
	@Parameter(defaultValue = "vkodoch@mail.ru", property = "emailTo", required = true)
	private String emailTo;
	@Parameter(defaultValue = "vkodoch@gmail.com", property = "emailFrom", required = true)
	private String emailFrom;
	@Parameter(defaultValue = "smtp.gmail.com", property = "emailHost", required = true)
	private String emailHost;
	@Parameter(defaultValue = "465", property = "emailPort", required = true)
	private int emailPort;
	@Parameter(defaultValue = "true", property = "emailAuth", required = true)
	private boolean emailAuth;
	@Parameter(defaultValue = "true", property = "emailSSL", required = true)
	private boolean emailSSL;
	
	private static GoogleDriveAccess drive;
	
	SendmailMojo() {
		//Создаем экземпляр класса GoogleDriveAccess, в котором получены необходимые разрешения на обработку файлов
		// на Google Диске
		drive = new GoogleDriveAccess();
	}
	
	// Свойство buggyLoger - временное решение для бага в com.google.api.client.util.store.FileDataStoreFactory
	// setPermissionsToOwnerOnly под Windows при работе с Google Drive API
	//https://github.com/googleapis/google-http-java-client/issues/315
	final java.util.logging.Logger buggyLogger = java.util.logging.Logger.getLogger(FileDataStoreFactory.class.getName());
	
	// В методе getJarFile обходим директорию target и возвращаем путь к jar-файлу c зависимостями, созданному с помощью assembly-plugin,
	// который в случае создания двух jar-файлов (с помощью jar-plugin и assembly-plugin) будет более поздним по времени создания
	// и соответственно будет последним в списке jars, отсортированном по времени.
	// В случае отсутствия jar-файла получаем пустую строку.
	
	static String getJarFile(String directoryName) throws IOException {
		final List<Path> jars = new LinkedList<>();
		final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:**/*.jar");
		Set<FileVisitOption> set = new HashSet<>();
		Files.walkFileTree(Paths.get(directoryName), set, 1, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
				if (pathMatcher.matches(path) && !Files.isDirectory(path)) {
					jars.add(path);
					jars.sort(
									(a, b) -> {
										try {
											return Long.compare(Files.getLastModifiedTime(a).toMillis(), Files.getLastModifiedTime(b).toMillis());
										} catch (IOException e) {
											e.printStackTrace();
										}
										return 0;
									});
				}
				return FileVisitResult.CONTINUE;
			}
			
			@Override
			public FileVisitResult visitFileFailed(Path file, IOException e) {
				return FileVisitResult.CONTINUE;
			}
		});
		return ((LinkedList<Path>) jars).getLast().toString();
	}
	
	// В методе createMailReport создаем и возвращаем строку с отчетом для отправки по почте.
	//  Для этого путем обхода ищем в архиве, путь к которому получен в методе getJarFile, файл MAINIFEST
	// считываем его содержимое и на его основе возвращаем строку с отчетом.
	// Если путь к архиву пуст, создаем отчет о неудаче.
	
	static String createMailReport(String jarFilePath) throws IOException {
		String mf = "";
		String report = "";
		Date date = new Date();
		String caption = "REPORT " + date.toString() + "\n";
		String underline = "--------------------------------------------------" + "\n";
		String header = caption + underline;
		String success = "Archive file successfully created \n\n";
		String failure = "Archive file has not been created";
		
		if (!jarFilePath.isEmpty()) {
			
			try (ZipFile zipFile = new ZipFile(jarFilePath)) {
				Enumeration<? extends ZipEntry> entries = zipFile.entries();
				while (entries.hasMoreElements()) {
					ZipEntry entry = entries.nextElement();
					if (!entry.isDirectory() && entry.getName().toLowerCase().contains("manifest")) {
						try (InputStream inputStream = zipFile.getInputStream(entry);
						     Scanner scanner = new Scanner(inputStream)) {
							
							while (scanner.hasNextLine()) {
								String line = scanner.nextLine();
								mf += line + "\n";
							}
						}
					}
				}
			}
			report = header + success + mf;
		} else {
			report = header + failure;
		}
		return report;
	}
	/*---------------------------------------------------------------------------------------------------------
	* !!! Политика безопасности Google запрещает прикреплять к письмам отправляемым с сервера gmail.com
	* файлы с рядом расширений, в том числе архивы с расширениями .zip, .gZip, .jar
	* В эту группу не входят архивы с раширением .rar, эти файлы отправляются (но только нативный файлы, созданные на WinRAR)!
	* Поскольку rar - это проприетарный формат сжатия данных (то есть программное обеспечение находится в собственности авторов)
	* воспороизвести его программными средствами не получится.
	* Рабочий вариант - предварительно отправить jar-файл на Google Диск и прикрепить к письму ссылку на архив на Google Диске.
	* Для этой цели созданы класс GoogleDriveAccess и метод getGoogleDriveJarFileLink.
	*----------------------------------------------------------------------------------------------------------
	*/
	
	// В методе getGoogleDriveJarFileLink мы используем классы и методы Google Drive API.
	// Создаем экземпляр класса Drive - service, которому присваиваем значение экземпляра класса GoogleDriveAccess drive.
	// drive.getDriveService() уже имеет полномочия необходимые нашему плагину (приложению) для обработки файлов на Google Диске
	// (эти полномочия (credentials) получены в классе GoogleDriveAccess в процессе обработки авторизации и
	// верификации приложения на Google Диске)
	// С помощью service:
	// - во-первых создаем и выполняем запрос на поиск на Диске файла соответствующего названию нашего jar-файла и
	// при наличии такого файла запрос на его удаление (чтобы избежать переполнения Диска устаревшими версиями архива)
	// - во-вторых выполняем запрос на создание копии нашего архива на Диске
	// - в-третьих, используя класс Permission, выполняем запрос на предоставление ссылке на копию нашего архива на Диске
	// разрешения для любого пользователя, получившего ссылку, скачать по ней архив и ознакомиться с его содержанием
	// Метод возвращает строку со ссылкой на наш jar-файл на Google Диске.
	static String getGoogleDriveJarFileLink(String jarPath) throws  IOException {
		
		String jarFileLink = "";
		// создаем экземпляр пустого файла согласно модели, определенной в Google Drive API
		com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
		
		// drive - ссылка на инициализированный в конструкторе класса SendmailMojo класс GoogleDriveAccess,
		// getDriveAccess возвращает полномочия (credentials) приложения для обработки файлов на Диске
		// создаем ссылку service класса Drive для работы с файлами на Диске
		Drive service = drive.getDriveService();
		// присваиваем пустому файлу имя нашего jar-файла
		java.io.File filePath = new java.io.File(SendmailMojo.getJarFile(jarPath));
		fileMetadata.setName(filePath.getName());
		// метка страницы Диска
		String pageToken = null;
		// запрос на поиск файла с условиями: имя файла (имя нашего архива) + не является каталогом
		String query = " name contains '" + filePath.getName() + "' "
						               + " and mimeType != 'application/vnd.google-apps.folder' ";
		// цикл для обхода страниц Диска по меткам
		do {
			// создание и исполнение запроса на поиск определенного условиями файла в списке файлов на странице Диска
			FileList result = service.files().list().setQ(query).setSpaces("drive")
							                  .setFields("nextPageToken, files(id, name, webContentLink, webViewLink)")
							                  .setPageToken(pageToken).execute();
			
			for (com.google.api.services.drive.model.File f : result.getFiles()) {
				// создание и исполнение запроса на удаление с Диска файла, соответствующего условиям поиска
				Drive.Files.Delete request = service.files().delete(f.getId());
				request.execute();
			}
			pageToken = result.getNextPageToken();
		} while (pageToken != null);
		// создание и исполнение запроса на создание на Диске копии нашего архива путем наполнения созданного
		// ранее пустого файла контентом нашего архива.
		// setFields определяет поля, которые можно будет у этой копии получить,
		// например, по webViewLink можно получить ссылку на файл на Диске, по которой можно открыть его
		// на web-странице и оттуда скачать
		FileContent mediaContent = new FileContent("application/java-archive", filePath);
		com.google.api.services.drive.model.File file = service.files().create(fileMetadata, mediaContent)
						                                                .setFields("id, name, webContentLink, webViewLink")
						                                                .execute();
		// в экземпляре класса Permission устанавливаем разрешения для любого пользователя на чтение и запись файла,
		Permission permission = new Permission();
		permission.setType("anyone");
		permission.setRole("writer");
		// исполнение запроса на присвоение созданной на Диске копии архива (по id) установленного разрешения
		service.permissions().create(file.getId(), permission).execute();
		// получаем и присваиваем строке значение web-ссылки на копию нашего архива на Диске
		jarFileLink = file.getWebViewLink();
		
		return jarFileLink;
	}
	
	public void execute() throws MojoExecutionException {
		// Свойство buggyLoger - временное решение для бага в com.google.api.client.util.store.FileDataStoreFactory
		// setPermissionsToOwnerOnly под Windows при использовании Google Drive API
		//https://github.com/googleapis/google-http-java-client/issues/315
		buggyLogger.setLevel(java.util.logging.Level.SEVERE);
		// Устанавливаем  свойства определенные с помощью параметров Mojo-класса и в конфигурации плагина в pom.xml
		File target = outputDirectory;
		// Используя метод из класса GoogleDriveAccess даем возможность ввести вручную на консоли email адресата письма
		String to = drive.readInputLine("\nНапишите здесь email-адрес, куда нужно отправить отчет:");
		String from = emailFrom;
		String host = emailHost;
		int port = emailPort;
		boolean auth = emailAuth;
		boolean ssl = emailSSL;
		
		final String username = "vkodoch@gmail.com";
		// Для отправки почты програмным путем в своем Google Account нужно создавть 16-символьный пароль для приложений
		//!!!Это НЕ пароль для почты
		/*
		- Перейдите в свой аккаунт Google.
		- Выберите раздел "Безопасность".
		- В разделе "Вход в Google" нажмите "Двухэтапная проверка".
		- Внизу страницы найдите и нажмите "Пароли для приложений".
		- Укажите имя, которое поможет вам запомнить, где вы собираетесь использовать пароль приложения.
		- Нажмите "Сгенерировать".
		Чтобы использовать пароль приложения, следуйте инструкциям на экране.
		Пароль приложения - это 16-значный код, сгенерированный на вашем устройстве.
		Нажмите "Готово".
		*/
		final String password = "fnefzuokbwfgjcmi";
		
		if (!target.exists()) {
			target.mkdirs();
		}
		// Устанавливаем системные свойства почтового сервера
		Properties properties = System.getProperties();
		
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.auth", auth);
		properties.put("mail.smtp.ssl.enable", ssl);
		
		// Получаем объект Session с именем пользователя и паролем  и передаем
		Session session = Session.getInstance(properties,
						new javax.mail.Authenticator() {
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(username, password);
							}
						});
		// Логирование отправки почты
		session.setDebug(true);
		try {
			// Создаем объект письма, где устанавливаем заголовки
			MimeMessage message = new MimeMessage(session);
			// От кого: почтовый адрес
			message.setFrom(new InternetAddress(from));
			// Кому:
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			// Тема письма
			message.setSubject("Create jar file");
			// Создаем объекты для отправки письма со ссылкой на файл на Google Диске
			Multipart multipart = new MimeMultipart();
			MimeBodyPart linkPart = new MimeBodyPart();
			MimeBodyPart textPart = new MimeBodyPart();
			
			try {
				// С помощью методов класса получаем объекты отчета и ссылки на файл на Google Диске
				String jarFile = SendmailMojo.getJarFile(target.getPath());
				String report = SendmailMojo.createMailReport(jarFile);
				String webLink = SendmailMojo.getGoogleDriveJarFileLink(target.getPath());
				Path jarPath = Paths.get(jarFile);
				String jarName = jarPath.getFileName().toString();
				// Делаем ссылку ведущую к файлу на Google Диске в формате HTML
				linkPart.setContent(
								"<a href='" + webLink + "'>"+ jarName + "</a>",
								"text/html");
				textPart.setText(report);
				multipart.addBodyPart(textPart);
				multipart.addBodyPart(linkPart);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Отправляем письмо
			message.setContent(multipart);
			System.out.println("\n Отправка письма...");
			Transport.send(message);
			System.out.println("Письмо успешно доставлено адресату....");
			
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}
