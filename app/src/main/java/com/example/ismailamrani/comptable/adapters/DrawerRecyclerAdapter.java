package com.example.ismailamrani.comptable.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.DrawerItem;

import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 25/05/2016.
 */
public class DrawerRecyclerAdapter extends RecyclerView.Adapter<DrawerRecyclerAdapter.DrawerViewHolder> {
    private List<DrawerItem> mItems;

    /**
     * The index of the selected drawer item.
     */
    private int selectedIndex;
    private DrawerClickListener listener;

    public DrawerRecyclerAdapter(List<DrawerItem> items) {
        this.mItems = items;

        selectedIndex = -1;
    }

    @Override
    public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        if (viewType == 1) {
            View itemLayout = layoutInflater.inflate(R.layout.drawer_item_layout, parent, false);
            return new DrawerViewHolder(itemLayout, viewType);
        } else if (viewType == 0) {
            View itemHeader = layoutInflater.inflate(R.layout.nav_header_layout, parent, false);
            return new DrawerViewHolder(itemHeader, viewType);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(DrawerViewHolder holder, int position) {
        if (position != 0) {
            DrawerItem item = mItems.get(position - 1);

            holder.itemView.setSelected(selectedIndex == position);
            holder.itemView
                    .setOnClickListener(v -> {
                        notifyItemChanged(selectedIndex);
                        selectedIndex = position;
                        notifyItemChanged(selectedIndex);

                        if (listener != null)
                            listener.onItemClicked(item, holder.iconImage);
                    });


            holder.itemLabel.setText(item.getTitle());
            holder.iconImage.setImageResource(item.getIcon());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 0;
        else return 1;
    }

    public void setSelectedIndex(int index) {
        selectedIndex = index;
    }

    public class DrawerViewHolder extends RecyclerView.ViewHolder {
        TextView itemLabel;
        ImageView iconImage;

        public DrawerViewHolder(View drawerItem, int itemType) {
            super(drawerItem);

            if (itemType == 1) {
                itemLabel = (TextView) itemView.findViewById(R.id.itemLabel);
                iconImage = (ImageView) itemView.findViewById(R.id.iconImage);
            }
        }
    }

    public void setDrawerClickListener(DrawerClickListener listener) {
        this.listener = listener;
    }

    public interface DrawerClickListener {
        void onItemClicked(DrawerItem clickedDrawerItem, View clickedImage);
    }
}