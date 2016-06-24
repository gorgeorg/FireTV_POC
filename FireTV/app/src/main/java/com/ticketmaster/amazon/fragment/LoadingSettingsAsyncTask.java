package com.ticketmaster.amazon.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.appengine.repackaged.org.joda.time.IllegalFieldValueException;
import com.ticketmaster.amazon.activity.PrefActivity;

/**
 * Created by Georgii_Goriachev on 6/10/2016.
 */
public class LoadingSettingsAsyncTask extends AsyncTask<Void, Void, Void> {
    OnLoadSharedPreferencesListener onLoadSharedPreferencesListener;
    private Context context;

    public LoadingSettingsAsyncTask(@NonNull Context context, @NonNull OnLoadSharedPreferencesListener listener) {
        this.context = context;
        this.onLoadSharedPreferencesListener = listener;
    }

    @Override
    protected Void doInBackground(Void... params) {
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.context);
        String latitudeLongitude = sharedPref.getString(PrefActivity.LATITUDE_LONGITUDE_PROPERTY, null);
        String zip = sharedPref.getString(PrefActivity.ZIP_PROPERTY, "10023");
        if (latitudeLongitude != null) {
            this.onLoadSharedPreferencesListener.onLoaded(PrefActivity.LATITUDE_LONGITUDE_PROPERTY, latitudeLongitude);
        }
        this.onLoadSharedPreferencesListener.onLoaded(PrefActivity.ZIP_PROPERTY, zip);
        return null;
    }

    public interface OnLoadSharedPreferencesListener {
        void onLoaded(String key, String value);
    }
}
