package ru.progwards.java1.lessons.interfaces;

public class Animal implements FoodCompare,CompareWeight {
	public static void main(String[] args) {
		Animal animal = new Animal(150);
		Animal animal2 = new Animal(980);
		Duck duck = new Duck(1.5);
		Cow cow = new Cow(1000);
		Hamster hamster = new Hamster(0.5);
		System.out.println(duck.getFoodPrice());
		System.out.println(cow.compareFoodPrice(hamster));
		System.out.println(hamster.compareFoodPrice(duck));
		System.out.println(animal.compareWeight(animal2));
		Food food1 = new Food(-40);
		Food food2 = new Food(13);
		Food food3 = new Food(1);
		Food food4 = new Food(7);
		System.out.println(food1.compareWeight(food2));
		CompareWeight[] foodWeights = {food1, food2, food3, food4};
		ArraySort.sort(foodWeights);
		CompareWeight[] weights = {animal, animal2, cow, hamster, duck};
		ArraySort.sort(weights);
	}
	double weight;
	AnimalKind animalKind;
	FoodKind foodKind;
	double foodCoeff;
	double eatWeight;
	enum AnimalKind{
		ANIMAL,
		COW,
		HAMSTER,
		DUCK
	}
	enum FoodKind{
		UNKNOWN,
		HAY,
		CORN
	}
	public Animal(double weight){
		this.weight = weight;
	}
	public AnimalKind getKind(){
		animalKind = AnimalKind.ANIMAL;
		switch(animalKind){
			case COW:
				animalKind = AnimalKind.COW;
				break;
			case HAMSTER:
				animalKind = AnimalKind.HAMSTER;
				break;
			case DUCK:
				animalKind = AnimalKind.DUCK;
				break;
			default:
				animalKind = AnimalKind.ANIMAL;
		}
		return animalKind;
	}
	public FoodKind getFoodKind() {
		foodKind = FoodKind.UNKNOWN;
		switch(foodKind){
			case HAY:
				foodKind = FoodKind.HAY;
				break;
			case CORN:
				foodKind = FoodKind.CORN;
				break;
			default:
				foodKind = FoodKind.UNKNOWN;
		}
		return foodKind;
	}
	public double getWeight(){
		return weight;
	}
	public double getFoodCoeff(){
		foodCoeff = 0.02;
		return foodCoeff;
	}
	public double calculateFoodWeight(){
		foodCoeff = getFoodCoeff();
		eatWeight = weight * foodCoeff;
		return eatWeight;
	}
	public double getFood1kgPrice(){
		switch(getFoodKind()){
			case HAY:
				return 20;
			case CORN:
				return 50;
			default:
				return 0;
		}
	}
	public double getFoodPrice(){
		return calculateFoodWeight() * getFood1kgPrice();
	}
	@Override
	public int compareFoodPrice(Animal animal){
		return Double.compare(this.getFoodPrice(), animal.getFoodPrice());
	}
	@Override
	public CompareResult compareWeight(CompareWeight smthHasWeigt){
		CompareResult compareResult;
		if(Double.compare(this.weight, ((Animal) smthHasWeigt).getWeight()) == 1)
			compareResult  = CompareResult.GREATER;
		else if(Double.compare(this.weight, ((Animal) smthHasWeigt).getWeight()) == -1)
			compareResult  = CompareResult.LESS;
		else
			compareResult = CompareResult.EQUAL;
		return compareResult;
	}
	@Override
	public boolean equals(Object anObject){
			if (this == anObject) return true;
			if (anObject == null || getClass() != anObject.getClass()) return false;
			Animal animal = (Animal) anObject;
			if(Double.compare(animal.weight, weight) == 0)
				return true;
			else
				return false;
	}
	@Override
	public String toString() {
		return getKind() + ": " + getWeight();
	}
	public String toStringFull() {
		return "I am "+getKind()+", eat "+getFoodKind()+"|"+calculateFoodWeight()+"|"+getFood1kgPrice()+"|"+getFoodPrice();
	}
}