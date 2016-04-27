package com.example.ismailamrani.comptable.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ismailamrani.comptable.Models.SpinnerModelCharge;
import com.example.ismailamrani.comptable.R;

import java.util.ArrayList;

/**
 * Created by Redouane on 07/04/2016.
 */
public class SpinnerAdapter extends BaseAdapter {

    ArrayList<SpinnerModelCharge> List = new ArrayList<>();
    Context context;



    public SpinnerAdapter(Context context,ArrayList<SpinnerModelCharge> List) {
        this.context = context;
        this.List=List;
    }

    @Override
    public int getCount() {
        return List.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View Layout = inflater.inflate(R.layout.spinner_items, null);

        TextView spinnername;
        spinnername=(TextView)Layout.findViewById(R.id.spinnername);
        spinnername.setText(List.get(position).getName());

        return Layout;
    }
}
