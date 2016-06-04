package com.example.ismailamrani.comptable.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.customitems.CustomTextView;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.ui.ModifierProduitActivity;
import com.example.ismailamrani.comptable.ui.ProductDetailsActivity;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.parsing.JSONUtils;
import com.example.ismailamrani.comptable.webservice.PhpAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Ismail Amrani on 23/03/2016.
 * Altered by Mohammed Aouf ZOUAG on 04/06/2016.
 */
public class ProductsAdapter extends BaseAdapter {

    private List<Product> mProducts;
    private Context context;
    private ProductListener listener;

    public ProductsAdapter(Context context, List<Product> List) {
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
            String id = mProducts.get(position).getID() + "";
            if (listener != null) {
                listener.onDeleteProduct(PhpAPI.removeProduit,
                        JSONUtils.bundleIDToJSON(id), Method.POST);
            }
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
            i.putExtra("product", mProducts.get(position));
            context.startActivity(i);
        });

        return Layout;
    }

    public void setProductListener(ProductListener listener) {
        this.listener = listener;
    }

    public interface ProductListener {
        void onDeleteProduct(String url, JSONObject params, Method method);
    }
}
