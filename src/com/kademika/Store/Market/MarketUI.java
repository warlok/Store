package com.kademika.Store.Market;

import com.kademika.Store.Market.Objects.Animal;
import com.kademika.Store.Market.Objects.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class MarketUI {

    private SimpleDateFormat sdfDate = new SimpleDateFormat("MM.dd.yyyy hh:mm");
	private LinkedList<Customer> cust;
	private JPanel panel1;
	private JPanel panel2;
	private JFrame frame;
	private JTextField text;
	private Animal[] pets;
	private String[] petStrings;
	private JComboBox petList;
	private JTextField amountAnimals;
	private JButton button;
	private JButton buy;

	public MarketUI(final Market market) {
		SplashScreen splashScreen = SplashScreen.getSplashScreen();
		cust = new LinkedList<>();

		panel1 = new JPanel();
        panel2 = new JPanel();
        panel2.add(market.printTransactions());
        panel1.setLayout(new GridBagLayout());

		frame = new JFrame("Petshop");
		frame.setBounds(350, 50, 300, 500);
		frame.setContentPane(panel2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem menuItem = new JMenuItem("Buy Animals");
        menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setContentPane(panel1);
				frame.pack();
			}
		});

		menu.add(menuItem);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
		JLabel lable = new JLabel("Customer's Name:");
		text = new JTextField(10);

		pets = new Animal[market.getAnimals().size()];
		pets = market.getAnimals().toArray(pets);
		petStrings = new String[pets.length];
		for (int i=0; i< pets.length; i++) {
			petStrings[i] = pets[i].getName();
		}
		petList = new JComboBox(petStrings);
		petList.setMaximumRowCount(5);

		JLabel amountLable = new JLabel("Input Amount:");
		amountAnimals = new JTextField(2);
		button = new JButton("Add");
		buy = new JButton("BUY");
		JLabel lablePetsList = new JLabel("Choose an Animal:");

		panel1.add(lable, new GridBagConstraints(0, 0, 1, 1, 0, 0,
                GridBagConstraints.LINE_START, 0, new Insets(0, 0, 0, 0), 0, 0));
        panel1.add(text, new GridBagConstraints(1, 0, 1, 1, 0, 0,
                GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        panel1.add(lablePetsList, new GridBagConstraints(0, 1, 1, 1, 0, 0,
                GridBagConstraints.LINE_START, 0, new Insets(0, 0, 0, 0), 0, 0));
        panel1.add(petList, new GridBagConstraints(1, 1, 1, 1, 0, 0,
                GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        panel1.add(amountLable, new GridBagConstraints(0, 2, 1, 1, 0, 0,
                GridBagConstraints.LINE_START, 0, new Insets(0, 0, 0, 0), 0, 0));
        panel1.add(amountAnimals,
                new GridBagConstraints(1, 2, 1, 1, 0, 0,
                        GridBagConstraints.LINE_START, 0,
                        new Insets(5, 0, 0, 0), 15, 7));
        panel1.add(button,
                new GridBagConstraints(1, 2, 1, 1, 0, 0,
                        GridBagConstraints.LINE_START, 0, new Insets(5, 47, 0,
                        0), 0, 0));
        panel1.add(buy,
				new GridBagConstraints(1, 3, 1, 1, 0, 0,
						GridBagConstraints.LINE_START, 0, new Insets(50, 0, 0,
						0), 0, 0));

		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
                Customer c;
				if (!cust.isEmpty() && cust.getFirst().getName().equals(text.getText())) {
					c = cust.getFirst();
				} else {
					c = new Customer();
					c.setName(text.getText());
					cust.addFirst(c);
				}
				int counts = Integer.valueOf(amountAnimals.getText());
				c.addOnBucket(findAnimal(pets, (String) petList.getSelectedItem()), counts);
			}
		});

		buy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Customer cu = cust.getFirst();
				for (int i = 0; i < 100; i++) {
					System.out.println("\n\n\n\n");
				}
				market.sell(sdfDate.format(new Date()), cu, cu.getBucket());
				market.printStore();
				panel2.remove(0);
				panel2.add(market.printTransactions());
				frame.setContentPane(panel2);
				frame.pack();

			}
		});

		frame.pack();

		try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            System.err.println("Caught exeption");
        }

		try {
			splashScreen.close();
		} catch (NullPointerException e) {
			System.err.println("Add in your JVM config VM options: -splash:loading.gif");
			System.exit(-1);
		}
		frame.setVisible(true);
	}

	private Animal findAnimal(Animal[] array,String name) {
		for (Animal a : array) {
			if (a.getName().equals(name)) {
				return a;
			}
		}
		return null;
	}
}
