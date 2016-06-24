package com.ticketmaster.api.helper;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Artur Bryzhatiy on 4/21/16.
 */
public class PropertiesHelper {

    private static final String PROPERTIES_FILE = "apis.properties";

    private static final String YOUTUBE_API_KEY = "youtube.apikey";
    private static final String TICKETMASTER_API_KEY = "ticketmaster.apikey";

    private static final String FLICKR_API_KEY = "flickr.apikey";
    private static final String FLICKR_API_SECRET = "flickr.apisecret";

    private static Properties properties;

    public static void loadProperties(Context context) {
        try {
            properties = new Properties();
            InputStream propertiesStream = context.getAssets().open(PROPERTIES_FILE);
            properties.load(propertiesStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getYoutubeApiKey() {
        String apikey = null;
        try {
            apikey = properties.getProperty(YOUTUBE_API_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apikey;
    }

    public static String getFlickrApiKey() {
        String apikey = null;
        try {
            apikey = properties.getProperty(FLICKR_API_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apikey;
    }

    public static String getFlickrApiSecret() {
        String apisecret = null;
        try {
            apisecret = properties.getProperty(FLICKR_API_SECRET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apisecret;
    }
    public static String getTicketMasterApiKey() {
        String apikey = null;
        try {
            apikey = properties.getProperty(TICKETMASTER_API_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apikey;
    }
}
