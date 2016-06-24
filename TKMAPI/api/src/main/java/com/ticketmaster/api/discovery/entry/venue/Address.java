package com.ticketmaster.api.discovery.entry.venue;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Artur Bryzhatiy on 18.04.16.
 */
public class Address implements Serializable {

    @SerializedName("line1")
    String line1;

    @SerializedName("line2")
    String line2;

    public String getStringAddress() {
        return line1 == null ? "" : (line2 == null ? line1 : line1 +" " + line2);
    }
}
