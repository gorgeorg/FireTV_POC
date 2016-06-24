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
package android.support.v17.leanback.app;

import android.os.Bundle;
import android.support.v17.leanback.transition.TransitionHelper;
import android.support.v17.leanback.widget.BrowseFrameLayout;
import android.support.v17.leanback.widget.ItemAlignmentFacet;
import android.support.v17.leanback.widget.ItemBridgeAdapter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.TitleHelper;
import android.support.v17.leanback.widget.TitleView;
import android.support.v17.leanback.widget.VerticalGridView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ticketmaster.amazon.R;
import com.ticketmaster.amazon.activity.FullWidthEventDetailsOverviewRowPresenter;

public class DetailsEventFragment extends BaseFragment {
    private static final String TAG = "DetailsFragment";

    private class SetSelectionRunnable implements Runnable {
        int mPosition;
        boolean mSmooth = true;

        @Override
        public void run() {
            if (mRowsFragment == null) {
                return;
            }
            mRowsFragment.setSelectedPosition(mPosition, mSmooth);
        }
    }

    private RowsFragment mRowsFragment;

    private ObjectAdapter mAdapter;
    private int mContainerListAlignTop;
    private OnItemViewSelectedListener mExternalOnItemViewSelectedListener;
    private OnItemViewClickedListener mOnItemViewClickedListener;

    private Object mSceneAfterEntranceTransition;

    private final SetSelectionRunnable mSetSelectionRunnable = new SetSelectionRunnable();

