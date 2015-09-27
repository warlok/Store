package com.kademika.Store.Market.Objects;

import java.util.HashMap;

public class Transaction {

    private int id;
	private String date;
	private Customer customer;
	private HashMap<Animal,Integer> goods;
	private double price;

	public Transaction(String date, Customer customer, HashMap<Animal,Integer> goods, double price) {
        this.date = date;
		this.customer = customer;
		this.goods = goods;
		this.price = price;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setGoods(HashMap<Animal, Integer> goods) {
        this.goods = goods;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Transaction(int id) {
        this.id = id;
    }

	public String getDate() {
		return date;
	}

	public Customer getCustomer() {
		return customer;
	}

	public HashMap<Animal,Integer> getGoods() {
		return goods;
	}

	public double getPrice() {
		return price;
	}
	
}