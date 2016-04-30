package com.example.ismailamrani.comptable.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.barcodescanner.IntentIntegrator;
import com.example.ismailamrani.comptable.barcodescanner.IntentResult;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBarInterface;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.webservice.PhpAPI;
import com.example.ismailamrani.comptable.webservice.convertInputStreamToString;
import com.example.ismailamrani.comptable.webservice.getQuery;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Ismail Amrani on 23/03/2016.
 */
public class AddProductActivity extends Activity implements OGActionBarInterface {
    private static final String TAG = AddProductActivity.class.getSimpleName();

    OGActionBar MyActionBar;
    RelativeLayout AddCodeBarre;
    Context context;
    TextView CodeBarre, ajouterProduit;

    ImageView produitImage;
    EditText nomProduit, PrixHt, PrixTtc;


    private static int RESULT_LOAD_IMAGE = 1;
    private String selectedImagePath;

    private String codeimage, imageProduit;
    String Filename = "Produit.txt";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_produit);
        Log.d(TAG, TAG);

        context = this;

        MyActionBar = (OGActionBar) findViewById(R.id.MyActionBar);
        MyActionBar.setActionBarListener(this);
        MyActionBar.setTitle("Ajouter Un Produit");
        MyActionBar.AddDisable();


        produitImage = (ImageView) findViewById(R.id.Image);
        nomProduit = (EditText) findViewById(R.id.nom_produit);
        PrixHt = (EditText) findViewById(R.id.Prix_HT_produit);
        PrixTtc = (EditText) findViewById(R.id.Prix_TTC_produit);
        ajouterProduit = (TextView) findViewById(R.id.ajouterProduit);

        CodeBarre = (TextView) findViewById(R.id.CodeBarre);

        AddCodeBarre = (RelativeLayout) findViewById(R.id.AddCodeBarre);
        AddCodeBarre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator scanIntegrator = new IntentIntegrator((Activity) context);
                scanIntegrator.initiateScan();

            }
        });

        // image

        produitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
            }
        });
        //Enregistrer

        ajouterProduit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codeimage.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "photo is required", Toast.LENGTH_LONG);
                    toast.show();
                } else if (nomProduit.getText().toString().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "produit name is required", Toast.LENGTH_LONG);
                    toast.show();
                } else if (PrixHt.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "prix HT is required", Toast.LENGTH_LONG);
                    toast.show();
                } else if (PrixTtc.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "prix TTC is required", Toast.LENGTH_LONG);
                    toast.show();
                } else if (CodeBarre.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "codeBarre is required", Toast.LENGTH_LONG);
                    toast.show();
                }
                Product product = new Product();
                product.setLibelle(nomProduit.getText().toString());
                product.setPrixHT(Double.parseDouble(PrixHt.getText().toString()));
                product.setPrixTTC(Double.parseDouble(PrixTtc.getText().toString()));
                product.setPhoto(codeimage);
                product.setCodeBarre(CodeBarre.getText().toString());
                product.setUrl(PhpAPI.addProduit);
                product.setLocale_ID(1);
                product.setQte(0);

                new addproduit().execute(product);
            }
        });
    }
    // Post

    private class addproduit extends AsyncTask<Product, Void, String> {

        @Override
        protected String doInBackground(Product... params) {

            try {
                URL url = new URL(params[0].getUrl());
                URLConnection conn = url.openConnection();
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Map<String, Object> Params = new LinkedHashMap<>();
                // Params.put("ID", id);
                Params.put("Libelle", params[0].getLibelle());
                Params.put("PrixHT", params[0].getPrixHT());
                Params.put("PrixTTC", params[0].getPrixTTC());
                Params.put("CodeBar", params[0].getCodeBarre());
                Params.put("Qte", params[0].getQte());
                Params.put("Photo", params[0].getPhoto());
                Params.put("Local", params[0].getLocale_ID());

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


                    Toast toast = Toast.makeText(getApplicationContext(), "successfully add", Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                    startActivity(new Intent(context, ProduisActivity.class));


                } else if (resp == 0) {


                    Toast toast = Toast.makeText(getApplicationContext(), "erreur  !!!!", Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //CropHelper.handleResult(cropHandler, requestCode, resultCode, data);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();

            CodeBarre.setText(scanContent);

        }

        // image bitmap

        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    produitImage.setImageBitmap(bitmap);
                    codeimage = BitmapToString(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public String BitmapToString(Bitmap Image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String imageCode = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return imageCode;
    }


    @Override
    public void onMenuPressed() {

    }

    @Override
    public void onAddPressed() {

    }

}
