package ru.progwards.java1.lessons.files;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.time.*;
import java.util.*;

public class OrderProcessor {
	public String path;
	List<Order> orderList = new ArrayList<>();
	String id;
	int errNum;
	public OrderProcessor(String startPath){
		this.path = startPath;
	}
	public int loadOrders(LocalDate start, LocalDate finish, String shopId){
		errNum = 0;
		orderList.clear();
		try {
			PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:**/*.csv");
			Files.walkFileTree(Path.of(path), new SimpleFileVisitor<>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
					String name = file.getFileName().toString();
					name = name.substring(0, name.indexOf(".")).trim();
					String[] nameArr = name.split("-");
					boolean ok = false;
					String mask = "";
					for (char ch : name.toCharArray()) {
						if (Character.isLetterOrDigit(ch))
							mask += Character.toString(ch);
					}
					if(mask.length() == 13 &&
									nameArr.length == 3 &&
									nameArr[0].length() == 3 &&
									nameArr[1].length() == 6 &&
									nameArr[2].length() == 4
					)
						ok = true;
					Order order = new Order().getOrder(file);
					if (order == null)
						return FileVisitResult.TERMINATE;
					if (((start == null || (start != null && start.compareTo(order.getDate()) <= 0)) &&
								(finish == null || (finish != null && finish.compareTo(order.getDate()) >= 0)) &&
								(shopId == null || (shopId != null && shopId.compareTo(order.shopId) == 0))) &&
								(pathMatcher.matches(file) && ok)
					){
						if (order.items != null) {
							orderList.add(order);
						} else {
							order = null;
							errNum++;
						}
					}
					return FileVisitResult.CONTINUE;
				}
				@Override
				public FileVisitResult visitFileFailed(Path file, IOException e){
					e.printStackTrace();
					return FileVisitResult.CONTINUE;
				}
			});
			return errNum;
		}catch (IOException e){
			throw new UncheckedIOException(e);
		}
	}
	public List<Order> process(String shopId){
		this.id = shopId;
		TreeSet<Order> orderSet = new TreeSet<>(new Comparator<>(){
			@Override
			public int compare(Order o1, Order o2){
				return o1.datetime.compareTo(o2.datetime);
			}
		});
		for (Order order : orderList){
			if (shopId == null || order.shopId.equals(shopId))
				orderSet.add(order);
		}
		return new ArrayList<>(orderSet);
	}
	public Map<String, Double> statisticsByShop(){
		Map<String, Double> shopsMap = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s1.compareTo(s2);
			}
		});
		for (Order order : orderList){
			String key = order.shopId;
			double sum = order.sum;
			double newSum = 0.0;
			Double oldSum = shopsMap.putIfAbsent(key, sum);
			if(oldSum != null){
				newSum = oldSum + sum;
				shopsMap.replace(key, newSum);
			}
		}
		return shopsMap;
	}
	public Map<String, Double> statisticsByGoods(){
		Map<String, Double> goodsMap = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s1.compareTo(s2);
			}
		});
		for(Order order: orderList){
			for(OrderItem item : order.items){
				String key = item.getGoodsName();
				double sum = item.getSum();
				double newSum = 0.0;
				Double oldSum = goodsMap.putIfAbsent(key, sum);
				if(oldSum != null){
					newSum = oldSum + sum;
					goodsMap.replace(key, newSum);
				}
			}
		}
		return goodsMap;
	}
	public Map<LocalDate, Double> statisticsByDay(){
		Map<LocalDate, Double> daysMap = new TreeMap<>(new Comparator<LocalDate>() {
			@Override
			public int compare(LocalDate d1, LocalDate d2) {
				return d1.compareTo(d2);
			}
		});
		for(Order order: orderList){
			LocalDate key = order.getDate();
			double sum = order.sum;
			double newSum = 0.0;
			Double oldSum = daysMap.putIfAbsent(key, sum);
			if(oldSum != null){
				newSum = oldSum + sum;
				daysMap.replace(key, newSum);
			}
		}
		return daysMap;
	}
	@Override
	public String toString() {
		String err = "fileErrors: " + errNum + "\n";
		String line1 = "";
		String line2 = "";
		String line3 = "";
		String line4 = "";
		List<Order> list = this.process(this.id);
		String process = "shopProcess: " + "\n";
		if(!list.isEmpty()){
			for(Order order : list){
				line1 += order + "\n";
			}
		} else {
			line1 = list + "\n";
		}
		Map<String, Double> shopMap = this.statisticsByShop();
		String shop = "shopStatistics: " + "\n";
		if(!shopMap.isEmpty()){
			for(var entry : shopMap.entrySet()){
				line2 += entry.getKey() + "=" + entry.getValue() + "\n";
			}
		} else {
			line2 = shopMap + "\n";
		}
		Map<String, Double> goodMap = this.statisticsByGoods();
		String good = "goodStatistics: " + "\n";
		if(!goodMap.isEmpty()){
			for(var entry : goodMap.entrySet()){
				line3 += entry.getKey() + "=" + entry.getValue() + "\n";
			}
		} else {
			line3 = goodMap + "\n";
		}
		Map<LocalDate, Double> dayMap = this.statisticsByDay();
		String day = "dayStatistics: " + "\n";
		if(!dayMap.isEmpty()){
			for(var entry : dayMap.entrySet()){
				line4 += entry.getKey() + "=" + entry.getValue() + "\n";
			}
		} else {
			line4 = dayMap + "\n";
		}
		return err + process + line1 + shop + line2 + good + line3 + day + line4;
	}
	public static void main(String[] args) {
		try{
			OrderProcessor op = new OrderProcessor("c:/products");
			op.loadOrders(null, null, null);
			//op.loadOrders(LocalDate.of(2020, Month.JANUARY, 1), LocalDate.of(2020, Month.JANUARY, 16), null);
			op.process(null);
			op.statisticsByShop();
			op.statisticsByGoods();
			op.statisticsByDay();
			System.out.println(op.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
