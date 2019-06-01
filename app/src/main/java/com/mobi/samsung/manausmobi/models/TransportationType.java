package com.mobi.samsung.manausmobi.models;

/**
 * Created by taynara.p on 11/21/2017.
 */

public enum TransportationType {
    Bus("Ônibus"), Microbus("Micro-ônibus"), Taxi("Táxi"), MotoTaxi("Moto-táxi");

    private String description;

    private TransportationType(String sigla) {
        this.description = sigla;
    }

    public String getDescription() {
        return description;
    }
}
