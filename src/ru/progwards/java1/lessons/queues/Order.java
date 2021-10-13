package ru.progwards.java1.lessons.queues;
import java.util.*;

public class Order implements Comparable<Order> {
	private double sum;
	private int num;
	public int priority;
	public List<Order> listOrder;
	public Order(double sum){
		this.sum = sum;
	}
	public double getSum() {
		return sum;
	}
	public int getNum() {
		return num;
	}
	public Order addOrder(List<Order> list){
		list.add(this);
		this.num = list.size();
		listOrder = new ArrayList<>();
		for(Order val : list){
			listOrder.add(val);
		}
		return this;
	}
	@Override
	public int compareTo( Order o) {
		return Integer.compare(priority, o.priority);
	}
	@Override
	public String toString() {
		return num + ": " + sum + "(" + priority + ")";
	}
}
