package com.ticketmaster.api.discovery.entry.venue;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Artur Bryzhatiy on 18.04.16.
 */
public class Location implements Serializable {

    @SerializedName("longitude")
    String longitude;

    @SerializedName("latitude")
    String latitude;

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
