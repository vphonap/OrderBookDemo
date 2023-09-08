package com.messages;

public class Order {

    String orderId;
    double quantity;
    double price;
    String side;
    long createTime;
    long updateTime;
    String status;

    public Order()
    {
        long time = System.currentTimeMillis();
        setCreateTime(time);
        setUpdateTime(time);
    }

    @Override
    public String toString()
    {
        return orderId + "," + quantity + "," + price + "," + side + "," + createTime + "," + updateTime+","+status;
    }
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String s) {
        this.status = s;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isDeleted()
    {
        return getStatus() != "X";
    }
}
