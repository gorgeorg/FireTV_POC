package com.ticketmaster.amazon.activity;

import android.support.v17.leanback.widget.Presenter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ticketmaster.amazon.R;
import com.ticketmaster.api.discovery.entry.event.Event;

/**
 * Created by Georgii_Goriachev on 5/5/2016.
 */
public class EventDetailsPresenter extends Presenter {
    private static final String TAG = "EventDetailsPresenter";

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_description, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        Event event = (Event) item;
        Log.d(TAG, event + "");
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
