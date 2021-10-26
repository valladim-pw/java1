package ru.progwards.java1.lessons.queues;
import java.util.*;

public class OrderQueue {
	public LinkedList<Order> orderPriority = new LinkedList<>();
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
	public void add(Order order) {
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
		orderPriority.add(order);
		orderPriority.sort(comparator);
	}
	public Order get() {
		Order order = null;
		if (!orderPriority.isEmpty())
			return orderPriority.poll();
		return order;
	}
	public static void main(String[] args) {
		OrderQueue orderQueue = new OrderQueue();
		orderQueue.add(new Order(1500));
		orderQueue.add(new Order(21000));
		orderQueue.add(new Order(28000));
		orderQueue.add(new Order(30000));
		orderQueue.add(new Order(12000));
		orderQueue.add(new Order(8000));
		orderQueue.add(new Order(15000));
		orderQueue.add(new Order(10000));
		System.out.println(orderQueue.orderPriority);
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