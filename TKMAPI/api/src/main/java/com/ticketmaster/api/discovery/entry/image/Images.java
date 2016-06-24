package com.ticketmaster.api.discovery.entry.image;

import com.google.gson.annotations.SerializedName;
import com.ticketmaster.api.discovery.entry.base.BaseModel;

import java.util.List;

/**
 * Created by Artur Bryzhatiy on 18.04.16.
 */
public class Images extends BaseModel {

    @SerializedName("images")
    List<Image> images;

    public List<Image> getImages() {
        return images;
    }
}
