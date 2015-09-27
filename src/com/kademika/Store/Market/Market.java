package com.kademika.Store.Market;

import com.kademika.Store.Connector;
import com.kademika.Store.Market.Objects.Animal;
import com.kademika.Store.Market.Objects.Type;
import com.kademika.Store.Market.Objects.Customer;
import com.kademika.Store.Market.Objects.Transaction;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.*;

public class Market extends Observable {

	private ArrayList<Transaction> transactions = new ArrayList<>();
    private HashMap<Integer,Animal> animals = new HashMap<>();
    private HashMap<Integer,Customer> customers = new HashMap<>();
    public final Connector CONNECTOR = new Connector();
    private double budget;
    private int purchase_id;

	public Market() {
//        startServer();
    }

    private void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try (
                        ServerSocket ss = new ServerSocket(9191);
                ) {
                    while (true) {
                        final Socket sock = ss.accept();
                        processSocket(sock);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void processSocket(final Socket sock) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try (InputStream is = sock.getInputStream();
                     BufferedInputStream bis = new BufferedInputStream(is);
                     OutputStream os = sock.getOutputStream();
                     BufferedOutputStream bos = new BufferedOutputStream(os);
                     ObjectOutputStream oos = new ObjectOutputStream(bos)
                ) {
                    while (true) {
                        if ((char) bis.read() == 'a') {
                            break;
                        }
                    }
                    for (Customer customer : customers.values()) {
                        customer.writeExternal(oos);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void addAnimal(Animal animal) {
        animals.put(animal.getId(), animal);
        setChanged();
        notifyObservers();
        Connection connection = CONNECTOR.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO `market`.`Goods` (`id_good`,`name`,`type`,`price`) VALUES (?,?,?,?)");
            stmt.setInt(1, animal.getId());
            stmt.setString(2, animal.getName());
            stmt.setString(3,animal.getType().toString());
            stmt.setDouble(4, animal.getPrice());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

	public void removeAnimal(Animal animal) {
        animals.remove(animal.getId());
        setChanged();
        notifyObservers();
        Connection connection = CONNECTOR.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM `market`.`Goods` WHERE `id_good`=?");
            stmt.setInt(1, animal.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
	}

    public void addCustomer(Customer cust) {
        customers.put(cust.getId(), cust);
        Connection connection = CONNECTOR.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO `market`.`Customers` (`id_cust`,`name`) VALUES (?,?)");
            stmt.setInt(1,cust.getId());
            stmt.setString(2, cust.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

    private void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeCustomer(Customer cust) {
        customers.remove(cust.getId());
        Connection connection = CONNECTOR.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM `market`.`Customers` WHERE `id_cust`=?");
            stmt.setInt(1,cust.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

    public void setPrice(Animal animal, double price) {
        Connection connection = CONNECTOR.getConnection();
        animal.setPrice(price);
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE `market`.`Goods` SET `price`=? WHERE `id_good`=?");
            stmt.setDouble(1, price);
            stmt.setInt(2, animal.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

    public void buyToStore(Animal animal, int amount, double price) {
        int newAmount = animal.getAmount()+amount;
        if (!haveEnoughMoney(price*amount)) {
            System.err.println("You don't have enough money");
            return;
        }
        animal.setAmount(newAmount);
        Connection connection = CONNECTOR.getConnection();
        try {
            String operation = "Bought " + amount + " " + animal.getName() + "(s)";
            moneyFromBudget(operation,price);
            setChanged();
            notifyObservers();
                    PreparedStatement pstmt = connection.prepareStatement(
                            "UPDATE `market`.`Goods` SET `amount`=? WHERE `id_good`=?");
            pstmt.setDouble(1, newAmount);
            pstmt.setInt(2, animal.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

    public boolean takeFromStore(Animal animal, int amount) {
        int newAmount = animal.getAmount()-amount;
        if (newAmount > 0) {
        animal.setAmount(newAmount);
        Connection connection = CONNECTOR.getConnection();
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "UPDATE `market`.`Goods` SET `amount`=? WHERE `id_good`=?");
            pstmt.setDouble(1, newAmount);
            pstmt.setInt(2, animal.getId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
        }
        return false;
    }


	public void sell(String day, Customer cust, HashMap<Animal,Integer> goods) {
        Connection connection = CONNECTOR.getConnection();
        double price = 0;
		if (!goods.isEmpty() && haveEnoughGoods(goods).isEmpty()) {
            try {
                purchase_id ++;
            for (Animal animal : goods.keySet()) {
                int amount = goods.get(animal);
                updatePurchases(connection,purchase_id,animal.getId(),amount);
                takeFromStore(animal, amount);
                price += animal.getPrice() * amount;
            }
            transactions.add(new Transaction(day, cust, goods, price));
                updateTransactions(connection,cust.getId(),purchase_id,price);
                cust.spendMoney(price);
                cust.addPurchase();
                updateCustomer(connection,cust.getId(),cust.getAmountPurchases(),cust.getSpendMoney());
                cust.clearBucket();
                String operation = "Sold animals by purchase #" + purchase_id;
                moneyToBudget(operation,price);
                setChanged();
                notifyObservers();
        } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeConnection(connection);
            }
        } else {System.out.println("Bucket is empty");}
	}
	
	public void printPrices() {
		for (Animal a : animals.values()) {
			System.out.println("Name: " + a.getName() + "\tPrice: " + a.getPrice());
		}
	}
	
	public void printCatalog() {
		
		Animal[] anim = (Animal[]) animals.values().toArray();
        Arrays.sort(anim);
		Type category = null;
		for (Animal a : anim) {
			
			if (a.getType() != category) {
				category = a.getType();
				System.out.println();
				System.out.println("Class: " + category);
			}
			System.out.println("Name: " + a.getName() + "\tPrice: " + a.getPrice());
		}
		}

    public boolean checkAmount(Animal a) {
        if (a.getAmount() >= 3) {
            return false;
        }
        System.out.println("There is only" + a.getAmount() + " items" );
        return true;
    }

    public void printStore() {
        for (Animal a : animals.values()) {
            System.out.println("Name: " + a.getName() + "\tAmount: " + a.getAmount());
        }
    }
	
	public String getGoodsNames(HashMap<Animal,Integer> bucket) {
		String result = "";
		for (Animal animal : bucket.keySet()) {
                result += animal.getName() + "(x" + bucket.get(animal) + "); ";
		}
		return result;
	}

	public HashMap<Integer,Animal> getAnimals() {
		return animals;
	}
	
	public JScrollPane printTransactions() {
		int i = 1;
        Object[][] data = new Object[100][5];
		for (Transaction tr : transactions) {
            data[i-1][0] = i;
            data[i-1][1] = tr.getDate();
            data[i-1][2] = tr.getCustomer().getName();
            data[i-1][3] = getGoodsNames(tr.getGoods());
            data[i-1][4] = tr.getPrice();
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
        Connection connection = CONNECTOR.getConnection();
        try {
            getGoods(connection);
            getCustomers(connection);
            getTransactions(connection);
            getBudget(connection);
            purchase_id = maxID(connection,"Purchases","id_purch");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }

	}

    private void getGoods(Connection connection) throws SQLException {
        ResultSet result = null;
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM `market`.`Goods`");
        result = stmt.executeQuery();
        while (result.next()) {
            Animal animal = new Animal(result.getInt("id_good"));
            animal.setName(result.getString("name"));
            animal.setPrice(result.getDouble("price"));
            animal.setAmount(result.getInt("amount"));
            animal.setType(Type.valueOf(result.getString("type")));
            animals.put(animal.getId(), animal);
        }
    }

    private void getCustomers(Connection connection) throws SQLException {
        PreparedStatement stmt;
        ResultSet result;
        stmt = connection.prepareStatement(
                "SELECT * FROM `market`.`Customers`");
        result = stmt.executeQuery();
        while (result.next()) {
            Customer cust = new Customer(result.getInt("id_cust"));
            cust.setName(result.getString("name"));
            cust.setAmountPurchases(result.getInt("purchases"));
            cust.setSpendMoney(result.getDouble("spent"));
            customers.put(cust.getId(), cust);
        }
    }

    private void getTransactions(Connection connection) throws SQLException {
        PreparedStatement stmt;
        ResultSet result;
        stmt = connection.prepareStatement(
                "SELECT * FROM `market`.`Transactions`");
        result = stmt.executeQuery();
        while (result.next()) {
            Transaction transacrion = new Transaction(result.getInt("id"));
            transacrion.setPrice(result.getDouble("price"));
            transacrion.setCustomer(customers.get(result.getInt("id_cust")));
            transacrion.setDate(result.getString("date"));
            HashMap<Animal, Integer> goods = getGoods(
                    connection, result.getInt("id_purch"));
            transacrion.setGoods(goods);
            transactions.add(transacrion);
        }
    }

    private HashMap<Animal, Integer> getGoods(Connection connection, int purch_id) throws SQLException {
        HashMap<Animal,Integer> goods = new HashMap<>();
        PreparedStatement stmtInner = connection.prepareStatement(
                "SELECT * FROM `market`.`Purchases` WHERE `id_purch`=? ");
        stmtInner.setInt(1,purch_id);
        ResultSet resultInner = stmtInner.executeQuery();
        while (resultInner.next()) {
            goods.put(animals.get(resultInner.getInt("id_good")),resultInner.getInt("amount"));
        }
        return goods;
    }

    private int maxID(Connection connection,String table,String field) throws SQLException {
        PreparedStatement stmt;
        ResultSet result;
        stmt = connection.prepareStatement(
                "SELECT " + field + " FROM `market`.`"  + table + "`  ORDER BY id_purch DESC LIMIT 1");
        result = stmt.executeQuery();
        result.next();
        return result.getInt(1);
    }

    private void updatePurchases(Connection connection, int id_purch, int id_good, int amount) throws SQLException {
        PreparedStatement stmt;
        stmt = connection.prepareStatement(
                "INSERT INTO `market`.`Purchases` VALUES (?,?,?)");
        stmt.setInt(1, id_purch);
        stmt.setInt(2, id_good);
        stmt.setInt(3, amount);
        stmt.executeUpdate();

    }

    private void updateTransactions(
            Connection connection, int id_cust, int id_purch, double price) throws SQLException {
        PreparedStatement stmt;
        stmt = connection.prepareStatement(
                "INSERT INTO `market`.`Transactions` (`id_cust`,`id_purch`,`price`,`date`) " +
                        "VALUES (?,?,?,NOW())");
        stmt.setInt(1, id_cust);
        stmt.setInt(2, id_purch);
        stmt.setDouble(3, price);
        stmt.executeUpdate();

    }

    private void updateCustomer(
            Connection connection,int id_cust, int purchases, double spent) throws SQLException {
        PreparedStatement stmt;
        stmt = connection.prepareStatement(
                "UPDATE `market`.`Customers` SET `purchases`=?, `spent`=?  WHERE `id_cust`=?");
        stmt.setInt(1, purchases);
        stmt.setDouble(2, spent);
        stmt.setInt(3, id_cust);
        stmt.executeUpdate();

    }

    private void  getBudget(Connection connection) throws SQLException {
        PreparedStatement stmt;
        ResultSet result;
        stmt = connection.prepareStatement(
                "SELECT balance FROM `market`.`Budget`  ORDER BY id_operation DESC LIMIT 1");
        result = stmt.executeQuery();
        result.next();
        budget = result.getDouble(1);

    }

    private void moneyToBudget(String operation, double credit) throws SQLException {
        Connection connection = CONNECTOR.getConnection();
        this.budget += credit;
        try {
            PreparedStatement stmt;
            stmt = connection.prepareStatement(
                    "INSERT INTO `market`.`Budget` (" +
                            "`operation`,`credit`,`balance`,`date`) " +
                            "VALUES (?,?,?,NOW())");
            stmt.setString(1,operation);
            stmt.setDouble(2, credit);
            stmt.setDouble(3,this.budget);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

    private void moneyFromBudget(String operation, double debit) throws SQLException {
        Connection connection = CONNECTOR.getConnection();
        this.budget -= debit;
        try {
            PreparedStatement stmt;
            stmt = connection.prepareStatement(
                    "INSERT INTO `market`.`Budget` (" +
                            "`operation`,`debit`,`balance`,`date`) " +
                            "VALUES (?,?,?,NOW())");
            stmt.setString(1,operation);
            stmt.setDouble(2, debit);
            stmt.setDouble(3,this.budget);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

    private void moneyNewBudget(double balance) throws SQLException {
        Connection connection = CONNECTOR.getConnection();
        this.budget = balance;
        setChanged();
        notifyObservers();
        String operation = "Correcting balance";
        try {
            PreparedStatement stmt;
            stmt = connection.prepareStatement(
                    "INSERT INTO `market`.`Budget` (" +
                            "`operation`, `balance`,`date`) " +
                            "VALUES (?,?,NOW())");
            stmt.setString(1,operation);
            stmt.setDouble(2,balance);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

    public Customer customerByName(String name) {
        Connection connection = CONNECTOR.getConnection();
        Customer cust = null;
        try {
            PreparedStatement stmt;
            ResultSet result;
            stmt = connection.prepareStatement(
                    "SELECT id_cust FROM `market`.`Customers` where name=?");
            stmt.setString(1,name);
            result = stmt.executeQuery();
            if (result.next()) {
                cust = customers.get(result.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
        return cust;
    }

    public boolean haveEnoughMoney(Double price) {
        return (budget - price) > 1000 ? true : false;
    }

    public List<Animal> haveEnoughGoods(HashMap<Animal,Integer> goods) {
        List<Animal> animals = new ArrayList<>();
        for (Animal animal : goods.keySet()) {
            int amount = goods.get(animal);
            if ((animal.getAmount() - amount) <= 0) {
                animals.add(animal);
            }
        }
        return animals;
    }

    public double getBudget() {
        return budget;
    }
}
