package ru.progwards.java1.lessons.interfaces;

public class Food implements CompareWeight{
	public static void main(String[] args) {
		Food food1 = new Food(40);
		Food food2 = new Food(13);
		Food food3 = new Food(1);
		Food food4 = new Food(7);
		System.out.println(food1.compareWeight(food2));
		CompareWeight[] foodWeights = {food1, food2, food3, food4};
		ArraySort.sort(foodWeights);
	}
	private int weight;
	public Food(int weight) {
		this.weight = weight;
	}
	public int getWeight() {
		return weight;
	}
	public CompareResult compareWeight(CompareWeight smthHasWeigt){
		CompareResult compareResult;
		if(Double.compare(this.weight, ((Food) smthHasWeigt).getWeight()) == 1)
			compareResult  = CompareResult.GREATER;
		else if(Double.compare(this.weight, ((Food) smthHasWeigt).getWeight()) == -1)
			compareResult  = CompareResult.LESS;
		else
			compareResult = CompareResult.EQUAL;
		return compareResult;
	}
	@Override
	public String toString() {
		return "Food weight: " + getWeight();
	}
}
