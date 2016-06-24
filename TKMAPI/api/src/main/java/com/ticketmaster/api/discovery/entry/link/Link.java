package com.ticketmaster.api.discovery.entry.link;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Artur Bryzhatiy on 18.04.16.
 */
public class Link implements Serializable {

    @SerializedName("href")
    String href;

    @SerializedName("next")
    String next;

    public String getNext() {
        return next;
    }

    public String getHref() {
        return href;
    }
}
