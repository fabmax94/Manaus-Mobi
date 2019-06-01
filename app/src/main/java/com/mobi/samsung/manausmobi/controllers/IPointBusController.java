package com.mobi.samsung.manausmobi.controllers;

import com.mobi.samsung.manausmobi.listeners.OnMapListener;
import com.mobi.samsung.manausmobi.models.InfoBus;
import com.mobi.samsung.manausmobi.models.PointBus;
import com.mobi.samsung.manausmobi.models.Shared;

import java.util.List;

/**
 * Created by daniel on 17/11/17.
 */

public interface IPointBusController {

    void setList(final OnMapListener listener, boolean isConnect);

    void addLocal(PointBus pointBus);

    List<PointBus> getPointBusList();

    PointBus findBusByKey(String key);

    void cleanEventListener();

    List<InfoBus> findInfoBusByKey(int id);
}
