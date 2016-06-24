package com.ticketmaster.amazon.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ticketmaster.amazon.R;
import com.ticketmaster.amazon.util.EventManager;
import com.ticketmaster.api.discovery.entry.event.Event;
import com.ticketmaster.api.discovery.entry.event.EventEmbedded;
import com.ticketmaster.api.discovery.entry.venue.Location;
import com.ticketmaster.api.discovery.entry.venue.Venue;
import com.ticketmaster.api.helper.MapImageManager;

import java.util.List;


public class LocationActivity extends Activity {

    private static final String TAG = LocationActivity.class.getSimpleName();
    public static final String EVENT_INDEX = "event_index";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        final Intent intent = getIntent();
        final int eventIndex = intent.getIntExtra(EVENT_INDEX, 0);
        final String eventLocation = getEventLocation(eventIndex);
        String mapUrl = new MapImageManager.Builder()
                .setCenter(eventLocation)
                .build()
                .getMapUrl();
        initView(mapUrl);
    }

    private void initView(String mapUrl) {
        ImageView iv = (ImageView) findViewById(R.id.mapImage);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
        Glide.with(this)
                .load(mapUrl)
                .centerCrop()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(iv)
        ;
    }

    private String getEventLocation(int eventIndex) {
        String eventLocation = "";
        final Event event = EventManager.INSTANCE.getEventsList().get(eventIndex);
        final EventEmbedded embedded = event.getEmbedded();
        final List<Venue> venues = embedded != null ? embedded.getVenues() : null;
        if (venues != null && venues.size() > 0) {
            final Venue venue = venues.get(0);
            final Location venueLocation = venue.getLocation();
            if (venueLocation == null) {
                eventLocation = venue.getAddress().getStringAddress();
            } else {
                eventLocation = venueLocation.getLatitude() + "," + venueLocation.getLongitude();
            }
        }
        return eventLocation;
    }
}
