package com.ticketmaster.api.discovery.entry.base.video;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.google.api.services.youtube.model.SearchResult;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ticketmaster.api.youtube.YouTubeVideoConfiguration;

import com.ticketmaster.api.youtube.YouTubeVideoOperationListener;

import java.io.Serializable;

/**
 * Created by Artur Bryzhatiy on 4/21/16.
 */
public class VideoModel implements Serializable {

    private static final String TAG = "VideoModel";
    private final SearchResult searchResult;

    private final String defaultThumbnailURL;
    private final String mediumThumbnailURL;
    private final String largeThumbnailURL;

    private final String videoId;
    private final String videoURL;

    private final String videoTitle;
    private YouTubeVideoConfiguration youTubeVideoConfiguration;

    public VideoModel(SearchResult searchResult) {
        this.searchResult = searchResult;

        this.defaultThumbnailURL = searchResult.getSnippet().getThumbnails().getDefault().getUrl();
        this.mediumThumbnailURL = searchResult.getSnippet().getThumbnails().getMedium().getUrl();
        this.largeThumbnailURL = searchResult.getSnippet().getThumbnails().getHigh().getUrl();

        this.videoId = searchResult.getId().getVideoId();
        this.videoURL = String.format("https://www.youtube.com/watch?v=%s", videoId);

        this.videoTitle = searchResult.getSnippet().getTitle();
    }

    public void loadThumbnail(ThumbnailType thumbnailType, Context context, ImageView target, Callback callback) {
        String url = defaultThumbnailURL;
        switch (thumbnailType) {
            case DEFAULT: {
                url = defaultThumbnailURL;
            }
            break;

            case MEDIUM: {
                url = mediumThumbnailURL;
            }
            break;

            case LARGE: {
                url = largeThumbnailURL;
            }
            break;

            default: {
                url = defaultThumbnailURL;
            }
            break;
        }

        Picasso.with(context).load(url).into(target, callback);
    }

    public void loadThumbnail(ThumbnailType thumbnailType, ImageView target, Callback callback) {
        Context context = target.getContext();
        loadThumbnail(thumbnailType, context, target, callback);
    }

    public String getDefaultThumbnailURL() {
        return defaultThumbnailURL;
    }

    public String getMediumThumbnailURL() {
        return mediumThumbnailURL;
    }

    public String getLargeThumbnailURL() {
        return largeThumbnailURL;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setYouTubeVideoConfiguration(YouTubeVideoConfiguration youTubeVideoConfiguration) {
        this.youTubeVideoConfiguration = youTubeVideoConfiguration;
    }

    public YouTubeVideoConfiguration getYouTubeVideoConfiguration() {
        return youTubeVideoConfiguration;
    }

    public enum ThumbnailType {
        DEFAULT,
        MEDIUM,
        LARGE
    }

    @Override
    public String toString() {
        return "VideoModel: "
                + "videoId=" + videoId
                + "videoTitle=" + videoTitle
                + "videoURL=" + videoURL;
    }
}
