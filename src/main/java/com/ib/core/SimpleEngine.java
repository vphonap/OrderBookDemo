package com.ib.core;

import com.messages.Order;

import java.util.*;

public class SimpleEngine implements Engine {

    private OrderBook orderBook;
    //orderId as key against order
    private HashMap<String, Order> orderTable;
    //updateTime based tracker
    private HashMap<Long, List<Order>> updateTimeBuckets;

    public void printStats()
    {
        System.out.println("@@@@@@Ordertable size:"+orderTable.size()+", timebuckets size:"+updateTimeBuckets.size());
        orderBook.printStats();
    }
    public SimpleEngine()
    {
        orderBook = new OrderBook();
        orderTable = new HashMap<>(20000000);
        updateTimeBuckets = new HashMap<>(20000000);
    }

    public boolean addOrder(Order m)
    {
        //TODO- Validate and ACK order after
        boolean result = false;
        if(orderTable.put(m.getOrderId(),m) == null) //added
        {
            result = orderBook.addOrder(m);
            if (result) {
                setUpdateTimeBuckets(m);
            }
        }
        //TODO- match order
        return result;
    }

    public boolean replaceOrder(Order m)
    {
        boolean result = true;
        Order o = getOrder(m.getOrderId());
        if(o != null)
        {
            if(m.getPrice() != 0 && m.getPrice() != o.getPrice()) //update price level if price change
            {
                m.setSide(o.getSide());
                double origPrice = o.getPrice();
                result = orderBook.replaceOrder(m);
                if(result) {
                    o.setPrice(m.getPrice());
                    result = orderBook.checkAndUpdatePriceLevel(origPrice, m.getSide());
                }
            }

            if(result)
            {
                if(m.getQuantity() > 0 && m.getQuantity() != o.getQuantity())
                    o.setQuantity(m.getQuantity());
                o.setUpdateTime(System.currentTimeMillis());
                setUpdateTimeBuckets(o);
            }
            return result;
        }

        return false;
    }

    public boolean removeOrder(String orderId)
    {
        boolean result= false;
        Order x = orderTable.get(orderId);
        if(x != null)
        {
            x.setStatus("X");
            x.setUpdateTime(System.currentTimeMillis());
            setUpdateTimeBuckets(x);
            result = orderBook.removeOrder(x);
        }
        return result;
    }
    public boolean isOrderBookCrossed()
    {
        PriceLevel bl = getTopPriceLevel("B");
        PriceLevel sl = getTopPriceLevel("S");
        return bl != null && sl != null && bl.levelPrice >= sl.levelPrice;
    }

    public void setUpdateTimeBuckets(Order m)
    {
        List<Order> l = updateTimeBuckets.get(m.getUpdateTime());
        if(l != null)
        {
            l.add(m);
        }
        else
        {
            List<Order> lst = new LinkedList<>();
            lst.add(m);
            updateTimeBuckets.put(m.getUpdateTime(),lst);
        }
    }
    public PriceLevel getTopPriceLevel(String side)
    {
        return orderBook.getTopPriceLevel(side);
    }
    public PriceLevel getBottomPriceLevel(String side)
    {
        return orderBook.getBottomPriceLevel(side);
    }
    public long getNumOfPriceLevels(String side)
    {
        return orderBook.getNumOfPriceLevels(side);
    }

    //total number of orders *across* price levels on given side
    public long getNumOrders(String side)
    {
        return orderBook.getNumOrders(side);
    }

    //iterate price levels on each side of the book
    public void iterPriceLevels(String side)
    {
        ArrayList<PriceLevel> lst = new ArrayList<>();
        orderBook.getPriceLevels(side,lst);
        lst.forEach(e->System.out.println("Side:"+side+", Price Level:"+ e.levelPrice));
    }

    //get number of orders at each price level
    //iterate orders across all price levels on given side by their priority
    public void iterOrdersAtPriceLevels(String side)
    {
        ArrayList<PriceLevel> lst = new ArrayList<>();
        orderBook.getPriceLevels(side,lst);
        lst.forEach(e->{
            System.out.println("Price level:"+e.toString());
            List<Order> orders = e.getLevelOrders();
            for(Order o:orders)
            {
                if(o.isDeleted() && e.levelPrice == o.getPrice()) //Not deleted And as order might have updated price through replace, in which case it may not belong to this price level. But not removed from the earlier price level
                    System.out.println(o);
            }
        });
    }

    public Order getOrder(String orderId)
    {
        return orderTable.get(orderId);
    }

    //TODO- get last transaction that was done on the order

    //TODO- iterate orders that were created/ updated *before a given time*
    //TODO- iterate orders that were created/ updated *after a given time*
    public void iterOrdersByTimeStamp()
    {
        ArrayList<Long> times = new ArrayList<>(updateTimeBuckets.keySet());
        Collections.sort(times);
        for(Long updtTime:times)
        {
            List<Order> orders = updateTimeBuckets.get(updtTime);
            orders.forEach(o->{
                if(o.getUpdateTime()==updtTime) {
                    System.out.println(o);
                }
            });
        }
    }

}
