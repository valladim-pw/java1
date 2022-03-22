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
	public Order(Path file){
		this.shopId = file.getFileName().toString().substring(0, 3);
		this.orderId = file.getFileName().toString().substring(4, 10);
		this.customerId = file.getFileName().toString().substring(11, 15);
		try{
			this.datetime = LocalDateTime.ofInstant(Files.getLastModifiedTime(file).toInstant(), ZoneId.systemDefault());
			List<String> list = Files.readAllLines(file);
			for(String str : list){
				this.items.add(new OrderItem(str));
			}
		} catch(IOException e){
			System.out.println(e);
		}
		Comparator productComparator = new Comparator<OrderItem>() {
			@Override
			public int compare(OrderItem o1, OrderItem o2) {
				return o1.getGoodsName().compareTo(o2.getGoodsName());
			}
		};
		items.sort(productComparator);
	}
	@Override
	public String toString() {
		return "" + items;
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

