package com.ticketmaster.api.discovery.entry.base.listener.video;

import com.google.api.services.youtube.model.SearchResult;

import java.util.List;

/**
 * Created by Artur Bryzhatiy on 4/21/16.
 */
public interface YouTubeListener {

    void searchVideosDidComplete(List <SearchResult> searchResults);

}
