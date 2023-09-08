package com.ib.core;


import com.messages.Order;

public interface Engine {

    boolean addOrder(Order m);
    boolean removeOrder(String orderid);
    boolean replaceOrder(Order m);

    boolean isOrderBookCrossed();

    //get top /bottom price level of book on each side
    PriceLevel getTopPriceLevel(String side);
    PriceLevel getBottomPriceLevel(String side);

    //-
    long getNumOfPriceLevels(String side);

    // iterate price levels on each side of the book
    void iterPriceLevels(String side);

    //get number of orders at each price level
    //iterate orders at price level by their priority
    void iterOrdersAtPriceLevels(String side);

    //total number of orders across price levels on given side
    long getNumOrders(String side);

    //TODO- iterate orders across all price levels on given side by their priority

    Order getOrder(String orderId);

    //TODO- get last transaction that was done on the order

    //TODO- iterate orders that were created/ updated *before a given time*
    //TODO- iterate orders that were created/ updated *after a given time*
    void iterOrdersByTimeStamp();
}
