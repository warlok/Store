package com.kademika.Store.Market;


import com.kademika.Store.Market.Objects.Customer;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by dean on 8/18/15.
 */
public class Client {


    public static void main(String[] args) {
        ArrayList<Customer> customers = new ArrayList<>();
        ObjectInputStream ois = null;
        try (Socket socket = new Socket("localhost", 9191);
        InputStream is = socket.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        OutputStream os = socket.getOutputStream()
        ){
            char b = 'a';
            os.write(b);
            ois = new ObjectInputStream(bis);
            while (true) {
                try {
                    Customer cust = new Customer();
                    cust.readExternal(ois);
                    customers.add(cust);
                } catch (EOFException e) {
                    break;
                }
            }
            ois.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (Customer cust : customers) {
            System.out.println(cust.getName());
        }

    }

}
