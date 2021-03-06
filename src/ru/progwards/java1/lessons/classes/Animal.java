package ru.progwards.java1.lessons.classes;

class Animal {
	public static void main(String[] args) {
		Animal animal = new Animal(1000);
		System.out.println(animal.toStringFull());
		Cow cow = new Cow(1500);
		System.out.println(cow.toStringFull());
		Hamster hamster = new Hamster(0.5);
		System.out.println(hamster.toStringFull());
		Duck duck = new Duck(1);
		System.out.println(duck.toStringFull());
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
	@Override
	public String toString() {
		return "I am " + getKind() + ", eat " + getFoodKind();
	}
	public String toStringFull() {
		return "I am " + getKind() + ", eat " + getFoodKind() + " " + calculateFoodWeight();
	}
}
