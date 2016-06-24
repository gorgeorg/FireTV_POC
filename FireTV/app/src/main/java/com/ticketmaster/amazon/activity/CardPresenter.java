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

package com.ticketmaster.amazon.activity;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.appengine.repackaged.com.google.common.base.StringUtil;
import com.ticketmaster.amazon.R;
import com.ticketmaster.api.discovery.entry.base.music.iTunesItem;
import com.ticketmaster.api.discovery.entry.base.photo.PhotoModel;
import com.ticketmaster.api.discovery.entry.base.video.VideoModel;
import com.ticketmaster.api.discovery.entry.event.Event;

/*
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an Image CardView
 */
public class CardPresenter extends Presenter {
    private static final String TAG = "CardPresenter";

    private static final int CARD_WIDTH = 313;
    private static final int CARD_HEIGHT = 176;
    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;
    private Drawable mDefaultCardImage;

    private static void updateCardBackgroundColor(ImageCardView view, boolean selected) {
        int color = selected ? sSelectedBackgroundColor : sDefaultBackgroundColor;
        // Both background colors should be set because the view's background is temporarily visible
        // during animations.
        view.setBackgroundColor(color);
        view.findViewById(R.id.info_field).setBackgroundColor(color);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {

        sDefaultBackgroundColor = ContextCompat.getColor(parent.getContext(), R.color.default_background);
        sSelectedBackgroundColor = ContextCompat.getColor(parent.getContext(),R.color.img_soft_opaque);
        mDefaultCardImage = parent.getResources().getDrawable(R.drawable.ticketmaster, null);

        ImageCardView cardView = new ImageCardView(parent.getContext()) {
            @Override
            public void setSelected(boolean selected) {
                updateCardBackgroundColor(this, selected);
                super.setSelected(selected);
            }
        };

        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        updateCardBackgroundColor(cardView, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        final ImageCardView cardView = (ImageCardView) viewHolder.view;

        if (item instanceof Event) {
            Event event = (Event) item;
            cardView.setTitleText(event.getName());
            cardView.setContentText(event.getPleaseNote());
            cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
            if (!event.getImages().isEmpty()) {
                Glide.with(viewHolder.view.getContext())
                        .load(event.getImages().get(3).getUrl())
                        .centerCrop()
                        .error(mDefaultCardImage)
                        .into(cardView.getMainImageView());
            }
        } else if (item instanceof VideoModel) {
            VideoModel videoModel = (VideoModel) item;
            cardView.setTitleText(videoModel.getVideoTitle());
            cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
            if (!videoModel.getDefaultThumbnailURL().isEmpty()) {
                Glide.with(viewHolder.view.getContext())
                        .load(videoModel.getDefaultThumbnailURL())
                        .centerCrop()
                        .error(mDefaultCardImage)
                        .into(cardView.getMainImageView());
            }
        } else if (item instanceof PhotoModel) {
            PhotoModel photoModel = (PhotoModel) item;
            cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
            //cardView.setTitleText(photoModel.getDescription());
            Glide.with(viewHolder.view.getContext())
                    .load(photoModel.getPhotoUrl())
                    .centerCrop()
                    .error(mDefaultCardImage)
                    .into(cardView.getMainImageView());
        } else if (item instanceof iTunesItem) {
            iTunesItem itunesItem = (iTunesItem) item;

            cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
            cardView.setTitleText(itunesItem.getTitle());
            Glide.with(viewHolder.view.getContext())
                    .load(itunesItem.getThumbURL())
                    .centerCrop()
                    .error(mDefaultCardImage)
                    .into(cardView.getMainImageView());
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        Log.d(TAG, "onUnbindViewHolder");
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        // Remove references to images so that the garbage collector can free up memory
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }
}
