package com.mobi.samsung.manausmobi.controllers;

import com.mobi.samsung.manausmobi.listeners.OnDashboardListener;
import com.mobi.samsung.manausmobi.models.Shared;
import com.mobi.samsung.manausmobi.models.TrafficBIDashboard;
import com.mobi.samsung.manausmobi.models.TrafficWarningDashboard;
import com.mobi.samsung.manausmobi.models.Transportation;

import java.util.List;

/**
 * Created by fabio.silva on 11/21/2017.
 */

public interface IDashboardController {

    void getTrafficWarning(OnDashboardListener listener);

    void getTrafficWarningLocal(OnDashboardListener listener);

    List<TrafficWarningDashboard> groupByLocal(List<Shared> sharedList, boolean isSubLocal);

    List<TrafficBIDashboard> groupByLocal(List<Shared> sharedList);

    List<TrafficBIDashboard> groupByHour(List<Shared> sharedList);

    List<TrafficBIDashboard> filterByLocal(List<TrafficBIDashboard> dashboardList, String local);

    List<TrafficBIDashboard> filterByHours(List<TrafficBIDashboard> dashboardList, String subOption);

    void addTransportation(List<Transportation> transpList);

    void setList(OnDashboardListener listener);

    void setListLocal(OnDashboardListener listener);

    void add(Transportation transportation);
}
