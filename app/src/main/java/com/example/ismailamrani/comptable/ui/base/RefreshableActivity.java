package com.example.ismailamrani.comptable.ui.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;

import butterknife.Bind;

public abstract class RefreshableActivity extends AnimatedActivity {

    /**
     * The stock's products list.
     */
    @Bind(R.id.dataRecyclerView)
    protected RecyclerView dataRecyclerView;

    /**
     * The view to be displayed in case there were no products in store.
     */
    @Bind(R.id.emptyLayout)
    protected RelativeLayout emptyLayout;

    @Bind(R.id.emptyMessageLabel)
    protected TextView emptyMessageLabel;

    /**
     * The view to be displayed in case a network error occur.
     */
    @Bind(R.id.errorLayout)
    protected RelativeLayout errorLayout;

    @Bind(R.id.progressBar)
    protected ProgressBar progressBar;

    @Bind(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;

    protected void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this::refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.swipeRefresh1,
                R.color.swipeRefresh2,
                R.color.swipeRefresh3,
                R.color.swipeRefresh4
        );
    }

    protected void refresh() {
        if (!swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(true);
    }

    protected void stopSwipeRefresh() {
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }

    protected void setupRecyclerView() {
        dataRecyclerView.setHasFixedSize(true);
    }

    /**
     * Invoked when an HTTP call fails.
     */
    protected void handleRequestError() {
        if (errorLayout.getVisibility() != View.VISIBLE) {
            errorLayout.setVisibility(View.VISIBLE);
            dataRecyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

        stopSwipeRefresh();
    }
}
