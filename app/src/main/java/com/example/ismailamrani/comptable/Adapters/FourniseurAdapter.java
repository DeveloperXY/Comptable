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

import com.example.ismailamrani.comptable.Information;
import com.example.ismailamrani.comptable.InformationFournisseur;
import com.example.ismailamrani.comptable.Models.ClientModel;
import com.example.ismailamrani.comptable.Models.Fournisseur;
import com.example.ismailamrani.comptable.R;

import java.util.ArrayList;

/**
 * Created by Redouane on 08/04/2016.
 */
public class FourniseurAdapter extends BaseAdapter{

    ArrayList<Fournisseur> List = new ArrayList<>();
    Context context;



    public FourniseurAdapter(Context context,ArrayList<Fournisseur> List) {
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
        View Layout = inflater.inflate(R.layout.fournisseur_items, null);


        TextView NomPrenom,adresse;
        ImageView imageView;
        RelativeLayout imglay,nomlay;
        NomPrenom=(TextView)Layout.findViewById(R.id.nomPrenomFournisseur);
        adresse=(TextView)Layout.findViewById(R.id.adresseFournisseur);
        imageView=(ImageView)Layout.findViewById(R.id.imageclient);
        imglay = (RelativeLayout)Layout.findViewById(R.id.imglay);
        nomlay = (RelativeLayout)Layout.findViewById(R.id.nomlay);


        NomPrenom.setText(List.get(position).getNom());
        adresse.setText(List.get(position).getAdresse());
        String img=List.get(position).getImage();
        imageView.setImageResource(R.drawable.flogo);

        imglay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, InformationFournisseur.class);
                i.putExtra("id",List.get(position).getId());
                context.startActivity(i);
            }
        });
        nomlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, InformationFournisseur.class);
                i.putExtra("id",List.get(position).getId());
                context.startActivity(i);
            }
        });







        return Layout;
    }
}
