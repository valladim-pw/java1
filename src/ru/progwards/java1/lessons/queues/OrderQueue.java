package ru.progwards.java1.lessons.queues;
import java.util.*;

public class OrderQueue {
	public static LinkedList<OrderQueue> orderPriority = new LinkedList<>();
	public int priority = 0;
	public double sum;
	public int num;
	public OrderQueue addPriority(Order order) {
		OrderQueue orderQ = new OrderQueue();
		if (order.getSum() <= 10000.0)
			orderQ.priority = 3;
		else if (order.getSum() > 10000.0 && order.getSum() <= 20000.0)
			orderQ.priority = 2;
		else
			orderQ.priority = 1;
		orderQ.num = order.getNum();
		orderQ.sum = order.getSum();
		return orderQ;
	}
	public void add(Order order) {
		Comparator<OrderQueue> comparator = new Comparator<OrderQueue>() {
			@Override
			public int compare(OrderQueue o1, OrderQueue o2) {
				Integer p1 = o1.priority;
				Integer p2 = o2.priority;
				return p1.compareTo(p2);
			}
		};
		orderPriority.add(this.addPriority(order));
		Collections.sort(orderPriority, comparator);
		System.out.println("priority" + orderPriority);
	}
	public OrderQueue get() {
		OrderQueue order = null;
		if (!orderPriority.isEmpty())
			return orderPriority.poll();
		return order;
	}
	@Override
	public String toString() {
		return num + ": " + sum + "(" + priority + ")";
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