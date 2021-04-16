package ru.progwards.java1.lessons.interfaces;
import static ru.progwards.java1.lessons.interfaces.Animal.*;

public class Food {
	private int weight;
	public Food(int weight) {
		this.weight = weight;
	}
	public int getWeight() {
		return weight;
	}
	public CompareResult compareWeight(CompareWeight smthHasWeigt){
		CompareResult compareResult;
		if(Double.compare(this.weight, ((Animal) smthHasWeigt).calculateFoodWeight()) == 1)
			compareResult  = CompareResult.GREATER;
		else if(Double.compare(this.weight, ((Animal) smthHasWeigt).calculateFoodWeight()) == -1)
			compareResult  = CompareResult.LESS;
		else
			compareResult = CompareResult.EQUAL;
		return compareResult;
	}
}
