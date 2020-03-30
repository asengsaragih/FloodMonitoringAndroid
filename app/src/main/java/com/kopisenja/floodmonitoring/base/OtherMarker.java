package com.kopisenja.floodmonitoring.base;

public class OtherMarker {
    public OtherMarker() {
    }

    public OtherMarker(String location) {
        this.location = location;
    }

    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
