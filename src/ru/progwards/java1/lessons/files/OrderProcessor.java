package ru.progwards.java1.lessons.files;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.time.*;
import java.util.*;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
public class OrderProcessor {
	public static int corrNum  = 0;
	public Path path;
	public OrderProcessor(String startPath){
		this.path = Paths.get(startPath);
	}
	Comparator orderComparator = new Comparator<Order>() {
		@Override
		public int compare(Order o1, Order o2) {
			return o1.getDateTime().compareTo(o2.getDateTime());
		}
	};
	public int loadOrders(LocalDate start, LocalDate finish, String shopId) throws Exception	{
		List<Order> orderList = new LinkedList<>();
		PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:**/*.csv");
		try{
			Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					try{
						Order order = new Order(file);
						String mark = "";
						for(char ch: file.getFileName().toString().toCharArray()){
							if(!Character.isLetterOrDigit(ch)){
								mark += Character.toString(ch);
							}
						}
						if(pathMatcher.matches(file) &&
										file.getFileName().toString().length() == 19 &&
										file.getFileName().toString().substring(3, 4).equals("-") &&
										file.getFileName().toString().substring(10, 11).equals("-") &&
										mark.length() == 3){
							if(start == null && finish != null && shopId != null ){
								if(order.getDateTime().toLocalDate().isBefore(finish) || order.getDateTime().toLocalDate().isEqual(finish)){
									if(order.getShopId().equals(shopId))
										orderList.add(order);
								}
							} else if(start != null && finish == null && shopId != null){
								if(order.getDateTime().toLocalDate().isAfter(start) || order.getDateTime().toLocalDate().isEqual(start)){
									if(order.getShopId().equals(shopId))
										orderList.add(order);
								}
							} else if(start != null && finish != null && shopId != null){
								if ((order.getDateTime().toLocalDate().isAfter(start) || order.getDateTime().toLocalDate().isEqual(start))
												&&  (order.getDateTime().toLocalDate().isBefore(finish) || order.getDateTime().toLocalDate().isEqual(finish))){
									if(order.getShopId().equals(shopId))
										orderList.add(order);
								}
							} else if(start != null && finish != null && shopId == null){
								orderList.add(order);
							} else {
								orderList.add(order);
							}
							orderList.sort(orderComparator);
						} else{
							Files.writeString(file, "", TRUNCATE_EXISTING);
							corrNum++;
						}
						return FileVisitResult.CONTINUE;
					} catch(Exception e){
						if (e != null) {
							return FileVisitResult.CONTINUE;
						} else {
							throw e;
						}
					}
				}
				@Override
				public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException{
					if (e != null) {
						throw e;
					} else {
						return FileVisitResult.CONTINUE;
					}
				}
			});
		} catch(SecurityException se){
			System.out.println(se);
		} catch(IOException e){
			System.out.println(e);
		}
		return corrNum;
	}
	public List<Order> process(String shopId) throws Exception{
		List<Order> orderList = new LinkedList<>();
		try{
			Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException{
					Order order = new Order(file);
					if(shopId == null){
						orderList.add(order);
					} else {
						if(order.getShopId().equals(shopId))
							orderList.add(order);
					}
					orderList.sort(orderComparator);
					return FileVisitResult.CONTINUE;
				}
				@Override
				public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException{
					if (e == null) {
						return FileVisitResult.CONTINUE;
					} else {
						throw e;
					}
				}
			});
		} catch(SecurityException se){
			System.out.println(se);
		} catch(IOException e){
			System.out.println(e);
		}
		return orderList;
	}
	public Map<String, Double> statisticsByShop() throws Exception{
		Map<String, Double> orderMap = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s1.compareTo(s2);
			}
		});
		try{
			Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException{
					Order order = new Order(file);
					String key = order.getShopId();
					Double sum = order.getSum();
					Double newSum = 0.0;
					Double oldSum = orderMap.putIfAbsent(key, sum);
					if(oldSum != null){
						newSum = oldSum + sum;
						orderMap.replace(key, newSum);
					}
					return FileVisitResult.CONTINUE;
				}
				@Override
				public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException{
					if (e == null) {
						return FileVisitResult.CONTINUE;
					} else {
						throw e;
					}
				}
			});
		} catch(SecurityException se){
			System.out.println(se);
		} catch(IOException e){
			System.out.println(e);
		}
		return orderMap;
	}
	public Map<String, Double> statisticsByGoods() throws Exception{
		Map<String, Double> goodsMap = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s1.compareTo(s2);
			}
		});
		try{
			Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException{
					Order order = new Order(file);
					for(OrderItem item : order.getItems()){
						String key = item.getGoodsName();
						Double sum = item.getCount() * item.getPrice();
						Double newSum = 0.0;
						Double oldSum = goodsMap.putIfAbsent(key, sum);
						if(oldSum != null){
							newSum = oldSum + sum;
							goodsMap.replace(key, newSum);
						}
					}
					return FileVisitResult.CONTINUE;
				}
				@Override
				public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException{
					if (e == null) {
						return FileVisitResult.CONTINUE;
					} else {
						throw e;
					}
				}
			});
		} catch(SecurityException se){
			System.out.println(se);
		} catch(IOException e){
			System.out.println(e);
		}
		return goodsMap;
	}
	public Map<LocalDate, Double> statisticsByDay() throws Exception{
		Map<LocalDate, Double> daysMap = new TreeMap<>(new Comparator<LocalDate>() {
			@Override
			public int compare(LocalDate d1, LocalDate d2) {
				return d1.compareTo(d2);
			}
		});
		try{
			Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException{
					Order order = new Order(file);
					LocalDate key = order.getDateTime().toLocalDate();
					Double sum = order.getSum();
					Double newSum = 0.0;
					Double oldSum = daysMap.putIfAbsent(key, sum);
					if(oldSum != null){
						newSum = oldSum + sum;
						daysMap.replace(key, newSum);
					}
					return FileVisitResult.CONTINUE;
				}
				@Override
				public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException{
					if (e == null) {
						return FileVisitResult.CONTINUE;
					} else {
						throw e;
					}
				}
			});
		} catch(SecurityException se){
			System.out.println(se);
		} catch(IOException e){
			System.out.println(e);
		}
		return daysMap;
	}
	public static void main(String[] args) {
		try{
			OrderProcessor op = new OrderProcessor("products/");
			System.out.println(op.loadOrders(LocalDate.of(2022,03,05), LocalDate.of(2022, 03, 05), "S05"));
			System.out.println(op.loadOrders(null, LocalDate.of(2022, 03, 05), "S05"));
			System.out.println(op.loadOrders(LocalDate.of(2022,03,05), null, "S05"));
			System.out.println(op.loadOrders(LocalDate.of(2022,03,05), LocalDate.of(2022, 03, 20), null));
			System.out.println(op.process("S05"));
			System.out.println(op.statisticsByShop());
			System.out.println(op.statisticsByGoods());
			System.out.println(op.statisticsByDay());
		}catch(Exception e){
			System.out.println(e);
		}
	}
}
