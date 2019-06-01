package com.mobi.samsung.manausmobi.services.impl;

import com.mobi.samsung.manausmobi.services.IPointBusService;
import com.mobi.samsung.manausmobi.services.ISharedService;
import com.mobi.samsung.manausmobi.services.ITransportationService;
import com.mobi.samsung.manausmobi.services.ITutoriaService;

/**
 * Created by fabio.silva on 11/20/2017.
 */

public class ConcreteServiceFactory extends AbstractServiceFactory {

    @Override
    public ISharedService createSharedService() {
        if (sharedService == null) {
            sharedService = new SharedService();
        }
        return sharedService;
    }

    @Override
    public IPointBusService createPontService() {
        if (pointBusService == null) {
            pointBusService = new PointBusService();
        }
        return pointBusService;
    }

    @Override
    public ITransportationService createTransportationService() {
        if (transportationService == null) {
            transportationService = new TransportationService();
        }
        return transportationService;
    }

    @Override
    public ITutoriaService createTutoriaService() {
        if (tutoriaService == null) {
            tutoriaService = new TutoriaService();
        }
        return tutoriaService;
    }
}
