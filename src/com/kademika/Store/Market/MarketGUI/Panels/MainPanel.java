package com.kademika.Store.Market.MarketGUI.Panels;

import com.kademika.Store.Market.Market;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;

/**
 * Created by dean on 8/17/15.
 */
public class MainPanel extends AbstractPanel{

    private JLabel balanceLable;

    public MainPanel(Market market) {
        setLayout(new BorderLayout());
        add(market.printTransactions(),BorderLayout.CENTER);
        balanceLable = new JLabel("Balance: " + market.getBudget() + "$");
        add(balanceLable, BorderLayout.SOUTH);
    }

    @Override
    public void update(Observable o, Object arg) {
        Market market = (Market) o;
        removeAll();
        add(market.printTransactions(), BorderLayout.CENTER);
        add(new JLabel("Balance: " + market.getBudget() + "$"), BorderLayout.SOUTH);
    }
}
