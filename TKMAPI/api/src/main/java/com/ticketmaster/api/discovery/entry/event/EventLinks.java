package com.ticketmaster.api.discovery.entry.event;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Artur Bryzhatiy on 18.04.16.
 */
public class EventLinks implements Serializable {

    @SerializedName("self")
    EventLink self;

    @SerializedName("attractions")
    List <EventLink> attractions;

    @SerializedName("venues")
    List <EventLink> venues;

    public EventLink getSelf() {
        return self;
    }

    public List<EventLink> getAttractions() {
        return attractions;
    }

    public List<EventLink> getVenues() {
        return venues;
    }
}
