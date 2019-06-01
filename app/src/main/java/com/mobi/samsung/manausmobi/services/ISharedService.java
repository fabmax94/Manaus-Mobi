package com.mobi.samsung.manausmobi.services;

import com.mobi.samsung.manausmobi.controllers.ISharedController;
import com.mobi.samsung.manausmobi.listeners.OnDashboardListener;
import com.mobi.samsung.manausmobi.listeners.OnMapListener;
import com.mobi.samsung.manausmobi.listeners.OnRequestListener;
import com.mobi.samsung.manausmobi.listeners.OnSharedListener;
import com.mobi.samsung.manausmobi.models.Shared;

import java.util.List;

/**
 * Created by fabio.silva on 11/16/2017.
 */

public interface ISharedService {
    void add(Shared shared);

    void requestList(final OnRequestListener listener, final boolean isFilterDate);

    void existOrDelete(final List<Shared> shareds, final OnMapListener listener);

    void cleanEventListener();

    void existWithImage(final Shared shared, final OnSharedListener listener);
}
