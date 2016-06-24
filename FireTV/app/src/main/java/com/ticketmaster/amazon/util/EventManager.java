package com.ticketmaster.amazon.util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.api.services.youtube.model.SearchResult;
import com.google.appengine.repackaged.com.google.common.base.Flag;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.ticketmaster.amazon.fragment.HomeFragment;
import com.ticketmaster.api.discovery.DiscoveryAPI;
import com.ticketmaster.api.discovery.PromoDiscoveryAPI;
import com.ticketmaster.api.discovery.entry.base.listener.flickr.FlickrImagesListener;
import com.ticketmaster.api.discovery.entry.base.listener.flickr.PhotoModelListener;
import com.ticketmaster.api.discovery.entry.base.listener.video.VideoModelListener;
import com.ticketmaster.api.discovery.entry.base.listener.video.VideoStreamUrlListener;
import com.ticketmaster.api.discovery.entry.base.listener.video.YouTubeListener;
import com.ticketmaster.api.discovery.entry.base.music.SearchMusicResult;
import com.ticketmaster.api.discovery.entry.base.music.iTunesItem;
import com.ticketmaster.api.discovery.entry.base.music.iTunesItemListener;
import com.ticketmaster.api.discovery.entry.base.music.iTunesService;
import com.ticketmaster.api.discovery.entry.base.music.iTunesServiceGenerator;
import com.ticketmaster.api.discovery.entry.base.photo.PhotoModel;
import com.ticketmaster.api.discovery.entry.base.task.FlickrImagesTask;
import com.ticketmaster.api.discovery.entry.base.task.YouTubeTask;
import com.ticketmaster.api.discovery.entry.base.video.VideoModel;
import com.ticketmaster.api.discovery.entry.event.Event;
import com.ticketmaster.api.discovery.search.result.SearchEventsResult;
import com.ticketmaster.api.generator.APIServiceGenerator;
import com.ticketmaster.api.youtube.YouTubeVideoConfiguration;
import com.ticketmaster.api.youtube.YouTubeVideoOperationListener;
import com.ticketmaster.api.youtube.YouTubeVideoOperationTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Georgii_Goriachev on 4/21/2016.
 */
public enum EventManager implements YouTubeListener, YouTubeVideoOperationListener, FlickrImagesListener {

    INSTANCE {
        @Override
        public void searchVideosDidComplete(List<SearchResult> searchResults) {
            Log.i(TAG, "searchVideosDidComplete: ");

            if (searchResults != null) {

                if (mVideoList != null) {
                    mVideoList.clear();
                } else {
                    mVideoList = new ArrayList<>();
                }

                Iterator<SearchResult> iterator = searchResults.iterator();
                while (iterator.hasNext()) {
                    SearchResult searchResult = iterator.next();
                    VideoModel videoModel = new VideoModel(searchResult);
                    mVideoList.add(videoModel);

                }

                if (videoModelListener != null) {
                    videoModelListener.videosDidBuild();
                }
            }
        }

        @Override
        public void videoConfigurationDidReceived(YouTubeVideoConfiguration videoConfiguration) {
            Log.i(TAG, "videoConfigurationDidReceived: " + videoConfiguration);
            if (videoStreamUrlListener == null) {
                throw new NullPointerException("VideoStreamUrlListener must be set");
            } else {
                videoStreamUrlListener.gotYoutubeStreamUrl(videoConfiguration);
            }
        }

        @Override
        public void videoConfigurationEmpty(String videoUrl) {
            Log.w(TAG, "videoConfigurationEmpty: " + videoUrl);
        }

        @Override
        public void videoConfigurationException(Exception e) {
            Log.e(TAG, "videoConfigurationException: " + e.getMessage());
        }





//        public void retrieveITunesMusic(iTunesItemListener iTunesItemListener) {
//            retrieveITunesMusic(name, iTunesItemListener);
//        }
        @Override
        public void searchPhotosDidComplete(PhotoList photosList) {
            Log.i(TAG, "searchPhotosDidComplete: ");

            if (photosList != null) {

                if (mPhotoList != null) {
                    mPhotoList.clear();
                } else {
                    mPhotoList = new ArrayList<>();
                }

                Iterator <Photo> iterator = photosList.iterator();
                while (iterator.hasNext()) {
                    Photo photo = iterator.next();
                    PhotoModel photoModel = new PhotoModel(photo);
                    mPhotoList.add(photoModel);
                }

                if (photoModelListener != null) {
                    photoModelListener.photosDidBuild();
                }
            }
        }

    };

    private static final String TAG = "EventManager";
    private static final String DEFAULT_RADIUS = "100";
    private static final String SIZE = "100";
    private List<Event> mEventsList;

    private static List<PhotoModel> mPhotoList;
    private static List<VideoModel> mVideoList;
    private static VideoModelListener videoModelListener;
    private static VideoStreamUrlListener videoStreamUrlListener;

    private static PhotoModelListener photoModelListener;
    protected List<iTunesItem> music;
    private com.ticketmaster.api.discovery.entry.base.music.iTunesItemListener iTunesItemListener;

