package com.mobi.samsung.manausmobi.models;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fabio.silva on 11/24/2017.
 */

public class SafetyMessage {

    public String date;
    public String description;
    public String emitter;
    public double latitude;
    public double longitude;

    public SafetyMessage(double latitude, double longitude, String message) {
        description = message;
        emitter = "mobilidade";
        this.latitude = latitude;
        this.longitude = longitude;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = simpleDateFormat.format(new Date());
    }
}
