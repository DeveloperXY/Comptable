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

import com.example.ismailamrani.comptable.BarCodeScanner.IntentIntegrator;
import com.example.ismailamrani.comptable.BarCodeScanner.IntentResult;
import com.example.ismailamrani.comptable.ServiceWeb.PhpAPI;
import com.example.ismailamrani.comptable.Models.ProduitModel;
import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.ServiceWeb.convertInputStreamToString;
import com.example.ismailamrani.comptable.ServiceWeb.getQuery;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
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
 * Created by Redouane on 24/03/2016.
 */
public class ModifierProduitActivity extends Activity {
    private static final String TAG = ModifierProduitActivity.class.getSimpleName();
    int id;
    ImageView produitImage;
    EditText nomProduit,PrixHt,PrixTtc;
    TextView enregistrer,CodeBarre;
    RelativeLayout AddCodeBarre;

    private static int RESULT_LOAD_IMAGE = 1;
    private String selectedImagePath;

    private String codeimage;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifier_produit);
        Log.d(TAG, TAG);

        context = this;


        produitImage =(ImageView)findViewById(R.id.ImageModifier);
        nomProduit = (EditText)findViewById(R.id.nom_produit_modifier);
        PrixHt = (EditText)findViewById(R.id.Prix_HT_produit_modifier);
        PrixTtc = (EditText)findViewById(R.id.Prix_TTC_produit_modifier);
        enregistrer = (TextView)findViewById(R.id.modifierproduit);
        CodeBarre = (TextView) findViewById(R.id.CodeBarre);
        AddCodeBarre = (RelativeLayout) findViewById(R.id.AddCodeBarre);
        Intent intent = getIntent();



        id = intent.getExtras().getInt("id");
        new  getproduitbyid().execute(PhpAPI.getProduitById);

        produitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
            }
        });


        enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProduitModel produitModel = new ProduitModel();
                produitModel.setLibelle(nomProduit.getText().toString());
                produitModel.setPrixHT(Double.parseDouble(PrixHt.getText().toString()));
                produitModel.setPrixTTC(Double.parseDouble(PrixTtc.getText().toString()));
                produitModel.setPhoto(codeimage);
                produitModel.setCodeBarre(CodeBarre.getText().toString());
                produitModel.setUrl(PhpAPI.editproduit);
                produitModel.setLocale_ID(1);
                produitModel.setQte(0);
                new  editproduit().execute(produitModel);

            }
        });

        AddCodeBarre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator scanIntegrator = new IntentIntegrator((Activity) context);
                scanIntegrator.initiateScan();

            }
        });


    }

    private class getproduitbyid extends AsyncTask<String, Void, String> {

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
                if (resp == 1){



                    try {
                        JSONObject o = new JSONObject(s);
                        JSONArray listproduits = o.getJSONArray("produit");

                        for (int i = 0; i < listproduits.length(); i++) {
                            JSONObject usr = listproduits.getJSONObject(i);
                            Picasso.with(getApplicationContext()).load(PhpAPI.IpBackend+"produits/"+ usr.getString("photo")).into(produitImage);
                            nomProduit.setText(usr.getString("libelle"));
                            PrixHt.setText(usr.getString("prixHT"));
                            PrixTtc.setText(usr.getString("prixTTC"));
                            CodeBarre.setText(usr.getString("codeBar"));

                            //itm.setPhoto(URLs.IpBackend + "produits/" + usr.getString("photo"));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                }
                else if (resp == 0){

                    //  Intent intent = new Intent(getApplicationContext(),ContactUs.class);
                    //  startActivity(intent);
                    Toast toast = Toast.makeText(getApplicationContext(), "Produit Not Found  !!!!", Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    //enregister


    private class editproduit extends AsyncTask<ProduitModel, Void, String> {

        @Override
        protected String doInBackground(ProduitModel... params) {

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
                Params.put("ID", id);
                Params.put("Libelle", params[0].getLibelle());
                Params.put("PrixHT", params[0].getPrixHT());
                Params.put("PrixTTC",params[0].getPrixTTC());
                Params.put("CodeBar",params[0].getCodeBarre());
                Params.put("Qte",params[0].getQte());
                Params.put("Photo", params[0].getPhoto());
                Params.put("Local",params[0].getLocale_ID());

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


                    Toast toast = Toast.makeText(getApplicationContext(), "Bien Modifier", Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), ProduisActivity.class));


                } else if (resp == 0) {


                    Toast toast = Toast.makeText(getApplicationContext(), "pas  modifier verifier les champs  !!!!", Toast.LENGTH_LONG);
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
                    // System.out.println("code de l'image " + codeimage);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //image.setImageURI(selectedImageUri);
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
}
