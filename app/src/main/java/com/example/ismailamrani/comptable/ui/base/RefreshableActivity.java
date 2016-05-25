package com.example.ismailamrani.comptable.ui.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;

import butterknife.Bind;

/**
 * Extending this class automatically provides you with :
 * 1- A RecyclerView where to show data
 * 2- A SwipeRefreshLayout so as to be able to refresh the list above
 * 3- A progress bar to be shown while fetching the RecyclerView's data
 * 4- An empty view to be displayed if there were no items to show.
 * 5- An error layout to be displayed in case there was no internet connection.
 *
 * & a few methods for the initial setup of the UI.
 */
public abstract class RefreshableActivity extends AnimatedActivity {

    @Bind(R.id.dataRecyclerView)
    protected RecyclerView dataRecyclerView;

    @Bind(R.id.emptyLayout)
    protected RelativeLayout emptyLayout;

    @Bind(R.id.emptyMessageLabel)
    protected TextView emptyMessageLabel;

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
