package com.ivan.thesisproject.models;

import java.util.List;

public class Order {
    private List<Product> productList;
    private String address;
    private int userId;

    public Order(){
        //empty constructor
    }

    public Order(List<Product> productList, String address,int userId) {
        this.productList = productList;
        this.address = address;
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
