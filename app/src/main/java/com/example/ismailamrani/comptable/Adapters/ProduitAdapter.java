package com.example.ismailamrani.comptable.adapters;

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

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.customitems.CustomTextView;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.ui.ModifierProduitActivity;
import com.example.ismailamrani.comptable.ui.ProductDetailsActivity;
import com.example.ismailamrani.comptable.ui.ProductsActivity;
import com.example.ismailamrani.comptable.webservice.PhpAPI;
import com.example.ismailamrani.comptable.webservice.convertInputStreamToString;
import com.example.ismailamrani.comptable.webservice.getQuery;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ismail Amrani on 23/03/2016.
 */
public class ProduitAdapter extends BaseAdapter {

    List<Product> mProducts;
    Context context;
    int id;

    public ProduitAdapter(Context context, List<Product> List) {
        this.context = context;
        this.mProducts = List;
    }

    @Override
    public int getCount() {
        return mProducts.size();
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
        Info.setOnClickListener(v -> Actions.setVisibility(View.VISIBLE));

        ImageView Image;
        CustomTextView Libelle, Qte, Prix, supprimer, modifier, afficher;

        Image = (ImageView) Layout.findViewById(R.id.Image);
        Picasso.with(context).load(mProducts.get(position).getPhoto()).into(Image);

        Libelle = (CustomTextView) Layout.findViewById(R.id.Libelle);
        Libelle.SetText(mProducts.get(position).getLibelle());

        Qte = (CustomTextView) Layout.findViewById(R.id.Qte);
        Qte.SetText("" + mProducts.get(position).getQte());

        Prix = (CustomTextView) Layout.findViewById(R.id.Prix);
        Prix.SetText("" + mProducts.get(position).getPrixTTC() + " DH");

        supprimer = (CustomTextView) Layout.findViewById(R.id.supprimer);
        supprimer.setOnClickListener(v -> {
            //  System.out.println("<<>>>> Supprimer");
            id = mProducts.get(position).getID();
            System.out.println(">>> id :" + id);
            new supprimer().execute(PhpAPI.removeProduit);
        });
        modifier = (CustomTextView) Layout.findViewById(R.id.modifier);
        modifier.setOnClickListener(v -> {

            Intent i = new Intent(context, ModifierProduitActivity.class);
            i.putExtra("id", mProducts.get(position).getID());
            context.startActivity(i);
        });
        afficher = (CustomTextView) Layout.findViewById(R.id.afficher);

        afficher.setOnClickListener(v -> {
            Intent i = new Intent(context, ProductDetailsActivity.class);
            i.putExtra("id", mProducts.get(position).getID());
            context.startActivity(i);
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
                Params.put("ID", id);
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
                if (resp == 1) {
                    Toast toast = Toast.makeText(context, "Bien Supprimer", Toast.LENGTH_LONG);
                    toast.show();

                    Intent i = new Intent(context, ProductsActivity.class);
                    context.startActivity(i);

                } else if (resp == 0) {
                    Toast toast = Toast.makeText(context, "erreur de suppression !!!!", Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
