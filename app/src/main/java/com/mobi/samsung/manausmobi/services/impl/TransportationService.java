package com.mobi.samsung.manausmobi.services.impl;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobi.samsung.manausmobi.listeners.OnDashboardListener;
import com.mobi.samsung.manausmobi.listeners.OnMapListener;
import com.mobi.samsung.manausmobi.listeners.OnRequestListener;
import com.mobi.samsung.manausmobi.models.Transportation;
import com.mobi.samsung.manausmobi.services.ITransportationService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taynara.p on 11/21/2017.
 */

public class TransportationService implements ITransportationService{
    private FirebaseDatabase database;
    private DatabaseReference reference;
    List<Transportation> transportationList;

    public TransportationService() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("mobilidade").child("transportation");
        transportationList = new ArrayList<>();
    }

    @Override
    public void add(Transportation transportation) {
        reference.push().setValue(transportation);
    }

    @Override
    public void requestList(final OnDashboardListener listener) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Transportation> transpList = new ArrayList<>();
                for (DataSnapshot transpSnapshot : dataSnapshot.getChildren()) {
                    Transportation transportation = transpSnapshot.getValue(Transportation.class);
                    transportation.key = transpSnapshot.getKey();
                    transpList.add(transportation);
                }
                listener.createTranspList(transpList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
