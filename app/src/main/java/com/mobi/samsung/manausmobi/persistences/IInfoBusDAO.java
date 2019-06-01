package com.mobi.samsung.manausmobi.persistences;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.mobi.samsung.manausmobi.models.InfoBus;
import com.mobi.samsung.manausmobi.models.PointBus;

import java.util.List;

/**
 * Created by daniel on 17/11/17.
 */

@Dao
public interface IInfoBusDAO {
    @Insert
    void save(InfoBus pointBus);

    @Delete
    void delete(InfoBus pointBus);

    @Query("SELECT * FROM InfoBus")
    List<InfoBus> findAll();

    @Query("SELECT * FROM InfoBus WHERE `pointBusId` like  :pointBusId")
    List<InfoBus> findAllByInfoBusId(int pointBusId);

    @Query("SELECT * FROM InfoBus WHERE `key` like :key")
    InfoBus get(String key);
}
