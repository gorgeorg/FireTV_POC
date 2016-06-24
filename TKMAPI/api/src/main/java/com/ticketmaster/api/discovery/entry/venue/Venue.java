package com.ticketmaster.api.discovery.entry.venue;

import com.google.gson.annotations.SerializedName;
import com.ticketmaster.api.discovery.entry.base.BaseModel;

import java.util.List;

/**
 * Created by Artur Bryzhatiy on 18.04.16.
 */
public class Venue extends BaseModel {

    @SerializedName("postalCode")
    String postalCode;

    @SerializedName("timezone")
    String timeZone;

    @SerializedName("city")
    City city;

    @SerializedName("state")
    State state;

    @SerializedName("country")
    Country country;

    @SerializedName("address")
    Address address;

    @SerializedName("location")
    Location location;

    @SerializedName("markets")
    List <Market> markets;

    public Location getLocation() {
        return location;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public Address getAddress() {
        return address;
    }

    public City getCity() {
        return city;
    }

    public Country getCountry() {
        return country;
    }

    public State getState() {
        return state;
    }
}
