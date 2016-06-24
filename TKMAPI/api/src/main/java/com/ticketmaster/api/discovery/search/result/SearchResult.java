package com.ticketmaster.api.discovery.search.result;

import com.google.gson.annotations.SerializedName;
import com.ticketmaster.api.discovery.entry.link.Link;
import com.ticketmaster.api.discovery.entry.page.Page;
import com.ticketmaster.api.discovery.search.embedded.EventsEmbedded;

import java.util.HashMap;

/**
 * Created by Artur Bryzhatiy on 4/19/16.
 */
public class SearchResult {

    @SerializedName("_links")
    protected HashMap<String, Link> links;

    @SerializedName("page")
    protected Page page;

    public Page getPage() {
        return page;
    }
}
