package com.ticketmaster.api.discovery.entry.event;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Artur Bryzhatiy on 18.04.16.
 */
public class EventDateStart implements Serializable {

    @SerializedName("localDate")
    String localDate;

    @SerializedName("localTime")
    String localTime;

    @SerializedName("dateTime")
    String dateTime;

    @SerializedName("dateTBD")
    boolean dateTBD;

    @SerializedName("dateTBA")
    boolean dateTBA;

    @SerializedName("timeTBA")
    boolean timeTBA;

    @SerializedName("noSpecificTime")
    boolean noSpecificTime;

    public String getLocalDate() {
        return localDate;
    }

    public String getLocalTime() {
        return localTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public boolean isDateTBD() {
        return dateTBD;
    }

    public boolean isDateTBA() {
        return dateTBA;
    }

    public boolean isTimeTBA() {
        return timeTBA;
    }

    public boolean isNoSpecificTime() {
        return noSpecificTime;
    }
}
