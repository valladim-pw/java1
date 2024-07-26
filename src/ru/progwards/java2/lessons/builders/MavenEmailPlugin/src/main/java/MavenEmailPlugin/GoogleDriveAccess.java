package MavenEmailPlugin;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential.Builder;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/*---------------------------------------------------------------------------------------------------------
 * Класс GoogleDriveAccess позволяет плагину (приложению) получить доступ к операциям на Google Диске
 * через процедуру авторизации и верификации
 *----------------------------------------------------------------------------------------------------------
 */

public final class GoogleDriveAccess {
	// Чтобы плагин (приложение) получило возможность манипулировать файлами на пространстве Google Диска,
	// которое выделяется для аккаунта Google (в данном случае это мой аккаунт vkodoch@gmail.com)
	// предварительно нужно пройти разовую процедуру регистрации приложения на Google API Console, в результате которой
	// предоставляется файл с полномочиями (Credentials) - client_secret.json
	// пример процесса можно посмотреть здесь: https://betacode.net/11917/create-credentials-for-google-drive-api
	// Возможны разные варианты авторизации и верификации приложения:
	// - можно положить секретный файл (client_secret.json) где-либо на компьютере или в сети и считывать данные из него
	// - в данном классе значения CLIENT_ID и CLIENT_SECRET уже взяты из секретного файла и являются свойства класса,
	// а процесс авторизации и верификации проходит в режиме онлайн.
	private static final String CLIENT_ID = "568884029640-ljk5jgp02u6jnl3866hsfvg4ifkd1iaf.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "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";
	// адрес перенаправления предоставляемый Google API для внеполосной (Out Of Band) авторизации,
	// т.е. копирования и вставки вручную.
	// Метод устаревший но наглядный и для учебного приложения вполне подходит
	private static final String CALLBACK_URL = "urn:ietf:wg:oauth:2.0:oob";
	private static HttpTransport httpTransport;
	private static JsonFactory jsonFactory;
	private static Drive service;
	
	// INIT
	GoogleDriveAccess() {
		httpTransport = new NetHttpTransport();
		jsonFactory = new JacksonFactory();
	}
	// Метод создает и возвращает экземпляр GoogleClientSecrets, содержащий CLIENT_ID и CLIENT_SECRET
	private static GoogleClientSecrets createClientSecrets() {
		Details webSecrets = new Details();
		webSecrets.setClientId(CLIENT_ID);
		webSecrets.setClientSecret(CLIENT_SECRET);
		
		GoogleClientSecrets clientSecrets = new GoogleClientSecrets();
		clientSecrets.setWeb(webSecrets);
		
		return clientSecrets;
	}
	// Метод конструирует создание экземпляра GoogleCredential используя в качестве аргументов секретные данные приложения
	// и строку с кодом из ответа сервера
	private static GoogleCredential buildCredential(GoogleClientSecrets clientSecrets,
	                                                GoogleTokenResponse tokenResponse) {
		GoogleCredential credential = new Builder().setTransport(httpTransport)
						                              .setJsonFactory(jsonFactory).setClientSecrets(clientSecrets).build();
		
		credential.setFromTokenResponse(tokenResponse);
		return credential;
	}
	// Вспомогательный метод для считывания строки введенной вручную в консоли
	static String readInputLine(String prompt) {
		System.out.println(prompt);
		return new Scanner(System.in).nextLine();
	}
	// В методе на основе секретных данных приложения создаются и обрабатываются запросы к серверу Google API
	// для получения разового кода авторизации.
	// Метод возвращает разовый код верификации полученный из ответа сервера
	public static Credential getCredentials() throws IOException {
		// создаем экземпляр с секретными данными приложения
		GoogleClientSecrets clientSecrets = createClientSecrets();
		// создаем экземпляр процесса авторизации на основе секретных данных с областью применения DRIVE (Google Drive API)
		GoogleAuthorizationCodeFlow authorizationFlow = new GoogleAuthorizationCodeFlow.Builder(
						httpTransport, jsonFactory, clientSecrets, Arrays.asList(DriveScopes.DRIVE))
						                                                .setApprovalPrompt("force")
						                                                .setAccessType("offline")
						                                                .build();
		// Получеам ссылку на web-страницы авторизации Google, на которых в несколько этапов требуется ручное подтверждение
		// полномочий приложения и после подтверждения выдается разовый код авторизации для ручного копирования
		String authorizeUrl = authorizationFlow.newAuthorizationUrl().setRedirectUri(CALLBACK_URL).build();
		System.out.println("\nОткройте эту ссылку в браузере и следуйте инструкциям ниже ссылки: \n" + authorizeUrl + "\n");
		String authorizationCode = readInputLine("1. На странице \"Choose an account\" выбираете Vadim Kodochigov,\n" +
						                                        "2. На двух следующих страницах нажимаете кнопку \"Продолжить\",\n" +
						                                        "3. На странице, где есть \"Код авторизации\", копируете код и вставляете его (Ctrl + V) здесь:");
		// создаем запрос на основе разового кода авторизации и обрабатываем ответ на этот запрос
		GoogleAuthorizationCodeTokenRequest tokenRequest = authorizationFlow.newTokenRequest(authorizationCode);
		tokenRequest.setRedirectUri(CALLBACK_URL);
		GoogleTokenResponse tokenResponse = tokenRequest.execute();
		// после ручного ввода в консоли кода авторизации получаем разовый код верификации который предоставляет приложению
		// полномочия, необходимые для работы с файлами на Google Диске
		GoogleCredential credential = buildCredential(clientSecrets, tokenResponse);
		System.out.println("\nВаш код верификации:\n" + credential.getRefreshToken());
		return credential;
	}
	// На основе полученных полномочий возвращается экземпляр класса Drive для операций с файлами на Диске
	public static Drive getDriveService() throws IOException {
		
		if (service != null) {
			return service;
		}
		Credential credential = getCredentials();
		
		service = new Drive.Builder(httpTransport, jsonFactory, credential)
						          .setApplicationName("Email Maven Plugin").build();
		return service;
	}
}
