package com.mobi.samsung.manausmobi.persistences;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.mobi.samsung.manausmobi.models.Transportation;

import java.util.List;

/**
 * Created by taynara.p on 11/21/2017.
 */

@Dao
public interface ITransportationDAO {
    @Insert
    void save(Transportation transportation);

    @Delete
    void delete(Transportation transportation);

    @Query("SELECT *FROM transportation")
    List<Transportation> findAll();

    @Query("SELECT *FROM transportation WHERE `key` like :key")
    Transportation get(String key);
}
