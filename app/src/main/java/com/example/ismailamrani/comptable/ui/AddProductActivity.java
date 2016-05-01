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
import android.view.View;
import android.widget.Button;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ismail Amrani on 23/03/2016.
 */
public class AddProductActivity extends Activity implements OGActionBarInterface {

    OGActionBar MyActionBar;

    @Bind(R.id.productImage)
    ImageView productImage;
    @Bind(R.id.productName)
    EditText productName;
    @Bind(R.id.priceHT)
    EditText priceHT;
    @Bind(R.id.priceTTC)
    EditText priceTTC;
    @Bind(R.id.barCodeLabel)
    TextView barCodeLabel;

    private static int RESULT_LOAD_IMAGE = 1;
    private String selectedImagePath;

    private String codeimage;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);
        ButterKnife.bind(this);

        MyActionBar = (OGActionBar) findViewById(R.id.MyActionBar);
        MyActionBar.setActionBarListener(this);
        MyActionBar.setTitle("Ajouter Un Produit");
        MyActionBar.AddDisable();
    }

    @OnClick({R.id.productImage, R.id.scanBarcode, R.id.addProductBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.productImage:
                // Add product image
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
                break;
            case R.id.scanBarcode:
                // Scan product bar code
                IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                scanIntegrator.initiateScan();
                break;
            case R.id.addProductBtn:
                // Add product to store
                if (codeimage.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "photo is required", Toast.LENGTH_LONG);
                    toast.show();
                } else if (productName.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "produit name is required", Toast.LENGTH_LONG);
                    toast.show();
                } else if (priceHT.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "prix HT is required", Toast.LENGTH_LONG);
                    toast.show();
                } else if (priceTTC.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "prix TTC is required", Toast.LENGTH_LONG);
                    toast.show();
                } else if (barCodeLabel.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "codeBarre is required", Toast.LENGTH_LONG);
                    toast.show();
                }
                Product product = new Product(
                        0,
                        productName.getText().toString(),
                        Double.parseDouble(priceHT.getText().toString()),
                        Double.parseDouble(priceTTC.getText().toString()),
                        barCodeLabel.getText().toString(),
                        codeimage,
                        0,
                        1,
                        PhpAPI.addProduit
                );

                new addproduit().execute(product);
                break;
        }
    }

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
                    startActivity(new Intent(
                            AddProductActivity.this, ProduisActivity.class));
                } else if (resp == 0) {
                    Toast.makeText(getApplicationContext(),
                            "erreur  !!!!", Toast.LENGTH_LONG).show();
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

            barCodeLabel.setText(scanContent);
        }

        // image bitmap

        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    productImage.setImageBitmap(bitmap);
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
