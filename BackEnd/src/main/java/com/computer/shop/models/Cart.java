package com.computer.shop.models;

public class Cart {
    private int id;
    private int user;

    public Cart() {
    }

    public Cart(int id, int user) {
        this.id = id;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}
