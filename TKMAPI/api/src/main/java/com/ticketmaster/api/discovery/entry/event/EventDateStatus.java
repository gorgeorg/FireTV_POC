package com.ticketmaster.api.discovery.entry.event;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Artur Bryzhatiy on 18.04.16.
 */
public class EventDateStatus implements Serializable {

    @SerializedName("code")
    String code;

    public String getCode() {
        return code;
    }
}
