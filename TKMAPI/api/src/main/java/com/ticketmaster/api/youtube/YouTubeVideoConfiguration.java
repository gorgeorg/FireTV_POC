package com.ticketmaster.api.youtube;

import android.support.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Artur Bryzhatiy on 5/10/16.
 */
public class YouTubeVideoConfiguration {

    private static final String TAG = "VideoConfiguration";
    @NonNull
    private final String configurationJson;


    private HashMap<String, String> playerConfiguration;

    private HashMap<String, String> videoInformation;
    private HashMap<Integer, URL> streamUrls = new HashMap<>();

    public static final int YouTubeVideoQuality = 134;

    /**
     * Video: 240p MPEG-4 Visual | 0.175 Mbit/s
     * Audio: AAC | 36 kbit/s
     */
    public static final int YouTubeVideoQualitySmall240 = 36;

    /**
     * Video: 360p H.264 | 0.5 Mbit/s
     * Audio: AAC | 96 kbit/s
     */
    public static final int YouTubeVideoQualityMedium360 = 18;

    /**
     * Video: 720p H.264 | 2-3 Mbit/s
     * Audio: AAC | 192 kbit/s
     */
    public static final int YouTubeVideoQualityHD720 = 22;


    public YouTubeVideoConfiguration(@NonNull String configurationJson) {
        this.configurationJson = configurationJson;
        composeConfiguration();
    }

    private void composeConfiguration() {
        playerConfiguration = Utils.jsonStringToMap(configurationJson);

        String videoInformationJson = playerConfiguration.get("args");
        videoInformation = Utils.jsonStringToMap(videoInformationJson);

        String streamMap = videoInformation.get("url_encoded_fmt_stream_map");
        String adaptiveFormats = videoInformation.get("adaptive_fmts");

        if (streamMap != null && streamMap.length() > 0) {

            List<String> streamQueries = new ArrayList<>();
            if (streamMap != null) {
                List<String> streamQueriesTemp = Arrays.asList(
                        streamMap.split("\\s*,\\s*")
                );
                streamQueries.addAll(streamQueriesTemp);
            }
            if (adaptiveFormats != null) {
                List<String> adaptiveFormatsList = Arrays.asList(
                        adaptiveFormats.split("\\s*,\\s*")
                );
                streamQueries.addAll(adaptiveFormatsList);
            }
            for (String streamQuery : streamQueries) {

                HashMap<String, String> stream = Utils.parametersStringToMap(streamQuery);

                String urlValue = stream.get("url");
                String itag = stream.get("itag");

                if (urlValue != null && itag != null) {
                    String urlString = String.format("%s&ratebypass=yes", urlValue);
                    URL url = null;
                    try {
                        urlString = URLDecoder.decode(urlString, "utf-8");
                        url = new URL(urlString);
                    } catch (MalformedURLException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (url != null) {
                        Integer key = Integer.valueOf(itag);
                        if (key != null) {
                            streamUrls.put(key, url);
                        }
                    }
                }
            }
        }
    }

    public URL getStreamUrl(int kYouTubeVideoQuality) {
        URL url = null;
        if (streamUrls.containsKey(kYouTubeVideoQuality)) {
            url = streamUrls.get(kYouTubeVideoQuality);
        } else {
            url = streamUrls.get(streamUrls.keySet().toArray()[0]); // avoid NPE
        }
        return url;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": " + streamUrls;
    }
}
