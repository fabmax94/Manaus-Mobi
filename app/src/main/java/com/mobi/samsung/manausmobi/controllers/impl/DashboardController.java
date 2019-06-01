package com.mobi.samsung.manausmobi.controllers.impl;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Pair;

import com.mobi.samsung.manausmobi.controllers.IDashboardController;
import com.mobi.samsung.manausmobi.listeners.OnDashboardListener;
import com.mobi.samsung.manausmobi.models.Shared;
import com.mobi.samsung.manausmobi.models.SharedType;
import com.mobi.samsung.manausmobi.models.TrafficBIDashboard;
import com.mobi.samsung.manausmobi.models.TrafficWarningDashboard;
import com.mobi.samsung.manausmobi.models.Transportation;
import com.mobi.samsung.manausmobi.persistences.AppDatabase;
import com.mobi.samsung.manausmobi.services.ISharedService;
import com.mobi.samsung.manausmobi.services.ITransportationService;
import com.mobi.samsung.manausmobi.services.impl.AbstractServiceFactory;
import com.mobi.samsung.manausmobi.services.impl.ConcreteServiceFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fabio.silva on 11/21/2017.
 */

class DashboardController implements IDashboardController {

    private ISharedService sharedService;
    private AppDatabase app;
    private ITransportationService transportationService;
    private List<Transportation> transportationList;

    public DashboardController(Context context) {
        AbstractServiceFactory serviceFactory = new ConcreteServiceFactory();
        this.app = Room.databaseBuilder(context,
                AppDatabase.class, "database-mobi").allowMainThreadQueries().build();
        sharedService = serviceFactory.createSharedService();
        transportationService = serviceFactory.createTransportationService();
        transportationList = new ArrayList<>();
    }

    @Override
    public void add(Transportation transportation) {
        transportationService.add(transportation);
    }

    @Override
    public void setList(OnDashboardListener listener) {
        List<Transportation> tList = this.app.transportationDAO().findAll();
        for (Transportation transportation : tList) {
            transportationList.add(transportation);
        }
        transportationService.requestList(listener);
    }

    @Override
    public void setListLocal(OnDashboardListener listener) {
        listener.createTranspList(this.app.transportationDAO().findAll());
    }


    @Override
    public void getTrafficWarning(OnDashboardListener listener) {
        sharedService.requestList(listener, false);
    }

    @Override
    public void getTrafficWarningLocal(OnDashboardListener listener) {
        List<Shared> sharedList = this.app.sharedDAO().findAll();
        for (Shared shared : sharedList) {
            shared.convertStringToEnum();
        }
        listener.addShared(sharedList);
    }

    @Override
    public List<TrafficWarningDashboard> groupByLocal(List<Shared> sharedList, boolean isSubLocal) {
        HashMap<String, List<Shared>> map = new HashMap<String, List<Shared>>();
        List<TrafficWarningDashboard> result = new ArrayList<TrafficWarningDashboard>();
        for (Shared shared : sharedList) {
            String key = isSubLocal ? shared.subLocality : shared.thoroughfare;
            if (map.containsKey(key)) {
                List<Shared> list = map.get(key);
                list.add(shared);
            } else {
                List list = new ArrayList<Shared>();
                list.add(shared);
                map.put(key, list);
            }
        }
        for (Map.Entry<String, List<Shared>> entry : map.entrySet()) {
            int trafficWeight = 0;
            int warningWeight = 0;
            for (Shared shared : entry.getValue()) {
                if (shared.type == SharedType.Traffic) {
                    trafficWeight += shared.intensity.getWeight();
                } else {
                    warningWeight += shared.intensity.getWeight();
                }
            }
            result.add(new TrafficWarningDashboard(trafficWeight, warningWeight, entry.getKey()));
        }
        return result;
    }

    @Override
    public List<TrafficBIDashboard> groupByLocal(List<Shared> sharedList) {
        HashMap<String, List<Shared>> map = new HashMap<String, List<Shared>>();
        HashMap<String, HashMap<String, List<Shared>>> mapHours = new HashMap<String, HashMap<String, List<Shared>>>();
        ArrayList<Pair<Integer, Integer>> hours = new ArrayList<Pair<Integer, Integer>>() {
            {
                add(new Pair<Integer, Integer>(5, 9));
                add(new Pair<Integer, Integer>(10, 12));
                add(new Pair<Integer, Integer>(12, 14));
                add(new Pair<Integer, Integer>(14, 17));
                add(new Pair<Integer, Integer>(17, 19));
                add(new Pair<Integer, Integer>(19, 23));
            }
        };

        List<TrafficBIDashboard> result = new ArrayList<TrafficBIDashboard>();
        for (Shared shared : sharedList) {
            String key = shared.subLocality;
            if (map.containsKey(key)) {
                List<Shared> list = map.get(key);
                list.add(shared);
            } else {
                List list = new ArrayList<Shared>();
                list.add(shared);
                map.put(key, list);
            }
        }

        for (Map.Entry<String, List<Shared>> entry : map.entrySet()) {
            for (final Shared shared : entry.getValue()) {

                String key = null;
                for (Pair<Integer, Integer> hour : hours) {
                    if (shared.getHours() >= hour.first && hour.second >= shared.getHours()) {
                        key = hour.first + "-" + hour.second;
                    }
                }

                if (mapHours.containsKey(entry.getKey())) {
                    if (mapHours.get(entry.getKey()).containsKey(key)) {
                        mapHours.get(entry.getKey()).get(key).add(shared);
                    } else {
                        mapHours.get(entry.getKey()).put(key, new ArrayList<Shared>() {
                            {
                                add(shared);
                            }
                        });
                    }
                } else {
                    HashMap<String, List<Shared>> mapTemp = new HashMap<String, List<Shared>>();
                    mapTemp.put(key, new ArrayList<Shared>() {
                        {
                            add(shared);
                        }
                    });
                    mapHours.put(entry.getKey(), mapTemp);
                }
            }
        }

        int maxWeight = 0;
        for (Map.Entry<String, HashMap<String, List<Shared>>> entries : mapHours.entrySet()) {
            for (Map.Entry<String, List<Shared>> entry : entries.getValue().entrySet()) {
                int trafficWeight = 0;
                for (Shared shared : entry.getValue()) {
                    if (shared.type == SharedType.Traffic) {
                        trafficWeight += shared.intensity.getWeight();
                    }
                }
                if (trafficWeight > maxWeight) {
                    maxWeight = trafficWeight;
                }
                result.add(new TrafficBIDashboard(entries.getKey(), entry.getKey(), trafficWeight));
            }
        }

        for (TrafficBIDashboard norm : result) {
            norm.setTrafficWeight(norm.getTrafficWeight() / maxWeight);
        }

        return result;
    }

