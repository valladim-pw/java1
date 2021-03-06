package ru.progwards.java1.lessons.classes;

class Cow extends Animal{
	public Cow(double weight){
		super(weight);
	}
	@Override
	public AnimalKind getKind(){
		animalKind = AnimalKind.COW;
		return animalKind;
	}
	@Override
	public FoodKind getFoodKind(){
		foodKind = FoodKind.HAY;
		return foodKind;
	}
	@Override
	public double getFoodCoeff(){
		foodCoeff = 0.05;
		return foodCoeff;
	}
}
