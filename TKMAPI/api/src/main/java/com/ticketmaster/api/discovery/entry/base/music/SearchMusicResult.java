package com.ticketmaster.api.discovery.entry.base.music;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Artur Bryzhatiy on 4/21/16.
 */
public class SearchMusicResult {

    @SerializedName("results")
    List <iTunesItem> iTunesItems;

    public List<iTunesItem> getITunesItems() {
        return iTunesItems;
    }
}
