package com.mobi.samsung.manausmobi.services.impl;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobi.samsung.manausmobi.models.SafetyMessage;
import com.mobi.samsung.manausmobi.models.SecurityMessage;
import com.mobi.samsung.manausmobi.models.Shared;
import com.mobi.samsung.manausmobi.services.ITutoriaService;

/**
 * Created by fabio.silva on 11/24/2017.
 */

class TutoriaService implements ITutoriaService {
    private FirebaseDatabase database;
    private DatabaseReference referenceSecurity;
    private DatabaseReference referenceSafety;

    public TutoriaService() {
        database = FirebaseDatabase.getInstance();
        referenceSecurity = database.getReference("segura").child("messages");
        referenceSafety = database.getReference("saude").child("alert");
    }

    @Override
    public void add(SecurityMessage message) {
        referenceSecurity.push().setValue(message);
    }

    @Override
    public void add(SafetyMessage message) {
        referenceSafety.push().setValue(message);
    }
}