    @Override
    public List<TrafficBIDashboard> groupByHour(List<Shared> sharedList) {
        HashMap<String, List<Shared>> map = new HashMap<String, List<Shared>>();
        HashMap<String, HashMap<String, List<Shared>>> mapHours = new HashMap<String, HashMap<String, List<Shared>>>();
        ArrayList<Pair<Integer, Integer>> hours = new ArrayList<Pair<Integer, Integer>>() {
            {
                add(new Pair<Integer, Integer>(5, 9));
                add(new Pair<Integer, Integer>(10, 12));
                add(new Pair<Integer, Integer>(12, 14));
                add(new Pair<Integer, Integer>(14, 17));
                add(new Pair<Integer, Integer>(17, 19));
                add(new Pair<Integer, Integer>(19, 23));
            }
        };

        List<TrafficBIDashboard> result = new ArrayList<TrafficBIDashboard>();
        for (Shared shared : sharedList) {
            String key = null;
            for (Pair<Integer, Integer> hour : hours) {
                if (shared.getHours() >= hour.first && hour.second >= shared.getHours()) {
                    String first = hour.first == 5 ? "05" : hour.first.toString();
                    String second = hour.second == 9 ? "09" : hour.second.toString();
                    key = first + "-" + second;
                }
            }
            if (map.containsKey(key)) {
                List<Shared> list = map.get(key);
                list.add(shared);
            } else {
                List list = new ArrayList<Shared>();
                list.add(shared);
                map.put(key, list);
            }
        }


        for (Map.Entry<String, List<Shared>> entry : map.entrySet()) {
            for (final Shared shared : entry.getValue()) {
                String key = shared.subLocality;
                if (mapHours.containsKey(entry.getKey())) {
                    if (mapHours.get(entry.getKey()).containsKey(key)) {
                        mapHours.get(entry.getKey()).get(key).add(shared);
                    } else {
                        List<Shared> listTemp = new ArrayList<Shared>();
                        listTemp.add(shared);
                        mapHours.get(entry.getKey()).put(key, listTemp);
                    }
                } else {
                    HashMap<String, List<Shared>> mapTemp = new HashMap<String, List<Shared>>();
                    mapTemp.put(key, new ArrayList<Shared>() {
                        {
                            add(shared);
                        }
                    });
                    mapHours.put(entry.getKey(), mapTemp);
                }
            }
        }

        for (Map.Entry<String, HashMap<String, List<Shared>>> entries : mapHours.entrySet()) {
            for (Map.Entry<String, List<Shared>> entry : entries.getValue().entrySet()) {
                int trafficWeight = 0;
                for (Shared shared : entry.getValue()) {
                    if (shared.type == SharedType.Traffic) {
                        trafficWeight += shared.intensity.getWeight();
                    }
                }
                result.add(new TrafficBIDashboard(entry.getKey(), entries.getKey(), trafficWeight));
            }
        }

        return result;
    }

    @Override
    public List<TrafficBIDashboard> filterByLocal(List<TrafficBIDashboard> dashboardList, String local) {
        List<TrafficBIDashboard> result = new ArrayList<>();
        for (TrafficBIDashboard dashboard : dashboardList) {
            if (dashboard.getLocal() != null && dashboard.getHours() != null) {
                if (dashboard.getLocal().equals(local)) {
                    result.add(dashboard);
                }
            }
        }
        return result;
    }

    @Override
    public List<TrafficBIDashboard> filterByHours(List<TrafficBIDashboard> dashboardList, String subOption) {
        List<TrafficBIDashboard> result = new ArrayList<>();
        for (TrafficBIDashboard dashboard : dashboardList) {
            if (dashboard.getLocal() != null && dashboard.getHours() != null) {
                if (dashboard.getHours().equals(subOption)) {
                    result.add(dashboard);
                }
            }
        }
        return result;
    }

    @Override
    public void addTransportation(List<Transportation> transpList) {
        for (Transportation transportation : transpList) {
            if (this.app.transportationDAO().get(transportation.key) == null) {
                this.app.transportationDAO().save(transportation);
            }
        }
    }
}
