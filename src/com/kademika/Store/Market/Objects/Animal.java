package com.kademika.Store.Market.Objects;

public class Animal implements Comparable<Animal>{

    private static int current_id = 0;
    private int id;
	private String name;
	private double price;
    private int amount;
	private Type type; // Class: Aka Amphibian, Mammal

	public Animal() {
        current_id++;
        this.id = current_id;
	}

    public Animal(int id) {
        current_id++;
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrice(double price) {
        this.price = price;
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
