package com.ticketmaster.amazon.presenter;

import android.support.v17.leanback.widget.Presenter;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ticketmaster.amazon.activity.DetailsEventDescriptionPresenter;

/**
 * Created by Georgii_Goriachev on 6/6/2016.
 */
public class KeywordPresenter extends Presenter {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        TextView tvKeyWord = new TextView(parent.getContext());
        return new ViewHolder(tvKeyWord);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        final TextView tvKeyWord = (TextView) viewHolder.view;
        tvKeyWord.setText((String)item);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

}
