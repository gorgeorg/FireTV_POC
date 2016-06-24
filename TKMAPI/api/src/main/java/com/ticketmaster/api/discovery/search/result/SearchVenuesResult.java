package com.ticketmaster.api.discovery.search.result;

import com.google.gson.annotations.SerializedName;
import com.ticketmaster.api.discovery.search.embedded.VenuesEmbedded;

/**
 * Created by Artur Bryzhatiy on 4/19/16.
 */
public class SearchVenuesResult extends SearchResult {

    @SerializedName("_embedded")
    VenuesEmbedded embedded;


}
