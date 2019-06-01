package com.mobi.samsung.manausmobi.services.impl;

import com.mobi.samsung.manausmobi.services.IPointBusService;
import com.mobi.samsung.manausmobi.services.ISharedService;
import com.mobi.samsung.manausmobi.services.ITransportationService;
import com.mobi.samsung.manausmobi.services.ITutoriaService;

/**
 * Created by fabio.silva on 11/20/2017.
 */

public abstract class AbstractServiceFactory {

    protected static ISharedService sharedService;
    protected static IPointBusService pointBusService;
    protected static ITransportationService transportationService;
    protected static ITutoriaService tutoriaService;

    public abstract ISharedService createSharedService();

    public abstract IPointBusService createPontService();

    public abstract ITransportationService createTransportationService();

    public abstract ITutoriaService createTutoriaService();
}
