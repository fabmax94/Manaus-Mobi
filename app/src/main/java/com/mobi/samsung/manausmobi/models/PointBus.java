package com.mobi.samsung.manausmobi.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 17/11/17.
 */

@Entity(tableName = "pointBus")
public class PointBus {

    @PrimaryKey(autoGenerate = true)
    @Exclude
    public int id;

    public double latitude;

    public double longitude;

    @Ignore
    @Exclude
    public List<InfoBus> pointBusList;


    @Exclude
    public String key;

    public PointBus() {
        pointBusList = new ArrayList<>();
    }
}
