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
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.DetailsEventFragment;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.ticketmaster.amazon.R;
import com.ticketmaster.amazon.activity.CardPresenter;
import com.ticketmaster.amazon.activity.DetailsActivity;
import com.ticketmaster.amazon.activity.DetailsEventDescriptionPresenter;
import com.ticketmaster.amazon.activity.FlickerActivity;
import com.ticketmaster.amazon.activity.FullWidthEventDetailsOverviewRowPresenter;
import com.ticketmaster.amazon.activity.HomeScreenActivity;
import com.ticketmaster.amazon.activity.ITunesActivity;
import com.ticketmaster.amazon.activity.LocationActivity;
import com.ticketmaster.amazon.activity.Utils;
import com.ticketmaster.amazon.activity.YoutubePlaybackOverlayActivity;
import com.ticketmaster.amazon.presenter.EventDetailsOverviewLogoPresenter;
import com.ticketmaster.amazon.util.EventManager;
import com.ticketmaster.api.discovery.entry.attraction.Attraction;
import com.ticketmaster.api.discovery.entry.base.listener.flickr.PhotoModelListener;
import com.ticketmaster.api.discovery.entry.base.listener.video.VideoModelListener;
import com.ticketmaster.api.discovery.entry.base.music.iTunesItem;
import com.ticketmaster.api.discovery.entry.base.music.iTunesItemListener;
import com.ticketmaster.api.discovery.entry.base.photo.PhotoModel;
import com.ticketmaster.api.discovery.entry.base.video.VideoModel;
import com.ticketmaster.api.discovery.entry.event.Event;
import com.ticketmaster.api.discovery.entry.event.EventEmbedded;
import com.ticketmaster.api.discovery.entry.image.Image;
import com.ticketmaster.api.helper.PropertiesHelper;

import java.util.ArrayList;
import java.util.List;

public class EventDetailsFragment extends DetailsEventFragment {
    private static final String TAG = "EventDetailsFragment";

    private static final int ACTION_CHART = 1;
    private static final int ACTION_LOCATION = 2;
    private static final int ACTION_LIKE = 3;

    private static final int DETAIL_THUMB_WIDTH = 200;
    private static final int DETAIL_THUMB_HEIGHT = 300;

    private static final int NUM_COLS = 10;
    private static final int EVENT_IMAGE_DIMENTION_INDEX = 0;
    private static final long ACTION_QR = 4;
    private static final int ACTION_LIFE = 5;

    private Event mSelectedEvent;

    private ArrayObjectAdapter mAdapter;
    private ClassPresenterSelector mPresenterSelector;

