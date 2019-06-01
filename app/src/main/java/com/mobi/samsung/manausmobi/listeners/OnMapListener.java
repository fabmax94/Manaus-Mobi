package com.mobi.samsung.manausmobi.listeners;

import com.mobi.samsung.manausmobi.models.PointBus;
import com.mobi.samsung.manausmobi.models.Shared;
import com.mobi.samsung.manausmobi.models.SharedPoint;

import java.util.List;

/**
 * Created by fabio.silva on 11/16/2017.
 */

public interface OnMapListener extends OnRequestListener {

    void removeShared(Shared shared);

    void addPointBus(PointBus pointBus);

    void addShared(List<SharedPoint> shared);
}
