package com.ticketmaster.api.discovery.entry.event;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Artur Bryzhatiy on 18.04.16.
 */
public class EventDate implements Serializable {

    @SerializedName("start")
    EventDateStart start;

    @SerializedName("timezone")
    String timeZone;

    @SerializedName("status")
    EventDateStatus status;

    public EventDateStart getStart() {
        return start;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public EventDateStatus getStatus() {
        return status;
    }

}
