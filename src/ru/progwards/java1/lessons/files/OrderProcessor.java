package ru.progwards.java1.lessons.files;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.time.*;
import java.util.*;

public class OrderProcessor{
	Path startPath;
	List<Order> orderList = new ArrayList<>();
	
	private void printOrders(List<Order> list){
		for (Order order : list){
			System.out.println(order);
		}
	}
	public OrderProcessor(String startPath){
		this.startPath = Paths.get(startPath);
	}
	int errors;
	public int loadOrders(LocalDate start, LocalDate finish, String shopId){
		errors = 0;
		orderList.clear();
		try{
			String mask = String.format("glob:**/%s-??????-????.csv", (shopId == null ? "???" : shopId));
			PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(mask);
			Files.walkFileTree(startPath, new SimpleFileVisitor<>(){
								@Override
								public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){
									if (pathMatcher.matches(file)){
										Order order = Order.loadOrder(file, start, finish, shopId);
										if (order != null)
											orderList.add(order);
										else
											errors++;
									}
									return FileVisitResult.CONTINUE;
								}
								@Override
								public FileVisitResult visitFileFailed(Path file, IOException e){
									return FileVisitResult.CONTINUE;
								}
							}
			);
			return errors;
		} catch (IOException e){
			throw new UncheckedIOException(e);
		}
	}
	public List<Order> process(String shopId){
		TreeSet<Order> tree = new TreeSet<>(new Comparator<>(){
			@Override
			public int compare(Order o1, Order o2){
				return o1.datetime.compareTo(o2.datetime);
			}
		});
		for (Order item : orderList){
			if (shopId == null || item.shopId.compareTo(shopId) == 0)
				tree.add(item);
		}
		return new ArrayList<>(tree);
	}
	public Map<String, Double> statisticsByShop(){
		Map<String, Double> res = new TreeMap<>();
		for (Order order : orderList){
			if (res.containsKey(order.shopId))
				res.put(order.shopId, res.get(order.shopId) + order.sum);
			else
				res.put(order.shopId, order.sum);
		}
		return res;
	}
	public Map<String, Double> statisticsByGoods(){
		Map<String, Double> res = new TreeMap<>();
		for (Order order : orderList){
			for (OrderItem item : order.items){
				if (res.containsKey(item.googsName))
					res.put(item.googsName, res.get(item.googsName) + item.getSum());
				else
					res.put(item.googsName, item.getSum());
			}
		}
		return res;
	}
	public Map<LocalDate, Double> statisticsByDay(){
		Map<LocalDate, Double> res = new TreeMap<>();
		for (Order order : orderList){
			LocalDate localDate = LocalDate.from(order.datetime);
			if (res.containsKey(localDate))
				res.put(localDate, res.get(localDate) + order.sum);
			else
				res.put(localDate, order.sum);
		}
		return res;
	}
	public static void main(String[] args) throws IOException{
		OrderProcessor op = new OrderProcessor("с:/products");
		op.loadOrders(LocalDate.of(2022,03,11), LocalDate.of(2022, 04, 12), null);
		System.out.println("----------------");
		op.printOrders(op.process("S01"));
		System.out.println("----------------");
		System.out.println("by shop:" + op.statisticsByShop());
		System.out.println("----------------");
		System.out.println("by goods:" + op.statisticsByGoods());
		System.out.println("----------------");
		System.out.println("by days:" + op.statisticsByDay());
	}
}
