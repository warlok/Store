package com.kademika.Store.Market.Objects;

import java.util.LinkedList;

public class Customer {

	private String name;
	private int amountPurchases;
	private double spendMoney;
	private LinkedList<Animal> bucket = new LinkedList<>();

	public Customer() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAmountPurchases() {
		return amountPurchases;
	}

	public double getSpendMoney() {
		return spendMoney;
	}

	public void addPurchase() {
		amountPurchases++;
	}

	public void spendMoney(double SpendMoney) {
		this.spendMoney += SpendMoney;
	}

	public void addOnBucket(Animal a, int n) {
		for (int i = 0; i < n; i++) {
			bucket.add(a);
		}
	}

	public void addOnBucket(Animal a) {
		bucket.add(a);
	}
	
	public void delFromBucket(Animal a, int n) {
		for (int i = 0; i < n; i++) {
			if (bucket.contains(a)) {
				bucket.remove(a);
			}
		}
	}

	public void delFromBucket(Animal a) {
		bucket.remove(a);
	}

	public LinkedList<Animal> getBucket() {
		return bucket;
	}
	
	public void clearBucket() {
		bucket = new LinkedList<>();
	}

}
