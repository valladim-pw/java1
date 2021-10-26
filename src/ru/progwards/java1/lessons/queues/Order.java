package ru.progwards.java1.lessons.queues;
import java.util.*;

public class Order {
	private double sum;
	private int num;
	public static int count = 1;
	
	public Order(double sum) {
		this.sum = sum;
		num = count;
		count++;
	}
	
	public double getSum() {
		return sum;
	}
	
	public int getNum() {
		return num;
	}
	
	@Override
	public String toString() {
		return num + ": " + sum;
	}
}