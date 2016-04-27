package com.example.ismailamrani.comptable.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ismailamrani.comptable.InformationActivity;
import com.example.ismailamrani.comptable.Models.ClientModel;
import com.example.ismailamrani.comptable.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Redouane on 31/03/2016.
 */
public class ClientAdapter extends BaseAdapter {

    ArrayList<ClientModel> List = new ArrayList<>();
    Context context;



    public ClientAdapter(Context context,ArrayList<ClientModel> List) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View Layout = inflater.inflate(R.layout.client_items, null);


        TextView NomPrenom,adresse;
        ImageView imageView;
        RelativeLayout imglay,nomlay;
        NomPrenom=(TextView)Layout.findViewById(R.id.nomPrenom);
        adresse=(TextView)Layout.findViewById(R.id.adresse);
        imageView=(ImageView)Layout.findViewById(R.id.imageclient);
        imglay = (RelativeLayout)Layout.findViewById(R.id.imglay);
        nomlay = (RelativeLayout)Layout.findViewById(R.id.nomlay);


        NomPrenom.setText(List.get(position).getNomPrenom());
        adresse.setText(List.get(position).getAdresse());
        String img=List.get(position).getImage();
        imageView.setImageResource(R.drawable.neymar);
        Picasso.with(context).load(List.get(position).getImage()).into(imageView);
        imglay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, InformationActivity.class);
                i.putExtra("id",List.get(position).getId());
                context.startActivity(i);
            }
        });
        nomlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, InformationActivity.class);
                i.putExtra("id",List.get(position).getId());
                context.startActivity(i);
            }
        });







        return Layout;
    }
}
