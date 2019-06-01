package com.mobi.samsung.manausmobi.persistences;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.mobi.samsung.manausmobi.models.Shared;

import java.util.List;

/**
 * Created by fabio.silva on 11/6/2017.
 */

@Dao
public interface ISharedDAO {
    @Insert
    void save(Shared shared);

    @Delete
    void delete(Shared shared);

    @Query("SELECT *FROM shared")
    List<Shared> findAll();

    @Query("SELECT *FROM shared WHERE `key` like :key")
    Shared get(String key);
}
