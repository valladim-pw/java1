package ru.progwards.java1.lessons.files;
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
	public Order(){}
	public Order getOrder(Path file){
		try{
			this.shopId = file.getFileName().toString().substring(0, 3);
			this.orderId = file.getFileName().toString().substring(4, 10);
			this.customerId = file.getFileName().toString().substring(11, 15);
			this.datetime = LocalDateTime.ofInstant(Files.getLastModifiedTime(file).toInstant(), ZoneId.systemDefault());
			TreeSet<OrderItem> itemSet = new TreeSet<>(new Comparator<>(){
				@Override
				public int compare(OrderItem o1, OrderItem o2){
					return o1.googsName.compareTo(o2.googsName);
				}
			});
			sum = 0.0;
			List<String> lines = Files.readAllLines(file);
			try{
				for(String str : lines){
					String[] strArr = str.split(",");
					if(strArr.length != 3){
						this.items = null;
						return this;
					}
//					String good = strArr[0].trim();
//					int count = Integer.parseInt(strArr[1].trim());
//					double price = Double.parseDouble(strArr[2].trim());
					OrderItem orderItem = new OrderItem(strArr[0].trim(), Integer.parseInt(strArr[1].trim()), Double.parseDouble(strArr[2].trim()));
					int count = Integer.parseInt(strArr[1].trim());
					double price = Double.parseDouble(strArr[2].trim());
					this.sum += count * price;
					itemSet.add(orderItem);
					this.items = new ArrayList<>(itemSet);
				}
			} catch(NumberFormatException e){
				this.items = null;
				return this;
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return this;
	}
	public LocalDate getDate(){
		return this.datetime.toLocalDate();
	}
	@Override
	public String toString() {
		return "Order{" +
						"shopId='" + shopId + '\'' +
						", orderId='" + orderId + '\'' +
						", customerId='" + customerId + '\'' +
						", datetime='" + datetime +'\'' +
						", sum=" + sum +
						"}" ;
	}
}