    private BackgroundManager mBackgroundManager;
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private String eventId;
    private ListRow videoListRow;
    private ListRow photoListRow;
    private ListRow audioListRow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate DetailsFragment");
        super.onCreate(savedInstanceState);
        prepareBackgroundManager();
        eventId = getActivity().getIntent().getStringExtra(DetailsActivity.EVENT_ID);
        mSelectedEvent = EventManager.INSTANCE.getEvent(eventId);
        if (mSelectedEvent != null) {
            setupAdapter();
            setupDetailsOverviewRow();
            setupDetailsOverviewRowPresenter();
            setupMovieListRow();
            setupMovieListRowPresenter();
            final List<Image> images = mSelectedEvent.getImages();
            if (images != null) {
                updateBackground(images.get(0).getUrl());
            }
            setOnItemViewClickedListener(new ItemViewClickedListener());
        } else {
            Intent intent = new Intent(getActivity(), HomeScreenActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mDefaultBackground = getResources().getDrawable(R.drawable.ticketmaster, null);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    protected void updateBackground(String uri) {
        Glide.with(getActivity())
                .load(uri)
                .centerCrop()
                .error(mDefaultBackground)
                .into(new SimpleTarget<GlideDrawable>(mMetrics.widthPixels, mMetrics.heightPixels) {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        mBackgroundManager.setDrawable(resource);
                    }
                });
    }

    private void setupAdapter() {
        mPresenterSelector = new ClassPresenterSelector();
        mAdapter = new ArrayObjectAdapter(mPresenterSelector);
        setAdapter(mAdapter);
    }

    private void setupDetailsOverviewRow() {
        Log.d(TAG, "doInBackground: " + mSelectedEvent.toString());
        final DetailsOverviewRow row = new DetailsOverviewRow(mSelectedEvent);
        row.setImageDrawable(getResources().getDrawable(R.drawable.ticketmaster, null));
        int width = Utils.convertDpToPixel(getActivity()
                .getApplicationContext(), DETAIL_THUMB_WIDTH);
        int height = Utils.convertDpToPixel(getActivity()
                .getApplicationContext(), DETAIL_THUMB_HEIGHT);
        Log.d(TAG, "width=" + width + "; height=" + height);
        Glide.with(getActivity())
                .load(mSelectedEvent.getImages().get(EVENT_IMAGE_DIMENTION_INDEX).getUrl())
                .centerCrop()
                .error(R.drawable.ticketmaster)
                .listener(new RequestListener<String, GlideDrawable>() {

                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        Log.d(TAG, "model: " + model + e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.d(TAG, "onResourceReady model: " + model);
                        row.setImageDrawable(resource);
//                        mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());
                        return false;
                    }
                })
                .into(new SimpleTarget<GlideDrawable>(width, height) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        Log.d(TAG, "details overview card image url ready: " + resource);
                        //row.setImageDrawable(resource);
                        mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());
                    }

                });

        row.setActionsAdapter(new ObjectAdapter() {
            @Override
            public int size() {
                return 3;
            }

            @Override
            public Object get(int position) {
                if (position == 0) {
                    return new Action(ACTION_QR, getResources().getString(R.string.buy), " ",
                            getResources().getDrawable(R.drawable.ic_add_shopping_cart_black_24dp, null));
                } else if (position == 1) {
                    return new Action(ACTION_LOCATION, getResources().getString(R.string.location), " ",
                            getResources().getDrawable(R.drawable.ic_place_black_24dp, null));
                } else {
                    return new Action(ACTION_LIFE, getResources().getString(R.string.life), " ",
                            getResources().getDrawable(R.drawable.life, null));
                }
            }
        });
        mAdapter.add(row);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setupDetailsOverviewRowPresenter() {
        // Set detail background and style.
        final DetailsEventDescriptionPresenter descriptionPresenter = new DetailsEventDescriptionPresenter();
        FullWidthEventDetailsOverviewRowPresenter detailsPresenter =
                new FullWidthEventDetailsOverviewRowPresenter(
                        descriptionPresenter, new EventDetailsOverviewLogoPresenter());
        detailsPresenter.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.progress_bar_total));
        detailsPresenter.setAlignmentMode(FullWidthEventDetailsOverviewRowPresenter.ALIGN_MODE_START);

        detailsPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {
                if (action.getId() == ACTION_LOCATION) {
                    Intent intent = new Intent(getActivity(), LocationActivity.class);
                    final int index = EventManager.INSTANCE.getEventsList().indexOf(mSelectedEvent);
                    intent.putExtra(LocationActivity.EVENT_INDEX, index);
                    startActivity(intent);
                } else if (action.getId() == ACTION_QR) {
                    ImageView ivQR = new ImageView(getActivity());
                    ivQR.setImageBitmap(mSelectedEvent.getQRImage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                            .setView(ivQR);
                    AlertDialog ad = builder.create();
                    ad.show();

                } else {
                    Toast.makeText(getActivity(), action.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mPresenterSelector.addClassPresenter(DetailsOverviewRow.class, detailsPresenter);
    }

    private void setupMovieListRow() {
        String subcategories[] = {getString(R.string.related_videos),
                getString(R.string.related_images),
                getString(R.string.related_audios),
        };

        final ArrayObjectAdapter listRowVideosAdapter = new ArrayObjectAdapter(new CardPresenter());
        final ArrayObjectAdapter listRowPhotosAdapter = new ArrayObjectAdapter(new CardPresenter());
        final ArrayObjectAdapter listRowAudioAdapter = new ArrayObjectAdapter(new CardPresenter());
        PropertiesHelper.loadProperties(getActivity());
        EventManager.INSTANCE.retrieveVideos(5, mSelectedEvent.getName(), new VideoModelListener() {
            @Override
            public void videosDidBuild() {
                List<VideoModel> videoList = new ArrayList<>();
                for (VideoModel vm : EventManager.INSTANCE.getVideoList()) {
                    videoList.add(vm);
                    listRowVideosAdapter.add(vm);
                }
                if (listRowVideosAdapter.size() == 0) {
                    mAdapter.remove(videoListRow);
                }
            }
        });
        HeaderItem headerVideo = new HeaderItem(1, subcategories[0]);
        videoListRow = new ListRow(headerVideo, listRowVideosAdapter);
        mAdapter.add(videoListRow);
        final EventEmbedded embedded = mSelectedEvent.getEmbedded();
        final List<Attraction> attractions = (embedded == null ? null : embedded.getAttractions());
        final String keyWord;
        if (attractions == null) {
            keyWord = mSelectedEvent.getName();
        } else {
            keyWord = attractions.get(0).getName();
        }
        EventManager.INSTANCE.retrieveFlickrImages(keyWord, 5, 1, new PhotoModelListener() {
            @Override
            public void photosDidBuild() {
                Log.d(TAG, "photos size = " + EventManager.INSTANCE.getPhotos().size());
                List<PhotoModel> photoList = new ArrayList<>();
                for (PhotoModel pm : EventManager.INSTANCE.getPhotos()) {
                    photoList.add(pm);
                    listRowPhotosAdapter.add(pm);
                }
                if (listRowPhotosAdapter.size() == 0) {
                    mAdapter.remove(photoListRow);
                }
            }
        });
        HeaderItem headerPhoto = new HeaderItem(1, subcategories[1]);
        photoListRow = new ListRow(headerPhoto, listRowPhotosAdapter);
        mAdapter.add(photoListRow);
        // audio
        EventManager.INSTANCE.retrieveITunesMusic(mSelectedEvent.getName(), new iTunesItemListener() {
            @Override
            public void emptyResultDidReceive() {

            }

            @Override
            public void iTunesItemsDidBuild() {
                for (iTunesItem iTunesItem : EventManager.INSTANCE.getMusic()) {
                    listRowAudioAdapter.add(iTunesItem);
                }
                if (listRowAudioAdapter.size() == 0) {
                    mAdapter.remove(audioListRow);
                }
            }
        });
        HeaderItem headerAudio = new HeaderItem(1, subcategories[2]);
        audioListRow = new ListRow(headerAudio, listRowAudioAdapter);
        mAdapter.add(audioListRow);
    }

    private void setupMovieListRowPresenter() {
        mPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof VideoModel) {
                VideoModel wm = (VideoModel) item;
                Log.d(TAG, "VideoModel play: " + item.toString());
                Intent intent = new Intent(getActivity(), YoutubePlaybackOverlayActivity.class);
                final List<VideoModel> videoList = EventManager.INSTANCE.getVideoList();
                if (videoList != null && !videoList.isEmpty()) {
                    intent.putExtra(YoutubePlaybackOverlayActivity.YOUTUBE_VIDEO_ID, videoList.indexOf(wm));
                    startActivity(intent);
                }
            } else if (item instanceof PhotoModel) {
                Log.d(TAG, "PhotoModel show: " + item.toString());
                PhotoModel pm = (PhotoModel) item;
                int index = EventManager.INSTANCE.getPhotoList().indexOf(pm);
                Intent intent = new Intent(getActivity(), FlickerActivity.class);
                intent.putExtra(FlickerActivity.IMAGE_URL_INDEX, index);
                startActivity(intent);
            } else if (item instanceof iTunesItem) {
                Log.d(TAG, "iTunesItem play: " + item.toString());
                iTunesItem itunes = (iTunesItem) item;
                Intent intent = new Intent(getActivity(), ITunesActivity.class);
                intent.putExtra(ITunesActivity.ITUNES_TITLE, itunes.getTitle());
                intent.putExtra(ITunesActivity.ITUNES_THUMB_URL, itunes.getThumbURL());
                intent.putExtra(ITunesActivity.ITUNES_AUDIO_URL, itunes.getPreviewAudioURL());
                startActivity(intent);
            }
        }
    }

}
