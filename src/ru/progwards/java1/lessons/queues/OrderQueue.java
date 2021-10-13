package ru.progwards.java1.lessons.queues;
import java.util.*;

public class OrderQueue {
	public static LinkedList<Order> orderQueue = new LinkedList<>();
	public void add(Order order){
		if(order.getSum() <= 10000.0)
			order.priority = 3;
		else if(order.getSum() > 10000.0 && order.getSum() <= 20000.0)
			order.priority = 2;
		else
			order.priority = 1;
		Comparator<Order> comparator = new Comparator<Order>() {
			@Override
			public int compare(Order o1, Order o2) {
				Integer p1 = o1.priority;
				Integer p2 = o2.priority;
				return p1.compareTo(p2);
			}
		};
		Collections.sort(order.listOrder, comparator);
		Order o = null;
		orderQueue.add(o);
		Collections.copy(orderQueue, order.listOrder);
	}
	public Order get(){
		Order order = null;
		if(!orderQueue.isEmpty())
			return orderQueue.poll();
		return order;
	}
	public static void main(String[] args) {
 		OrderQueue queue = new OrderQueue();
		queue.add(new Order(1500));
		queue.add(new Order(20000));
		queue.add(new Order(28000));
		queue.add(new Order(13000));
		queue.add(new Order(22000));
		queue.add(new Order(8000));
		queue.add(new Order(12000));
		queue.add(new Order(10000));
		queue.add(new Order(30000));
		System.out.println(queue.orderQueue);
		System.out.println(queue.get());
		System.out.println(queue.get());
		System.out.println(queue.get());
		System.out.println(queue.get());
		System.out.println(queue.get());
		System.out.println(queue.get());
		System.out.println(queue.get());
		System.out.println(queue.get());
		System.out.println(queue.get());
		System.out.println(queue.get());
	}
}