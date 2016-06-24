package com.ticketmaster.amazon.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;

import com.ticketmaster.amazon.fragment.SettingsFragment;

public class PrefActivity extends Activity {

    public static final String ZIP_PROPERTY = "zip_property";
    public static final String LATITUDE_LONGITUDE_PROPERTY = "latitude_longitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        startActivity(new Intent(this, HomeScreenActivity.class));
    }

}
