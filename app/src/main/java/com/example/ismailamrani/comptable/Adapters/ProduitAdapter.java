package com.example.ismailamrani.comptable.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ismailamrani.comptable.CustumItems.CustumTextView;
import com.example.ismailamrani.comptable.LocalData.URLs;
import com.example.ismailamrani.comptable.Models.ProduitModel;
import com.example.ismailamrani.comptable.ui.ModifierProduitActivity;
import com.example.ismailamrani.comptable.ui.ProduisActivity;
import com.example.ismailamrani.comptable.ui.Produit_InfoActivity;
import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.ServiceWeb.convertInputStreamToString;
import com.example.ismailamrani.comptable.ServiceWeb.getQuery;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Ismail Amrani on 23/03/2016.
 */
public class ProduitAdapter extends BaseAdapter {

    ArrayList<ProduitModel> List = new ArrayList<>();
    Context context;
     int id;


    public ProduitAdapter(Context context, ArrayList<ProduitModel> List){
        this.context = context;
        this.List = List;
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
        View Layout = inflater.inflate(R.layout.produit_item, null);

        final RelativeLayout Info, Actions;
        Info = (RelativeLayout) Layout.findViewById(R.id.Info);
        Actions = (RelativeLayout) Layout.findViewById(R.id.Actions);

        Actions.setVisibility(View.GONE);
        Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Actions.setVisibility(View.VISIBLE);
            }
        });

        ImageView Image;
        CustumTextView Libelle, Qte, Prix,supprimer,modifier,afficher;

        Image = (ImageView) Layout.findViewById(R.id.Image);
        Picasso.with(context).load(List.get(position).getPhoto()).into(Image);

        Libelle = (CustumTextView) Layout.findViewById(R.id.Libelle);
        Libelle.SetText(List.get(position).getLibelle());

        Qte = (CustumTextView) Layout.findViewById(R.id.Qte);
        Qte.SetText(""+List.get(position).getQte());

        Prix = (CustumTextView) Layout.findViewById(R.id.Prix);
        Prix.SetText(""+ List.get(position).getPrixTTC() +" DH");

        supprimer = (CustumTextView)Layout.findViewById(R.id.supprimer);
        supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  System.out.println("<<>>>> Supprimer");
                id = List.get(position).getID();
                System.out.println(">>> id :" + id);
                new supprimer().execute(URLs.removeProduit);
            }
        });
        modifier = (CustumTextView)Layout.findViewById(R.id.modifier);
        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context,ModifierProduitActivity.class);
                i.putExtra("id",List.get(position).getID());
                context.startActivity(i);
            }
        });
        afficher = (CustumTextView)Layout.findViewById(R.id.afficher);

        afficher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Produit_InfoActivity.class);
                i.putExtra("id",List.get(position).getID());
                context.startActivity(i);
            }
        });


        return Layout;

    }

    // post supprimer
    private class supprimer extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                URLConnection conn = url.openConnection();
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Map<String, Object> Params = new LinkedHashMap<>();
                // Params.put("ID", id);
                Params.put("ID",id);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(new getQuery().getQuery(Params));
                writer.flush();
                writer.close();
                os.close();
                httpConn.connect();
                InputStream is = httpConn.getInputStream();

                return new convertInputStreamToString().convertInputStreamToString(is);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println(s);


            try {
                JSONObject j = new JSONObject(s);
                int resp = j.getInt("success");
                if (resp == 1){

                    Toast toast = Toast.makeText(context, "Bien Supprimer", Toast.LENGTH_LONG);
                    toast.show();



                    Intent i = new Intent(context,ProduisActivity.class);
                    context.startActivity(i);





                }
                else if (resp == 0){

                    //  Intent intent = new Intent(getApplicationContext(),ContactUs.class);
                    //  startActivity(intent);
                    Toast toast = Toast.makeText(context, "erreur de suppression !!!!", Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    // post modifier
}
