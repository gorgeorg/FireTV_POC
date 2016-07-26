package com.ticketmaster.api.discovery.entry.base.task;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photos.PhotosInterface;
import com.googlecode.flickrjandroid.photos.SearchParameters;
import com.ticketmaster.api.discovery.entry.base.listener.flickr.FlickrImagesListener;
import com.ticketmaster.api.helper.PropertiesHelper;

/**
 * Created by Artur Bryzhatiy on 4/21/16.
 */
public class FlickrImagesTask extends AsyncTask<String, Void, PhotoList> {

    private static final String TAG = "FlickrImagesTask";
    private final int perPage;
    private final int page;
    private String[] stopwords = {"a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "amoungst", "amount", "an", "and", "another", "any", "anyhow", "anyone", "anything", "anyway", "anywhere", "are", "around", "as", "at", "back", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom", "but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven", "else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own", "part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the"};
    @NonNull
    private final FlickrImagesListener listener;

    public FlickrImagesTask(int perPage,  int page, @NonNull FlickrImagesListener listener) {
        this.perPage = perPage;
        this.page = page;
        this.listener = listener;
    }

    @Override
    protected PhotoList doInBackground(String... params) {
        PhotoList photoList = null;

        if (params.length > 0) {
            String param = params[0];

            try {
                String flickrApiKey = PropertiesHelper.getFlickrApiKey();
                String flickrApiSecret = PropertiesHelper.getFlickrApiSecret();

                Flickr flickr = new Flickr(flickrApiKey, flickrApiSecret);

                PhotosInterface photosInterface = flickr.getPhotosInterface();

                SearchParameters searchParameters = new SearchParameters();
                //searchParameters.setTags(tags);
                final String words = filterStopWords(param);
                Log.d(TAG, "Flicker search phrase: " + words);
                searchParameters.setText(words);
                photoList = photosInterface.search(searchParameters, perPage, page);
                Log.d(TAG, "Flicker photo list size: " + photoList.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return photoList;
    }

    @Override
    protected void onPostExecute(PhotoList photos) {
        super.onPostExecute(photos);
        if (listener != null) {
            listener.searchPhotosDidComplete(photos);
        } else {
            Log.e(TAG, "FlickrImagesListener should be set");
            throw new IllegalArgumentException("FlickrImagesListener should be set");
        }
    }

    private String filterStopWords(String phrase) {
        String lowerCasePhrase = phrase.toLowerCase();
        for (String stopWord : stopwords) {
            lowerCasePhrase = lowerCasePhrase.replace(" " + stopWord + " ", " ");
        }
        return lowerCasePhrase;
    }
}
