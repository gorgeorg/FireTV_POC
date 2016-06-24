package com.ticketmaster.api.discovery.entry.base.task;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.ticketmaster.api.discovery.entry.base.listener.video.YouTubeListener;
import com.ticketmaster.api.helper.PropertiesHelper;

import java.io.IOException;
import java.util.List;

/**
 * Created by Artur Bryzhatiy on 4/21/16.
 */
public class YouTubeTask extends AsyncTask <String, Void, List<SearchResult>> {

    private long maxVideos;
    private YouTubeListener listener;

    public YouTubeTask(long maxVideos, @NonNull YouTubeListener listener) {
        this.maxVideos = maxVideos;
        this.listener = listener;
    }

    @Override
    protected List<SearchResult> doInBackground(String... params) {
        List <SearchResult> searchResults = null;

        if (params.length > 0) {
            String searchTerm = params[0];

            try {
                YouTube youtube = new YouTube.Builder(
                        new NetHttpTransport(),
                        new JacksonFactory(),
                        new HttpRequestInitializer() {
                            @Override
                            public void initialize(HttpRequest request) throws IOException {

                            }
                        }
                ).setApplicationName("tkm-events").build();

                YouTube.Search.List search = youtube.search().list("id,snippet");

                String apiKey = PropertiesHelper.getYoutubeApiKey();
                search.setKey(apiKey);
                search.setQ(searchTerm);

                search.setType("video");

                search.setMaxResults(maxVideos);

                SearchListResponse searchResponse = search.execute();
                searchResults = searchResponse.getItems();

            } catch (GoogleJsonResponseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        return searchResults;
    }

    @Override
    protected void onPostExecute(List <SearchResult> searchResults) {
        super.onPostExecute(searchResults);
        if (listener != null) {
            listener.searchVideosDidComplete(searchResults);
        }
    }
}
