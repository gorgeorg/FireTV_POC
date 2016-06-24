package com.ticketmaster.api.discovery.entry.attraction;

import com.google.gson.annotations.SerializedName;
import com.ticketmaster.api.discovery.entry.base.BaseModel;
import com.ticketmaster.api.discovery.entry.classification.Classification;
import com.ticketmaster.api.discovery.entry.image.Image;

import java.util.List;

/**
 * Created by Artur Bryzhatiy on 18.04.16.
 */
public class Attraction extends BaseModel {

    @SerializedName("url")
    String url;

    @SerializedName("classifications")
    List<Classification> classifications;

    @SerializedName("images")
    List<Image> images;

    public String getUrl() {
        return url;
    }
}
