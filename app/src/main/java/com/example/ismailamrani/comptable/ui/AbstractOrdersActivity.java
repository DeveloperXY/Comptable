package com.example.ismailamrani.comptable.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;

import butterknife.Bind;

/**
 * Created by Mohammed Aouf ZOUAG on 05/05/2016.
 */
public abstract class AbstractOrdersActivity extends AppCompatActivity {
    @Bind(R.id.MyActionBar)
    OGActionBar mActionBar;

    /**
     * The orders' list.
     */
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.emptyMessageLabel)
    TextView emptyMessageLabel;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    /**
     * The view to be displayed in case a network error occur.
     */
    @Bind(R.id.errorLayout)
    RelativeLayout errorLayout;

    /**
     * The view to be displayed in case there were no orders to show.
     */
    @Bind(R.id.emptyLayout)
    RelativeLayout emptyView;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
}
