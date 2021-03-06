package com.ticketmaster.amazon.activity;

/**
 * Created by Georgii_Goriachev on 5/10/2016.
 */

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.DetailsOverviewLogoPresenter;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v17.leanback.widget.ItemBridgeAdapter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.support.v17.leanback.widget.OnChildSelectedListener;
import android.support.v17.leanback.widget.OnChildViewHolderSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;

import com.ticketmaster.amazon.R;
import com.ticketmaster.amazon.presenter.EventDetailsOverviewLogoPresenter;

public class FullWidthEventDetailsOverviewRowPresenter extends RowPresenter {

    private static final String TAG = FullWidthEventDetailsOverviewRowPresenter.class.getSimpleName();
    private static final boolean DEBUG = false;

    private static Rect sTmpRect = new Rect();
    private static final Handler sHandler = new Handler();

    /**
     * This is the default state corresponding to layout file.  The view takes full width
     * of screen and covers bottom half of the screen.
     */
    public static final int STATE_HALF = 0;
    /**
     * This is the state when the view covers full width and height of screen.
     */
    public static final int STATE_FULL = 1;
    /**
     * This is the state where the view shrinks to a small banner.
     */
    public static final int STATE_SMALL = 2;

    /**
     * This is the alignment mode that the logo and description align to the starting edge of the
     * overview view.
     */
    public static final int ALIGN_MODE_START = 0;
    /**
     * This is the alignment mode that the ending edge of logo and the starting edge of description
     * align to the middle of the overview view. Note that this might not be the exact horizontal
     * center of the overview view.
     */
    public static final int ALIGN_MODE_MIDDLE = 1;

    /**
     * Listeners for events on ViewHolder.
     */
    public static abstract class Listener {

        /**
         * {@link FullWidthEventDetailsOverviewRowPresenter#notifyOnBindLogo(ViewHolder)} is called.
         *
         * @param vh The ViewHolder that has bound logo view.
         */
        public void onBindLogo(ViewHolder vh) {
        }

    }

    class ActionsItemBridgeAdapter extends ItemBridgeAdapter {
        FullWidthEventDetailsOverviewRowPresenter.ViewHolder mViewHolder;

        ActionsItemBridgeAdapter(FullWidthEventDetailsOverviewRowPresenter.ViewHolder viewHolder) {
            mViewHolder = viewHolder;
        }

