package ru.progwards.java1.lessons.queues;
import java.util.*;

public class Calculate {
	public static double calculation1(){
		LinkedList<Double> list = new LinkedList<>();
		StackCalc stack = new StackCalc();
		stack.push(2.2);
		stack.push(12.1);
		stack.push(3);
		stack.add();
		stack.mul();
		return stack.pop();
	}
	public static double calculation2(){
		LinkedList<Double> list = new LinkedList<>();
		StackCalc stack = new StackCalc();
		stack.push(737.22);
		stack.push(24);
		stack.add();
		double res1 = stack.pop();
		stack.push(55.6);
		stack.push(12.1);
		stack.sub();
		double res2 = stack.pop();
		stack.push(19);
		stack.push(3.33);
		stack.sub();
		double res3 = stack.pop();
		stack.push(87);
		stack.push(2);
		stack.push(13.001);
		stack.push(9.2);
		stack.sub();
		stack.mul();
		stack.add();
		double res4 = stack.pop();
		stack.push(res1);
		stack.push(res2);
		stack.div();
		double res5 = stack.pop();
		stack.push(res3);
		stack.push(res4);
		stack.mul();
		double res6= stack.pop();
		stack.push(res5);
		stack.push(res6);
		stack.add();
		return stack.pop();
	}
	public static void main(String[] args) {
		System.out.println(calculation1());
		System.out.println(calculation2());
	}
}
