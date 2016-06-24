package com.ticketmaster.api.discovery.entry.base.listener.video;

import com.ticketmaster.api.youtube.YouTubeVideoConfiguration;

/**
 * Created by Georgii_Goriachev on 6/9/2016.
 */
public interface VideoStreamUrlListener {
    void gotYoutubeStreamUrl(YouTubeVideoConfiguration youTubeVideoConfiguration);
}
