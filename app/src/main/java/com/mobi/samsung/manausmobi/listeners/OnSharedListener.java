package com.mobi.samsung.manausmobi.listeners;

import com.mobi.samsung.manausmobi.models.PointBus;
import com.mobi.samsung.manausmobi.models.Shared;

/**
 * Created by fabio.silva on 11/16/2017.
 */

public interface OnSharedListener {

    void saveShared(Shared shared);

    void getCamera();

    void isValidSaveShared(boolean isValid, Shared shared);

    void screenState(boolean screenStateFlag);

}
