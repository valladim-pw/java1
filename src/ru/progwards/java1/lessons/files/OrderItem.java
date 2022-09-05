package ru.progwards.java1.lessons.files;

public class OrderItem{
	public String googsName;
	public int count;
	public double price;
	public static int ioCount = 0;
	/*
	* ! При отсутствии конструктора без аргументов
	* код успешно компилируется и выполняется в InlelliJ IDEA и командной строке
	* но не проходит проверку Робота:
	* Ошибка: constructor OrderItem in class ru.progwards.java1.lessons.files.OrderItem cannot be applied to given types;
  * required: java.lang.String,int,double
  * found: no arguments
  * reason: actual and formal argument lists differ in length
	*/
	//public OrderItem(){}//конструктор без аргументов
	public OrderItem(){
		ioCount++;
	}
	public OrderItem(String goodsName, int count, double price){
		this.googsName = goodsName;
		this.count = count;
		this.price = price;
	}
	public String getGoodsName(){
		return googsName;
	}
	public double getSum(){
		return count * price;
	}
	@Override
	public String toString(){
		return googsName + " " + count + " " + price + "\n";
	}
}
