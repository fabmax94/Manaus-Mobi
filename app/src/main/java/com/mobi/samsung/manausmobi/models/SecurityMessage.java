package com.mobi.samsung.manausmobi.models;

/**
 * Created by fabio.silva on 11/24/2017.
 */

public class SecurityMessage {
    public boolean person;
    public String name;
    public String title;
    public String message;
    public double latitude;
    public double longitude;

    public SecurityMessage(double latitude, double longitude, String message) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = message;
        title = "Requisição de segurança.";
        person = false;
        name = "mobilidade";
    }
}
