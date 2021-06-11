package com.example.ex7;

public class Order {

    String orderId;
    String customer_name;
    int pickles;
    boolean hummus;
    boolean tahini;
    String comment;
    int status;

    public Order(String id, String customer, int picklesNum, boolean isHummus, boolean isTahini, String comment, int state) {
        this.orderId = id;
        this.customer_name = customer;
        this.pickles = picklesNum;
        this.hummus = isHummus;
        this.tahini = isTahini;
        this.comment = comment;
        this.status = state;
    }

}
