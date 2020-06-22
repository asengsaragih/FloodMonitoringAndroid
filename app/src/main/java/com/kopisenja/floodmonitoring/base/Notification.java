package com.kopisenja.floodmonitoring.base;

public class Notification {
    private String name;
    private int status;

    public Notification(String name, int status) {
        this.name = name;
        this.status = status;
    }

    public Notification() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
