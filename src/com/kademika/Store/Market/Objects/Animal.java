package com.kademika.Store.Market.Objects;

public class Animal implements Comparable<Animal>{

	private String name;
	private double price;
    private int amount;
	private Type type; // Class: Aka Amphibian, Mammal
	private String order; // Primates

	public Animal() {
		
	}

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public double getPrice() {
		return price;
	}

	public void setPrice(int price) {
		if (price > 0) {
		this.price = price;
		}
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
	
	@Override
	public int compareTo(Animal a) {
		String alphabet="MBRFAmr";
		char symbol = a.getType().toString().charAt(0);
		int category = alphabet.indexOf(symbol);
		if (category == 4) {
			symbol = a.getType().toString().charAt(1);
			category = alphabet.indexOf(symbol);
		}
		return alphabet.indexOf(this.type.toString().charAt(0)) - category;
	}
	
}
