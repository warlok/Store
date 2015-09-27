package com.kademika.Store.Market.MarketGUI.Panels;

import com.kademika.Store.Market.Market;
import com.kademika.Store.Market.Objects.Animal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.sql.*;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by dean on 8/17/15.
 */
public class TransacrionPanel extends JPanel {

    public TransacrionPanel(Market market) {
        Connection conn = market.CONNECTOR.getConnection();
        setLayout(new BorderLayout());
       try {
           Statement stmt = conn.createStatement();
           ResultSet rs = stmt.executeQuery("SELECT * from `market`.`Budget`");

           Object[][] data = new Object[100][6];
           int i=0;
          while (rs.next()) {
               data[i][0] = rs.getInt(1);
               data[i][1] = rs.getString(2);
               data[i][2] = rs.getDouble(3);
               data[i][3] = rs.getDouble(4);
               data[i][4] = rs.getDouble(5);
               data[i][5] = rs.getString(6);
               i++;
           }
           String[] columsNames = {"id", "Operation ", "Debit", "Credit", "Balance", "Date"};
           final JTable table = new JTable(data , columsNames);
           TableColumn column = null;
           column = table.getColumnModel().getColumn(0);
           column.setPreferredWidth(10);
           column = table.getColumnModel().getColumn(1);
           column.setPreferredWidth(300);
           column = table.getColumnModel().getColumn(2);
           column.setPreferredWidth(50);
           column = table.getColumnModel().getColumn(3);
           column.setPreferredWidth(50);
           column = table.getColumnModel().getColumn(4);
           column.setPreferredWidth(100);
           column = table.getColumnModel().getColumn(5);
           column.setPreferredWidth(300);
           JScrollPane sp = new JScrollPane(table);
           table.setFillsViewportHeight(true);
           add(sp, BorderLayout.CENTER);
       } catch (SQLException e) {
           e.printStackTrace();
       } finally {
           try {
               conn.close();
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
    }
}
