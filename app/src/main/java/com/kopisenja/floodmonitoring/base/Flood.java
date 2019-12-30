package com.kopisenja.floodmonitoring.base;

public class Flood {
    private String date;
    private String location;
    private String debit;
    private String level;
    private int status;

    public Flood() {
    }

    public Flood(String date, String location, String debit, String level, int status) {
        this.date = date;
        this.location = location;
        this.debit = debit;
        this.level = level;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
