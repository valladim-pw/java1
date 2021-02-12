package ru.progwards.java1.lessons.basics;

public class AccuracyDoubleFloat {
	public static void main(String[] args) {
		System.out.println(volumeBallDouble(6371.2));
		System.out.println(volumeBallFloat(6371.2f));
		System.out.println(calculateAccuracy(6371.2));
	}
	public static double volumeBallDouble(double radius){
		double index = 4.0 / 3.0;
		double pi = 3.14;
		double R = radius * radius * radius;
		double volume = index * pi * R;
		return volume;
	}
	public static float volumeBallFloat(float radius){
		float index = 4.0f / 3.0f;
		float pi = 3.14f;
		float R = radius * radius * radius;
		float volume = index * pi * R;
		return volume;
	}
	public static double calculateAccuracy(double radius) {
		return volumeBallDouble(radius) - volumeBallFloat((float)radius);
	}
}
