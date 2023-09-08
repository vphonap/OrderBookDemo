package com.ib.core;

import com.messages.Order;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

final class OrderBook {

    //price as key, with sorted list based on updateTime
    private TreeMap<Double, PriceLevel> bidLevels;
    private TreeMap<Double, PriceLevel> askLevels;

    public OrderBook()
    {
        //descending order for bids
        bidLevels = new TreeMap<>((o1, o2) -> {
            if(o1 < o2) return 1;
            else if (o1 > o2) return -1;
            return 0;
        });
        askLevels = new TreeMap<>();
    }

    private TreeMap<Double, PriceLevel> getLevel(String side)
    {
        if (side.charAt(0) == 'B' || side.charAt(0) == 'b')
        {
            return bidLevels;
        }

        return askLevels;
    }

    public long getNumOfPriceLevels(String side)
    {
        if (side.charAt(0) == 'B' || side.charAt(0) == 'b')
            return bidLevels.size();
        else
            return askLevels.size();
    }


    public long getNumOrders(String side)
    {
        long total = 0;
        for (Map.Entry<Double, PriceLevel> entry : getLevel(side).entrySet() )
        {
            total += entry.getValue().getNumOrders();
        }
        return total;
    }

    public boolean addOrder(Order m)
    {
        boolean result = true;
        PriceLevel pl = getLevel(m.getSide()).get(m.getPrice());
        if(pl != null)
        {
            pl.addOrder(m);
        }
        else
        { //no entry for the price -create
            PriceLevel newpl = new PriceLevel(m.getPrice());
            newpl.addOrder(m);
            if(getLevel(m.getSide()).put(m.getPrice(), newpl) != null) //entry exist already--should not be here in else block
            {
                result = false;
            }
        }
        return result;
    }

    public void getPriceLevels(String side, ArrayList<PriceLevel> lst)
    {
        for (Map.Entry<Double, PriceLevel> entry : getLevel(side).entrySet() )
        {
            lst.add(entry.getValue());
        }
    }

    public boolean replaceOrder(Order m)
    {
        return addOrder(m);
    }

    public boolean removeOrder(Order m)
    {
        return checkAndUpdatePriceLevel(m.getPrice(), m.getSide());
    }

    public boolean checkAndUpdatePriceLevel(Double price, String side)
    {
        PriceLevel pl = getLevel(side).get(price);
        if(pl.getNumOrders() ==0) //no valid orders at price level, remove it
        {
            return getLevel(side).remove(pl.levelPrice) != null;
        }
        return true;
    }

    public PriceLevel getTopPriceLevel(String side)
    {
        Map.Entry<Double,PriceLevel> entry= getLevel(side).firstEntry();
        return entry!=null ? entry.getValue() : null;
    }

    public PriceLevel getBottomPriceLevel(String side)
    {
        Map.Entry<Double,PriceLevel> entry= getLevel(side).lastEntry();
        return entry!=null ? entry.getValue() : null;
    }
}