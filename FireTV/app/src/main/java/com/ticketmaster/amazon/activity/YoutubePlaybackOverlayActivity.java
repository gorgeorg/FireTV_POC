/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ticketmaster.amazon.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ticketmaster.amazon.R;
import com.ticketmaster.amazon.fragment.YoutubePlaybackOverlayFragment;
import com.ticketmaster.amazon.util.EventManager;
import com.ticketmaster.api.discovery.entry.base.video.VideoModel;

/**
 * PlaybackOverlayActivity for video playback that loads PlaybackOverlayFragment
 */
public class YoutubePlaybackOverlayActivity extends Activity implements
        YoutubePlaybackOverlayFragment.OnPlayPauseClickedListener {
    private static final String TAG = "PlaybackOverlayActivity";
    public static final String YOUTUBE_VIDEO_ID = "youtube_url";
    private VideoView mVideoView;
    private PlaybackState mPlaybackState = PlaybackState.IDLE;
    private MediaSession mSession;
    private YoutubePlaybackOverlayFragment playbackOverlayFragment;
    private ProgressBar progressBar;
    public enum PlaybackState {
        PLAYING, PAUSED, BUFFERING, IDLE
    }
    /**
     * Called when the activity is first created.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.playback_controls);
        progressBar = (ProgressBar) findViewById(R.id.progress_youtube);
        progressBar.setVisibility(View.VISIBLE);
        loadViews();
        setupCallbacks();
        mSession = new MediaSession(this, "LeanbackSampleApp");
        //mSession.setCallback(new MediaSessionCallback());
        mSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mSession.setActive(true);
        playbackOverlayFragment = (YoutubePlaybackOverlayFragment) getFragmentManager().findFragmentById(R.id.playback_controls_fragment);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mVideoView.suspend();
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
                if (mPlaybackState == PlaybackState.PLAYING) {
                    playbackOverlayFragment.togglePlayback(false);
                } else {
                    playbackOverlayFragment.togglePlayback(true);
                }
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    /**
     * Implementation of OnPlayPauseClickedListener
     */
    public void onFragmentPlayPause(String youtubeStreamVideoUrl, int position, Boolean playPause) {
        mVideoView.setVideoPath(youtubeStreamVideoUrl);

        if (position == 0 || mPlaybackState == PlaybackState.IDLE) {
            setupCallbacks();
            mPlaybackState = PlaybackState.IDLE;
        }

        if (playPause && mPlaybackState != PlaybackState.PLAYING) {
            mPlaybackState = PlaybackState.PLAYING;
            if (position > 0) {
                mVideoView.seekTo(position);
            }
            mVideoView.start();
            mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {


                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    String errMsg = null;
                    switch (what) {
                        case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                            errMsg = "MEDIA_ERROR_UNKNOWN";
                            break;
                        case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                            errMsg = "MEDIA_ERROR_SERVER_DIED";
                            break;
                        case MediaPlayer.MEDIA_ERROR_IO:
                            errMsg = "MEDIA_ERROR_IO";
                            break;
                        case MediaPlayer.MEDIA_ERROR_MALFORMED:
                            errMsg = "MEDIA_ERROR_MALFORMED";
                            break;
                        case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                            errMsg = "MEDIA_ERROR_UNSUPPORTED";
                            break;
                        case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                            errMsg = "MEDIA_ERROR_TIMED_OUT";
                            break;
                        default:
                            errMsg = "Unknown media error";
                            break;
                    }
                    Toast.makeText(getApplicationContext(), "VideoView call onError with massage: " + errMsg, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "VideoView call onError with massage: " + errMsg);
                    return false;
                }
            });
        } else {
            mPlaybackState = PlaybackState.PAUSED;
            mVideoView.pause();
        }
        updatePlaybackState(position);
        updateMetadata(0); // TODO: hardcode
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void updatePlaybackState(int position) {
        android.media.session.PlaybackState.Builder stateBuilder = new android.media.session.PlaybackState.Builder()
                .setActions(getAvailableActions());
        int state = android.media.session.PlaybackState.STATE_PLAYING;
        if (mPlaybackState == PlaybackState.PAUSED) {
            state = android.media.session.PlaybackState.STATE_PAUSED;
        }
        stateBuilder.setState(state, position, 1.0f);
        mSession.setPlaybackState(stateBuilder.build());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private long getAvailableActions() {
        long actions = android.media.session.PlaybackState.ACTION_PLAY |
                android.media.session.PlaybackState.ACTION_PLAY_FROM_MEDIA_ID |
                android.media.session.PlaybackState.ACTION_PLAY_FROM_SEARCH;

        if (mPlaybackState == PlaybackState.PLAYING) {
            actions |= android.media.session.PlaybackState.ACTION_PAUSE;
        }

        return actions;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void updateMetadata(final int videoModelIndex) {
        final MediaMetadata.Builder metadataBuilder = new MediaMetadata.Builder();

        final VideoModel videoModel = getVideoModel(videoModelIndex);
        String title = videoModel.getVideoTitle();

        metadataBuilder.putString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE, title);
//        metadataBuilder.putString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE,
//                videoModel.getDescription());
        metadataBuilder.putString(MediaMetadata.METADATA_KEY_DISPLAY_ICON_URI,
                videoModel.getDefaultThumbnailURL());

        // And at minimum the title and artist for legacy support
        metadataBuilder.putString(MediaMetadata.METADATA_KEY_TITLE, title);
        // metadataBuilder.putString(MediaMetadata.METADATA_KEY_ARTIST, videoModel.getStudio());

        Glide.with(this)
                .load(Uri.parse(videoModel.getDefaultThumbnailURL()))
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(500, 500) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        metadataBuilder.putBitmap(MediaMetadata.METADATA_KEY_ART, bitmap);
                        mSession.setMetadata(metadataBuilder.build());
                    }
                });
    }

    private VideoModel getVideoModel(int videoModelIndex) {
        return EventManager.INSTANCE.getVideoList().get(videoModelIndex);
    }

    private void loadViews() {
        mVideoView = (VideoView) findViewById(R.id.videoView);
        mVideoView.setFocusable(false);
        mVideoView.setFocusableInTouchMode(false);
    }

    private void setupCallbacks() {

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                String msg = "";
                if (extra == MediaPlayer.MEDIA_ERROR_TIMED_OUT) {
                    msg = getString(R.string.video_error_media_load_timeout);
                } else if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
                    msg = getString(R.string.video_error_server_inaccessible);
                } else {
                    msg = getString(R.string.video_error_unknown_error);
                }
                mVideoView.stopPlayback();
                mPlaybackState = PlaybackState.IDLE;
                return false;
            }
        });

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
                if (mPlaybackState == PlaybackState.PLAYING) {
                    mVideoView.start();
                }
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlaybackState = PlaybackState.IDLE;
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        super.onResume();
        mSession.setActive(true);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPause() {
        super.onPause();
        if (mVideoView.isPlaying()) {
            if (!requestVisibleBehind(true)) {
                // Try to play behind launcher, but if it fails, stop playback.
                stopPlayback();
            }
        } else {
            requestVisibleBehind(false);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStop() {
        super.onStop();
        mSession.release();
    }


    @Override
    public void onVisibleBehindCanceled() {
        super.onVisibleBehindCanceled();
    }

    private void stopPlayback() {
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
    }


}
