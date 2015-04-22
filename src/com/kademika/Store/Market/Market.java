package com.kademika.Store.Market;

import com.kademika.Store.Market.Objects.*;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class Market {
	
	private Store store = new Store();
	private ArrayList<Purchase> purchases = new ArrayList<>();
	
	public Market() {

	}

	public void addAnimal(Animal a) {
		store.addAnimal(a);
	}

	public void removeAnimal(Animal a) {
		store.delAnimal(a);
	}

	public void addOnStore(Animal a, int n) {
		store.getOnStore(a, n);
	}

	public void sell(String day, Customer cust, LinkedList<Animal> goods) {
		double price = 0;
		if (goods != null) {
		for (Animal a : goods) {
			store.takeFromStore(a);
			price += a.getPrice();
		}
		purchases.add(new Purchase(day,cust,goods,price));
		cust.spendMoney(price);
		cust.addPurchase();
		cust.clearBucket();
		} else {System.out.println("Bucket is empty");}
	}

	public void addOnStore(Animal a) {
		store.getOnStore(a, 1);
	}
	
	public void printPrices() {
		for (Animal a : store.getAnimals()) {
			System.out.println("Name: " + a.getName() + "\tPrice: " + a.getPrice());
		}
	}
	
	public void printCatalog() {
		
		ArrayList<Animal> anim = (ArrayList<Animal>) store.getAnimals().clone();
		Collections.sort(anim);
		Type category = null;
		for (Animal a : anim) {
			
			if (a.getType() != category) {
				category = a.getType();
				System.out.println();
				System.out.println("Class: " + category);
			}
			System.out.println("Name: " + a.getName() + "\tPrice: " + a.getPrice() + "\tOrder: " + a.getOrder());
		}
		}
	
	public void printStore() {
		store.printStore();
	}
	
	public String getGoodsNames(LinkedList<Animal> a) {
		String result = "";
		for (int i=0, j=1; i < a.size(); i++) {
			if (i == 0) {
                result += a.get(i).getName();
            } else if (a.get(i).equals(a.get(i-1)) && i != a.size()-1) {
                j++;
            } else if (!a.get(i).equals(a.get(i-1))){
                result += "(x" + j +  "), " + a.get(i).getName();
                j=1;
            } else {
                j++;
                result += "(x" + j +  ")";
            }
		}
		return result;
	}

	public ArrayList<Animal> getAnimals() {
		return store.getAnimals();
	}
	
	public JScrollPane printTransactions() {
		int i = 1;
        Object[][] data = new Object[100][5];
		for (Purchase pur : purchases) {
            data[i-1][0] = i;
            data[i-1][1] = pur.getDate();
            data[i-1][2] = pur.getCustomer().getName();
            data[i-1][3] = getGoodsNames(pur.getGoods());
            data[i-1][4] = pur.getPrice();
            i++;
		}
        String[] columsNames = {"Id ", "Date", "Customer name", "Goods", "Price"};
        JTable table = new JTable(data , columsNames);
        TableColumn column = null;
        column = table.getColumnModel().getColumn(0);
        column.setPreferredWidth(25);
        column = table.getColumnModel().getColumn(1);
        column.setPreferredWidth(150);
        column = table.getColumnModel().getColumn(3);
        column.setPreferredWidth(200);
        JScrollPane sp = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        return sp;
	}

	public void init() {
		final LinkedList<Customer> cust = new LinkedList<>();
		Animal tiger = new Animal();
		tiger.setName("tiger");
		tiger.setOrder("Carnivora"); // Hishnik
		tiger.setType(Type.Mammal); // Mlekopitauhee
		tiger.setPrice(500);
		addAnimal(tiger);
		addOnStore(tiger, 5);

		Animal rabbit = new Animal();
		rabbit.setName("rabbit");
		rabbit.setOrder("Lagomorpha"); // Zytseobraznie
		rabbit.setType(Type.Mammal); // Mlekopitauhee
		rabbit.setPrice(10);
		addAnimal(rabbit);
		addOnStore(rabbit, 45);

		Animal raccoon = new Animal();
		raccoon.setName("raccoon");
		raccoon.setOrder("Carnivora");
		raccoon.setType(Type.Mammal);
		raccoon.setPrice(100);
		addAnimal(raccoon);
		addOnStore(raccoon, 10);

		Animal penguin = new Animal();
		penguin.setName("penguin");
		penguin.setOrder("Sphenisciformes");
		penguin.setType(Type.Bird);
		penguin.setPrice(80);
		addAnimal(penguin);
		addOnStore(penguin, 15);

		Animal chameleon = new Animal();
		chameleon.setName("chameleon");
		chameleon.setOrder("Squamata");
		chameleon.setType(Type.Reptile);
		chameleon.setPrice(20);
		addAnimal(chameleon);
		addOnStore(chameleon, 20);

		Animal iguana = new Animal();
		iguana.setName("iguana");
		iguana.setOrder("Squamata");
		iguana.setType(Type.Reptile);
		iguana.setPrice(30);
		addAnimal(iguana);
		addOnStore(iguana, 25);

		Animal owl = new Animal();
		owl.setName("owl");
		owl.setOrder("Strigiformes"); // Zaytseobraznie
		owl.setType(Type.Bird);
		owl.setPrice(30);
		addAnimal(owl);
		addOnStore(owl, 40);

		Animal parrot = new Animal();
		parrot.setName("parrot");
		parrot.setOrder("Psittaciformes");
		parrot.setType(Type.Bird);
		parrot.setPrice(10);
		addAnimal(parrot);
		addOnStore(parrot, 15);

		Animal sunfish = new Animal();
		sunfish.setName("sunfish");
		sunfish.setOrder("Tetraodontiformes"); // Kostnie
		sunfish.setType(Type.Fish);
		sunfish.setPrice(2000);
		addAnimal(sunfish);
		addOnStore(sunfish, 5);

		Animal boa = new Animal();
		boa.setName("boa");
		boa.setOrder("Squamata");
		boa.setType(Type.Reptile);
		boa.setPrice(100);
		addAnimal(boa);
		addOnStore(boa, 10);

		Animal frog = new Animal();
		frog.setName("frog");
		frog.setOrder("Anura");
		frog.setType(Type.Amphibian);
		frog.setPrice(15);
		addAnimal(frog);
		addOnStore(frog, 20);

		Animal aligator = new Animal();
		aligator.setName("aligator");
		aligator.setOrder("Crocodilia");
		aligator.setType(Type.Reptile);
		aligator.setPrice(500);
		addAnimal(aligator);
		addOnStore(aligator, 4);

		Animal newt = new Animal();
		newt.setName("newt");
		newt.setOrder("Caudata");
		newt.setType(Type.Amphibian);
		newt.setPrice(5);
		addAnimal(newt);
		addOnStore(newt, 50);
	}

	// makeADiscount(int disc);

}
