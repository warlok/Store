package com.kademika.Store;

import com.kademika.Store.Market.Market;
import com.kademika.Store.Market.MarketGUI.MarketUI;

public class MarketDemo {

    public static void main(String[] args) {

        Market m = new Market();
        m.init();
        new MarketUI(m);

    }

}
