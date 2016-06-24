package com.ticketmaster.api.discovery.entry.event;

import com.google.gson.annotations.SerializedName;
import com.ticketmaster.api.discovery.entry.attraction.Attraction;
import com.ticketmaster.api.discovery.entry.venue.Venue;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Artur Bryzhatiy on 18.04.16.
 */
public class EventEmbedded implements Serializable {

    @SerializedName("venues")
    List<Venue> venues;

    @SerializedName("attractions")
    List<Attraction> attractions;

    public List<Venue> getVenues() {
        return venues;
    }

    public List<Attraction> getAttractions() {
        return attractions;
    }
}
