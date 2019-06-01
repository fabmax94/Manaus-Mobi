package com.mobi.samsung.manausmobi.controllers.impl;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.mobi.samsung.manausmobi.controllers.IPointBusController;
import com.mobi.samsung.manausmobi.listeners.OnMapListener;
import com.mobi.samsung.manausmobi.models.InfoBus;
import com.mobi.samsung.manausmobi.models.PointBus;
import com.mobi.samsung.manausmobi.models.Shared;
import com.mobi.samsung.manausmobi.persistences.AppDatabase;
import com.mobi.samsung.manausmobi.services.IPointBusService;
import com.mobi.samsung.manausmobi.services.impl.AbstractServiceFactory;
import com.mobi.samsung.manausmobi.services.impl.ConcreteServiceFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 17/11/17.
 */

class PointBusController implements IPointBusController {
    private IPointBusService pointBusService;
    private AppDatabase app;
    private List<PointBus> pointBusList;

    public PointBusController(Context context) {
        AbstractServiceFactory serviceFactory = new ConcreteServiceFactory();
        pointBusService = serviceFactory.createPontService();

        this.app = Room.databaseBuilder(context,
                AppDatabase.class, "database-mobi").allowMainThreadQueries().build();

        pointBusList = new ArrayList<PointBus>();
    }

    @Override
    public void setList(OnMapListener listener, boolean isConnect) {
        for (PointBus pointBus : this.app.pointBusDAO().findAll()) {
            listener.addPointBus(pointBus);
            pointBus.pointBusList = this.app.inboBusDAO().findAllByInfoBusId(pointBus.id);
            pointBusList.add(pointBus);
        }

        if (isConnect) {
            pointBusService.requestList(listener);
        }
    }

    @Override
    public void addLocal(PointBus pointBus) {
        if (this.app.pointBusDAO().get(pointBus.key) == null) {
            this.app.pointBusDAO().save(pointBus);
            pointBusList.add(pointBus);
            for (InfoBus info : pointBus.pointBusList) {
                info.pointBusId = this.app.pointBusDAO().get(pointBus.key).id;
                this.app.inboBusDAO().save(info);
            }
        }
    }

    @Override
    public List<PointBus> getPointBusList() {
        return pointBusList;
    }

    @Override
    public PointBus findBusByKey(String key) {
        return this.app.pointBusDAO().get(key);
    }

    @Override
    public void cleanEventListener() {
        pointBusService.cleanEventListener();
    }

    @Override
    public List<InfoBus> findInfoBusByKey(int id) {
        return this.app.inboBusDAO().findAllByInfoBusId(id);
    }
}