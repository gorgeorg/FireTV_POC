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

import android.graphics.Paint;
import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;
import android.support.v17.leanback.widget.Presenter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.ticketmaster.amazon.R;
import com.ticketmaster.api.discovery.entry.event.Event;
import com.ticketmaster.api.discovery.entry.event.EventEmbedded;

import org.jsoup.helper.StringUtil;


public class DetailsEventDescriptionPresenter extends Presenter {

    private static final String TAG = "DetailsEventDescription";


    protected void onBindDescription(ViewHolder viewHolder, Object item) {

        Event event = (Event) item;
        Log.d(TAG, event.getName());
        if (event != null) {
            viewHolder.getTitle().setText(event.getName());
            //viewHolder.getSubtitle().setText(event.getPleaseNote());
            final String localTime = event.getDates().getStart().getLocalTime();
            viewHolder.getEventDate().setText(event.getDates().getStart().getLocalDate()
                    + "\t" + (localTime == null ? "" : localTime.substring(0, 5))
            );
            final String info = event.getInfo();
            viewHolder.getBody().setText(StringUtil.isBlank(info) ? event.getPleaseNote() : info);
            final String formattedAddress = getFormattedAddress(event);
            viewHolder.getEventVenueAddress().setText(formattedAddress);

        }
    }

    private String getFormattedAddress(Event event) {
        final EventEmbedded embedded = event.getEmbedded();
        if (embedded != null) {
            final String address = embedded.getVenues().get(0).getAddress().getStringAddress();
            final String city = embedded.getVenues().get(0).getCity().getName();
            final String country = embedded.getVenues().get(0).getCountry().getName();
            return String.format("%s, %s, %s", address, city, country);
        } else {
            return "empty";
        }
    }

    /**
     * The ViewHolder for the {@link AbstractDetailsDescriptionPresenter}.
     */
    public static class ViewHolder extends Presenter.ViewHolder {
        private final TextView mTitle;
        private final TextView mSubtitle;
        private final TextView mBody;
        private final int mTitleMargin;
        private final int mUnderTitleBaselineMargin;
        private final int mUnderSubtitleBaselineMargin;
        private final int mTitleLineSpacing;
        private final int mBodyLineSpacing;
        private final int mBodyMaxLines;
        private final int mBodyMinLines;
        private final Paint.FontMetricsInt mTitleFontMetricsInt;
        private final Paint.FontMetricsInt mSubtitleFontMetricsInt;
        private final Paint.FontMetricsInt mBodyFontMetricsInt;
        private final int mTitleMaxLines;
        private final TextView mEventDate;
        private ViewTreeObserver.OnPreDrawListener mPreDrawListener;
        private TextView mEventVenueAddress;

