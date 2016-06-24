package com.ticketmaster.api.location;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.PendingResult;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import java.util.NoSuchElementException;

/**
 * Created by Georgii_Goriachev on 6/3/2016.
 */
public class GeolocationService extends AsyncTask<String, Void, Void> {
    private static final String TAG = "GeolocationService";
    private static final String LOCATION_FORMAT = "%f,%f";
    private static final String GEOLOCATION_API_KEY = "AIzaSyCRA5wQ_k2LxnrX2iE3g2nfshYiuadVOTQ";

    private OnGeocodingResultReceived onGeocodingResultReceived;

    public void setOnGeocodingResultReceived(OnGeocodingResultReceived onGeocodingResultReceived) {
        this.onGeocodingResultReceived = onGeocodingResultReceived;
    }

    @Override
    protected Void doInBackground(String... address) {
        if (this.onGeocodingResultReceived == null) {
            throw new NoSuchElementException("The OnGeocodingResultReceived muste be set by method GeolocationService.setOnGeocodingResultReceived()");
        }
        GeoApiContext context = new GeoApiContext().setApiKey(GEOLOCATION_API_KEY);
        GeocodingApiRequest req = GeocodingApi.newRequest(context).address(address[0]);
        req.setCallback(new PendingResult.Callback<GeocodingResult[]>() {
            @Override
            public void onResult(GeocodingResult[] result) {
                try {
                    if (result !=null && result.length > 0) {
                        final LatLng location = result[0].geometry.location;
                        final String formatedLocation = String.format(LOCATION_FORMAT,
                                location.lat, location.lng);
                        onGeocodingResultReceived.onResult(formatedLocation);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Empty geometry or GeocodingResult");
                }
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, e.getMessage());
            }
        });
        return null;
    }

    public interface OnGeocodingResultReceived {
        void onResult(String latLng);
    }
}
