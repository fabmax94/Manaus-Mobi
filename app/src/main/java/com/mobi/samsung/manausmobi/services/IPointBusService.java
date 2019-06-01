package com.mobi.samsung.manausmobi.services;

import com.mobi.samsung.manausmobi.listeners.OnMapListener;
import com.mobi.samsung.manausmobi.models.PointBus;

/**
 * Created by daniel on 17/11/17.
 */

public interface IPointBusService {
    void add(PointBus pointBus);

    void requestList(final OnMapListener listener);

    void cleanEventListener();
}
