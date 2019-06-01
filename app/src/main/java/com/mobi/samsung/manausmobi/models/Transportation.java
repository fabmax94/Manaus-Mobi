package com.mobi.samsung.manausmobi.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.firebase.database.Exclude;

/**
 * Created by taynara.p on 11/21/2017.
 */

@Entity(tableName = "transportation")
public class Transportation {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int amount;

    public String type;

    @Exclude
    public String key;

    public Transportation(){

    }

    public Transportation(int amount, String type) {
        this.amount = amount;
        this.type = type;
    }
}
