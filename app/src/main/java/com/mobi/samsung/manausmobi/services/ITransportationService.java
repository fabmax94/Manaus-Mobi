package com.mobi.samsung.manausmobi.services;

import com.mobi.samsung.manausmobi.listeners.OnDashboardListener;
import com.mobi.samsung.manausmobi.listeners.OnRequestListener;
import com.mobi.samsung.manausmobi.models.Transportation;

import java.util.List;

/**
 * Created by taynara.p on 11/21/2017.
 */

public interface ITransportationService {
    void add(Transportation transportation);
    void requestList(final OnDashboardListener listener);
}
