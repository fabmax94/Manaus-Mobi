package com.mobi.samsung.manausmobi.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.v4.media.session.PlaybackStateCompat;

import com.google.firebase.database.Exclude;

import java.util.Date;

/**
 * Created by fabio.silva on 11/15/2017.
 */

@Entity(tableName = "shared")
public class Shared {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public long date;

    public double latitude;

    public double longitude;

    public String subLocality;

    public String thoroughfare;

    public String userId;

    @Exclude
    public String key;
    @Ignore
    public SharedType type;
    @Ignore
    public SharedIntensity intensity;
    @Exclude
    public String typeString;
    @Exclude
    public String intensityString;

    public String image;

    @Exclude
    public int getHours() {
        Date d = new Date(date);
        return d.getHours();
    }

    public Shared() {

    }

    public Shared(long date, double latitude, double longitude, String subLocality, String thoroughfare, SharedType type, SharedIntensity intensity) {
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.subLocality = subLocality;
        this.thoroughfare = thoroughfare;
        this.type = type;
        this.typeString = type.toString();
        this.intensity = intensity;
        this.intensityString = intensity.toString();
    }

    public void convertStringToEnum() {
        type = SharedType.valueOf(typeString);
        intensity = SharedIntensity.valueOf(intensityString);
    }

    public void convertEnumToString() {
        typeString = type.toString();
        intensityString = intensity.toString();
    }

    @Exclude
    public boolean isValidDate() {
        Date now = new Date();
        long diff = Math.abs(date - now.getTime());
        long diffHours = diff / (60 * 60 * 1000);
        //return diffHours < 1;
        return true;
    }
}