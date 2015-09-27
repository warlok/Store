package com.kademika.Store.Market.MarketGUI.Panels;

import com.kademika.Store.Market.Market;
import com.kademika.Store.Market.Objects.Animal;
import com.kademika.Store.Market.Objects.Customer;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by dean on 8/16/15.
 */
public abstract class AbstractPanel extends JPanel implements Observer {

    protected JTextField name;
    protected JTextField amountAnimals;
    protected JButton buttonAdd;
    protected JButton buttonRemove;
    protected JButton buy;
    protected JComboBox petList;

    protected String getAnimalsInBasket(Customer cust) {
        String result = "";
        if (cust.getBucket().isEmpty()) {
            return "Basket is empty";
        }
        for (Iterator<Animal> iter = cust.getBucket().keySet().iterator();iter.hasNext();) {
            Animal animal = iter.next();
            if (iter.hasNext()) {
                result += animal.getName() + "(x" + cust.getBucket().get(animal) + "),";
            } else {
                result += animal.getName() + "(x" + cust.getBucket().get(animal) + ");";
            }
        }
        return result;
    }

    protected String getAnimalsInBasket(HashMap<Animal,HashMap<Integer,Double>> bucket) {
        String result = "";
        if (bucket.isEmpty()) {
            return "Basket is empty";
        }
        for (Iterator<Animal> iter = bucket.keySet().iterator();iter.hasNext();) {
            Animal animal = iter.next();
            Set<Integer> amount = bucket.get(animal).keySet();
            int amount_anim = amount.iterator().next();
            if (iter.hasNext()) {
                result += animal.getName() + "(x" + amount_anim + "),";
            } else {
                result += animal.getName() + "(x" + amount_anim + ");";
            }
        }
        return result;
    }

    protected boolean checkString(String string) {
        try {
            Double.parseDouble(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void update(Observable o, Object arg) {
        Market market = (Market) o;
        remove(petList);
        petList = new JComboBox(market.getAnimals().values().toArray());
        petList.setRenderer(new ObjectBoxRenderer());
        petList.setMaximumRowCount(5);
        add(petList, new GridBagConstraints(1, 1, 1, 1, 0, 0,
                GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
    }
}
