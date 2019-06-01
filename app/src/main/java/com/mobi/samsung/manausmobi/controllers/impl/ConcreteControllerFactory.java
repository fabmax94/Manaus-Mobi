package com.mobi.samsung.manausmobi.controllers.impl;

import android.content.Context;

import com.mobi.samsung.manausmobi.controllers.IDashboardController;
import com.mobi.samsung.manausmobi.controllers.IPointBusController;
import com.mobi.samsung.manausmobi.controllers.ISharedController;

/**
 * Created by fabio.silva on 11/20/2017.
 */

public class ConcreteControllerFactory extends AbstractControllerFactory {
    @Override
    public ISharedController createSharedController(Context context) {
        return new SharedController(context);
    }

    @Override
    public IPointBusController createPointBusController(Context context) {
        return new PointBusController(context);
    }

    @Override
    public IDashboardController createDashboardController(Context context) {
        return new DashboardController(context);
    }
}
