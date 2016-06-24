/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ticketmaster.amazon.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TimingLogger;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.maps.model.LatLng;
import com.ticketmaster.amazon.R;
import com.ticketmaster.amazon.activity.CardPresenter;
import com.ticketmaster.amazon.activity.DetailsActivity;
import com.ticketmaster.amazon.activity.PrefActivity;
import com.ticketmaster.amazon.keyboard.SearchKeyboardView;
import com.ticketmaster.amazon.presenter.KeywordPresenter;
import com.ticketmaster.amazon.util.EventManager;
import com.ticketmaster.api.discovery.DiscoveryAPI;
import com.ticketmaster.api.discovery.PromoDiscoveryAPI;
import com.ticketmaster.api.discovery.entry.classification.Classification;
import com.ticketmaster.api.discovery.entry.event.Event;
import com.ticketmaster.api.discovery.search.embedded.EventsEmbedded;
import com.ticketmaster.api.discovery.search.result.SearchEventsResult;
import com.ticketmaster.api.generator.APIServiceGenerator;
import com.ticketmaster.api.location.GeolocationService;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BrowseFragment
        implements SearchKeyboardView.OnCharClick, GeolocationService.OnGeocodingResultReceived,
        LoadingSettingsAsyncTask.OnLoadSharedPreferencesListener, Callback<SearchEventsResult> {
    private static final String TAG = "HomeFragment";

    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private static final int GRID_ITEM_WIDTH = 200;
    private static final int GRID_ITEM_HEIGHT = 200;
    private static final String NO_CATEGORY = "Other";


    private static List<Event> events = new ArrayList<>();

    private final Handler mHandler = new Handler();
    private ArrayObjectAdapter mRowsAdapter;
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private URI mBackgroundURI;
    private BackgroundManager mBackgroundManager;
    private AlertDialog alertDialog;
    private EditText searchEditText;
    private String mZip;
    private String mUserLatitudeLongitude;
    private String mKeyword = "";
    private Map<String, List<Event>> categorizeEventsMap;
    private int curEventPage = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        final long startTime = System.nanoTime();
        prepareBackgroundManager();
        setupSettings();
        setupUIElements();

        asyncEventLoading();

        setupEventListeners();
        Log.i(TAG, "onActivityCreated duration=" + (System.nanoTime() - startTime) + "ns");
    }

    private void asyncEventLoading() {
        if (curEventPage < 5) {
            String page = String.valueOf(curEventPage);
            EventManager.INSTANCE.performSearchEventsKeyword(null, mUserLatitudeLongitude, mZip, HomeFragment.this, page);
            curEventPage++;
        } else {
            curEventPage = 0;
        }
        Log.d(TAG, "asyncEventLoading page="+ curEventPage);
    }

    private void setupSettings() {
        AsyncTask<Void, Void, Void> loadSettingsAsyncTask = new LoadingSettingsAsyncTask(this.getActivity(), this);
        loadSettingsAsyncTask.execute();
    }

    @Override
    public void onLoaded(String key, String value) {
        if (PrefActivity.ZIP_PROPERTY.equals(key) && value != null) {
            mZip = value;
        } else if (PrefActivity.LATITUDE_LONGITUDE_PROPERTY.equals(key) && value != null) {
            mUserLatitudeLongitude = value;
        }
        if (mZip != null) {
            GeolocationService geolocationService = new GeolocationService();
            geolocationService.setOnGeocodingResultReceived(HomeFragment.this);
            geolocationService.execute(mZip);
        }
    }


    @Override
    public void onResult(final String latLng) {
        if (latLng != null && !latLng.equals(mUserLatitudeLongitude)) {
            Log.d(TAG, "run User Latitude Longitude detection by ZIP-code");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    sharedPref.edit().putString(PrefActivity.LATITUDE_LONGITUDE_PROPERTY, latLng).apply();
                }
            }).start();
            asyncEventLoading();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mBackgroundTimer) {
            Log.d(TAG, "onDestroy: " + mBackgroundTimer.toString());
            mBackgroundTimer.cancel();
        }
    }

    public void loadRows(List<Event> events) {
        TimingLogger timings = new TimingLogger("HomeFragment", "loadRows");
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        if (!mKeyword.isEmpty()) {
            addSearchKeyword(mKeyword);
        }
        CardPresenter cardPresenter = new CardPresenter();
        if (events != null) {
            categorizeEventsMap = categorizeEvents(categorizeEventsMap, events);
            timings.addSplit("create categories");
            for (String key : categorizeEventsMap.keySet()) {
                ArrayObjectAdapter listRowCategoryAdapter = new ArrayObjectAdapter(cardPresenter);
                final List<Event> eventsList = categorizeEventsMap.get(key);
                Log.d(TAG, "Category" + key + "; events: " + eventsList.size());
                for (Event e : eventsList) {
                    listRowCategoryAdapter.add(e);
                }
                HeaderItem header = new HeaderItem(key);
                mRowsAdapter.add(new ListRow(header, listRowCategoryAdapter));
            }

        }

        setupPreferencesUi();

        setAdapter(mRowsAdapter);
        timings.dumpToLog();
    }

    private Map<String, List<Event>> categorizeEvents(Map<String, List<Event>> categoryMap, List<Event> events) {
        if (categoryMap == null) {
            categoryMap = new LinkedHashMap<>();
        }
        for (Event e : events) {
            final List<Classification> classifications = e.getClassifications();
            final String name;
            if (classifications != null && !classifications.isEmpty()) {
                name = classifications.get(0).getSegment().getName();
            } else {
                name = NO_CATEGORY;
            }
            if (categoryMap.containsKey(name)) {
                final List<Event> internalListEvents = categoryMap.get(name);
                internalListEvents.add(e);
                categoryMap.put(name, internalListEvents);
            } else {
                final List<Event> internalListEvents = new ArrayList<>();
                internalListEvents.add(e);
                categoryMap.put(name, internalListEvents);
            }
        }
        categoryMap = sortByListSize(categoryMap);
        return categoryMap;
    }

    public static Map<String, List<Event>> sortByListSize(Map<String, List<Event>> map) {
        List<Map.Entry<String, List<Event>>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, List<Event>>>() {
            @Override
            public int compare(Map.Entry<String, List<Event>> o1, Map.Entry<String, List<Event>> o2) {
                if (o1.getValue().size() == o2.getValue().size()) {
                    return 0;
                } else {
                    return (o1.getValue().size()) > (o2.getValue().size()) ? -1 : 1;
                }
            }
        });

        Map<String, List<Event>> result = new LinkedHashMap<>();
        for (Map.Entry<String, List<Event>> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    private void addSearchKeyword(String keyWord) {
        KeywordPresenter keywordPresenter = new KeywordPresenter();
        ArrayObjectAdapter keyWordAdapter = new ArrayObjectAdapter(keywordPresenter);
        keyWordAdapter.add(String.format(getResources().getString(R.string.serach_results_keyword_label), keyWord));
        mRowsAdapter.add(new ListRow(keyWordAdapter));
    }

    private void setupPreferencesUi() {
        HeaderItem gridHeader = new HeaderItem(getResources().getString(R.string.preferences));
        GridItemPresenter mGridPresenter = new GridItemPresenter();
        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
        gridRowAdapter.add(getResources().getString(R.string.personal_settings));
        mRowsAdapter.add(new ListRow(gridHeader, gridRowAdapter));
    }

    private void prepareBackgroundManager() {
        Log.d(TAG, "prepareBackgroundManager");
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mDefaultBackground = getResources().getDrawable(R.drawable.default_background, null);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setupUIElements() {
        setTitle(getString(R.string.browse_title)); // Badge, when set, takes precedent
        // over title
        setHeadersState(HEADERS_HIDDEN);
        setHeadersTransitionOnBackEnabled(true);
        // set fastLane (or headers) background color
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.default_background));
        // set search icon color
        setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.title_color));
        setBadgeDrawable(getResources().getDrawable(R.drawable.ticketmaster, null));
    }

    private void setupEventListeners() {

        setOnSearchClickedListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                searchEditText = new EditText(getActivity());
                final SearchKeyboardView kb = new SearchKeyboardView(getActivity(), null);
                kb.setOnCharClicked(HomeFragment.this);
                searchEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick" + v);
                    }
                });
                kb.setFocusable(true);
                kb.setFocusableInTouchMode(true);

                LinearLayout viewGroup = new LinearLayout(getActivity());
                viewGroup.setOrientation(LinearLayout.VERTICAL);
                viewGroup.addView(kb);
                viewGroup.addView(searchEditText);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setNeutralButton(getResources().getText(R.string.search_start), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mKeyword = searchEditText.getText().toString();
                                categorizeEventsMap.clear();
                                EventManager.INSTANCE.performSearchEventsKeyword(mKeyword, mUserLatitudeLongitude, mZip, HomeFragment.this, null);
                            }
                        })
                        .setView(viewGroup)
                        .setTitle(R.string.search_title);
                alertDialog = builder.create();
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                alertDialog.show();
                searchEditText.setFocusable(true);
                searchEditText.requestFocus();
            }
        });

        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());

    }

    @Override
    public void onType(CharSequence charString) {
        if (charString.equals("\u2190")) {
            final Editable text = searchEditText.getText();
            if (text.length() > 0) {
                searchEditText.setText(text.subSequence(0, text.length() - 1));
            }
        } else if (charString.equals("˽")) {
            searchEditText.setText(searchEditText.getText().append(" "));
        } else {
            searchEditText.setText(searchEditText.getText().append(charString));
        }
    }

    protected void updateBackground(String uri) {
        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;
        Glide.with(getActivity())
                .load(uri)
                .centerCrop()
                .error(mDefaultBackground)
                .into(new SimpleTarget<GlideDrawable>(width, height) {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable>
                                                        glideAnimation) {
                        mBackgroundManager.setDrawable(resource);
                    }
                });
        mBackgroundTimer.cancel();
    }

    private void startBackgroundTimer() {
        if (null != mBackgroundTimer) {
            mBackgroundTimer.cancel();
        }
        mBackgroundTimer = new Timer();
        mBackgroundTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    }

    @Override
    public void onResponse(Call<SearchEventsResult> call, Response<SearchEventsResult> response) {
        Log.i(TAG, "onResponse: " + response.raw());
        if (response.isSuccessful()) {
            try {
                // Retrieve root object of response json
                SearchEventsResult searchEventsResult = response.body();
                final EventsEmbedded eventsEmbedded = searchEventsResult.getEmbedded();
                if (eventsEmbedded == null) {
                    Log.d(TAG, "eventsEmbedded == null");
                    if (searchEventsResult.getPage().getNumber().intValue() == 0) {
                        Toast.makeText(getActivity(), getResources().getText(R.string.noting_found), Toast.LENGTH_LONG).show();
                    }
                } else {
                    events = eventsEmbedded.getEvents();
                    HomeFragment.this.loadRows(events);
                    EventManager.INSTANCE.addEventList(events);
                    asyncEventLoading();
                }
                Log.i(TAG, "onResponse: success Returned " + (events == null ? 0 : events.size()) + " results");
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else {
            try {
                // Handle an api error here
                Log.i(TAG, "onResponse: fault");
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    @Override
    public void onFailure(Call<SearchEventsResult> call, Throwable t) {
        Log.i(TAG, "onFailure: " + t.getMessage());
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Event) {
                Event event = (Event) item;
                Log.d(TAG, "Item: " + item.toString());
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.EVENT_ID, event.getId());

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        DetailsActivity.SHARED_ELEMENT_NAME).toBundle();
                getActivity().startActivity(intent, bundle);
            } else if (item instanceof String) {
                if (((String) item).contains(getString(R.string.personal_settings))) {
                    Intent intent = new Intent(getActivity(), PrefActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof Event) {
                final String url = ((Event) item).getImages().get(4).getUrl();
                mBackgroundURI = URI.create(url);
                startBackgroundTimer();
            }

        }
    }

    private class UpdateBackgroundTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mBackgroundURI != null) {
                        updateBackground(mBackgroundURI.toString());
                    }
                }
            });

        }
    }

    private class GridItemPresenter extends Presenter {
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            TextView view = new TextView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.default_background));
            view.setTextColor(Color.WHITE);
            view.setGravity(Gravity.CENTER);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            ((TextView) viewHolder.view).setText((String) item);
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {
        }
    }

}
