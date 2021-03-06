package ru.progwards.java1.lessons.classes;

class Duck extends Animal{
	public Duck(double weight){
		super(weight);
	}
	@Override
	public AnimalKind getKind(){
		animalKind = AnimalKind.DUCK;
		return animalKind;
	}
	@Override
	public FoodKind getFoodKind(){
		foodKind = FoodKind.CORN;
		return foodKind;
	}
	@Override
	public double getFoodCoeff(){
		foodCoeff = 0.04;
		return foodCoeff;
	}
}
