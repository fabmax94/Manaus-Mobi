package com.mobi.samsung.manausmobi.models;

/**
 * Created by fabio.silva on 11/22/2017.
 */

public class TrafficBIDashboard {
    private String hours;
    private String local;
    private float trafficWeight;

    public TrafficBIDashboard(String local, String hours, int trafficWeight) {
        this.setHours(hours);
        this.setTrafficWeight(trafficWeight);
        this.setLocal(local);
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public float getTrafficWeight() {
        return trafficWeight;
    }

    public void setTrafficWeight(float trafficWeight) {
        this.trafficWeight = trafficWeight;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
