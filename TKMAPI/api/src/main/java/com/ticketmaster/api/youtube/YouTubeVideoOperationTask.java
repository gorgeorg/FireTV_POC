package com.ticketmaster.api.youtube;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ticketmaster.api.discovery.entry.base.video.VideoModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Artur Bryzhatiy on 5/10/16.
 */
public class YouTubeVideoOperationTask extends AsyncTask<Void,Void,YouTubeVideoConfiguration> {

    private static final String TAG = "VideoOperationTask";

    private static final String WATCH_PAGE_URL_FORMAT = "https://www.youtube.com/watch?v=%s&hl=%s&has_verified=true";
    private static final String DEFAULT_LANGUAGE_IDENTIFIER = "en";
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36";
    private static final int TIMEOUT = 1000;

    private static final String CONFIG_REGEX_FULL = "ytplayer.config\\s*=\\s*(\\{.*?\\});|[\\({]\\s*'PLAYER_CONFIG'[,:]\\s*(\\{.*?\\})\\s*(?:,'|\\))";
    private static final String CONFIG_REGEX = "ytplayer.config\\s*= ";
    private final VideoModel videoModel;

    @NonNull
    private String videoIdentifier;
    private String languageIdentifier;
    private String videoPageUrl;
    private final YouTubeVideoOperationListener operationListener;

//    public YouTubeVideoOperationTask(@NonNull String videoIdentifier, YouTubeVideoOperationListener operationListener) {
//        this(videoIdentifier, DEFAULT_LANGUAGE_IDENTIFIER, operationListener);
//    }
    public YouTubeVideoOperationTask(@NonNull VideoModel videoModel, YouTubeVideoOperationListener operationListener) {
        this(videoModel, DEFAULT_LANGUAGE_IDENTIFIER, operationListener);

    }
    public YouTubeVideoOperationTask(@NonNull VideoModel videoModel, String languageIdentifier, YouTubeVideoOperationListener operationListener) {
        this.operationListener = operationListener;
        this.videoIdentifier = videoModel == null ? "" : videoModel.getVideoId();
        this.languageIdentifier = languageIdentifier == null ? DEFAULT_LANGUAGE_IDENTIFIER : languageIdentifier;
        this.videoPageUrl = String.format(WATCH_PAGE_URL_FORMAT, this.videoIdentifier, this.languageIdentifier);
        this.videoModel = videoModel;
    }

    @Override
    protected YouTubeVideoConfiguration doInBackground(Void... params) {
        YouTubeVideoConfiguration videoConfiguration = null;
        Log.d(TAG, "Getting html page: " + videoPageUrl);
        try {
            String html = getHtml();

            videoConfiguration = getYouTubeVideoConfiguration(html);

        } catch (Exception e) {
            if (operationListener != null) {
                operationListener.videoConfigurationException(e);
            }
        }

        return videoConfiguration;
    }

    private String getHtml() throws IOException {
        Document videoPageDocument = Jsoup.connect(videoPageUrl)
                .userAgent(USER_AGENT)
                .timeout(TIMEOUT)
                .get();
        return videoPageDocument.html();
    }

    private YouTubeVideoConfiguration getYouTubeVideoConfiguration(String html) {
        YouTubeVideoConfiguration videoConfiguration = null;
        Pattern patternConfigFull = Pattern.compile(CONFIG_REGEX_FULL);
        Matcher matcherConfigFull = patternConfigFull.matcher(html);
        while (matcherConfigFull.find()) {
            String configGroup = matcherConfigFull.group();

            Pattern patternConfig = Pattern.compile(CONFIG_REGEX);
            Matcher matcherConfig = patternConfig.matcher(configGroup);

            if (matcherConfig.find()) {
                int endConfigIndex = matcherConfig.end();
                int configGroupLength = configGroup.length();

                String configurationJson = configGroup.substring(endConfigIndex, configGroupLength);
                videoConfiguration = new YouTubeVideoConfiguration(configurationJson);
            }

        }
        return videoConfiguration;
    }

    @Override
    protected void onPostExecute(YouTubeVideoConfiguration youTubeVideoConfiguration) {
        super.onPostExecute(youTubeVideoConfiguration);
        if (operationListener != null) {
            if (youTubeVideoConfiguration != null ) {
                videoModel.setYouTubeVideoConfiguration(youTubeVideoConfiguration);
                operationListener.videoConfigurationDidReceived(youTubeVideoConfiguration);
            } else {
                operationListener.videoConfigurationEmpty(this.videoPageUrl);
            }
        } else {
            throw new NullPointerException("YouTubeVideoOperationListener must be set");
        }
    }


}
