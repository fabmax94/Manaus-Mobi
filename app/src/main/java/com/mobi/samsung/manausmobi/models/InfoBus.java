package com.mobi.samsung.manausmobi.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.icu.text.IDNA;

import com.google.firebase.database.Exclude;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by taila.s on 12/1/2017.
 */

@Entity(tableName = "infoBus", foreignKeys = @ForeignKey(entity = PointBus.class,
        parentColumns = "id",
        childColumns = "pointBusId"))
public class InfoBus {
    @PrimaryKey(autoGenerate = true)
    @Exclude
    public int id;

    public String key;
    public String busInfo;

    @Exclude
    public int pointBusId;

    public InfoBus() {

    }

    public InfoBus(String key, String busInfo) {
        this.key = key;
        this.busInfo = busInfo;
    }
}
