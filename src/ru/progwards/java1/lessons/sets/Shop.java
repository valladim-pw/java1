package ru.progwards.java1.lessons.sets;
import java.util.*;

public class Shop {
	private List<Product> products;
	public Shop(List<Product> products){
		this.products = products;
	}
	public List<Product> getProducts() {
		return products;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Shop shop = (Shop) o;
		return Objects.equals(products, shop.products);
	}
	@Override
	public int hashCode() {
		return Objects.hash(products);
	}
	@Override
	public String toString() {
		return "Shop->" +  products + "\n";
	}
}
