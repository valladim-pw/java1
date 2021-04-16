package ru.progwards.java1.lessons.interfaces;

class Hamster extends Animal{
	public Hamster(double weight){
		super(weight);
	}
	@Override
	public AnimalKind getKind(){
		animalKind = AnimalKind.HAMSTER;
		return animalKind;
	}
	@Override
	public FoodKind getFoodKind(){
		foodKind = FoodKind.CORN;
		return foodKind;
	}
	@Override
	public double getFoodCoeff(){
		foodCoeff = 0.03;
		return foodCoeff;
	}
}