package ru.progwards.java2.lessons.recursion;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Goods {
	String name;
	String number;
	int available;
	double price;
	Instant expired;
	
	public Goods(String name, String number, int available, double price, String time) {
		this.name = name;
		this.number = number;
		this.available = available;
		this.price = price;
		LocalDate date = LocalDate.parse(time.trim(), DateTimeFormatter.ISO_DATE);
		ZonedDateTime zdt = date.atStartOfDay(ZoneId.systemDefault());
		this.expired = Instant.from(zdt);
	}
	
	@Override
	public String toString() {
		return 	"name: " + name +
						", number: " + number +
						", available: " + available +
						", price: " + price +
						", expired: " + expired;
	}
}
