package com.ticketmaster.api.discovery.entry.event;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;
import com.ticketmaster.api.discovery.entry.base.BaseModel;
import com.ticketmaster.api.discovery.entry.classification.Classification;
import com.ticketmaster.api.discovery.entry.image.Image;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Artur Bryzhatiy on 18.04.16.
 */
public class Event extends BaseModel {

    @SerializedName("url")
    String url;

    @SerializedName("images")
    List<Image> images;

    @SerializedName("sales")
    HashMap<String, EventSale> sales;

    @SerializedName("dates")
    EventDate dates;

    @SerializedName("classifications")
    List <Classification> classifications;

    @SerializedName("promoter")
    HashMap <String, String> promoterIds;

    @SerializedName("info")
    String info;

    @SerializedName("pleaseNote")
    String pleaseNote;

    @SerializedName("_embedded")
    EventEmbedded embedded;

    @SerializedName("description")
    protected String description;

    public List<Image> getImages() {
        return images;
    }

    public List<Classification> getClassifications() {
        return classifications;
    }

    public String getDescription() {
        return description;
    }

    public String getPleaseNote() {
        return pleaseNote;
    }

    public String getInfo() {
        return info;
    }

    public EventDate getDates() {
        return dates;
    }

    public EventEmbedded getEmbedded() {
        return embedded;
    }

    public Bitmap getQRImage() {
        return QRCode.from(url).to(ImageType.PNG)
                .withSize(WIDTH, HEIGHT)
                .bitmap();
    }

    @Override
    public String toString() {
        return "Event{" +
                "url=" + url +
                ", images='" + images + '\'' +
                ", info='" + info + '\'' +
                ", pleaseNote='" + pleaseNote + '\'' +
                '}';
    }
}
