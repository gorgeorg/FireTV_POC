package com.ticketmaster.api.discovery.search.result;

import com.google.gson.annotations.SerializedName;
import com.ticketmaster.api.discovery.search.embedded.EventsEmbedded;

/**
 * Created by Artur Bryzhatiy on 4/18/16.
 */
public class SearchEventsResult extends SearchResult {

    @SerializedName("_embedded")
    EventsEmbedded embedded;

    public EventsEmbedded getEmbedded() {
        return embedded;
    }
}