    private final OnItemViewSelectedListener mOnItemViewSelectedListener =
            new OnItemViewSelectedListener() {
                @Override
                public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                           RowPresenter.ViewHolder rowViewHolder, Row row) {
                    int position = mRowsFragment.getVerticalGridView().getSelectedPosition();
                    int subposition = mRowsFragment.getVerticalGridView().getSelectedSubPosition();
                    Log.v(TAG, "row selected position " + position + " subposition " + subposition);
                    onRowSelected(position, subposition);
                    if (mExternalOnItemViewSelectedListener != null) {
                        mExternalOnItemViewSelectedListener.onItemSelected(itemViewHolder, item,
                                rowViewHolder, row);
                    }
                }
            };

    /**
     * Sets the list of rows for the fragment.
     */
    public void setAdapter(ObjectAdapter adapter) {
        mAdapter = adapter;
        Presenter[] presenters = adapter.getPresenterSelector().getPresenters();
        if (presenters != null) {
            for (Presenter presenter : presenters) {
                setupPresenter(presenter);
            }
        } else {
            Log.e(TAG, "PresenterSelector.getPresenters() not implemented");
        }
        if (mRowsFragment != null) {
            mRowsFragment.setAdapter(adapter);
        }
    }

    /**
     * Returns the list of rows.
     */
    public ObjectAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * Sets an item selection listener.
     */
    public void setOnItemViewSelectedListener(OnItemViewSelectedListener listener) {
        mExternalOnItemViewSelectedListener = listener;
    }

    /**
     * Sets an item clicked listener.
     */
    public void setOnItemViewClickedListener(OnItemViewClickedListener listener) {
        if (mOnItemViewClickedListener != listener) {
            mOnItemViewClickedListener = listener;
            if (mRowsFragment != null) {
                mRowsFragment.setOnItemViewClickedListener(listener);
            }
        }
    }

    /**
     * Returns the item clicked listener.
     */
    public OnItemViewClickedListener getOnItemViewClickedListener() {
        return mOnItemViewClickedListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContainerListAlignTop = getResources().getDimensionPixelSize(R.dimen.lb_details_rows_align_top);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lb_event_details_fragment, container, false);
        ViewGroup fragment_root = (ViewGroup) view.findViewById(R.id.details_fragment_root);
        View titleView = inflateTitle(inflater, fragment_root, savedInstanceState);
        if (titleView != null) {
            fragment_root.addView(titleView);
        }
        mRowsFragment = (RowsFragment) getChildFragmentManager().findFragmentById(
                R.id.details_rows_dock);
        if (mRowsFragment == null) {
            mRowsFragment = new RowsFragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.details_rows_dock, mRowsFragment).commit();
        }
        mRowsFragment.setAdapter(mAdapter);
        mRowsFragment.setOnItemViewSelectedListener(mOnItemViewSelectedListener);
        mRowsFragment.setOnItemViewClickedListener(mOnItemViewClickedListener);

        if (titleView != null) {
            View titleGroup = titleView.findViewById(R.id.browse_title_group);
            if (titleGroup instanceof TitleView) {
                setTitleView((TitleView) titleGroup);
            } else {
                setTitleView(null);
            }
        }

        mSceneAfterEntranceTransition = TransitionHelper.createScene(
                (ViewGroup) view, new Runnable() {
                    @Override
                    public void run() {
                        mRowsFragment.setEntranceTransitionState(true);
                    }
                });
        return view;
    }

    protected View inflateTitle(LayoutInflater inflater, ViewGroup parent,
                                Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lb_browse_title, parent, false);
    }

    void setVerticalGridViewLayout(VerticalGridView listverticalGridViewiew) {
        // align the top edge of item to a fixed position
        listverticalGridViewiew.setItemAlignmentOffset(-mContainerListAlignTop);
        listverticalGridViewiew.setItemAlignmentOffsetPercent(VerticalGridView.ITEM_ALIGN_OFFSET_PERCENT_DISABLED);
        listverticalGridViewiew.setWindowAlignmentOffset(0);
        listverticalGridViewiew.setWindowAlignmentOffsetPercent(VerticalGridView.WINDOW_ALIGN_OFFSET_PERCENT_DISABLED);
        listverticalGridViewiew.setWindowAlignment(VerticalGridView.WINDOW_ALIGN_NO_EDGE);
    }

    protected void setupPresenter(Presenter rowPresenter) {
        if (rowPresenter instanceof FullWidthEventDetailsOverviewRowPresenter) {
            setupDetailsOverviewRowPresenter((FullWidthEventDetailsOverviewRowPresenter) rowPresenter);
        }
    }

    protected void setupDetailsOverviewRowPresenter(FullWidthEventDetailsOverviewRowPresenter presenter) {
        ItemAlignmentFacet facet = new ItemAlignmentFacet();
        // by default align details_frame to half window height
        ItemAlignmentFacet.ItemAlignmentDef alignDef1 = new ItemAlignmentFacet.ItemAlignmentDef();
        alignDef1.setItemAlignmentViewId(R.id.details_frame);
        alignDef1.setItemAlignmentOffset(-getResources()
                .getDimensionPixelSize(R.dimen.lb_details_v2_align_pos_for_actions));
        alignDef1.setItemAlignmentOffsetPercent(0);
        // when description is selected, align details_frame to top edge
        ItemAlignmentFacet.ItemAlignmentDef alignDef2 = new ItemAlignmentFacet.ItemAlignmentDef();
        alignDef2.setItemAlignmentViewId(R.id.details_frame);
        alignDef2.setItemAlignmentFocusViewId(R.id.details_overview_description);
        alignDef2.setItemAlignmentOffset(-getResources()
                .getDimensionPixelSize(R.dimen.lb_details_v2_align_pos_for_description));
        alignDef2.setItemAlignmentOffsetPercent(0);
        ItemAlignmentFacet.ItemAlignmentDef[] defs =
                new ItemAlignmentFacet.ItemAlignmentDef[]{alignDef1, alignDef2};
        facet.setAlignmentDefs(defs);
        presenter.setFacet(ItemAlignmentFacet.class, facet);
    }

    VerticalGridView getVerticalGridView() {
        return mRowsFragment == null ? null : mRowsFragment.getVerticalGridView();
    }

    public RowsFragment getRowsFragment() {
        return mRowsFragment;
    }

    private void setupChildFragmentLayout() {
        setVerticalGridViewLayout(mRowsFragment.getVerticalGridView());
    }

    private void setupFocusSearchListener() {
        TitleHelper titleHelper = getTitleHelper();
        if (titleHelper != null) {
            BrowseFrameLayout browseFrameLayout = (BrowseFrameLayout) getView().findViewById(
                    R.id.details_fragment_root);
            browseFrameLayout.setOnFocusSearchListener(titleHelper.getOnFocusSearchListener());
        }
    }

    /**
     * Sets the selected row position with smooth animation.
     */
    public void setSelectedPosition(int position) {
        setSelectedPosition(position, true);
    }

    /**
     * Sets the selected row position.
     */
    public void setSelectedPosition(int position, boolean smooth) {
        mSetSelectionRunnable.mPosition = position;
        mSetSelectionRunnable.mSmooth = smooth;
        if (getView() != null && getView().getHandler() != null) {
            getView().getHandler().post(mSetSelectionRunnable);
        }
    }

    private void onRowSelected(int selectedPosition, int selectedSubPosition) {
        ObjectAdapter adapter = getAdapter();
        if (adapter == null || adapter.size() == 0 ||
                (selectedPosition == 0 && selectedSubPosition == 0)) {
            showTitle(true);
        } else {
            showTitle(false);
        }
        if (adapter != null && adapter.size() > selectedPosition) {
            final VerticalGridView gridView = getVerticalGridView();
            final int count = gridView.getChildCount();
            for (int i = 0; i < count; i++) {
                ItemBridgeAdapter.ViewHolder bridgeViewHolder = (ItemBridgeAdapter.ViewHolder)
                        gridView.getChildViewHolder(gridView.getChildAt(i));
                RowPresenter rowPresenter = (RowPresenter) bridgeViewHolder.getPresenter();
                onSetRowStatus(rowPresenter,
                        rowPresenter.getRowViewHolder(bridgeViewHolder.getViewHolder()),
                        bridgeViewHolder.getAdapterPosition(),
                        selectedPosition, selectedSubPosition);
            }
        }
    }

    protected void onSetRowStatus(RowPresenter presenter, RowPresenter.ViewHolder viewHolder, int
            adapterPosition, int selectedPosition, int selectedSubPosition) {
        if (presenter instanceof FullWidthEventDetailsOverviewRowPresenter) {
            onSetDetailsOverviewRowStatus((FullWidthEventDetailsOverviewRowPresenter) presenter,
                    (FullWidthEventDetailsOverviewRowPresenter.ViewHolder) viewHolder,
                    adapterPosition, selectedPosition, selectedSubPosition);
        }
    }

    protected void onSetDetailsOverviewRowStatus(FullWidthEventDetailsOverviewRowPresenter presenter,
                                                 FullWidthEventDetailsOverviewRowPresenter.ViewHolder viewHolder, int adapterPosition,
                                                 int selectedPosition, int selectedSubPosition) {
        presenter.setState(viewHolder, FullWidthEventDetailsOverviewRowPresenter.STATE_SMALL);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupChildFragmentLayout();
        setupFocusSearchListener();
        if (isEntranceTransitionEnabled()) {
            mRowsFragment.setEntranceTransitionState(false);
        }
    }

    @Override
    protected Object createEntranceTransition() {
        return TransitionHelper.loadTransition(getActivity(),
                R.transition.lb_details_enter_transition);
    }

    @Override
    protected void runEntranceTransition(Object entranceTransition) {
        TransitionHelper.runTransition(mSceneAfterEntranceTransition, entranceTransition);
    }

    @Override
    protected void onEntranceTransitionEnd() {
        mRowsFragment.onTransitionEnd();
    }

    @Override
    protected void onEntranceTransitionPrepare() {
        mRowsFragment.onTransitionPrepare();
    }

    @Override
    protected void onEntranceTransitionStart() {
        mRowsFragment.onTransitionStart();
    }
}
