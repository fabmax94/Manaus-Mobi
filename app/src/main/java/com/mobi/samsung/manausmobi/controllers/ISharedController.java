package com.mobi.samsung.manausmobi.controllers;

import com.mobi.samsung.manausmobi.listeners.OnMapListener;
import com.mobi.samsung.manausmobi.listeners.OnSharedListener;
import com.mobi.samsung.manausmobi.models.SafetyMessage;
import com.mobi.samsung.manausmobi.models.SecurityMessage;
import com.mobi.samsung.manausmobi.models.Shared;
import com.mobi.samsung.manausmobi.models.SharedPoint;

import java.util.List;

/**
 * Created by fabio.silva on 11/16/2017.
 */

public interface ISharedController {
    void add(Shared shared);

    void setList(final OnMapListener listener, boolean isConnect);

    void addLocal(Shared shared);

    void removeLocal(Shared shared);

    List<SharedPoint> getSharedList();

    SharedPoint findSharedByIndex(String index);

    void cleanDatabase(OnMapListener listener);

    void existsWithImage(final Shared shared, final OnSharedListener listener);

    void send(SecurityMessage message);

    void send(SafetyMessage message);

    void cleanEventListener();

    void setSharedList(List<SharedPoint> sharedList);

    List<String> findSharedsByKey(String[] keys);
}
