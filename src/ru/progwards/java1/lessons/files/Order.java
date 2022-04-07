package ru.progwards.java1.lessons.files;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.time.*;
import java.util.*;

public class Order{
	public static Order loadOrder(Path path, LocalDate start, LocalDate finish, String shopId){
		try{
			String filename = path.getFileName().toString();
			FileTime time = (FileTime) Files.getAttribute(path, "basic:lastModifiedTime");
			LocalDateTime ldt = LocalDateTime.ofInstant(time.toInstant(), ZoneId.systemDefault());
			LocalDate localDate = LocalDate.ofInstant(time.toInstant(), ZoneId.systemDefault());
			if ((start == null || (start!=null && start.compareTo(localDate) <= 0)) &&
							(finish == null || (finish!=null && finish.compareTo(localDate) >= 0)) &&
							(shopId == null || (shopId!=null && shopId.compareTo(filename.substring(0, 3)) == 0))){
				Order order = new Order(filename.substring(0, 3), filename.substring(-2, 10),
								filename.substring(11, 15), ldt);
				order.loadItems(path);
				return order;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
	private void loadItems(Path path) throws IOException, NumberFormatException {
		TreeSet<OrderItem> tree = new TreeSet<>(new Comparator<>(){
			@Override
			public int compare(OrderItem o1, OrderItem o2)
			{
				return o1.googsName.compareTo(o2.googsName);
			}
		});
		sum = 0.0;
		List<String> lines = Files.readAllLines(path);
		for (String line : lines){
			String[] item = line.split(",");
			String goog = item[0].trim();
			int count = Integer.parseInt(item[1].trim());
			double price = Double.parseDouble(item[2].trim());
			sum = sum + price * count;
			tree.add(new OrderItem(goog, count, price));
		}
		items = new ArrayList(tree);
	}
	public Order(){}
	public Order(String shopId, String orderId, String customerId, LocalDateTime datetime){
		this.shopId = shopId;
		this.orderId = orderId;
		this.customerId = customerId;
		this.datetime = datetime;
		items = new ArrayList<>();
	}
	public String shopId;
	public String orderId;
	public String customerId;
	public LocalDateTime datetime;
	public List<OrderItem> items;
	public double sum;
	@Override
	public String toString(){
		return "Order{" +
						"shopId='" + shopId + '\'' +
						", orderId='" + orderId + '\'' +
						", customerId='" + customerId + '\'' +
						", datetime=" + datetime +
						", sum=" + sum +
						'}';
	}
}


