package com.mobi.samsung.manausmobi.persistences;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.mobi.samsung.manausmobi.models.PointBus;

import java.util.List;

/**
 * Created by daniel on 17/11/17.
 */

@Dao
public interface IPointBusDAO {
    @Insert
    void save(PointBus pointBus);

    @Delete
    void delete(PointBus pointBus);

    @Query("SELECT * FROM pointBus")
    List<PointBus> findAll();

    @Query("SELECT * FROM pointBus WHERE `key` like :key")
    PointBus get(String key);
}
