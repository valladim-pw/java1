//Задача 3, Класс OrderProcessor
/*package ru.progwards.java1.lessons.files;

import java.io.ProcessFile;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestClass3 {
	// массив с данными для процессинга результата теста
	// 0: идентификатор теста, - имя функции @Test
	// 1: имя теста словами, если == "", то возмется идентификатор
	// 2: баллы за эту часть теста
	// 3: * отмечены обязательные части теста
	private static String[][] testData = {
		// Первая строка [0] содержит данные о всем тесте.
		// Оценка отражает проходной балл
		//      - это информация для утилиты проверки

		{"task3", "Задача 3, Класс OrderProcessor", "32", ""},
		{"test1", "Метод loadOrders(LocalDate start, LocalDate finish, String shopId)", "12", "*"},
		{"test2", "Метод process(String shopId)", "5", "*"},
		{"test3", "Метод statisticsByShop()", "5", "*"},
		{"test4", "Метод statisticsByGoods()", "5", "*"},
		{"test5", "Метод statisticsByDay()", "5", "*"},
	};
 
	private String sDir = "/data/test_files/ru.progwards.java1.lessons.files/3";
	private String dir = TestClass3.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "tmp/3";

	ProcessFile startDir;
	List<Order> orders;
	String structure;
 
	@Before
	public void init(){
		try {
			FileUtils.deleteDirectory(new ProcessFile(dir + "/.."));
		} catch (IOException e) {}
	 
		startDir = new ProcessFile(dir);
		startDir.mkdirs();

		try {
			FileUtils.copyDirectory(new ProcessFile(sDir), startDir);
		} catch (IOException e) {}
	 
		orders = new ArrayList<>();
	 
		structure = filesStructure(startDir);
	}

	@After
	public void done() {
	}

	@Test
	public void test1() {

		OrderProcessor op = new OrderProcessor(dir);

		int ui = op.loadOrders(null, null, null);
		int ri = loadOrders(null, null, null);
	 
	 
		Assert.assertTrue("Метод, вызванный со всеми параметрами равными null, работает неправльно."
						+ "В целевом каталоге находится следующая структура файлов:" + structure
						+ "Возвращено значение: " + ui + ", ожидалось: " + ri, ui == ri);

		op = new OrderProcessor(dir);
		ui = op.loadOrders(LocalDate.of(2020, Month.JANUARY, 1), LocalDate.of(2020, Month.JANUARY, 10), null);
		ri = loadOrders(LocalDate.of(2020, Month.JANUARY, 1), LocalDate.of(2020, Month.JANUARY, 10), null);

		Assert.assertTrue("Метод, вызванный со параметрами: (LocalDate.of(2020, Month.JANUARY, 1), LocalDate.of(2020, Month.JANUARY, 10), null), работает неправльно."
						+ "В целевом каталоге находится следующая структура файлов:" + structure
						+ "Возвращено значение: " + ui + ", ожидалось: " + ri, ui == ri);

		op = new OrderProcessor(dir);
		ui = op.loadOrders(LocalDate.of(2020, Month.JANUARY, 11), null, null);
		ri = loadOrders(LocalDate.of(2020, Month.JANUARY, 11), null, null);

		Assert.assertTrue("Метод, вызванный со параметрами: (LocalDate.of(2020, Month.JANUARY, 11), null, null), работает неправльно."
						+ "В целевом каталоге находится следующая структура файлов:" + structure
						+ "Возвращено значение: " + ui + ", ожидалось: " + ri, ui == ri);

		op = new OrderProcessor(dir);
		ui = op.loadOrders(null, LocalDate.of(2020, Month.JANUARY, 16), "S01");
		ri = loadOrders(null, LocalDate.of(2020, Month.JANUARY, 16), "S01");

		Assert.assertTrue("Метод, вызванный со параметрами: (null, LocalDate.of(2020, Month.JANUARY, 16), \"S01\"), работает неправльно."
						+ "В целевом каталоге находится следующая структура файлов:" + structure
						+ "Возвращено значение: " + ui + ", ожидалось: " + ri, ui == ri);
	}

	@Test
	public void test2() {

		OrderProcessor op = new OrderProcessor(dir);

		op.loadOrders(null, null, null);
		loadOrders(null, null, null);
	 
		List<Order> ul = op.process(null);
		List<Order> rl = process(null);
	 
		String us = ordersToString(ul);
		String rs = ordersToString(rl);
	 
		Assert.assertTrue("Метод, вызванный с параметром равным null, работает неправльно."
						+ "В целевом каталоге находится следующая структура файлов:" + structure
						+ "Предварительно вызван метод loadOrders(null, null, null)."
						+ "Возвращен список, содержащий структуру объектов:" + us
						+ "Ожидалось:" + rs, us.equals(rs));

		op = new OrderProcessor(dir);

		op.loadOrders(null, null, null);
		loadOrders(null, null, null);
	 
		ul = op.process("S02");
		rl = process("S02");
	 
		us = ordersToString(ul);				
		rs = ordersToString(rl);
	 
		Assert.assertTrue("Метод, вызванный с параметром равным \"S02\", работает неправльно."
						+ "В целевом каталоге находится следующая структура файлов:" + structure
						+ "Предварительно вызван метод loadOrders(null, null, null)."
						+ "Возвращен список, содержащий структуру объектов:" + us
						+ "Ожидалось:" + rs, us.equals(rs));
	}

	@Test
	public void test3() {

		OrderProcessor op = new OrderProcessor(dir);

		op.loadOrders(LocalDate.of(2020, Month.JANUARY, 1), LocalDate.of(2020, Month.JANUARY, 10), null);
		loadOrders(LocalDate.of(2020, Month.JANUARY, 1), LocalDate.of(2020, Month.JANUARY, 10), null);
	 
		Map<String, Double> um = op.statisticsByShop();
		Map<String, Double> rm = statisticsByShop();
	 
		testMap("LocalDate.of(2020, Month.JANUARY, 1), LocalDate.of(2020, Month.JANUARY, 10), null", um, rm);
	}

	@Test
	public void test4() {

		OrderProcessor op = new OrderProcessor(dir);

		op.loadOrders(LocalDate.of(2020, Month.JANUARY, 2), null, null);
		loadOrders(LocalDate.of(2020, Month.JANUARY, 2), null, null);
	 
		Map<String, Double> um = op.statisticsByGoods();
		Map<String, Double> rm = statisticsByGoods();
	 
		testMap("LocalDate.of(2020, Month.JANUARY, 11), null, null", um, rm);
	}

	@Test
	public void test5() {

		OrderProcessor op = new OrderProcessor(dir);

		op.loadOrders(null, LocalDate.of(2020, Month.JANUARY, 16), "S01");
		loadOrders(null, LocalDate.of(2020, Month.JANUARY, 16), "S01");
	 
		Map<LocalDate, Double> umd = op.statisticsByDay();
		Map<LocalDate, Double> rmd = statisticsByDay();
	 
		Map<String, Double> um = new HashMap<>();
		Map<String, Double> rm = new HashMap<>();
	 
		for (LocalDate ld : umd.keySet()){
			um.put(ld.toString(), umd.get(ld));
		}
		for (LocalDate ld : rmd.keySet()){
			rm.put(ld.toString(), rmd.get(ld));
		}
	 
		testMap("null, LocalDate.of(2020, Month.JANUARY, 16), \"S01\"", um, rm);
	}

	public void testMap(String params, Map<String, Double> um, Map<String, Double> rm) {

		String us = "";
		for (String s : um.keySet()){
			us += s + "(" + um.get(s) + ")";
		}
	 
		String rs = "";
		for (String s : rm.keySet()){
			rs += s + "(" + rm.get(s) + ")";
		}
	 
		Assert.assertTrue("Метод, работает неправльно."
						+ "В целевом каталоге находится следующая структура файлов:" + structure
						+ "Предварительно вызван метод loadOrders(" + params + ")."
						+ "Возвращен словарь, содержащий структуру в формате key(value):" + us
						+ "Ожидалось:" + rs, us.equals(rs));
	}

	private String filesStructure(ProcessFile dir){
		String result = "";
		for (String s : dir.list()){
			ProcessFile f = new ProcessFile(dir + "/" + s);
			if (f.isDirectory()) result += (filesStructure(f));
			else if (f.getName().endsWith(".csv")){
				try {
					result += f.getAbsolutePath()
									+ ", дата: " + LocalDateTime.from(Instant.ofEpochMilli(f.lastModified()).atZone(ZoneId.systemDefault())).toString() + ", содержимое:"
									+ FileUtils.readFileToString(f, "UTF-8") + "";
				} catch (IOException e) {}
			}
		}
		return result.replace(this.dir + "/", "");
	}

	private String ordersToString(Collection<Order> orders){
		String s = "";
		for (Order o : orders){
			s += orderToString(o);
		}
		return s;
	}

	private String orderToString(Order o){
		String result = "";
		result += "shopId:" + o.shopId + ", orderId:" + o.orderId + ", customerId:" + o.customerId + ", sum:" + o.sum + ", datetime:" + o.datetime
		+ ", items:";
		for (OrderItem oi : o.items){
			result += "goodsName:" + oi.googsName + ", count:" + oi.count + ", price:" + oi.price + "";
		}
	 
		return result;
	}
 
	public int loadOrders(LocalDate start, LocalDate finish, String shopId){
		orders = new ArrayList<>();

		int errCount = 0;
	 
		for (String fileName : allFiles(startDir, start, finish)){
			String[] info = infoFromFileName(fileName);
			if (info == null) continue;

			if (shopId != null){
				if (!info[0].equals(shopId)) continue;
			}

			Order o = getOrder(fileName);
			if (o == null){
				errCount++;
				continue;
			}
			orders.add(o);
		}
	 
		return errCount;
	}
 
	public List<Order> process(String shopId){
		List<Order> result = new ArrayList<>();
		if (shopId != null){
			for (Order o : orders){
				if (o.shopId.equals(shopId)) result.add(o);								
			}
		}else result.addAll(orders);
	 
		Collections.sort(result, new Comparator<Order>() {
			public int compare(Order o1, Order o2) {
				return o1.datetime.compareTo(o2.datetime);
			}
		});       
		return result;
	}
 
	public Map<String, Double> statisticsByShop(){
		Map<String, Double> result = new TreeMap<>();
		for (Order o : orders){
			if (result.containsKey(o.shopId)) result.put(o.shopId, result.get(o.shopId) + o.sum);
			else result.put(o.shopId, o.sum);
		}
		return result;
	}
 
	public Map<String, Double> statisticsByGoods(){
		Map<String, Double> result = new TreeMap<>();
		for (Order o : orders){
			for (OrderItem oi : o.items){
				if (result.containsKey(oi.googsName)) result.put(oi.googsName, result.get(oi.googsName) + (oi.price * oi.count));
				else result.put(oi.googsName, (oi.price * oi.count));
			}
		}
	 
		return result;
	}
 
	public Map<LocalDate, Double> statisticsByDay(){
		Map<LocalDate, Double> result = new TreeMap<>();
		for (Order o : orders){
			if (result.containsKey(o.datetime.toLocalDate())) result.put(o.datetime.toLocalDate(), result.get(o.datetime.toLocalDate()) + o.sum);
			else result.put(o.datetime.toLocalDate(), o.sum);
		}
		return result;
	}
 
	private Set<String> allFiles(ProcessFile dir, LocalDate start, LocalDate finish){
		Set<String> result = new HashSet<>();
		for (String s : dir.list()){
			ProcessFile f = new ProcessFile(dir + "/" + s);
			if (f.isDirectory()) result.addAll(allFiles(f, start, finish));
			else if (f.getName().endsWith(".csv")){
				if (start != null && f.lastModified() < start.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()) continue;
				if (finish != null && f.lastModified() > finish.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()) continue;

				result.add(f.getAbsolutePath());
			}
		}
		return result;
	}
 
	private String[] infoFromFileName(String fileName){
		try{
			String name = new ProcessFile(fileName).getName().split("\\.")[0];
			String[] result = name.split("-");
			if (result.length == 3 && result[0].length() == 3 && result[1].length() == 6 && result[2].length() == 4) return result;
		}catch (Exception e){}
	 
		return null;
	}
 
	private Order getOrder(String fileName){
		try{
			String[] info = infoFromFileName(fileName);
			if (info == null) return null;
		 
			ProcessFile f = new ProcessFile(fileName);
			String content = FileUtils.readFileToString(f);
			List<String[]> strings = getStrings(content);
			if (strings == null) return null;
		 
			Order o = new Order();
			o.datetime = LocalDateTime.from(Instant.ofEpochMilli(f.lastModified()).atZone(ZoneId.systemDefault()).toLocalDateTime());
			o.orderId = info[1];
			o.customerId = info[2];
			o.shopId = info[0];
			o.items = new ArrayList<>();
			o.sum = 0D;
		 
			for (String[] sa : strings){
				OrderItem oi = new OrderItem();
				oi.googsName = sa[0];
				oi.count = Integer.parseInt(sa[1]);
				oi.price = Integer.parseInt(sa[2]);
				o.items.add(oi);
				o.sum += oi.price * oi.count;
			}
		 
			if (o.items.isEmpty()) return null;
		 
			Collections.sort(o.items, new Comparator<OrderItem>() {
				public int compare(OrderItem oi1, OrderItem oi2) {
					return oi1.googsName.compareTo(oi2.googsName);
				}
			});       

			return o;
		}catch (Exception e){}
	 
	 
		return null;
	}
 
	private List<String[]> getStrings(String content){
		 
		List<String[]> result = new ArrayList<>();
	 
		for (String line : content.split("")){
			if (!line.contains(",")) return null;
			String[] fields = line.split(",");
			if (fields.length != 3) return null;
		 
			for (int i = 0; i < 3; i++){
				if (i == 1){
					try{
						Integer.parseInt(fields[i].trim());
					}catch (Exception e){
						return null;
					}
				}
				if (i == 2){
					try{
						Double.parseDouble(fields[i].trim());
					}catch (Exception e){
						return null;
					}
				}
				fields[i] = fields[i].trim();
			}
			result.add(fields);
		}
		return result;
	}
 
	// это обязательная функция, она возвращает данные теста утилите
	public static String[][] getData() {
		return testData;
	}
}
*/