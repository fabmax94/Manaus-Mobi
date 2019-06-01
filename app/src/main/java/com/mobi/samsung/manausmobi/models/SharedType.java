package com.mobi.samsung.manausmobi.models;

/**
 * Created by fabio.silva on 11/15/2017.
 */

public enum SharedType {
    Traffic("Trânsito"), Warning("Perigo");

    private String description;

    private SharedType(String sigla) {
        this.description = sigla;
    }

    public String getDescription() {
        return description;
    }
}
