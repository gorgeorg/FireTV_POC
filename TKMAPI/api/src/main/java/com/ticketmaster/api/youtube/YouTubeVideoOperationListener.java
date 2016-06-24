package com.ticketmaster.api.youtube;

/**
 * Created by Artur Bryzhatiy on 5/10/16.
 */
public interface YouTubeVideoOperationListener {

    void videoConfigurationDidReceived(YouTubeVideoConfiguration videoConfiguration);
    void videoConfigurationEmpty(String videoUrl);
    void videoConfigurationException(Exception e);

}
