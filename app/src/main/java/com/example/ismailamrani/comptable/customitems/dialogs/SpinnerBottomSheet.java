package com.example.ismailamrani.comptable.customitems.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.spinners.Item;
import com.example.ismailamrani.comptable.adapters.spinners.ItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 03/05/2016.
 */
public class SpinnerBottomSheet extends BottomSheetDialog {

    private Context context;

    public SpinnerBottomSheet(@NonNull Context context) {
        super(context);
        this.context = context;

        View view = getLayoutInflater().inflate(R.layout.sheet, null);
        setContentView(view);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.spinnerRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new ItemAdapter(createItems(), item -> dismiss()));

        setOnDismissListener(dialog -> this.context = null);
    }

    public List<Item> createItems() {

        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item(R.mipmap.ic_launcher, "Item 1"));
        items.add(new Item(R.mipmap.ic_launcher, "Item 2"));
        items.add(new Item(R.mipmap.ic_launcher, "Item 3"));
        items.add(new Item(R.mipmap.ic_launcher, "Item 4"));

        return items;
    }
}
