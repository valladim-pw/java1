package ru.progwards.java2.lessons.recursion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GoodsWithLambda {
	List<Goods> goods = new ArrayList<>();
	
	public GoodsWithLambda(){}
	
	public void setGoods(List<Goods> list) {
		goods = list.stream().collect(Collectors.toList());
		goods.forEach(System.out::println);
	}
	
	public static void main(String[] args) {
		List<Goods> goods = new ArrayList<>(List.of(
						new Goods("Минтай", "РБ_МН_Р0_ВМ_КН", 70, 170.0, "2024-01-01"),
						new Goods("Майский чай", "ЧА_МА_Р0_ЧР_ПК", 50, 150.0, "2024-12-03"),
						new Goods("Кофе растворимый", "КФ_НФ_Р0_РС_СТ", 20, 270.0, "2025-02-13"),
						new Goods("Молоко сгущеное", "МЛ_РГ_БЛ_СГ_КН", 30, 110.0, "2025-06-06"),
						new Goods("Масло оливковое", "МС_ОЛ_РО_ОЧ_СТ", 10, 250.0, "2023-12-15")
		));
		GoodsWithLambda gwl = new GoodsWithLambda();
		gwl.setGoods(goods);
	}
}
