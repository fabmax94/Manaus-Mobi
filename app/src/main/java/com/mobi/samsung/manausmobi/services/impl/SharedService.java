package com.mobi.samsung.manausmobi.services.impl;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobi.samsung.manausmobi.listeners.OnDashboardListener;
import com.mobi.samsung.manausmobi.listeners.OnMapListener;
import com.mobi.samsung.manausmobi.listeners.OnRequestListener;
import com.mobi.samsung.manausmobi.listeners.OnSharedListener;
import com.mobi.samsung.manausmobi.models.Shared;
import com.mobi.samsung.manausmobi.models.SharedPoint;
import com.mobi.samsung.manausmobi.services.ISharedService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fabio.silva on 11/16/2017.
 */

class SharedService implements ISharedService {

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private ValueEventListener requestEventListener;
    private ChildEventListener requestRemoveChildEventListener;
    private ValueEventListener extistOrDeleteEventListener;
    private ValueEventListener extistWithImageEventListener;

    public SharedService() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("mobilidade").child("shared");
    }

    @Override
    public void add(Shared shared) {

        reference.push().setValue(shared);
    }

    @Override
    public void requestList(final OnRequestListener listener, final boolean isFilterDate) {
        reference.addValueEventListener(requestEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Shared> sharedList = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Shared shared = snap.getValue(Shared.class);
                    if (shared != null) {
                        shared.key = snap.getKey();
                        if ((isFilterDate && shared.isValidDate()) || !isFilterDate) {
                            sharedList.add(shared);
                        }
                    }
                }
                if (listener instanceof OnDashboardListener) {
                    ((OnDashboardListener) listener).addShared(sharedList);
                } else {
                    List<SharedPoint> sharedPoints = SharedPoint.generateSharedPoint(sharedList);
                    ((OnMapListener) listener).addShared(sharedPoints);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        reference.addChildEventListener(requestRemoveChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Shared shared = dataSnapshot.getValue(Shared.class);
                if (shared != null && listener instanceof OnMapListener) {
                    shared.key = dataSnapshot.getKey();
                    ((OnMapListener) listener).removeShared(shared);
                }
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
    public void existOrDelete(final List<Shared> shares, final OnMapListener listener) {
        reference.addValueEventListener(extistOrDeleteEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Shared> td = (HashMap<String, Shared>) dataSnapshot.getValue();
                List<String> keys = new ArrayList<String>();
                if (td != null) {
                    keys.addAll(td.keySet());
                }

                for (Shared shared : shares) {
                    if (!keys.contains(shared.key) || !shared.isValidDate()) {
                        listener.removeShared(shared);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void existWithImage(final Shared shared, final OnSharedListener listener) {
        reference.addValueEventListener(extistWithImageEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, HashMap<String, String>> td = (HashMap<String, HashMap<String, String>>) dataSnapshot.getValue();
                int count = 0;
                if (td != null) {
                    for (HashMap<String, String> s : td.values()) {
                        if (s.get("userId").equals(shared.userId) && s.get("image") != null) {
                            count++;
                        }
                    }
                }
                listener.isValidSaveShared(count <= 2, shared);
                reference.removeEventListener(this);
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
        if (requestRemoveChildEventListener != null) {
            reference.removeEventListener(requestRemoveChildEventListener);
        }
        if (extistOrDeleteEventListener != null) {
            reference.removeEventListener(extistOrDeleteEventListener);
        }
        if (extistWithImageEventListener != null) {
            reference.removeEventListener(extistWithImageEventListener);
        }
    }
}
