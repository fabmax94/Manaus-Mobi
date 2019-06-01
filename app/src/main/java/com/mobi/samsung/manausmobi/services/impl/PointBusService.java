package com.mobi.samsung.manausmobi.services.impl;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobi.samsung.manausmobi.listeners.OnMapListener;
import com.mobi.samsung.manausmobi.models.InfoBus;
import com.mobi.samsung.manausmobi.models.PointBus;
import com.mobi.samsung.manausmobi.services.IPointBusService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by daniel on 17/11/17.
 */

class PointBusService implements IPointBusService {
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ChildEventListener requestEventListener;

    public PointBusService() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("mobilidade").child("pointBus");
    }

    @Override
    public void add(PointBus pointBus) {
        reference.push().setValue(pointBus);
    }


    @Override
    public void requestList(final OnMapListener listener) {
        reference.addChildEventListener(requestEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PointBus pointBus = dataSnapshot.getValue(PointBus.class);
                if (pointBus != null) {
                    pointBus.key = dataSnapshot.getKey();
                    Set<Map.Entry<String, HashMap<String, String>>> setList = ((HashMap<String, HashMap<String, String>>) dataSnapshot.child("lines").getValue()).entrySet();
                    for (Map.Entry<String, HashMap<String, String>> bus : setList) {
                        pointBus.pointBusList.add(new InfoBus(bus.getKey(), bus.getValue().get("line")));
                    }
                    listener.addPointBus(pointBus);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void cleanEventListener() {
        if (requestEventListener != null) {
            reference.removeEventListener(requestEventListener);
        }
    }
}
