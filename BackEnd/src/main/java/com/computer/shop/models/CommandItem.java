package com.computer.shop.models;

public class CommandItem {
    private int id;
    private int item;
    private int quantity;
    private int command;

    public CommandItem() {
    }

    public CommandItem(int id, int item, int quantity, int command) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
        this.command = command;
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

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }
}
