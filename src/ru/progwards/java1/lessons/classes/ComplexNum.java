package ru.progwards.java1.lessons.classes;

public class ComplexNum {
	public static void main(String[] args) {
		ComplexNum num1 = new ComplexNum(3,6);
		ComplexNum num2 = new ComplexNum(2,3);
		System.out.println(num1);
		System.out.println(num2);
		System.out.println(num1.add(num2));
		System.out.println(num1.sub(num2));
		System.out.println(num1.mul(num2));
		System.out.println(num1.div(num2));
	}
	int a, b;
	int i = 1;
	int z;
	public ComplexNum(int a, int b){
		this.a = a;
		this.b = b;
		z = a + b*i;
	}
	@Override
	public String toString() {
		String strA = Integer.toString(a);
		String strB = Integer.toString(b);
		return strA + "+" + strB + "i";
	}
	public ComplexNum add(ComplexNum num){
		ComplexNum x = new ComplexNum(a, b);
		x.z = x.a + num.a;
		num.z = (x.b + num.b)*i;
		ComplexNum sum = new ComplexNum(x.z, num.z);
		return sum;
	}
	public ComplexNum sub(ComplexNum num){
		ComplexNum x = new ComplexNum(a, b);
		x.z = x.a - num.a;
		num.z = (x.b - num.b)*i;
		ComplexNum sub = new ComplexNum(x.z, num.z);
		return sub;
	}
	public ComplexNum mul(ComplexNum num){
		ComplexNum x = new ComplexNum(a, b);
		x.z = x.a * num.a - x.b * num.b;
		num.z = (x.b * num.a + x.a * num.b)*i;
		ComplexNum mul = new ComplexNum(x.z, num.z);
		return mul;
	}
	public ComplexNum div(ComplexNum num){
		ComplexNum x = new ComplexNum(a, b);
		x.z = (x.a * num.a + x.b * num.b) / (num.a * num.a + num.b * num.b);
		num.z = ((x.b * num.a - x.a * num.b) / (num.a * num.a + num.b * num.b))*i;
		ComplexNum div = new ComplexNum(x.z, num.z);
		return div;
	}
}
