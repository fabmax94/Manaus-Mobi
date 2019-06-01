package com.mobi.samsung.manausmobi.persistences;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.mobi.samsung.manausmobi.models.InfoBus;
import com.mobi.samsung.manausmobi.models.PointBus;
import com.mobi.samsung.manausmobi.models.Shared;
import com.mobi.samsung.manausmobi.models.Transportation;

/**
 * Created by fabio.silva on 11/6/2017.
 */

@Database(entities = {Shared.class, PointBus.class, Transportation.class, InfoBus.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ISharedDAO sharedDAO();

    public abstract IInfoBusDAO inboBusDAO();

    public abstract IPointBusDAO pointBusDAO();

    public abstract ITransportationDAO transportationDAO();
}
