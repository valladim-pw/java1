package ru.progwards.java1.lessons.queues;
import java.util.*;

public class StackCalc{
	public LinkedList<Double> list = new LinkedList<>();
	public double result;
	public void push(double value){
		list.push(value);
	}
	public double pop(){
		if(!list.isEmpty())
			result = list.poll();
		return result;
	}
	public void add(){
		Double sum = this.pop() + this.pop();
		this.push(sum);
	}
	public void sub(){
		Double sub = this.pop();
		Double reduce = this.pop();
		this.push(reduce - sub);
	}
	public void mul(){
		Double mul = this.pop() * this.pop();
		this.push(mul);
	}
	public void div(){
		Double div = this.pop();
		Double divis = this.pop();
		this.push(divis / div);
	}
	@Override
	public String toString() {
		return "" + list;
	}
}