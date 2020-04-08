package com.ivan.thesisproject.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {
    @SerializedName("order_id")
    private String Id;
    @SerializedName("product")
    private List<Product> productList;
    @SerializedName("Address")
    private String address;
    private String status;
    private int userId;
    @SerializedName("total_price")
    private int totalPrice;
    @SerializedName("order_date")
    private String orderDate;

    public Order(){
        //empty constructor
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Order(List<Product> productList, String address, int userId) {
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
