package com.mobi.samsung.manausmobi.models;

/**
 * Created by fabio.silva on 11/21/2017.
 */

public class TrafficWarningDashboard {
    private int trafficWeight;
    private int warningWeight;
    private String local;

    public TrafficWarningDashboard(int trafficWeight, int warningWeight, String local) {
        this.setTrafficWeight(trafficWeight);
        this.setWarningWeight(warningWeight);
        this.setLocal(local);
    }

    public int getTrafficWeight() {
        return trafficWeight;
    }

    public void setTrafficWeight(int trafficWeight) {
        this.trafficWeight = trafficWeight;
    }

    public int getWarningWeight() {
        return warningWeight;
    }

    public void setWarningWeight(int warningWeight) {
        this.warningWeight = warningWeight;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
