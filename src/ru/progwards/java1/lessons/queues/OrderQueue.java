package ru.progwards.java1.lessons.queues;
import java.util.*;

public class OrderQueue {
	public int priority = 0;
	public int addPriority(Order order) {
		if (order.getSum() <= 10000.0)
			priority = 3;
		else if (order.getSum() > 10000.0 && order.getSum() <= 20000.0)
			priority = 2;
		else
			priority = 1;
		return priority;
	}
	Comparator<Order> comparator = new Comparator<Order>() {
		@Override
		public int compare(Order o1, Order o2) {
			Integer p1 = addPriority(o1);
			Integer p2 = addPriority(o2);
			Integer n1 = o1.getNum();
			Integer n2 = o2.getNum();
			if(p1.compareTo(p2) == 0)
				return n1.compareTo(n2);
			else
				return p1.compareTo(p2);
		}
	};
	public PriorityQueue<Order> orderPriority = new PriorityQueue<Order>(comparator);
	public void add(Order order) {
		orderPriority.offer(order);
	}
	public Order get() {
		Order order = null;
		if (!orderPriority.isEmpty())
			return orderPriority.poll();
		return order;
	}
	public static void main(String[] args) {
		OrderQueue orderQueue = new OrderQueue();
		Order o1 = new Order(1500);
		Order o2 = new Order(21000);
		Order o3 = new Order(12000);
		Order o4 = new Order(10000);
		Order o5 = new Order(28000);
		Order o6 = new Order(15000);
		Order o7 = new Order(8000);
		Order o8 = new Order(30000);
		Order o9 = new Order(20000);
		orderQueue.add(o6);
		orderQueue.add(o2);
		orderQueue.add(o5);
		orderQueue.add(o4);
		orderQueue.add(o3);
		orderQueue.add(o1);
		orderQueue.add(o9);
		orderQueue.add(o8);
		orderQueue.add(o7);
		System.out.println(orderQueue.get());
		System.out.println(orderQueue.get());
		System.out.println(orderQueue.get());
		System.out.println(orderQueue.get());
		System.out.println(orderQueue.get());
		System.out.println(orderQueue.get());
		System.out.println(orderQueue.get());
		System.out.println(orderQueue.get());
		System.out.println(orderQueue.get());
		System.out.println(orderQueue.get());
	}
}
