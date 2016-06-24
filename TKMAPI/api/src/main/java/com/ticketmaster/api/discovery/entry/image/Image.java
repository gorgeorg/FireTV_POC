package com.ticketmaster.api.discovery.entry.image;

import android.content.Context;
import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

/**
 * Created by Artur Bryzhatiy on 18.04.16.
 */
public class Image implements Serializable {

    @SerializedName("ration")
    String ratio;

    @SerializedName("url")
    String url;

    @SerializedName("width")
    Number width;

    @SerializedName("height")
    Number height;

    @SerializedName("fallback")
    boolean fallback;

    public void load(Context context, ImageView target, Callback callback) {
        Picasso.with(context).load(url).into(target, callback);
    }

    public void load(ImageView target, Callback callback) {
        Context context = target.getContext();
        Picasso.with(context).load(url).into(target, callback);
    }
    public String getUrl() {
        return url;
    }
}
