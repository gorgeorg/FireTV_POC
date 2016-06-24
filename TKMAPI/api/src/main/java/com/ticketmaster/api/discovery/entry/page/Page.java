package com.ticketmaster.api.discovery.entry.page;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Artur Bryzhatiy on 18.04.16.
 */
public class Page {

    @SerializedName("size")
    Number page;

    @SerializedName("totalElements")
    Number totalElements;

    @SerializedName("totalPages")
    Number totalPages;

    @SerializedName("number")
    Number number;

    public Number getNumber() {
        return number;
    }
}
