package com.kopisenja.floodmonitoring.base;

public class FloodData {
    private String date;
    private String time;
    private String debit;
    private String level;
    private int status;
    private int category;
    private String miliestime;

    public FloodData() {
    }

    public FloodData(String date, String time, String debit, String level, int status, int category, String miliestime) {
        this.date = date;
        this.time = time;
        this.debit = debit;
        this.level = level;
        this.status = status;
        this.category = category;
        this.miliestime = miliestime;
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

    public String getMiliestime() {
        return miliestime;
    }

    public void setMiliestime(String miliestime) {
        this.miliestime = miliestime;
    }
}
