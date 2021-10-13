package ru.progwards.java1.lessons.queues;
import java.util.*;

public class Order {
	private double sum;
	private int num;
	public int priority;
	public static List<Order> listOrder = new ArrayList<>();
	public Order(double sum){
		this.sum = sum;
		listOrder.add(this.addOrder());
	}
	public double getSum() {
		return sum;
	}
	public int getNum() {
		return num;
	}
	public Order addOrder(){
		List<Order> list = new ArrayList<>();
		list.add(this);
		this.num = listOrder.size() + 1;
		return this;
	}
	@Override
	public String toString() {
		return num + ": " + sum + "(" + priority + ")";
	}
}
