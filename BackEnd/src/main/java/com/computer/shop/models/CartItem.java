package com.computer.shop.models;

public class CartItem {
    private int id;
    private int item;
    private int quantity;
    private int cart;

    public CartItem() {
    }

    public CartItem(int id, int item, int quantity, int cart) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
        this.cart = cart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCart() {
        return cart;
    }

    public void setCart(int cart) {
        this.cart = cart;
    }
}
