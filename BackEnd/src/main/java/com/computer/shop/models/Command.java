package com.computer.shop.models;

public class Command {
    private int id;
    private int user;
    private String date;
    private String time;
    private String address;
    private String status;

    public Command() {
    }

    public Command(int id, int user, String date, String time, String address, String status) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.time = time;
        this.address = address;
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
