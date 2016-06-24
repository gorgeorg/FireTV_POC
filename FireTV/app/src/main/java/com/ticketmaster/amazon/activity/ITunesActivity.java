package com.ticketmaster.amazon.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.ticketmaster.amazon.R;
import com.ticketmaster.amazon.fragment.PlaybackITunesOverlayFragment;
import com.ticketmaster.amazon.fragment.PlaybackOverlayFragment;

import java.io.IOException;

/**
 * Created by Georgii on 4/26/2016.
 */
public class ITunesActivity extends Activity implements PlaybackITunesOverlayFragment.OnPlayPauseClickedListener {
    private static final String TAG = "ITunesActivity";
    public static final String ARTIST_NAME = "artist_name";
    public static final String ITUNES_TITLE = "itunes_title";
    public static final String ITUNES_THUMB_URL = "itunes_thumb";
    public static final String ITUNES_AUDIO_URL = "itunes_audio";
    private MediaPlayer mediaPlayer;
    private PlaybackITunesOverlayFragment playbackOverlayFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long start = System.nanoTime();
        setContentView(R.layout.activity_itunes);
        playbackOverlayFragment = (PlaybackITunesOverlayFragment)
                getFragmentManager().findFragmentById(R.id.itunes_fragment);
        playbackOverlayFragment.togglePlayback(true);
        Log.d(TAG, "onCreate execution time:" + (System.nanoTime() - start) + "ns");
    }

    @Override
    public void onFragmentPlayPause(final String itunesItem, int position, final Boolean playPause) {
        Log.d(TAG, itunesItem);
        mediaPlayer = new MediaPlayer();
        try {
            if (playPause) {
                mediaPlayer.setDataSource(ITunesActivity.this, Uri.parse(itunesItem));
                mediaPlayer.prepare();
                mediaPlayer.start();
            } else {
                mediaPlayer.pause();
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_MEDIA_PLAY:
                playbackOverlayFragment.togglePlayback(false);
                return true;
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
                playbackOverlayFragment.togglePlayback(false);
                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER://KEYCODE_MEDIA_PLAY_PAUSE:
                if (mediaPlayer.isPlaying()) {
                    playbackOverlayFragment.togglePlayback(false);
                } else {
                    playbackOverlayFragment.togglePlayback(true);
                }
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            Log.d(TAG, "stop mediaplayer");
            mediaPlayer.stop();
        } catch (IllegalStateException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
