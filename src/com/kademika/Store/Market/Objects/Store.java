package com.kademika.Store.Market.Objects;

import java.util.ArrayList;

public class Store {

	private ArrayList<Animal> animals = new ArrayList<>();

	public Store() {

	}

	public void getOnStore(Animal a, int n) {
		a.setAmount(a.getAmount()+n);
	}

	public void takeFromStore(Animal a, int n) {
		if (a.getAmount() >= n) {
			a.setAmount(a.getAmount()-n);
		} else {
			System.out.println("There is only" + a.getAmount() + " items");
		}
	}

	public void takeFromStore(Animal a) {
		if (a.getAmount() > 0) {
		a.setAmount(a.getAmount()-1);
		} else {
			System.out.println("There is 0 items on Store");
		}
	}

	public boolean checkAmount(Animal a) {
		if (a.getAmount() >= 3) {
			return false;
		}
		System.out.println("There is only" + a.getAmount() + " items" );
		return true;
	}
	
	public void addAnimal(Animal a) {
		animals.add(a);
	}

	public void delAnimal(Animal a) {
		animals.remove(a);
	}

	public void printStore() {
			for (Animal a : animals) {
				System.out.println("Name: " + a.getName() + "\tAmount: " + a.getAmount());
			}
	}

	public ArrayList<Animal> getAnimals() {
		return animals;
	}
	
}
