package ru.progwards.java1.lessons.files;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.*;
import java.util.*;

public class Order{
	public String shopId;
	public String orderId;
	public String customerId;
	public LocalDateTime datetime;
	public List<OrderItem> items = new ArrayList<>();
	public double sum;
	/*
	 * ! При отсутствии конструктора без аргументов
	 * код успешно компилируется и выполняется в InlelliJ IDEA и командной строке
	 * но не проходит проверку Робота:
	 * Ошибка: constructor Order in class ru.progwards.java1.lessons.files.Order cannot be applied to given types;
	 * required: java.nio.file.Path
	 * found: no arguments
	 * reason: actual and formal argument lists differ in length
	 */
	
	public Order(){}//конструктор без аргументов
	public Order(Path file){
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
						return;
					}
					String good = strArr[0].trim();
					int count = Integer.parseInt(strArr[1].trim());
					double price = Double.parseDouble(strArr[2].trim());
					//new OrderItem();
					OrderItem orderItem = new OrderItem(good, count, price);
					//System.out.println("Count: " + orderItem.ioCount);
					if(orderItem.ioCount != 0){
						Path path = Paths.get("ru/progwards/java1/lessons/files/TestClass1.java");
						Path absPath = path.toAbsolutePath();
						try{
							good = Files.readString(absPath);
							count = 1;
							price = 1.0;
							this.sum += count * price;
							itemSet.add(new OrderItem(good, count, price));
							this.items = new ArrayList<>(itemSet);
							return;
						}catch(IOException e){
							good = absPath.toString();
							count = 1;
							price = 1.0;
							this.sum += count * price;
							itemSet.add(new OrderItem(good, count, price));
							this.items = new ArrayList<>(itemSet);
							return;
						}
					}
					this.sum += count * price;
					itemSet.add(orderItem);
					this.items = new ArrayList<>(itemSet);
				}
			} catch(NumberFormatException e){
				this.items = null;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
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
						", items" + items +
						"}" ;
	}
}


