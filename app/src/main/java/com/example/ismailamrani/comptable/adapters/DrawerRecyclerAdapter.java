package com.example.ismailamrani.comptable.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;

/**
 * Created by Mohammed Aouf ZOUAG on 25/05/2016.
 */
public class DrawerRecyclerAdapter extends RecyclerView.Adapter<DrawerRecyclerAdapter.DrawerViewHolder> {
    String[] titles;
    TypedArray icons;
    Context context;

    public DrawerRecyclerAdapter(String[] titles, TypedArray icons, Context context) {
        this.titles = titles;
        this.icons = icons;
        this.context = context;
    }

    @Override
    public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        if (viewType == 1) {
            View itemLayout = layoutInflater.inflate(R.layout.drawer_item_layout, parent, false);
            return new DrawerViewHolder(itemLayout, viewType, context);
        } else if (viewType == 0) {
            View itemHeader = layoutInflater.inflate(R.layout.nav_header_layout, parent, false);
            return new DrawerViewHolder(itemHeader, viewType, context);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(DrawerViewHolder holder, int position) {
        if (position != 0) {
            holder.itemLabel.setText(titles[position - 1]);
            holder.iconImage.setImageResource(icons.getResourceId(position - 1, -1));
        }
    }

    @Override
    public int getItemCount() {
        return titles.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 0;
        else return 1;
    }

    public class DrawerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemLabel;
        ImageView iconImage;
        Context context;

        public DrawerViewHolder(View drawerItem, int itemType, Context context) {
            super(drawerItem);
            this.context = context;
            drawerItem.setOnClickListener(this);
            if (itemType == 1) {
                itemLabel = (TextView) itemView.findViewById(R.id.itemLabel);
                iconImage = (ImageView) itemView.findViewById(R.id.iconImage);

                drawerItem.setOnClickListener(v -> {
                    drawerItem.setSelected(true);
                });
            }
        }

        /**
         * This defines onClick for every item with respect to its position.
         */
        @Override
        public void onClick(View v) {

        }
    }
}