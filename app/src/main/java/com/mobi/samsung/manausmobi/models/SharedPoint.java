package com.mobi.samsung.manausmobi.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabio.silva on 12/13/2017.
 */

public class SharedPoint {

    private double latitude;

    private double longitude;

    private List<Shared> sharedList;

    private SharedType type;

    private SharedIntensity intensity;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<Shared> getSharedList() {
        return sharedList;
    }

    public void addShared(Shared shared) {
        this.sharedList.add(shared);
    }

    public int getWeigth() {
        int result = 0;
        for (Shared shared : sharedList) {
            result += shared.intensity.getWeight();
        }
        return result;
    }

    public SharedPoint(Shared shared) {
        this.sharedList = new ArrayList<Shared>();
        this.latitude = shared.latitude;
        this.longitude = shared.longitude;
        this.type = shared.type;
    }

    public static boolean isNearby(Shared shared1, Shared shared2) {

        if (shared1.type == shared2.type && shared1.thoroughfare != null && shared1.thoroughfare.equals(shared2.thoroughfare)) {
            final int R = 6371; // Radius of the earth

            double latDistance = Math.toRadians(shared2.latitude - shared1.latitude);
            double lonDistance = Math.toRadians(shared2.longitude - shared1.longitude);
            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(shared1.latitude)) * Math.cos(Math.toRadians(shared2.latitude))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distance = R * c * 1000; // convert to meters

            return distance <= 100;
        }

        return false;
    }

    public static List<SharedPoint> generateSharedPoint(List<Shared> tempList) {
        List<SharedPoint> sharedPoints = new ArrayList<SharedPoint>();
        List<Shared> tempSharedList = new ArrayList<>(tempList);
        for (Shared shared : tempList) {
            if (tempSharedList.contains(shared)) {
                SharedPoint point = new SharedPoint(shared);
                point.addShared(shared);
                tempSharedList.remove(shared);
                for (Shared tempShared : new ArrayList<>(tempSharedList)) {
                    if (SharedPoint.isNearby(shared, tempShared)) {
                        point.addShared(tempShared);
                        tempSharedList.remove(tempShared);
                    }
                }
                point.intensity = SharedIntensity.getEnum(Math.round((float) point.getWeigth() / (float) point.getSharedList().size()), shared.type);
                sharedPoints.add(point);
            }
        }
        return sharedPoints;
    }

    public void removeShared(Shared sh) {
        sharedList.remove(sh);
    }

    public SharedType getType() {
        return type;
    }

    public SharedIntensity getIntensity() {
        return intensity;
    }

}
