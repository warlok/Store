package com.kademika.Store.Market.Objects;

import java.util.LinkedList;

public class Purchase {

	private String date;
	private Customer customer;
	private LinkedList<Animal> goods;
	private double price;
	
	public Purchase(String date, Customer customer, LinkedList<Animal> goods, double price) {
		this.date = date;
		this.customer = customer;
		this.goods = goods;
		this.price = price;
	}

	public String getDate() {
		return date;
	}

	public Customer getCustomer() {
		return customer;
	}

	public LinkedList<Animal> getGoods() {
		return goods;
	}

	public double getPrice() {
		return price;
	}
	
}