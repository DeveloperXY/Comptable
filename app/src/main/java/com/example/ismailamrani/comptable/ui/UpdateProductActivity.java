package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.barcodescanner.IntentIntegrator;
import com.example.ismailamrani.comptable.barcodescanner.IntentResult;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.ui.base.WithDrawerActivity;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.http.SuccessRequestListener;
import com.example.ismailamrani.comptable.utils.ui.ResultCodes;
import com.example.ismailamrani.comptable.webservice.PhpAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by Redouane on 24/03/2016.
 * Altered by Mohammed Aouf ZOUAG on 04/06/2016.
 */
public class UpdateProductActivity extends WithDrawerActivity {
    ImageView produitImage;
    EditText nomProduit, PrixHt, PrixTtc;
    TextView enregistrer, CodeBarre;
    RelativeLayout AddCodeBarre;

    private static int RESULT_LOAD_IMAGE = 1;
    private String selectedImagePath;

    private Product selectedProduct;
    private String imageCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifier_produit);
        ButterKnife.bind(this);

        setupActionBar();

        produitImage = (ImageView) findViewById(R.id.ImageModifier);
        nomProduit = (EditText) findViewById(R.id.nom_produit_modifier);
        PrixHt = (EditText) findViewById(R.id.Prix_HT_produit_modifier);
        PrixTtc = (EditText) findViewById(R.id.Prix_TTC_produit_modifier);
        enregistrer = (TextView) findViewById(R.id.modifierproduit);
        CodeBarre = (TextView) findViewById(R.id.CodeBarre);
        AddCodeBarre = (RelativeLayout) findViewById(R.id.AddCodeBarre);

        selectedProduct = getIntent().getParcelableExtra("product");
        populateFieldsWithSelectedProductData();

        produitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                intent1.setType("image/*");
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(
                        intent1, getString(R.string.select_picture)), RESULT_LOAD_IMAGE);
            }
        });

        enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedProduct.setLibelle(nomProduit.getText().toString());
                selectedProduct.setPrixHT(Double.parseDouble(PrixHt.getText().toString()));
                selectedProduct.setPrixTTC(Double.parseDouble(PrixTtc.getText().toString()));
                selectedProduct.setCodeBarre(CodeBarre.getText().toString());
                selectedProduct.setUrl(PhpAPI.editproduit);
                updateProduct();
            }
        });

        AddCodeBarre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(UpdateProductActivity.this);
                scanIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();

        mActionBar.setTitle(getString(R.string.update_product));
        mActionBar.disableAddButton();
    }

    private void populateFieldsWithSelectedProductData() {
        Picasso.with(getApplicationContext())
                .load(selectedProduct.getPhoto())
                .into(produitImage);
        nomProduit.setText(selectedProduct.getLibelle());
        PrixHt.setText(selectedProduct.getPrixHT() + "");
        PrixTtc.setText(selectedProduct.getPrixTTC() + "");
        CodeBarre.setText(selectedProduct.getCodeBarre());
        imageCode = "";
    }

    private void updateProduct() {
        Map<String, String> data = new HashMap<>();
        data.put("ID", selectedProduct.getID() + "");
        data.put("Libelle", selectedProduct.getLibelle());
        data.put("PrixHT", selectedProduct.getPrixHT() + "");
        data.put("PrixTTC", selectedProduct.getPrixTTC() + "");
        data.put("CodeBar", selectedProduct.getCodeBarre());
        data.put("Qte", selectedProduct.getQte() + "");
        data.put("Photo", imageCode);
        data.put("Local", selectedProduct.getLocale_ID() + "");

        sendHTTPRequest(selectedProduct.getUrl(), data, Method.POST,
                new SuccessRequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UpdateProductActivity.this, R.string.product_updated,
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                        setResult(ResultCodes.PRODUCT_UPDATED);
                        finish();
                    }

                    @Override
                    public void onRequestFailed(int status, JSONObject response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UpdateProductActivity.this, R.string.unknown_error,
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();

            CodeBarre.setText(scanContent);
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    produitImage.setImageBitmap(bitmap);
                    imageCode = BitmapToString(bitmap);

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
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
