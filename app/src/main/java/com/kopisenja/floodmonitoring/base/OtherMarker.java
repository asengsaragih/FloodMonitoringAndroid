package com.kopisenja.floodmonitoring.base;

public class OtherMarker {
    private double altitude;
    private double latitude;
    private double longitude;
    private int status;
    private String name;

    public OtherMarker() {
    }

    public OtherMarker(double altitude, double latitude, double longitude, int status, String name) {
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.name = name;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
