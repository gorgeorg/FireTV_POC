package com.ticketmaster.api.discovery.entry.base;

import android.graphics.Bitmap;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.api.services.youtube.model.SearchResult;
import com.google.gson.annotations.SerializedName;
import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;

import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photos.PhotosInterface;
import com.googlecode.flickrjandroid.photos.SearchParameters;
import com.googlecode.flickrjandroid.test.TestInterface;
import com.ticketmaster.api.discovery.entry.base.listener.flickr.FlickrImagesListener;
import com.ticketmaster.api.discovery.entry.base.listener.flickr.PhotoModelListener;
import com.ticketmaster.api.discovery.entry.base.listener.video.VideoModelListener;
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
import com.ticketmaster.api.discovery.entry.event.EventLinks;
import com.ticketmaster.api.helper.PropertiesHelper;
import com.ticketmaster.api.youtube.YouTubeVideoOperationTask;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Artur Bryzhatiy on 18.04.16.
 */
public class BaseModel implements Serializable {

    private static final String TAG = "BaseModel";
    public static final int WIDTH = 700;
    public static final int HEIGHT = 700;

    @SerializedName("name")
    protected String name;

    @SerializedName("type")
    protected String type;

    @SerializedName("id")
    protected String id;

    @SerializedName("test")
    protected boolean test;

    @SerializedName("locale")
    protected String locale;

    @SerializedName("_links")
    protected EventLinks links;

    protected List<VideoModel> videos;

    protected List<PhotoModel> photos;

    protected List<iTunesItem> music;




    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public List<iTunesItem> getMusic() {
        return music;
    }

    public List<PhotoModel> getPhotos() {
        return photos;
    }

    public List<VideoModel> getVideos() {
        return videos;
    }

    public String getId() {
        return id;
    }


}
