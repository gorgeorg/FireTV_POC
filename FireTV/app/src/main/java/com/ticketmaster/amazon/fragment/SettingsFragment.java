package com.ticketmaster.amazon.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.ticketmaster.amazon.R;

/**
 * Created by Georgii_Goriachev on 5/25/2016.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String ZIP_PROPERTY = "zip_property";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        Preference zipPreference = getPreferenceScreen().findPreference(ZIP_PROPERTY);
        initSummary(zipPreference);
    }

    private void initSummary(Preference zipPreference) {
        if (zipPreference instanceof EditTextPreference) {
            EditTextPreference zipPref = (EditTextPreference) zipPreference;
            final String zipText = zipPref.getText() == null
                    ? getString(R.string.empty)
                    : zipPref.getText();
            final String summary = String.format(getString(R.string.my_zip_code_summary), zipText);
            zipPref.setSummary(summary);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePreference(findPreference(key), key);
    }

    private void updatePreference(Preference zipPreference, String key) {
        if (zipPreference == null) return;
        initSummary(zipPreference);
    }
}
