package com.mobi.samsung.manausmobi.controllers.impl;

import android.content.Context;

import com.mobi.samsung.manausmobi.controllers.IDashboardController;
import com.mobi.samsung.manausmobi.controllers.IPointBusController;
import com.mobi.samsung.manausmobi.controllers.ISharedController;

/**
 * Created by fabio.silva on 11/20/2017.
 */

public abstract class AbstractControllerFactory {
    public abstract ISharedController createSharedController(Context context);

    public abstract IPointBusController createPointBusController(Context context);

    public abstract IDashboardController createDashboardController(Context context);
}
