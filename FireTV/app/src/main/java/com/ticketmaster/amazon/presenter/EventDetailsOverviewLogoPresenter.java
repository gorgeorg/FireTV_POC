package com.ticketmaster.amazon.presenter;

import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.Presenter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ticketmaster.amazon.R;
import com.ticketmaster.amazon.activity.FullWidthEventDetailsOverviewRowPresenter;

/**
 * Created by Georgii_Goriachev on 5/10/2016.
 */
public class EventDetailsOverviewLogoPresenter extends Presenter {


    /**
     * ViewHolder for Logo view of DetailsOverviewRow.
     */
    public static class ViewHolder extends Presenter.ViewHolder {

        protected FullWidthEventDetailsOverviewRowPresenter mParentPresenter;
        protected FullWidthEventDetailsOverviewRowPresenter.ViewHolder mParentViewHolder;
        private boolean mSizeFromDrawableIntrinsic;

        public ViewHolder(View view) {
            super(view);
        }

        public FullWidthEventDetailsOverviewRowPresenter getParentPresenter() {
            return mParentPresenter;
        }

        public FullWidthEventDetailsOverviewRowPresenter.ViewHolder getParentViewHolder() {
            return mParentViewHolder;
        }

        /**
         * @return True if layout size of ImageView should be changed to intrinsic size of Drawable,
         *         false otherwise. Used by
         *         {@link EventDetailsOverviewLogoPresenter#onBindViewHolder(Presenter.ViewHolder, Object)}
         *         .
         *
         * @see EventDetailsOverviewLogoPresenter#onCreateView(ViewGroup)
         * @see EventDetailsOverviewLogoPresenter#onBindViewHolder(Presenter.ViewHolder, Object)
         */
        public boolean isSizeFromDrawableIntrinsic() {
            return mSizeFromDrawableIntrinsic;
        }

        /**
         * Change if the ImageView layout size should be synchronized to Drawable intrinsic size.
         * Used by
         * {@link EventDetailsOverviewLogoPresenter#onBindViewHolder(Presenter.ViewHolder, Object)}.
         *
         * @param sizeFromDrawableIntrinsic True if layout size of ImageView should be changed to
         *        intrinsic size of Drawable, false otherwise.
         *
         * @see EventDetailsOverviewLogoPresenter#onCreateView(ViewGroup)
         * @see EventDetailsOverviewLogoPresenter#onBindViewHolder(Presenter.ViewHolder, Object)
         */
        public void setSizeFromDrawableIntrinsic(boolean sizeFromDrawableIntrinsic) {
            mSizeFromDrawableIntrinsic = sizeFromDrawableIntrinsic;
        }
    }

    /**
     * Create a View for the Logo, default implementation loads from
     * {@link android.support.v17.leanback.R.layout#lb_fullwidth_details_overview_logo}. Subclass may override this method to use
     * a fixed layout size and change ImageView scaleType. If the layout params is WRAP_CONTENT for
     * both width and size, the ViewHolder would be using intrinsic size of Drawable in
     * {@link #onBindViewHolder(Presenter.ViewHolder, Object)}.
     *
     * @param parent Parent view.
     * @return View created for the logo.
     */
    public View onCreateView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lb_fullwidth_event_details_overview_logo, parent, false);
    }

    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = onCreateView(parent);
        ViewHolder vh = new ViewHolder(view);
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        vh.setSizeFromDrawableIntrinsic(lp.width == ViewGroup.LayoutParams.WRAP_CONTENT);
        return vh;
    }

    public void setContext(ViewHolder viewHolder,
                           FullWidthEventDetailsOverviewRowPresenter.ViewHolder parentViewHolder,
                           FullWidthEventDetailsOverviewRowPresenter parentPresenter) {
        viewHolder.mParentViewHolder = parentViewHolder;
        viewHolder.mParentPresenter = parentPresenter;
    }

    public boolean isBoundToImage(ViewHolder viewHolder, DetailsOverviewRow row) {
        return row != null && row.getImageDrawable() != null;
    }

    /**
     * Bind logo View to drawble of DetailsOverviewRow and call notifyOnBindLogo().  The
     * default implementation assumes the Logo View is an ImageView and change layout size to
     * intrinsic size of ImageDrawable if {@link ViewHolder#isSizeFromDrawableIntrinsic()} is true.
     * @param viewHolder ViewHolder to bind.
     * @param item DetailsOverviewRow object to bind.
     */
    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        DetailsOverviewRow row = (DetailsOverviewRow) item;
        ImageView imageView = ((ImageView) viewHolder.view);
        imageView.setImageDrawable(row.getImageDrawable());
        if (isBoundToImage((ViewHolder) viewHolder, row)) {
            ViewHolder vh = (ViewHolder) viewHolder;
            if (vh.isSizeFromDrawableIntrinsic()) {
                ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                lp.width = row.getImageDrawable().getIntrinsicWidth();
                lp.height = row.getImageDrawable().getIntrinsicHeight();
                if (imageView.getMaxWidth() > 0 || imageView.getMaxHeight() > 0) {
                    float maxScaleWidth = 1f;
                    if (imageView.getMaxWidth() > 0) {
                        if (lp.width > imageView.getMaxWidth()) {
                            maxScaleWidth = imageView.getMaxWidth() / (float) lp.width;
                        }
                    }
                    lp.width = (int) (lp.width * maxScaleWidth);
                    lp.height = ViewGroup.LayoutParams.MATCH_PARENT;//(int) (lp.height * maxScaleHeight);
                }
                imageView.setLayoutParams(lp);
            }
            vh.mParentPresenter.notifyOnBindLogo(vh.mParentViewHolder);
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
    }

}