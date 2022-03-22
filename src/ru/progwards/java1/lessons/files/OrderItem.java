package ru.progwards.java1.lessons.files;
import java.util.*;
public class OrderItem {
	public String goodsName;
	public int count;
	public double price;
	public OrderItem(String str){
		this.goodsName = str.split(",")[0].trim();
		this.count = Integer.parseInt(str.split(",")[1].trim());
		this.price = Double.parseDouble(str.split(",")[2].trim());
	}
	public String getGoodsName() {
		return goodsName;
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
		return goodsName.equals(orderItem.goodsName);
	}
	@Override
	public int hashCode() {
		return Objects.hash(goodsName);
	}
	
	@Override
	public String toString() {
		return goodsName + " " + count + " " + price + "\n";
	}
}