        @Override
        public void onBind(final ItemBridgeAdapter.ViewHolder ibvh) {
            if (mViewHolder.getOnItemViewClickedListener() != null ||
                    mActionClickedListener != null) {
                ibvh.getPresenter().setOnClickListener(
                        ibvh.getViewHolder(), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mViewHolder.getOnItemViewClickedListener() != null) {
                                    mViewHolder.getOnItemViewClickedListener().onItemClicked(
                                            ibvh.getViewHolder(), ibvh.getItem(),
                                            mViewHolder, mViewHolder.getRow());
                                }
                                if (mActionClickedListener != null) {
                                    mActionClickedListener.onActionClicked((Action) ibvh.getItem());
                                }
                            }
                        });
            }
        }

        @Override
        public void onUnbind(final ItemBridgeAdapter.ViewHolder ibvh) {
            if (mViewHolder.getOnItemViewClickedListener() != null ||
                    mActionClickedListener != null) {
                ibvh.getPresenter().setOnClickListener(ibvh.getViewHolder(), null);
            }
        }

        @Override
        public void onAttachedToWindow(ItemBridgeAdapter.ViewHolder viewHolder) {
            // Remove first to ensure we don't add ourselves more than once.
            viewHolder.itemView.removeOnLayoutChangeListener(mViewHolder.mLayoutChangeListener);
            viewHolder.itemView.addOnLayoutChangeListener(mViewHolder.mLayoutChangeListener);
        }

        @Override
        public void onDetachedFromWindow(ItemBridgeAdapter.ViewHolder viewHolder) {
            viewHolder.itemView.removeOnLayoutChangeListener(mViewHolder.mLayoutChangeListener);
            mViewHolder.checkFirstAndLastPosition(false);
        }
    }

    /**
     * A ViewHolder for the DetailsOverviewRow.
     */
    public class ViewHolder extends RowPresenter.ViewHolder {

        protected final DetailsOverviewRow.Listener mRowListener = createRowListener();

        protected DetailsOverviewRow.Listener createRowListener() {
            return new DetailsOverviewRowListener();
        }

        public class DetailsOverviewRowListener extends DetailsOverviewRow.Listener {
            @Override
            public void onImageDrawableChanged(DetailsOverviewRow row) {
                sHandler.removeCallbacks(mUpdateDrawableCallback);
                sHandler.post(mUpdateDrawableCallback);
            }

            @Override
            public void onItemChanged(DetailsOverviewRow row) {
                if (mDetailsDescriptionViewHolder != null) {
                    mDetailsPresenter.onUnbindViewHolder(mDetailsDescriptionViewHolder);
                }
                mDetailsPresenter.onBindViewHolder(mDetailsDescriptionViewHolder, row.getItem());
            }

            @Override
            public void onActionsAdapterChanged(DetailsOverviewRow row) {
                bindActions(row.getActionsAdapter());
            }
        }

        final ViewGroup mOverviewRoot;
        final FrameLayout mOverviewFrame;
        final ViewGroup mDetailsDescriptionFrame;
        final HorizontalGridView mActionsRow;
        final Presenter.ViewHolder mDetailsDescriptionViewHolder;
        final EventDetailsOverviewLogoPresenter.ViewHolder mDetailsLogoViewHolder;
        int mNumItems;
        ItemBridgeAdapter mActionBridgeAdapter;
        int mState = STATE_SMALL;

        final Runnable mUpdateDrawableCallback = new Runnable() {
            @Override
            public void run() {
                Row row = getRow();
                if (row == null) {
                    return;
                }
                mDetailsOverviewLogoPresenter.onBindViewHolder(mDetailsLogoViewHolder, row);
            }
        };

        void bindActions(ObjectAdapter adapter) {
            mActionBridgeAdapter.setAdapter(adapter);
            mActionsRow.setAdapter(mActionBridgeAdapter);
            mNumItems = mActionBridgeAdapter.getItemCount();

        }

        void onBind() {
            DetailsOverviewRow row = (DetailsOverviewRow) getRow();
            bindActions(row.getActionsAdapter());
        }

        void onUnbind() {
            sHandler.removeCallbacks(mUpdateDrawableCallback);
        }

        final View.OnLayoutChangeListener mLayoutChangeListener =
                new View.OnLayoutChangeListener() {

                    @Override
                    public void onLayoutChange(View v, int left, int top, int right,
                                               int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        checkFirstAndLastPosition(false);
                    }
                };

        final OnChildViewHolderSelectedListener mChildSelectedListener = new OnChildViewHolderSelectedListener() {
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child,
                                                  int position, int subposition) {
                dispatchItemSelection(child.itemView);
            }
        };

        void dispatchItemSelection(View view) {
            if (!isSelected()) {
                return;
            }
            ItemBridgeAdapter.ViewHolder ibvh = (ItemBridgeAdapter.ViewHolder) (view != null ?
                    mActionsRow.getChildViewHolder(view) :
                    mActionsRow.findViewHolderForLayoutPosition(mActionsRow.getSelectedPosition()));
            if (ibvh == null) {
                if (getOnItemViewSelectedListener() != null) {
                    getOnItemViewSelectedListener().onItemSelected(null, null,
                            ViewHolder.this, getRow());
                }
            } else {
                if (getOnItemViewSelectedListener() != null) {
                    getOnItemViewSelectedListener().onItemSelected(ibvh.getViewHolder(), ibvh.getItem(),
                            ViewHolder.this, getRow());
                }
            }
        }


        private int getViewCenter(View view) {
            return (view.getRight() - view.getLeft()) / 2;
        }

        private void checkFirstAndLastPosition(boolean fromScroll) {
            RecyclerView.ViewHolder viewHolder;
            viewHolder = mActionsRow.findViewHolderForLayoutPosition(mNumItems - 1);
            viewHolder = mActionsRow.findViewHolderForLayoutPosition(0);
            boolean showLeft = (viewHolder == null || viewHolder.itemView.getLeft() < 0);
        }

        /**
         * Constructor for the ViewHolder.
         *
         * @param rootView The root View that this view holder will be attached
         *                 to.
         */
        public ViewHolder(View rootView, Presenter detailsPresenter, EventDetailsOverviewLogoPresenter logoPresenter) {
            super(rootView);
            mOverviewRoot = (ViewGroup) rootView.findViewById(R.id.details_root);
            mOverviewFrame = (FrameLayout) rootView.findViewById(R.id.details_frame);
            mDetailsDescriptionFrame = (ViewGroup) rootView.findViewById(R.id.details_overview_description);
            mActionsRow = (HorizontalGridView) mOverviewFrame.findViewById(R.id.details_overview_actions);
            mActionsRow.setHasOverlappingRendering(false);
            mActionsRow.setAdapter(mActionBridgeAdapter);
            mActionsRow.setOnChildViewHolderSelectedListener(mChildSelectedListener);

            final int fadeLength = rootView.getResources().getDimensionPixelSize(
                    R.dimen.lb_details_overview_actions_fade_size);
            mActionsRow.setFadingRightEdgeLength(fadeLength);
            mActionsRow.setFadingLeftEdgeLength(fadeLength);

            mDetailsDescriptionViewHolder = detailsPresenter.onCreateViewHolder(mDetailsDescriptionFrame);
            mDetailsDescriptionFrame.addView(mDetailsDescriptionViewHolder.view);
            mDetailsLogoViewHolder = (EventDetailsOverviewLogoPresenter.ViewHolder) logoPresenter.onCreateViewHolder(mOverviewRoot);

//            final ViewGroup.LayoutParams ivLayoutParams = mDetailsLogoViewHolder.view.getLayoutParams();
            final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = 0;
            layoutParams.gravity = Gravity.TOP;
            mOverviewRoot.addView(mDetailsLogoViewHolder.view, layoutParams);
        }

        /**
         * Returns the rectangle area with a color background.
         */
        public final ViewGroup getOverviewView() {
            return mOverviewFrame;
        }

        /**
         * Returns the ViewHolder for logo.
         */
        public final EventDetailsOverviewLogoPresenter.ViewHolder getLogoViewHolder() {
            return mDetailsLogoViewHolder;
        }

        /**
         * Returns the ViewHolder for DetailsDescription.
         */
        public final Presenter.ViewHolder getDetailsDescriptionViewHolder() {
            return mDetailsDescriptionViewHolder;
        }

        /**
         * Returns the root view for inserting details description.
         */
        public final ViewGroup getDetailsDescriptionFrame() {
            return mDetailsDescriptionFrame;
        }

        /**
         * Returns the view of actions row.
         */
        public final ViewGroup getActionsRow() {
            return mActionsRow;
        }

        /**
         * Returns current state of the ViewHolder set by
         * {@link FullWidthEventDetailsOverviewRowPresenter#setState(ViewHolder, int)}.
         */
        public final int getState() {
            return mState;
        }
    }

    protected int mInitialState = STATE_FULL;

    private final Presenter mDetailsPresenter;
    private final EventDetailsOverviewLogoPresenter mDetailsOverviewLogoPresenter;
    private OnActionClickedListener mActionClickedListener;

    private int mBackgroundColor = Color.TRANSPARENT;
    private int mActionsBackgroundColor = Color.TRANSPARENT;
    private boolean mBackgroundColorSet;
    private boolean mActionsBackgroundColorSet;

    private Listener mListener;
    private boolean mParticipatingEntranceTransition;

    private int mAlignmentMode;

    /**
     * Constructor for a FullWidthDetailsOverviewRowPresenter.
     *
     * @param detailsPresenter The {@link Presenter} used to render the detailed
     *                         description of the row.
     */
    public FullWidthEventDetailsOverviewRowPresenter(Presenter detailsPresenter) {
        this(detailsPresenter, new EventDetailsOverviewLogoPresenter());
    }

    /**
     * Constructor for a FullWidthDetailsOverviewRowPresenter.
     *
     * @param detailsPresenter The {@link Presenter} used to render the detailed
     *                         description of the row.
     * @param logoPresenter    The {@link Presenter} used to render the logo view.
     */
    public FullWidthEventDetailsOverviewRowPresenter(Presenter detailsPresenter,
                                                     EventDetailsOverviewLogoPresenter logoPresenter) {
        setHeaderPresenter(null);
        setSelectEffectEnabled(false);
        mDetailsPresenter = detailsPresenter;
        mDetailsOverviewLogoPresenter = logoPresenter;
    }

    /**
     * Sets the listener for Action click events.
     */
    public void setOnActionClickedListener(OnActionClickedListener listener) {
        mActionClickedListener = listener;
    }

    /**
     * Returns the listener for Action click events.
     */
    public OnActionClickedListener getOnActionClickedListener() {
        return mActionClickedListener;
    }

    /**
     * Sets the background color.  If not set, a default from the theme will be used.
     */
    public final void setBackgroundColor(int color) {
        mBackgroundColor = color;
        mBackgroundColorSet = true;
    }

    /**
     * Returns the background color.  If {@link #setBackgroundColor(int)}, transparent
     * is returned.
     */
    public final int getBackgroundColor() {
        return mBackgroundColor;
    }

    /**
     * Sets the background color for Action Bar.  If not set, a default from the theme will be
     * used.
     */
    public final void setActionsBackgroundColor(int color) {
        mActionsBackgroundColor = color;
        mActionsBackgroundColorSet = true;
    }

    /**
     * Returns the background color of actions.  If {@link #setActionsBackgroundColor(int)}
     * is not called,  transparent is returned.
     */
    public final int getActionsBackgroundColor() {
        return mActionsBackgroundColor;
    }

    /**
     * Returns true if the overview should be part of shared element transition.
     */
    public final boolean isParticipatingEntranceTransition() {
        return mParticipatingEntranceTransition;
    }

    /**
     * Sets if the overview should be part of shared element transition.
     */
    public final void setParticipatingEntranceTransition(boolean participating) {
        mParticipatingEntranceTransition = participating;
    }

    /**
     * Change the initial state used to create ViewHolder.
     */
    public final void setInitialState(int state) {
        mInitialState = state;
    }

    /**
     * Returns the initial state used to create ViewHolder.
     */
    public final int getInitialState() {
        return mInitialState;
    }

    /**
     * Set alignment mode of Description.
     *
     * @param alignmentMode One of {@link #ALIGN_MODE_MIDDLE} or {@link #ALIGN_MODE_START}
     */
    public final void setAlignmentMode(int alignmentMode) {
        mAlignmentMode = alignmentMode;
    }

    /**
     * Returns alignment mode of Description.
     *
     * @return One of {@link #ALIGN_MODE_MIDDLE} or {@link #ALIGN_MODE_START}.
     */
    public final int getAlignmentMode() {
        return mAlignmentMode;
    }

    @Override
    protected boolean isClippingChildren() {
        return true;
    }

    /**
     * Set listener for details overview presenter. Must be called before creating
     * ViewHolder.
     */
    public final void setListener(Listener listener) {
        mListener = listener;
    }

    /**
     * Get resource id to inflate the layout.  The layout must match {@link #STATE_HALF}
     */
    protected int getLayoutResourceId() {
        return R.layout.lb_fullwidth_details_event_overview;
    }

    @Override
    protected RowPresenter.ViewHolder createRowViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(getLayoutResourceId(), parent, false);
        //v.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,  1000));
        final ViewHolder vh = new ViewHolder(v, mDetailsPresenter, mDetailsOverviewLogoPresenter);
        mDetailsOverviewLogoPresenter.setContext(vh.mDetailsLogoViewHolder, vh, this); // TODO: uncoment
        setState(vh, mInitialState);

        vh.mActionBridgeAdapter = new ActionsItemBridgeAdapter(vh);
        final View overview = vh.mOverviewFrame;
        if (mBackgroundColorSet) {
            overview.setBackgroundColor(mBackgroundColor);
        }
        if (mActionsBackgroundColorSet) {
            overview.findViewById(R.id.details_overview_actions_background)
                    .setBackgroundColor(mActionsBackgroundColor);
        }
        //RoundedRectHelper.getInstance().setClipToRoundedOutline(overview, true);

        if (!getSelectEffectEnabled()) {
            vh.mOverviewFrame.setForeground(null);
        }

