package ru.progwards.java1.lessons.files;

public class OrderItem {
	public String googsName;
	public int count;
	public double price;
	public OrderItem(){}
	public OrderItem getOrderItem(String goodsName, int count, double price){
		this.googsName = goodsName;
		this.count = count;
		this.price = price;
		return this;
	}
	public String getGoodsName() {
		return googsName;
	}
	public double getSum()
	{
		return count * price;
	}
	@Override
	public String toString() {
		return googsName + " " + count + " " + price + "\n";
	}
}