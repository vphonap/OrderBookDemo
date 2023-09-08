package com.ib.core;

import com.messages.Order;

import java.util.LinkedList;
import java.util.List;


public class PriceLevel {

    double levelPrice;
    //updateTime As Key against the message
    List<Order> leveOrders;

    public PriceLevel(double price)
    {
        levelPrice = price;
        leveOrders = new LinkedList<>();
    }

    @Override
    public String toString()
    {
        return "Price Level:"+ levelPrice; //+ "," + levelOrders.toString();
    }
    public void addOrder(Order m)
    {
       leveOrders.add(m);
    }
    //get number of orders at this price level
    public List<Order> getLevelOrders()
    {
        return leveOrders;
    }

    public long getNumOrders()
    {
        long total = 0;
        for(Order o:leveOrders)
        {
           if(o.isDeleted() && levelPrice == o.getPrice()) //not deleted and valid price
           {
               System.out.println("levelPrice:"+levelPrice+", order price:"+o.getPrice());
               total = total + 1;
           }
        }
        return total;
    }

    public double getLevelPrice()
    {
        return levelPrice;
    }
}
