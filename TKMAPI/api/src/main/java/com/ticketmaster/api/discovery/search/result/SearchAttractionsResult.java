package com.ticketmaster.api.discovery.search.result;

import com.google.gson.annotations.SerializedName;
import com.ticketmaster.api.discovery.search.embedded.AttractionsEmbedded;

/**
 * Created by Artur Bryzhatiy on 4/19/16.
 */
public class SearchAttractionsResult extends SearchResult {

    @SerializedName("_embedded")
    AttractionsEmbedded embedded;

}
