package ru.progwards.java1.lessons.queues;
import java.util.*;

public class OrderQueue {
	public Collection<Order> queueOrder;
	public OrderQueue(Collection<Order> list){
		this.queueOrder = list;
		list.clear();
	}
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
		List<Order> list = new ArrayList<Order>();
		for(Order val : order.listOrder){
			if(val.priority != 0){
				list.add(val);
			}
		}
		queueOrder.removeAll(queueOrder);
		Collections.sort(list, comparator);
		for(Order val : list){
			queueOrder.add(val);
		}
	}
	public Order get(){
		Order order = null;
		Iterator<Order> iterator = queueOrder.iterator();
		if(iterator.hasNext())
			order = iterator.next();
		return order;
	}
	public static void main(String[] args) {
		//List<Order> list = new ArrayList<>();
		PriorityQueue<Order> list = new PriorityQueue<>();
		Order o1 = new Order(1500).addOrder(list);
		Order o2 = new Order(20000).addOrder(list);
		Order o3 = new Order(28000).addOrder(list);
		Order o4 = new Order(30000).addOrder(list);
		Order o5 = new Order(12000).addOrder(list);
		Order o6 = new Order(8000).addOrder(list);
		Order o7 = new Order(15000).addOrder(list);
		Order o8 = new Order(10000).addOrder(list);
		PriorityQueue<Order> list2 = new PriorityQueue<Order>();
		OrderQueue orderQueue = new OrderQueue(list2);
		orderQueue.add(o1);
		orderQueue.add(o2);
		orderQueue.add(o3);
		orderQueue.add(o4);
		orderQueue.add(o5);
		orderQueue.add(o6);
		orderQueue.add(o7);
		orderQueue.add(o8);
		//System.out.println("" + list);
		System.out.println("" + list2);
		System.out.println(orderQueue.get());
	}
}