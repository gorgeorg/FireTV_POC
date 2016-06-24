package com.ticketmaster.api.discovery.entry.base.listener.flickr;

import com.googlecode.flickrjandroid.photos.PhotoList;

/**
 * Created by Artur Bryzhatiy on 4/21/16.
 */
public interface FlickrImagesListener {

    void searchPhotosDidComplete(PhotoList photosList);

}
