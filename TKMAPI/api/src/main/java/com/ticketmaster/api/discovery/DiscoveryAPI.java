package com.ticketmaster.api.discovery;

import com.ticketmaster.api.discovery.entry.attraction.Attraction;
import com.ticketmaster.api.discovery.entry.classification.Classification;
import com.ticketmaster.api.discovery.entry.image.Images;
import com.ticketmaster.api.discovery.entry.event.Event;
import com.ticketmaster.api.discovery.entry.venue.Venue;
import com.ticketmaster.api.discovery.search.result.SearchAttractionsResult;
import com.ticketmaster.api.discovery.search.result.SearchClassificationsResult;
import com.ticketmaster.api.discovery.search.result.SearchEventsResult;
import com.ticketmaster.api.discovery.search.result.SearchVenuesResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Artur Bryzhatiy on 4/18/16.
 */
public interface DiscoveryAPI {

    /**
     *  http://developer.ticketmaster.com/products-and-docs/apis/discovery/v2/#srch-events-v2
     */
    @GET("discovery/{version}/events.{format}")
    Call<SearchEventsResult> searchEvents(
            @Path("version")        String apiVersion,
            @Path("format")         String responseFormat,
            @Query("apikey")        String apiKey,
            @Query("keyword")       String keyword,
            @Query("attractionId")  String attractionId,
            @Query("venueId")       String venueId,
            @Query("promoterId")    String promoterId,
            @Query("postalCode")    String postalCode,
            @Query("latlong")       String latlong,
            @Query("radius")        String radius,
            @Query("unit")          String unit,
            @Query("source")        String source,
            @Query("locale")        String locale,
            @Query("marketId")      String marketId,
            @Query("startDateTime") String startDateTime,
            @Query("endDateTime")   String endDateTime,
            @Query("includeTBA")    String includeTBA,
            @Query("includeTBD")    String includeTBD,
            @Query("includeTest")   String includeTest,
            @Query("size")          String size,
            @Query("page")          String page,
            @Query("sort")          String sort
    );

    /**
     *  http://developer.ticketmaster.com/products-and-docs/apis/discovery/v2/#event-details-v2
     */
    @GET("discovery/{version}/events/{id}.{format}")
    Call<Event> getEventDetails(
            @Path("version")    String apiVersion,
            @Path("id")         String eventId,
            @Path("format")     String responseFormat,
            @Query("apikey")    String apiKey,
            @Query("locale")    String locale
    );

    /**
     *  http://developer.ticketmaster.com/products-and-docs/apis/discovery/v2/#event-img-v2
     */
    @GET("discovery/{version}/events/{id}/images.{format}")
    Call<Images> searchEventImages(
            @Path("version")    String apiVersion,
            @Path("id")         String eventId,
            @Path("format")     String responseFormat,
            @Query("apikey")    String apiKey,
            @Query("locale")    String locale
    );

    /**
     *  http://developer.ticketmaster.com/products-and-docs/apis/discovery/v2/#search-attractions-v2
     */
    @GET("discovery/{version}/attractions.{format}")
    Call<SearchAttractionsResult> searchAttractions(
            @Path("version")    String apiVersion,
            @Path("format")     String responseFormat,
            @Query("apikey")    String apiKey,
            @Query("keyword")   String keyword,
            @Query("domain")    String domain,
            @Query("locale")    String locale,
            @Query("size")      String size,
            @Query("page")      String page,
            @Query("sort")      String sort
    );

    /**
     *  http://developer.ticketmaster.com/products-and-docs/apis/discovery/v2/#attraction-details-v2
     */
    @GET("discovery/{version}/attractions/{attraction_id}.{format}")
    Call<Attraction> getAttractionDetails(
            @Path("version")        String apiVersion,
            @Path("attraction_id")  String attractionId,
            @Path("format")         String responseFormat,
            @Query("apikey")        String apiKey,
            @Query("locale")        String locale
    );

    /**
     *  http://developer.ticketmaster.com/products-and-docs/apis/discovery/v2/#search-classifications-v2
     */
    @GET("discovery/{version}/classifications.{format}")
    Call<SearchClassificationsResult> searchClassifications(
            @Path("version")    String apiVersion,
            @Path("format")     String responseFormat,
            @Query("apikey")    String apiKey,
            @Query("keyword")   String keyword,
            @Query("size")      String size,
            @Query("page")      String page,
            @Query("sort")      String sort
    );

    /**
     *  http://developer.ticketmaster.com/products-and-docs/apis/discovery/v2/#classifications-details-v2
     */
    @GET("discovery/{version}/classifications/{classification_id}.{format}")
    Call<Classification> getClassificationDetails(
            @Path("version")            String apiVersion,
            @Path("classification_id")  String classificationId,
            @Path("format")             String responseFormat,
            @Query("apikey")            String apiKey,
            @Query("locale")            String locale
    );

    /**
     *  http://developer.ticketmaster.com/products-and-docs/apis/discovery/v2/#search-venues-v2
     */
    @GET("discovery/{version}/venues.{format}")
    Call<SearchVenuesResult> searchVenues(
            @Path("version")    String apiVersion,
            @Path("format")     String responseFormat,
            @Query("apikey")    String apiKey,
            @Query("keyword")   String keyword,
            @Query("locale")    String locale,
            @Query("size")      String size,
            @Query("page")      String page,
            @Query("sort")      String sort
    );

    /**
     *  http://developer.ticketmaster.com/products-and-docs/apis/discovery/v2/#venue-details-v2
     */
    @GET("discovery/{version}/venues/{venue_id}.{format}")
    Call<Venue> getVenueDetails(
            @Path("version")    String apiVersion,
            @Path("venue_id")   String venueId,
            @Path("format")     String responseFormat,
            @Query("apikey")    String apiKey,
            @Query("locale")    String locale
    );
}
