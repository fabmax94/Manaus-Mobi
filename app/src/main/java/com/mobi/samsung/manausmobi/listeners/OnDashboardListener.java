package com.mobi.samsung.manausmobi.listeners;

import com.mobi.samsung.manausmobi.models.Shared;
import com.mobi.samsung.manausmobi.models.SharedPoint;
import com.mobi.samsung.manausmobi.models.Transportation;

import java.util.List;

/**
 * Created by fabio.silva on 11/21/2017.
 */

public interface OnDashboardListener extends OnRequestListener {
    void getTrafficWarning();

    void changeLocal();

    void createTranspList(List<Transportation> transpList);

    void changeBILocal();

    void changeBIOption(boolean s);

    void changeBIHours();

    void addShared(List<Shared> shared);
}