    public void setEventList(List<Event> events) {
        this.mEventsList = events;
    }

    public void addEventList(List<Event> events) {
        if (this.mEventsList == null) {
            this.mEventsList = new ArrayList<>();
        }
        this.mEventsList.addAll(events);
    }

    public List<Event> getEventsList() {
        return this.mEventsList;
    }

    public List<iTunesItem> getMusic() {
        return music;
    }

    public List<PhotoModel> getPhotos() {
        return mPhotoList;
    }

    public Event getEvent(String id) {
        if (id == null || mEventsList == null) {
            return null;
        }
        for (Event e : mEventsList) {
            if (id.equals(e.getId())) {
                return e;
            }
        }
        return null;
    }

    public List<PhotoModel> getPhotoList() {
        return mPhotoList;
    }

    public void setPhotoList(List<PhotoModel> mPhotoList) {
        this.mPhotoList = mPhotoList;
    }

    public List<VideoModel> getVideoList() {
        return mVideoList;
    }

    //public void setVideoList(List<VideoModel> mVideoList) {
//        this.mVideoList = mVideoList;
//    }

    public void retrieveVideos(int maxVideos, @NonNull String eventName, @NonNull VideoModelListener videoModelListener) {
        this.videoModelListener = videoModelListener;
        YouTubeTask youTubeTask = new YouTubeTask(maxVideos, this);
        youTubeTask.execute(eventName);
    }

    public void retriveVideoStreamUrl(@NonNull VideoModel videoModel, @NonNull VideoStreamUrlListener videoStreamUrlListener) {
        Log.d(TAG, "retriveVideoStreamUrl");
        this.videoStreamUrlListener = videoStreamUrlListener;
        YouTubeVideoOperationTask task = new YouTubeVideoOperationTask(videoModel, this);
        task.execute();
    }

    public void retrieveFlickrImages(@NonNull String keyWord,  int perPage, int page, @NonNull PhotoModelListener photoModelListener) {
        this.photoModelListener = photoModelListener;

        FlickrImagesTask flickrImagesTask = new FlickrImagesTask(perPage, page, this);
        flickrImagesTask.execute(keyWord);
        Log.i(TAG, "retrieveFlickrImages: ");
    }

    public void retrieveITunesMusic(final String requestedTerm, @NonNull final iTunesItemListener iTunesItemListener) {

        String term = requestedTerm.replace(" ", "+");

        iTunesService iTunesService = iTunesServiceGenerator.createService(iTunesService.class);
        Call<SearchMusicResult> call = iTunesService.search(term, "music", 20);
        call.enqueue(
                new Callback<SearchMusicResult>() {
                    @Override
                    public void onResponse(Call<SearchMusicResult> call, Response<SearchMusicResult> response) {
                        SearchMusicResult searchMusicResult = response.body();

                        music = searchMusicResult.getITunesItems();

                        String newTerm = "popular music";
                        if (music.size() == 0 && !requestedTerm.equals(newTerm)) {

                            if (iTunesItemListener != null) {
                                iTunesItemListener.emptyResultDidReceive();
                            }

                            retrieveITunesMusic(newTerm, iTunesItemListener);
                        } else {
                            if (iTunesItemListener != null) {
                                iTunesItemListener.iTunesItemsDidBuild();
                            }
                        }

                        Log.i(TAG, "onResponse: ");
                    }

                    @Override
                    public void onFailure(Call<SearchMusicResult> call, Throwable t) {
                        Log.i(TAG, "onFailure: ");
                    }
                }
        );
    }
    public void performSearchEventsKeyword(String keyword, String userLatitudeLongitude, String zip, Callback<SearchEventsResult> callback, String page) {
        Log.i(TAG, "performSearchEventsCall: keyword" + keyword);
        DiscoveryAPI api = APIServiceGenerator.createService(DiscoveryAPI.class);
        final Call<SearchEventsResult> call;
        if (userLatitudeLongitude == null) {
            Log.d(TAG, "used ZIP for search events: " + zip);
            call = api.searchEvents(
                    PromoDiscoveryAPI.API_VERSION,
                    PromoDiscoveryAPI.RESPONSE_FORMAT,
                    PromoDiscoveryAPI.API_KEY,
                    keyword,
                    null, null, null,
                    zip,
                    null, null, null, null, null, null, null, null, null, null, null,
                    SIZE,
                    page,
                    null
            );
        } else {
            Log.d(TAG, "used Latitude Longitude for search events: " + userLatitudeLongitude);
            call = api.searchEvents(
                    PromoDiscoveryAPI.API_VERSION,
                    PromoDiscoveryAPI.RESPONSE_FORMAT,
                    PromoDiscoveryAPI.API_KEY,
                    keyword, null, null, null, null,
                    userLatitudeLongitude,
                    DEFAULT_RADIUS,
                    null, null, null, null, null, null, null, null, null,
                    SIZE,
                    page,
                    null
            );
        }
        call.enqueue(callback);
    }

}
