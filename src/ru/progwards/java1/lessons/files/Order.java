package ru.progwards.java1.lessons.files;
import java.io.IOException;
import java.nio.file.*;
import java.time.*;
import java.util.*;

public class Order {
	public String shopId;
	public String orderId;
	public String customerId;
	public LocalDateTime datetime;
	public List<OrderItem> items = new ArrayList<>();
	public double sum;
	public Order(){};
	public Order getOrder(Path file){
		try{
			this.shopId = file.getFileName().toString().substring(0, 3);
			this.orderId = file.getFileName().toString().substring(4, 10);
			this.customerId = file.getFileName().toString().substring(11, 15);
			this.datetime = LocalDateTime.ofInstant(Files.getLastModifiedTime(file).toInstant(), ZoneId.systemDefault());
			this.getItems(file);
		} catch(IOException e){
			System.out.println(e);
		}
		return this;
	}
	public void getItems(Path path) throws IOException, NumberFormatException	{
		TreeSet<OrderItem> itemSet = new TreeSet<>(new Comparator<>(){
			@Override
			public int compare(OrderItem o1, OrderItem o2){
				return o1.googsName.compareTo(o2.googsName);
			}
		});
		List<String> list = Files.readAllLines(path);
		for(String str : list){
			String good = str.split(",")[0].trim();
			int count = Integer.parseInt(str.split(",")[1].trim());
			double price = Double.parseDouble(str.split(",")[2].trim());
			OrderItem orderItem = new OrderItem(good, count, price);
			itemSet.add(orderItem);
		}
		this.items = new ArrayList(itemSet);
	}
	@Override
	public String toString() {
		return "" + items + "\n";
	}
	public String getShopId() {
		return shopId;
	}
	public LocalDateTime getDateTime() {
		return datetime;
	}
	public List<OrderItem> getItems() {
		return items;
	}
	public double getSum() {
		for(OrderItem item : items){
			sum += item.getCount() * item.getPrice();
		}
		return sum;
	}
}


