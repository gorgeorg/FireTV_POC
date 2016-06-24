package com.ticketmaster.api.discovery.entry.base.music;

import android.content.Context;
import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by Artur Bryzhatiy on 4/21/16.
 */
public class iTunesItem {

    @SerializedName("artistName")
    String title;

    @SerializedName("artworkUrl100")
    String thumbURL;

    @SerializedName("previewUrl")
    String previewAudioURL;

    public void loadThumb(Context context, ImageView target, Callback callback) {
        Picasso.with(context).load(thumbURL).into(target, callback);
    }

    public void loadThumb(ImageView target, Callback callback) {
        Context context = target.getContext();
        loadThumb(context, target, callback);
    }

    public String getTitle() {
        return title;
    }

    public String getThumbURL() {
        return thumbURL;
    }

    // Audio file URL
    public String getPreviewAudioURL() {
        return previewAudioURL;
    }
}
