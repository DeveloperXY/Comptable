package com.example.ismailamrani.comptable.ui;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;


import com.example.ismailamrani.comptable.LocalData.URLs;
import com.example.ismailamrani.comptable.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Brahim on 24/03/2016.
 */
public class addSocieteActivity extends Activity{
    private static final String TAG = addSocieteActivity.class.getSimpleName();
    EditText nom_scoiete ;
    ImageView photo_societe  ;
    private static int RESULT_LOAD_IMAGE = 1;
    private String selectedImagePath;
    private String codeimage;
    String noms,photo ;
    TextView ajouter ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_societe);
        Log.d(TAG, TAG);
        nom_scoiete = (EditText) findViewById(R.id.nom_societe);
        photo_societe=(ImageView) findViewById(R.id.photo_societe);
        ajouter = (TextView) findViewById(R.id.ajouter);

        photo_societe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
            }
        });
        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noms=nom_scoiete.getText().toString();
                photo=codeimage;
                new addsociete().execute(URLs.addSociete);
            }
        });



    }
private class addsociete extends AsyncTask<String , Void ,String > {

    @Override
    protected String doInBackground(String... params) {

        try {
            URL url =  new URL(params[0]);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) connection;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            Map<String, Object> Params = new LinkedHashMap<>();
            Params.put("Nom", noms);
            Params.put("Logo", photo);

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(new getQuery().getQuery(Params));
            writer.flush();
            writer.close();
            os.close();
            httpConn.connect();
            InputStream is = httpConn.getInputStream();
            return new convertInputStreamToString().convertInputStreamToString(is);


        }catch (Exception e){
            e.printStackTrace();
            return  null ;
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        System.out.println(s);
        try {
            JSONObject j  = new JSONObject(s);
            int rep = j.getInt("success");
            if(rep==1) {
                Toast toast = Toast.makeText(getApplicationContext(), "Bien Ajouter", Toast.LENGTH_LONG);
                toast.show();
            }else  if (rep == 0) {
                Toast toast = Toast.makeText(getApplicationContext(), "erreur  !!!!", Toast.LENGTH_LONG);
                toast.show();
            }
        }catch (JSONException e) {
            e.printStackTrace();
    }
}
}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    photo_societe.setImageBitmap(bitmap);
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


    public class convertInputStreamToString {
        public String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;
            inputStream.close();
            return result;
        }
    }

    public class getQuery {
        public String getQuery(Map<String, Object> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");
                result.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            return result.toString();
        }
    }

    }

