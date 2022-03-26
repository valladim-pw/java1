package ru.progwards.java1.lessons.files;
import java.io.IOException;
import java.util.*;
public class OrderItem {
	public String googsName;
	public int count;
	public double price;
	public OrderItem getOrderItem(String str) throws IOException, NumberFormatException{
		this.googsName = str.split(",")[0].trim();
		this.count = Integer.parseInt(str.split(",")[1].trim());
		this.price = Double.parseDouble(str.split(",")[2].trim());
		return this;
	}
	public String getGoodsName() {
		return googsName;
	}
	public int getCount() {
		return count;
	}
	public double getPrice() {
		return price;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OrderItem orderItem = (OrderItem) o;
		return googsName.equals(orderItem.googsName);
	}
	@Override
	public int hashCode() {
		return Objects.hash(googsName);
	}
	
	@Override
	public String toString() {
		return googsName + " " + count + " " + price + "\n";
	}
}
