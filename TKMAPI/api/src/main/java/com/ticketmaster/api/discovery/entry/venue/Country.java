package com.ticketmaster.api.discovery.entry.venue;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Artur Bryzhatiy on 18.04.16.
 */
public class Country implements Serializable {

    @SerializedName("name")
    String name;

    @SerializedName("countryCode")
    String countryCode;

    public String getCountryCode() {
        return countryCode;
    }

    public String getName() {
        return name;
    }
}
