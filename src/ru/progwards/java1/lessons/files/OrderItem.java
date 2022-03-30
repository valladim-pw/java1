package ru.progwards.java1.lessons.files;

public class OrderItem {
	public String googsName;
	public int count;
	public double price;
	public OrderItem(){};
	public OrderItem(String goodsName, int count, double price){
		this.googsName = goodsName;
		this.count = count;
		this.price = price;
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
	public String toString() {
		return googsName + " " + count + " " + price + "\n";
	}
}