//        vh.mActionsRow.setOnUnhandledKeyListener(new BaseGridView.OnUnhandledKeyListener() {
//            @Override
//            public boolean onUnhandledKey(KeyEvent event) {
//                if (vh.getOnKeyListener() != null) {
//                    if (vh.getOnKeyListener().onKey(vh.view, event.getKeyCode(), event)) {
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
        return vh;
    }

    private static int getNonNegativeWidth(Drawable drawable) {
        final int width = (drawable == null) ? 0 : drawable.getIntrinsicWidth();
        return (width > 0 ? width : 0);
    }

    private static int getNonNegativeHeight(Drawable drawable) {
        final int height = (drawable == null) ? 0 : drawable.getIntrinsicHeight();
        return (height > 0 ? height : 0);
    }

    @Override
    protected void onBindRowViewHolder(RowPresenter.ViewHolder holder, Object item) {
        super.onBindRowViewHolder(holder, item);

        DetailsOverviewRow row = (DetailsOverviewRow) item;
        ViewHolder vh = (ViewHolder) holder;

        mDetailsOverviewLogoPresenter.onBindViewHolder(vh.mDetailsLogoViewHolder, row);
        mDetailsPresenter.onBindViewHolder(vh.mDetailsDescriptionViewHolder, row.getItem());
        vh.onBind();
    }

    @Override
    protected void onUnbindRowViewHolder(RowPresenter.ViewHolder holder) {
        ViewHolder vh = (ViewHolder) holder;
        vh.onUnbind();
        mDetailsPresenter.onUnbindViewHolder(vh.mDetailsDescriptionViewHolder);
        mDetailsOverviewLogoPresenter.onUnbindViewHolder(vh.mDetailsLogoViewHolder);
        super.onUnbindRowViewHolder(holder);
    }

    @Override
    public final boolean isUsingDefaultSelectEffect() {
        return false;
    }

    @Override
    protected void onSelectLevelChanged(RowPresenter.ViewHolder holder) {
        super.onSelectLevelChanged(holder);
    }

    @Override
    protected void onRowViewAttachedToWindow(RowPresenter.ViewHolder vh) {
        super.onRowViewAttachedToWindow(vh);
        ViewHolder viewHolder = (ViewHolder) vh;
        mDetailsPresenter.onViewAttachedToWindow(viewHolder.mDetailsDescriptionViewHolder);
        mDetailsOverviewLogoPresenter.onViewAttachedToWindow(viewHolder.mDetailsLogoViewHolder);
    }

    @Override
    protected void onRowViewDetachedFromWindow(RowPresenter.ViewHolder vh) {
        super.onRowViewDetachedFromWindow(vh);
        ViewHolder viewHolder = (ViewHolder) vh;
        mDetailsPresenter.onViewDetachedFromWindow(viewHolder.mDetailsDescriptionViewHolder);
        mDetailsOverviewLogoPresenter.onViewDetachedFromWindow(viewHolder.mDetailsLogoViewHolder);
    }

    /**
     * Called by {@link DetailsOverviewLogoPresenter} to notify logo was bound to view.
     * Application should not directly call this method.
     *
     * @param viewHolder The row ViewHolder that has logo bound to view.
     */
    public final void notifyOnBindLogo(ViewHolder viewHolder) {
        onLayoutOverviewFrame(viewHolder, viewHolder.getState(), true);
        onLayoutLogo(viewHolder, viewHolder.getState(), true);
        if (mListener != null) {
            mListener.onBindLogo(viewHolder);
        }
    }

    /**
     * Layout logo position based on current state.  Subclass may override.
     * The method is called when a logo is bound to view or state changes.
     *
     * @param viewHolder  The row ViewHolder that contains the logo.
     * @param oldState    The old state,  can be same as current viewHolder.getState()
     * @param logoChanged Whether logo was changed.
     */
    protected void onLayoutLogo(ViewHolder viewHolder, int oldState, boolean logoChanged) {
        View v = viewHolder.getLogoViewHolder().view;
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)
                v.getLayoutParams();
        switch (mAlignmentMode) {
            case ALIGN_MODE_START:
            default:
                lp.setMarginStart(v.getResources().getDimensionPixelSize(
                        R.dimen.lb_details_v2_logo_margin_start)/ 4);
                break;
            case ALIGN_MODE_MIDDLE:
                lp.setMarginStart(v.getResources().getDimensionPixelSize(R.dimen.lb_details_v2_left)
                        - lp.width);
                break;
        }

        switch (viewHolder.getState()) {
            case STATE_FULL:
            default:
                lp.topMargin =
                        v.getResources().getDimensionPixelSize(R.dimen.lb_details_v2_blank_height)
                                - lp.height / 2;
                break;
            case STATE_HALF:
                lp.topMargin = 0;
                break;
            case STATE_SMALL:
                lp.topMargin = 0;
                break;
        }
        v.setLayoutParams(lp);
    }

    /**
     * Layout overview frame based on current state.  Subclass may override.
     * The method is called when a logo is bound to view or state changes.
     *
     * @param viewHolder  The row ViewHolder that contains the logo.
     * @param oldState    The old state,  can be same as current viewHolder.getState()
     * @param logoChanged Whether logo was changed.
     */
    protected void onLayoutOverviewFrame(ViewHolder viewHolder, int oldState, boolean logoChanged) {
        boolean wasBanner = oldState == STATE_SMALL;
        boolean isBanner = viewHolder.getState() == STATE_SMALL;
        if (wasBanner != isBanner || logoChanged) {
            Resources res = viewHolder.view.getResources();

            int frameMarginStart;
            int descriptionMarginStart = 0;
            int logoWidth = 0;
            if (mDetailsOverviewLogoPresenter.isBoundToImage(viewHolder.getLogoViewHolder(),
                    (DetailsOverviewRow) viewHolder.getRow())) {
                logoWidth = viewHolder.getLogoViewHolder().view.getLayoutParams().width;
            }
            switch (mAlignmentMode) {
                case ALIGN_MODE_START:
                default:
                    frameMarginStart = 0;
                    descriptionMarginStart = logoWidth + res.getDimensionPixelSize(
                            R.dimen.lb_details_v2_logo_margin_start) / 4;
                    break;
                case ALIGN_MODE_MIDDLE:
                    frameMarginStart = 0;
                    descriptionMarginStart = res.getDimensionPixelSize(
                            R.dimen.lb_details_v2_left);
                    break;
            }
            MarginLayoutParams lpFrame =
                    (MarginLayoutParams) viewHolder.getOverviewView().getLayoutParams();
            lpFrame.topMargin = 0;
            lpFrame.leftMargin = lpFrame.rightMargin = frameMarginStart;
            viewHolder.getOverviewView().setLayoutParams(lpFrame);

            View description = viewHolder.getDetailsDescriptionFrame();
            MarginLayoutParams lpDesc = (MarginLayoutParams) description.getLayoutParams();
            lpDesc.setMarginStart(descriptionMarginStart);
            description.setLayoutParams(lpDesc);

            View action = viewHolder.getActionsRow();
            MarginLayoutParams lpActions = (MarginLayoutParams) action.getLayoutParams();
            lpActions.setMarginStart(descriptionMarginStart);
            lpActions.height = res.getDimensionPixelSize(R.dimen.lb_details_v2_actions_height);
            action.setLayoutParams(lpActions);
        }
    }

    /**
     * Switch state of a ViewHolder.
     *
     * @param viewHolder The ViewHolder to change state.
     * @param state      New state, can be {@link #STATE_FULL}, {@link #STATE_HALF}
     *                   or {@link #STATE_SMALL}.
     */
    public final void setState(ViewHolder viewHolder, int state) {
        if (viewHolder.getState() != state) {
            int oldState = viewHolder.getState();
            viewHolder.mState = state;
            onStateChanged(viewHolder, oldState);
        }
    }

    /**
     * Called when {@link ViewHolder#getState()} changes.  Subclass may override.
     * The default implementation calls {@link #onLayoutLogo(ViewHolder, int, boolean)} and
     * {@link #onLayoutOverviewFrame(ViewHolder, int, boolean)}.
     *
     * @param viewHolder The ViewHolder which state changed.
     * @param oldState   The old state.
     */
    protected void onStateChanged(ViewHolder viewHolder, int oldState) {
        onLayoutOverviewFrame(viewHolder, oldState, false);
        onLayoutLogo(viewHolder, oldState, false);
    }

    @Override
    public void setEntranceTransitionState(RowPresenter.ViewHolder holder,
                                           boolean afterEntrance) {
        super.setEntranceTransitionState(holder, afterEntrance);
        if (mParticipatingEntranceTransition) {
            holder.view.setVisibility(afterEntrance ? View.VISIBLE : View.INVISIBLE);
        }
    }
}
