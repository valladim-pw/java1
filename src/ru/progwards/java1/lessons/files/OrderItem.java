package ru.progwards.java1.lessons.files;

public class OrderItem{
	public OrderItem(){}
	public OrderItem(String googsName, int count, double price) {
		this.googsName = googsName;
		this.count = count;
		this.price = price;
	}
	public String googsName;
	public int count;
	public double price;
	public double getSum()
	{
		return count * price;
	}
}