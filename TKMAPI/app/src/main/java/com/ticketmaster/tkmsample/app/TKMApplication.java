package com.ticketmaster.tkmsample.app;

import android.app.Application;
import android.os.StrictMode;

import com.ticketmaster.api.discovery.PromoDiscoveryAPI;
import com.ticketmaster.api.helper.PropertiesHelper;


/**
 * Created by Artur Bryzhatiy on 4/18/16.
 */
public class TKMApplication extends Application {

    private static final String TAG = "TKMApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        PropertiesHelper.loadProperties(this);
        PromoDiscoveryAPI.performSearchEventsCall();
//        PromoDiscoveryAPI.performEventDetailsCall();
//        PromoDiscoveryAPI.performSearchEventImagesCall();
//        PromoDiscoveryAPI.performSearchAttractionsCall();
//        PromoDiscoveryAPI.performSearchAttractionDetailsCall();
//        PromoDiscoveryAPI.performSearchClassificationsCall();
//        PromoDiscoveryAPI.performClassificationDetailsCall();
//        PromoDiscoveryAPI.performSearchVenuesCall();
//        PromoDiscoveryAPI.performVenueDetailsCall();
//        PromoDiscoveryAPI.performSearchImagesWithCaching(this);
    }
}
