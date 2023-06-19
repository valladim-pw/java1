package ru.progwards.java2.lessons.recursion;

import java.time.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GoodsWithLambda {
	List<Goods> goods = new ArrayList<>();
	
	public GoodsWithLambda(){}
	
	public void setGoods(List<Goods> list) {
		
		goods = list.stream().collect(Collectors.toList());
		printGoods("setGoods:", goods);
	}
	
	public List<Goods> sortByName() {
		
		List<Goods> list = goods.stream().sorted(Comparator.comparing(x -> x.name)).collect(Collectors.toList());
		
		printGoods("sortByName:", list);
		return goods;
	}
	
	public List<Goods> sortByNumber() {
		List<Goods> list = goods.stream().sorted(Comparator.comparing(x -> x.number.toLowerCase())).collect(Collectors.toList());
		
		printGoods("sortByNumber:", list);
		return list;
	}
	
	public List<Goods> sortByPartNumber() {
		
		List<Goods> list = goods.stream()
						.sorted(Comparator.comparing(x -> x.number.replace("_", "").substring(0, 3).toLowerCase()))
						.collect(Collectors.toList());
		
		printGoods("sortByPartNumber:", list);
		return list;
	}
	
	public List<Goods> sortByAvailabilityAndNumber(){
		
		List<Goods> list = goods.stream().sorted(Comparator.comparing(x -> x.number.toLowerCase()))
						.sorted(Comparator.comparingInt(x -> x.available)).collect(Collectors.toList());
		
		printGoods("sortByAvailabilityAndNumber:", list);
		
		return list;
	}
	
	public List<Goods> expiredAfter(Instant date) {
		
		List<Goods> list = goods.stream().sorted(Comparator.comparingLong(x -> x.expired.getEpochSecond()))
						.dropWhile(x -> x.expired.isBefore(date)).collect(Collectors.toList());
						
		printGoods("expiredAfter:", list);
		return list;
	}
	
	public List<Goods> countLess(int count) {
		
		List<Goods> list = goods.stream().sorted(Comparator.comparingInt(x -> x.available))
						.takeWhile(x -> x.available < count).collect(Collectors.toList());
		
		printGoods("countLess:", list);
		return list;
	}
	
	public List<Goods> сountBetween(int count1, int count2) {
		
		Predicate<Goods> greater = x -> x.available > count1;
		Predicate<Goods> below = x -> x.available < count2;
		
		List<Goods> list = goods.stream().sorted(Comparator.comparingInt(x -> x.available))
						.filter(below.and(greater)).collect(Collectors.toList());
		
		
		printGoods("countBetween:", list);
		return list;
	}
	
	public void printGoods(String str, List<Goods> list) {
		
		System.out.println(str);
		list.forEach(System.out::println);
		System.out.println("---------------------------------");
	}
	
	public static void main(String[] args) {
		List<Goods> goods = new ArrayList<>(List.of(
						new Goods("Кофе", "БФ_НФ_Р0_РС_СТ", 30, 270.0, "2025-02-13"),
						new Goods("Тунец", "AРБ_ТН_Р0_ВМ_КН", 50, 170.0, "2024-01-01"),
						new Goods("Молоко сгущеное", "ГЛ_РГ_БЛ_СГ_КН", 40, 110.0, "2025-06-06"),
						new Goods("Чай Майский", "ВЧА_МА_Р0_ЧР_ПК", 20, 150.0, "2024-12-03"),
						new Goods("Пиво Балтика", "АПВ_БА_РО_СВ_БТ", 30, 250.0, "2023-12-15")
		));
		GoodsWithLambda gwl = new GoodsWithLambda();
		gwl.setGoods(goods);
		gwl.sortByName();
		gwl.sortByNumber();
		gwl.sortByPartNumber();
		gwl.sortByAvailabilityAndNumber();
		Instant ins = Instant.from(LocalDateTime.of(2024, 06, 16, 0, 0, 0).atZone(ZoneId.systemDefault()));
		gwl.expiredAfter(ins);
		gwl.countLess(32);
		gwl.сountBetween(12, 31);
	}
}
