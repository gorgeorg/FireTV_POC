package com.ticketmaster.api.discovery.search.embedded;

import com.google.gson.annotations.SerializedName;
import com.ticketmaster.api.discovery.entry.event.Event;

import java.util.List;

/**
 * Created by Artur Bryzhatiy on 18.04.16.
 */
public class EventsEmbedded {

    @SerializedName("events")
    List <Event> events;

    public List<Event> getEvents() {
        return events;
    }
}
