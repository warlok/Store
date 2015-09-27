package com.kademika.Store;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.*;

/**
 * Created by dean on 8/4/15.
 */
public class Connector {

    final ComboPooledDataSource CDPS = new ComboPooledDataSource();

    public Connector() {
        try {
            CDPS.setDriverClass("com.mysql.jdbc.Driver");
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        CDPS.setJdbcUrl("jdbc:mysql://arrow.wnet.ua:3306/market");
        CDPS.setUser("dean");
        CDPS.setPassword("Dean_2004");
        CDPS.setMaxStatements(180);
    }

    public Connection getConnection() {
        Connection result = null;
        try {
            result = CDPS.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void closeConnections() {
        CDPS.close();
    }

}
