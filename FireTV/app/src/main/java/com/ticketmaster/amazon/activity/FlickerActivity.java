package com.ticketmaster.amazon.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ticketmaster.amazon.R;
import com.ticketmaster.amazon.util.EventManager;
import com.ticketmaster.api.discovery.entry.base.photo.PhotoModel;

import java.util.List;

/**
 * Created by Georgii on 4/26/2016.
 */
public class FlickerActivity extends Activity {
    private static final String TAG = "FlickerActivity";

    public static final String IMAGE_URL_INDEX = "image_url";
    private ImageView iv;
    private List<PhotoModel> photoModels;
    private int indexPhoto = 0;
    private String imageUrl;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoModels = EventManager.INSTANCE.getPhotoList();
        setContentView(R.layout.flicker_layout);
        iv = (ImageView) findViewById(R.id.image);
        pb = (ProgressBar) findViewById(R.id.progress_bar);
        pb.setVisibility(View.VISIBLE);
        indexPhoto = getIntent().getIntExtra(IMAGE_URL_INDEX, 0);
        imageUrl = photoModels.get(indexPhoto).getPhotoUrl();
        Glide.with(this)
                .load(imageUrl)
                .centerCrop()
                //.placeholder(R.drawable.default_progress_bar3)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        pb.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        pb.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(iv);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "keyCode=" + keyCode + "");
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                showNextImage(true);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                showNextImage(false);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showNextImage(boolean leftToRightDirection) {
        if (photoModels == null || photoModels.size() == 0) {
            Log.w(TAG, "List of photos is empty");
            return;
        }
        if (leftToRightDirection ) {
            if (photoModels.listIterator(indexPhoto).hasNext()) {
                imageUrl = photoModels.listIterator(indexPhoto).next().getPhotoUrl();
                indexPhoto++;
            }
        } else {
            if (photoModels.listIterator(indexPhoto).hasPrevious()) {
                imageUrl = photoModels.listIterator(indexPhoto).previous().getPhotoUrl();
                indexPhoto--;
            }
        }
        pb.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(imageUrl)
                .centerCrop()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        pb.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        pb.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(iv);
    }
}
