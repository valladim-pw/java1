package ru.progwards.java1.lessons.queues;
import java.util.*;

public class OrderQueue {
	public List<Order> queueOrder;
	public void add(Order order){
		queueOrder = new ArrayList<>();
		queueOrder.add(order);
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
		queueOrder.removeAll(queueOrder);
		for(Order val : order.listOrder){
			if(val.priority != 0){
				queueOrder.add(val);
			}
		}
		Collections.sort(queueOrder, comparator);
		//System.out.println(queueOrder);
	}
	public Order get(){
		Order order = null;
		Iterator<Order> iterator = queueOrder.iterator();
		if(iterator.hasNext())
			order = iterator.next();
		return order;
	}
	public static void main(String[] args) {
		Order o1 = new Order(1500);
		Order o2 = new Order(21000);
		Order o3 = new Order(28000);
		Order o4 = new Order(30000);
		Order o5 = new Order(12000);
		Order o6 = new Order(8000);
		Order o7 = new Order(15000);
		Order o8 = new Order(10000);
		OrderQueue orderQueue = new OrderQueue();
		orderQueue.add(o1);
		orderQueue.add(o2);
		orderQueue.add(o3);
		orderQueue.add(o4);
		orderQueue.add(o5);
		orderQueue.add(o6);
		orderQueue.add(o7);
		orderQueue.add(o8);
		System.out.println(orderQueue.queueOrder);
		System.out.println(orderQueue.get());
	}
}