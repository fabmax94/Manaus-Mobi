package com.mobi.samsung.manausmobi.controllers.impl;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import com.mobi.samsung.manausmobi.controllers.ISharedController;
import com.mobi.samsung.manausmobi.listeners.OnMapListener;
import com.mobi.samsung.manausmobi.listeners.OnSharedListener;
import com.mobi.samsung.manausmobi.models.SafetyMessage;
import com.mobi.samsung.manausmobi.models.SecurityMessage;
import com.mobi.samsung.manausmobi.models.Shared;
import com.mobi.samsung.manausmobi.models.SharedPoint;
import com.mobi.samsung.manausmobi.persistences.AppDatabase;
import com.mobi.samsung.manausmobi.services.ISharedService;
import com.mobi.samsung.manausmobi.services.ITutoriaService;
import com.mobi.samsung.manausmobi.services.impl.AbstractServiceFactory;
import com.mobi.samsung.manausmobi.services.impl.ConcreteServiceFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabio.silva on 11/16/2017.
 */

class SharedController implements ISharedController {
    private ISharedService sharedService;
    private ITutoriaService tutoriaService;
    private AppDatabase app;
    private List<SharedPoint> sharedList;

    public SharedController(Context context) {
        AbstractServiceFactory serviceFactory = new ConcreteServiceFactory();
        sharedService = serviceFactory.createSharedService();
        tutoriaService = serviceFactory.createTutoriaService();
        this.app = Room.databaseBuilder(context,
                AppDatabase.class, "database-mobi").allowMainThreadQueries().build();
        sharedList = new ArrayList<SharedPoint>();
    }

    @Override
    public void add(Shared shared) {
        sharedService.add(shared);
    }

    @Override
    public void send(SecurityMessage message) {
        tutoriaService.add(message);
    }

    @Override
    public void send(SafetyMessage message) {
        tutoriaService.add(message);
    }

    @Override
    public void cleanEventListener() {
        sharedService.cleanEventListener();
    }

    @Override
    public void setSharedList(List<SharedPoint> sharedList) {
        this.sharedList = sharedList;
    }

    @Override
    public List<String> findSharedsByKey(String[] keys) {
        List<String> images = new ArrayList<>();
        for (String key : keys) {
            String image = this.app.sharedDAO().get(key).image;
            if (image != null) {
                images.add(image);
            }
        }
        return images;
    }

    @Override
    public void addLocal(Shared shared) {
        if (this.app.sharedDAO().get(shared.key) == null) {
            shared.convertEnumToString();
            this.app.sharedDAO().save(shared);
        }
    }

    @Override
    public void removeLocal(Shared shared) {
        int i = 0;
        for (SharedPoint point : sharedList) {
            for (Shared sh : new ArrayList<>(point.getSharedList())) {
                if (sh.key.equals(shared.key)) {
                    point.removeShared(sh);
                }
            }
        }
        Shared sharedTemp = this.app.sharedDAO().get(shared.key);
        if (sharedTemp != null) {
            this.app.sharedDAO().delete(sharedTemp);
        }
    }

    @Override
    public List<SharedPoint> getSharedList() {
        return sharedList;
    }

    @Override
    public SharedPoint findSharedByIndex(String index) {
        return sharedList.get(Integer.parseInt(index));
    }

    @Override
    public void cleanDatabase(OnMapListener listener) {
        List<Shared> sList = this.app.sharedDAO().findAll();
        sharedService.existOrDelete(sList, listener);
    }

    @Override
    public void existsWithImage(Shared shared, OnSharedListener listener) {
        sharedService.existWithImage(shared, listener);
    }

    @Override
    public void setList(final OnMapListener listener, boolean isConnect) {
        List<Shared> sList = this.app.sharedDAO().findAll();
        List<Shared> tempList = new ArrayList<Shared>();
        for (Shared tempShared : sList) {
            tempShared.convertStringToEnum();
            tempList.add(tempShared);
        }
        sharedList = SharedPoint.generateSharedPoint(tempList);
        listener.addShared(sharedList);
        if (isConnect) {
            sharedService.requestList(listener, true);
        }
    }
}
