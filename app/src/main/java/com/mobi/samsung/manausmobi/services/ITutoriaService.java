package com.mobi.samsung.manausmobi.services;

import com.mobi.samsung.manausmobi.models.SafetyMessage;
import com.mobi.samsung.manausmobi.models.SecurityMessage;

/**
 * Created by fabio.silva on 11/24/2017.
 */

public interface ITutoriaService {
    void add(SecurityMessage message);

    void add(SafetyMessage message);

}
