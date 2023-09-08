package com.util;

import com.messages.Order;

public class OrderFeeder implements MessageFeeder<Order> {
    public Order getMessage()
    {
        return new Order();
    }
}
