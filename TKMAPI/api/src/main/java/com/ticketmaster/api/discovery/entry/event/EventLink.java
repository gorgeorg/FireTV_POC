package com.ticketmaster.api.discovery.entry.event;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Artur Bryzhatiy on 18.04.16.
 */
public class EventLink implements Serializable {

    @SerializedName("href")
    String href;

    public String getHref() {
        return href;
    }
}
