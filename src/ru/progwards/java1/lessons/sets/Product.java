package ru.progwards.java1.lessons.sets;
import java.util.*;

public class Product {
	private String code;
	@Override
	public String toString() {
		return "{" + code + "}";
	}
	public Product(String code){
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Product product = (Product) o;
		return Objects.equals(code, product.code);
	}
	@Override
	public int hashCode() {
		return Objects.hash(code);
	}
}
