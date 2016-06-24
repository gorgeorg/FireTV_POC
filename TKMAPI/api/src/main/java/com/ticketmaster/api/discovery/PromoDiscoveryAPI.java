package com.ticketmaster.api.discovery;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.ticketmaster.api.discovery.entry.attraction.Attraction;
import com.ticketmaster.api.discovery.entry.base.listener.flickr.PhotoModelListener;
import com.ticketmaster.api.discovery.entry.base.listener.video.VideoModelListener;
import com.ticketmaster.api.discovery.entry.base.music.iTunesItemListener;
import com.ticketmaster.api.discovery.entry.classification.Classification;
import com.ticketmaster.api.discovery.entry.image.Image;
import com.ticketmaster.api.discovery.entry.image.Images;
import com.ticketmaster.api.discovery.entry.event.Event;
import com.ticketmaster.api.discovery.entry.venue.Venue;
import com.ticketmaster.api.discovery.search.result.SearchAttractionsResult;
import com.ticketmaster.api.discovery.search.result.SearchClassificationsResult;
import com.ticketmaster.api.discovery.search.result.SearchEventsResult;
import com.ticketmaster.api.discovery.search.result.SearchVenuesResult;
import com.ticketmaster.api.generator.APIServiceGenerator;
import com.ticketmaster.api.helper.PropertiesHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Artur Bryzhatiy on 18.04.16.
 */
public class PromoDiscoveryAPI {

    private static final String TAG = "PromoDiscoveryAPI";

    public static final String API_VERSION      = "v2";
    public static final String RESPONSE_FORMAT  = "json";
    public static final String API_KEY          = "hGohNpTGv0l2dh8rTGNNeccYd0lLkW5e";//"KRUnjq8y8Sg5eDpP90dNzOK70d4WiUst";
    public static final String STATIC_MAP_API_KEY          = "AIzaSyDHNZ5EZj6JPSd9bTlLv8jLRypjnagL0PE";

    public static final String TEST_EVENT_ID = "LvZ184j83k1A5TPn6ZZgc";
    public static final String TEST_ATTRACTION_ID = "K8vZ9178147";
    public static final String TEST_CLASSIFICATION_ID = "KZFzniwnSyZfZ7v7na";
    public static final String TEST_VENUE_ID = "KovZpZAFnIEA";

