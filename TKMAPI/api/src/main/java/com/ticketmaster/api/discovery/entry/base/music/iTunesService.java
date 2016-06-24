package com.ticketmaster.api.discovery.entry.base.music;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Artur Bryzhatiy on 4/21/16.
 */
public interface iTunesService {

    @GET("search")
    Call <SearchMusicResult> search(
            @Query("term")  String term,
            @Query("media") String media,
            @Query("limit") long limit
    );

}
