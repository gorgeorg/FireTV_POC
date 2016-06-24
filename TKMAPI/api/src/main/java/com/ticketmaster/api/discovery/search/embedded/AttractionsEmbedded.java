package com.ticketmaster.api.discovery.search.embedded;

import com.google.gson.annotations.SerializedName;
import com.ticketmaster.api.discovery.entry.attraction.Attraction;

import java.util.List;

/**
 * Created by Artur Bryzhatiy on 4/19/16.
 */
public class AttractionsEmbedded {

    @SerializedName("attractions")
    List<Attraction> attractions;

}
