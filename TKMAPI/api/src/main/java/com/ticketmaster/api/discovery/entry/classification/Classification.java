package com.ticketmaster.api.discovery.entry.classification;

import com.google.gson.annotations.SerializedName;
import com.ticketmaster.api.discovery.entry.link.Link;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Artur Bryzhatiy on 18.04.16.
 */
public class Classification implements Serializable {

    @SerializedName("primary")
    boolean primary;

    @SerializedName("segment")
    ClassificationOption segment;

    @SerializedName("genre")
    ClassificationOption genre;

    @SerializedName("subGenre")
    ClassificationOption subGenre;

    @SerializedName("_links")
    HashMap<String, Link> links;

    public ClassificationOption getSegment() {
        return segment;
    }
}