    public static void performSearchEventsCall() {
        Log.i(TAG, "performSearchEventsCall: ");

        DiscoveryAPI api = APIServiceGenerator.createService(DiscoveryAPI.class);

        Call<SearchEventsResult> call = api.searchEvents(
                API_VERSION,
                RESPONSE_FORMAT,
                API_KEY,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
        );
        call.enqueue(
                new Callback<SearchEventsResult>() {
                    @Override
                    public void onResponse(Call<SearchEventsResult> call, Response<SearchEventsResult> response) {
                        Log.i(TAG, "onResponse: ");
                        if (response.isSuccessful()) {
                            try {
                                // Retrieve root object of response json
                                SearchEventsResult searchEventsResult = response.body();
                                Event event = searchEventsResult.getEmbedded().getEvents().get(0);

//                                event.retrieveVideos(
//                                        10,
//                                        new VideoModelListener() {
//                                            @Override
//                                            public void videosDidBuild() {
//                                                Log.i(TAG, "videosDidBuild: ");
//                                            }
//                                        }
//                                );
//                                event.retrieveFlickrImages(
//                                        1, 10,
//                                        new PhotoModelListener() {
//                                            @Override
//                                            public void photosDidBuild() {
//                                                Log.i(TAG, "photosDidBuild: ");
//                                            }
//                                        }
//                                );
//                                event.retrieveITunesMusic(
//                                        new iTunesItemListener() {
//                                            @Override
//                                            public void emptyResultDidReceive() {
//                                                Log.i(TAG, "emptyResultDidReceive: ");
//                                            }
//
//                                            @Override
//                                            public void iTunesItemsDidBuild() {
//                                                Log.i(TAG, "iTunesItemsDidBuild: ");
//                                            }
//                                        }
//                                );

                                Log.i(TAG, "onResponse: success");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try{
                                // Handle an api error here
                                Log.i(TAG, "onResponse: fault");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchEventsResult> call, Throwable t) {
                        Log.i(TAG, "onFailure: ");
                    }
                }
        );
    }

    public static void performEventDetailsCall() {
        Log.i(TAG, "performEventDetailsCall: ");

        DiscoveryAPI api = APIServiceGenerator.createService(DiscoveryAPI.class);

        Call <Event> eventDetailsCall = api.getEventDetails(
                API_VERSION,
                TEST_EVENT_ID,
                RESPONSE_FORMAT,
                API_KEY,
                null
        );
        eventDetailsCall.enqueue(
                new Callback<Event>() {
                    @Override
                    public void onResponse(Call<Event> call, Response<Event> response) {
                        Log.i(TAG, "onResponse: ");
                        if (response.isSuccessful()) {
                            try {
                                // Retrieve event
                                Event event = response.body();

                                Log.i(TAG, "onResponse: success");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try{
                                // Handle an api error here
                                Log.i(TAG, "onResponse: fault");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Event> call, Throwable t) {
                        Log.i(TAG, "onFailure: ");
                    }
                }
        );
    }

    public static void performSearchEventImagesCall() {
        Log.i(TAG, "performSearchEventImagesCall: ");

        DiscoveryAPI api = APIServiceGenerator.createService(DiscoveryAPI.class);

        Call <Images> eventImagesCall = api.searchEventImages(
                API_VERSION,
                TEST_EVENT_ID,
                RESPONSE_FORMAT,
                API_KEY,
                null
        );
        eventImagesCall.enqueue(
                new Callback<Images>() {
                    @Override
                    public void onResponse(Call<Images> call, Response<Images> response) {
                        Log.i(TAG, "onResponse: ");
                        if (response.isSuccessful()) {
                            try {
                                // Retrieve images
                                Images images = response.body();

                                Log.i(TAG, "onResponse: success");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                // Handle an api error here
                                Log.i(TAG, "onResponse: fault");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Images> call, Throwable t) {
                        Log.i(TAG, "onFailure: ");
                    }
                }
        );
    }


    public static void performSearchAttractionsCall() {
        Log.i(TAG, "performSearchAttractionsCall: ");

        DiscoveryAPI api = APIServiceGenerator.createService(DiscoveryAPI.class);

        Call<SearchAttractionsResult> call = api.searchAttractions(
                API_VERSION,
                RESPONSE_FORMAT,
                API_KEY,
                null, null, null, null, null, null
        );
        call.enqueue(
                new Callback<SearchAttractionsResult>() {
                    @Override
                    public void onResponse(Call<SearchAttractionsResult> call, Response<SearchAttractionsResult> response) {
                        Log.i(TAG, "onResponse: ");
                        if (response.isSuccessful()) {
                            try {
                                // Retrieve root object of response json
                                SearchAttractionsResult searchAttractionsResult = response.body();


                                Log.i(TAG, "onResponse: success");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                // Handle an api error here
                                Log.i(TAG, "onResponse: fault");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchAttractionsResult> call, Throwable t) {
                        Log.i(TAG, "onFailure: ");
                    }
                }
        );
    }


    public static void performSearchAttractionDetailsCall() {
        Log.i(TAG, "performSearchAttractionDetails: ");

        DiscoveryAPI api = APIServiceGenerator.createService(DiscoveryAPI.class);

        Call <Attraction> attractionDetailsCall = api.getAttractionDetails(
                API_VERSION,
                TEST_ATTRACTION_ID,
                RESPONSE_FORMAT,
                API_KEY,
                null
        );
        attractionDetailsCall.enqueue(
                new Callback<Attraction>() {
                    @Override
                    public void onResponse(Call<Attraction> call, Response<Attraction> response) {
                        Log.i(TAG, "onResponse: ");
                        if (response.isSuccessful()) {
                            try {
                                // Retrieve attraction
                                Attraction attraction = response.body();

                                Log.i(TAG, "onResponse: success");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                // Handle an api error here
                                Log.i(TAG, "onResponse: fault");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Attraction> call, Throwable t) {
                        Log.i(TAG, "onFailure: ");
                    }
                }
        );
    }

    public static void performSearchClassificationsCall() {
        Log.i(TAG, "performSearchClassificationsCall: ");

        DiscoveryAPI api = APIServiceGenerator.createService(DiscoveryAPI.class);

        Call<SearchClassificationsResult> call = api.searchClassifications(
                API_VERSION,
                RESPONSE_FORMAT,
                API_KEY,
                null, null, null, null
        );
        call.enqueue(
                new Callback<SearchClassificationsResult>() {
                    @Override
                    public void onResponse(Call<SearchClassificationsResult> call, Response<SearchClassificationsResult> response) {
                        Log.i(TAG, "onResponse: ");
                        if (response.isSuccessful()) {
                            try {
                                // Retrieve root object of response json
                                SearchClassificationsResult searchClassificationsResult = response.body();

                                Log.i(TAG, "onResponse: success");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                // Handle an api error here
                                Log.i(TAG, "onResponse: fault");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchClassificationsResult> call, Throwable t) {
                        Log.i(TAG, "onFailure: ");
                    }
                }
        );
    }

    public static void performClassificationDetailsCall() {
        Log.i(TAG, "performClassificationDetailsCall: ");

        DiscoveryAPI api = APIServiceGenerator.createService(DiscoveryAPI.class);

        Call <Classification> classificationDetailsCall = api.getClassificationDetails(
                API_VERSION,
                TEST_CLASSIFICATION_ID,
                RESPONSE_FORMAT,
                API_KEY,
                null
        );
        classificationDetailsCall.enqueue(
                new Callback<Classification>() {
                    @Override
                    public void onResponse(Call<Classification> call, Response<Classification> response) {
                        Log.i(TAG, "onResponse: ");
                        if (response.isSuccessful()) {
                            try {
                                // Retrieve attraction
                                Classification classification = response.body();

                                Log.i(TAG, "onResponse: success");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                // Handle an api error here
                                Log.i(TAG, "onResponse: fault");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Classification> call, Throwable t) {
                        Log.i(TAG, "onFailure: ");
                    }
                }
        );
    }

    public static void performSearchVenuesCall() {
        Log.i(TAG, "performSearchVenuesCall: ");

        DiscoveryAPI api = APIServiceGenerator.createService(DiscoveryAPI.class);

        Call<SearchVenuesResult> call = api.searchVenues(
                API_VERSION,
                RESPONSE_FORMAT,
                API_KEY,
                null, null, null, null, null
        );
        call.enqueue(
                new Callback<SearchVenuesResult>() {
                    @Override
                    public void onResponse(Call<SearchVenuesResult> call, Response<SearchVenuesResult> response) {
                        Log.i(TAG, "onResponse: ");
                        if (response.isSuccessful()) {
                            try {
                                // Retrieve root object of response json
                                SearchVenuesResult searchVenuesResult = response.body();

                                Log.i(TAG, "onResponse: success");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                // Handle an api error here
                                Log.i(TAG, "onResponse: fault");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchVenuesResult> call, Throwable t) {
                        Log.i(TAG, "onFailure: ");
                    }
                }
        );
    }

    public static void performVenueDetailsCall() {
        Log.i(TAG, "performVenueDetailsCall: ");

        DiscoveryAPI api = APIServiceGenerator.createService(DiscoveryAPI.class);

        Call <Venue> venueDetailsCall = api.getVenueDetails(
                API_VERSION,
                TEST_VENUE_ID,
                RESPONSE_FORMAT,
                API_KEY,
                null
        );
        venueDetailsCall.enqueue(
                new Callback<Venue>() {
                    @Override
                    public void onResponse(Call<Venue> call, Response<Venue> response) {
                        Log.i(TAG, "onResponse: ");
                        if (response.isSuccessful()) {
                            try {
                                // Retrieve attraction
                                Venue venue = response.body();

                                Log.i(TAG, "onResponse: success");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                // Handle an api error here
                                Log.i(TAG, "onResponse: fault");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Venue> call, Throwable t) {
                        Log.i(TAG, "onFailure: ");
                    }
                }
        );
    }

    public static void performSearchImagesWithCaching(final Context context) {
        Log.i(TAG, "performSearchImagesWithCaching: ");

        DiscoveryAPI api = APIServiceGenerator.createService(DiscoveryAPI.class);

        Call <Images> eventImagesCall = api.searchEventImages(
                API_VERSION,
                TEST_EVENT_ID,
                RESPONSE_FORMAT,
                API_KEY,
                null
        );
        eventImagesCall.enqueue(
                new Callback<Images>() {
                    @Override
                    public void onResponse(Call<Images> call, Response<Images> response) {
                        Log.i(TAG, "onResponse: ");
                        if (response.isSuccessful()) {
                            try {
                                // Retrieve images
                                Images images = response.body();

                                ImageView imageView = new ImageView(context);

                                for (final Image image : images.getImages()) {
                                    image.load(
                                            context,
                                            imageView,
                                            new com.squareup.picasso.Callback() {
                                                @Override
                                                public void onSuccess() {
                                                    Log.i(TAG, "onSuccess: " + image.toString());
                                                }

                                                @Override
                                                public void onError() {
                                                    Log.i(TAG, "onError: " + image.toString());
                                                }
                                            }
                                    );
                                }

                                Log.i(TAG, "onResponse: success");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                // Handle an api error here
                                Log.i(TAG, "onResponse: fault");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Images> call, Throwable t) {
                        Log.i(TAG, "onFailure: ");
                    }
                }
        );
    }
}
