package com.ticketmaster.api.discovery.entry.base.photo;

import android.content.Context;
import android.widget.ImageView;

import com.googlecode.flickrjandroid.photos.Photo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by Artur Bryzhatiy on 4/21/16.
 */
public class PhotoModel {

    private final Photo photo;

    private final String photoUrl;


    private static final String PHOTO_URL_FORMAT = "https://farm%s.staticflickr.com/%s/%s_%s.%s";
    private final String description;

    public PhotoModel(Photo photo) {
        this.photo = photo;
        this.description = photo.getDescription();
        String farmId = photo.getFarm();
        String serverId = photo.getServer();
        String id = photo.getId();
        String secret = photo.getSecret();
        String format = photo.getOriginalFormat();

        this.photoUrl = String.format(PHOTO_URL_FORMAT, farmId, serverId, id, secret, format);
    }

    public void load(Context context, ImageView target, Callback callback) {
        Picasso.with(context).load(photoUrl).into(target, callback);
    }

    public void load(ImageView target, Callback callback) {
        Context context = target.getContext();
        load(context, target, callback);
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "PhotoModel: {" +
                "photoUrl=" + photoUrl;
    }
}
