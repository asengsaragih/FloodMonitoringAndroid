package com.kopisenja.floodmonitoring.base;

public class Flood {
    private String date;
    private String time;
    private String location;
    private String debit;
    private String level;
    private int status;
    private int category;

    public Flood() {
    }

    public Flood(String date, String time, String location, String debit, String level, int status, int category) {
        this.date = date;
        this.time = time;
        this.location = location;
        this.debit = debit;
        this.level = level;
        this.status = status;
        this.category = category;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
