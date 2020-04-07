package com.kopisenja.floodmonitoring.base;

public class Marker {
    private String nama;
    private int alt, longitude, latitude, status;

    public Marker() {
    }

    public Marker(String nama, int alt, int longitude, int latitude, int status) {
        this.nama = nama;
        this.alt = alt;
        this.longitude = longitude;
        this.latitude = latitude;
        this.status = status;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getAlt() {
        return alt;
    }

    public void setAlt(int alt) {
        this.alt = alt;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