        public ViewHolder(final View view) {
            super(view);
            mTitle = (TextView) view.findViewById(R.id.lb_details_description_title);
            mSubtitle = (TextView) view.findViewById(R.id.lb_details_description_subtitle);
            mBody = (TextView) view.findViewById(R.id.lb_details_description_body);
            mEventDate = (TextView) view.findViewById(R.id.event_date);
            mEventVenueAddress = (TextView) view.findViewById(R.id.event_venue_address);

            Paint.FontMetricsInt titleFontMetricsInt = getFontMetricsInt(mTitle);
            final int titleAscent = view.getResources().getDimensionPixelSize(
                    R.dimen.lb_details_description_title_baseline);
            // Ascent is negative
            mTitleMargin = titleAscent + titleFontMetricsInt.ascent;

            mUnderTitleBaselineMargin = view.getResources().getDimensionPixelSize(
                    R.dimen.lb_details_description_under_title_baseline_margin);
            mUnderSubtitleBaselineMargin = view.getResources().getDimensionPixelSize(
                    R.dimen.lb_details_description_under_subtitle_baseline_margin);

            mTitleLineSpacing = view.getResources().getDimensionPixelSize(
                    R.dimen.lb_details_description_title_line_spacing);
            mBodyLineSpacing = view.getResources().getDimensionPixelSize(
                    R.dimen.lb_details_description_body_line_spacing);

            mBodyMaxLines = view.getResources().getInteger(
                    R.integer.details_description_body_max_lines);
            mBodyMinLines = view.getResources().getInteger(
                    R.integer.lb_details_description_body_min_lines);
            mTitleMaxLines = mTitle.getMaxLines();

            mTitleFontMetricsInt = getFontMetricsInt(mTitle);
            mSubtitleFontMetricsInt = getFontMetricsInt(mSubtitle);
            mBodyFontMetricsInt = getFontMetricsInt(mBody);

            mTitle.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                           int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    addPreDrawListener();
                }
            });
        }

        void addPreDrawListener() {
            if (mPreDrawListener != null) {
                return;
            }
            mPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    if (mSubtitle.getVisibility() == View.VISIBLE &&
                            mSubtitle.getTop() > view.getHeight() &&
                            mTitle.getLineCount() > 1) {
                        mTitle.setMaxLines(mTitle.getLineCount() - 1);
                        return false;
                    }
                    return true;
                }
            };
            view.getViewTreeObserver().addOnPreDrawListener(mPreDrawListener);
        }

        void removePreDrawListener() {
            if (mPreDrawListener != null) {
                view.getViewTreeObserver().removeOnPreDrawListener(mPreDrawListener);
                mPreDrawListener = null;
            }
        }

        public TextView getTitle() {
            return mTitle;
        }

        public TextView getSubtitle() {
            return mSubtitle;
        }

        public TextView getBody() {
            return mBody;
        }

        public TextView getEventDate() {
            return mEventVenueAddress;
        }

        public TextView getEventVenueAddress() {
            return mEventDate;
        }

        private Paint.FontMetricsInt getFontMetricsInt(TextView textView) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setTextSize(textView.getTextSize());
            paint.setTypeface(textView.getTypeface());
            return paint.getFontMetricsInt();
        }
    }

    @Override
    public final ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lb_details_description, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public final void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        ViewHolder vh = (ViewHolder) viewHolder;
        onBindDescription(vh, item);

        boolean hasTitle = true;
        if (TextUtils.isEmpty(vh.mTitle.getText())) {
            vh.mTitle.setVisibility(View.GONE);
            hasTitle = false;
        } else {
            vh.mTitle.setVisibility(View.VISIBLE);
            vh.mTitle.setLineSpacing(vh.mTitleLineSpacing - vh.mTitle.getLineHeight() +
                    vh.mTitle.getLineSpacingExtra(), vh.mTitle.getLineSpacingMultiplier());
            vh.mTitle.setMaxLines(vh.mTitleMaxLines);
        }
        //setTopMargin(vh.mTitle, vh.mTitleMargin);

        if (TextUtils.isEmpty(vh.mSubtitle.getText())) {
            vh.mSubtitle.setVisibility(View.GONE);
        } else {
            vh.mSubtitle.setVisibility(View.VISIBLE);
            if (hasTitle) {
                setTopMargin(vh.mSubtitle, vh.mUnderTitleBaselineMargin +
                        vh.mSubtitleFontMetricsInt.ascent - vh.mTitleFontMetricsInt.descent);
            } else {
                setTopMargin(vh.mSubtitle, 0);
            }
        }

        if (TextUtils.isEmpty(vh.mBody.getText())) {
            vh.mBody.setVisibility(View.GONE);
        } else {
            vh.mBody.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
    }

    @Override
    public void onViewAttachedToWindow(Presenter.ViewHolder holder) {
        // In case predraw listener was removed in detach, make sure
        // we have the proper layout.
        ViewHolder vh = (ViewHolder) holder;
        vh.addPreDrawListener();
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(Presenter.ViewHolder holder) {
        ViewHolder vh = (ViewHolder) holder;
        vh.removePreDrawListener();
        super.onViewDetachedFromWindow(holder);
    }

    private void setTopMargin(TextView textView, int topMargin) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) textView.getLayoutParams();
        lp.topMargin = topMargin;
        textView.setLayoutParams(lp);
    }
}
