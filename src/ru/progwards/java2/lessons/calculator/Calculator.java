package ru.progwards.java2.lessons.calculator;

import java.util.Comparator;
import java.util.LinkedList;

public class Calculator {
	static LinkedList<Integer> bracesResult = new LinkedList<>();
	public static int calculate(String expression) {
		LinkedList<String> operands = new LinkedList<>();
		LinkedList<String> operators = new LinkedList<>();
		if(expression.indexOf("(") != -1) {
			while(expression.indexOf("(") != -1) {
				String bracesSub = bracesSubstr(expression);
				String sub = substr(expression);
				bracesResult.push(calculate(sub)) ;
				expression = expression.replace(bracesSub, "R");
			}
		}
		char[] values = expression.toCharArray();
		for(Character ch : values) {
			if(!Character.isLetterOrDigit(ch))
				operators.add(Character.toString(ch));
			if(Character.isLetter(ch))
				operands.add(Integer.toString(bracesResult.pop()));
			else
				operands.add(Character.toString(ch));
		}
		operators.sort(Comparator.comparing(Calculator::priority));
		int i = 0;
		while(operands.size() > 1) {
			if(operands.get(i).equals(operators.peek())) {
				operands.set(i - 1, operation(operands.get(i - 1), operands.remove(i + 1), operands.remove(i)));
				operators.pop();
				i = 0;
				continue;
			}
			i++;
		}
		return Integer.valueOf(operands.getFirst());
	}
	public static int toInt(String str) {
		return Integer.valueOf(str).intValue();
	}
	public static String bracesSubstr(String str) {
		return str.substring(str.lastIndexOf("("), str.indexOf(")", str.lastIndexOf("(")) + 1);
	}
	public static String substr(String str) {
		return str.substring(str.lastIndexOf("(") + 1, str.indexOf(")", str.lastIndexOf("(")));
	}
	public static String operation(String operand1, String operand2, String operator) {
		int result = toInt(operand1);
		switch(operator) {
			case "*" :
				result *= toInt(operand2); break;
			case "/" :
				result /= toInt(operand2); break;
			case "-" :
				result -= toInt(operand2); break;
			default:
				result += toInt(operand2);
		}
		return Integer.toString(result);
	}
	public static int priority(String operator) {
		int priority = 1;
		if(operator.equals("/") || operator.equals("*"))
			priority = 0;
		return priority;
	}
	public static void main(String[] args) {
		System.out.println(calculate("(2-8)*(2*((4+1*2)/3+2))/(3-7)"));
		int res = (2-8)*(2*((4+1*2)/3+2))/(3-7);
	}
}
