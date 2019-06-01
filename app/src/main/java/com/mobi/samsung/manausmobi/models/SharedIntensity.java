package com.mobi.samsung.manausmobi.models;

/**
 * Created by fabio.silva on 11/16/2017.
 */

public enum SharedIntensity {
    Moderate("Moderado", 1), Intense("Intenso", 2), Stopped("Parado", 3), InRoute("Em Rota", 3), Coasting("Acostamento", 1), Climate("Clima", 2);

    private String description;

    private int weight;

    private SharedIntensity(String description, int weight) {
        this.description = description;
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public int getWeight() {
        return weight;
    }

    public static SharedIntensity getEnum(int weight, SharedType type) {
        if (type == SharedType.Traffic) {
            switch (weight) {
                case 1:
                    return SharedIntensity.Moderate;
                case 2:
                    return SharedIntensity.Intense;
                case 3:
                    return SharedIntensity.Stopped;
            }
        } else {
            switch (weight) {
                case 1:
                    return SharedIntensity.Coasting;
                case 2:
                    return SharedIntensity.Climate;
                case 3:
                    return SharedIntensity.InRoute;
            }
        }
        return null;
    }
}
