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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.barcodescanner.IntentIntegrator;
import com.example.ismailamrani.comptable.barcodescanner.IntentResult;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBarInterface;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.ui.base.WithDrawerActivity;
import com.example.ismailamrani.comptable.utils.DialogUtil;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.RequestListener;
import com.example.ismailamrani.comptable.utils.ResultCodes;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ismail Amrani on 23/03/2016.
 */
public class AddProductActivity extends WithDrawerActivity
        implements OGActionBarInterface {
    private static int RESULT_LOAD_IMAGE = 1;

    private String selectedImagePath;
    private String codeimage = "";

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

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);
        ButterKnife.bind(this);

        setupActionBar();
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();

        mActionBar.setTitle("Ajouter Un Produit");
        mActionBar.disableAddButton();
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
                Product newProduct = validateProductInfos();
                if (newProduct != null) {
                    postAddProduct(PhpAPI.addProduit, newProduct.toJSON());
                }

                break;
        }
    }

    /**
     * Validates the new product's informations before adding it.
     *
     * @return a Product object representing the new product if everything
     * goes right, or null otherwise.
     */
    private Product validateProductInfos() {
        String dialogTitle;
        String dialogMessage;

        String name = productName.getText().toString();
        String ht = priceHT.getText().toString();
        String ttc = priceTTC.getText().toString();
        String barcode = barCodeLabel.getText().toString();

        boolean imageStatus = codeimage.length() != 0;
        boolean nameStatus = name.length() != 0;
        boolean htStatus = ht.length() != 0;
        boolean ttcStatus = ttc.length() != 0;
        boolean barcodeStatus = barcode.length() != 0;

        if (imageStatus && nameStatus && htStatus && ttcStatus && barcodeStatus) {
            return new Product(0, name, Double.parseDouble(ht),
                    Double.parseDouble(ttc), barcode, codeimage,
                    0, 3, PhpAPI.addProduit);
        } else if (!imageStatus) {
            // No image was selected for the product.
            dialogTitle = "Missing image.";
            dialogMessage = "You need to provide an image for your product.";
        } else if (!nameStatus) {
            // No name was specified for the product.
            dialogTitle = "Missing product name.";
            dialogMessage = "You need to provide a name for your product.";
        } else if (!htStatus) {
            // No HT price was specified for the product.
            dialogTitle = "Missing HT price.";
            dialogMessage = "You need to specify an HT price for your product.";
        } else if (!ttcStatus) {
            // No TTC price was specified for the product.
            dialogTitle = "Missing TTC price.";
            dialogMessage = "You need to specify an TTC price for your product.";
        } else {
            // No bar code was specified for the product.
            dialogTitle = "Missing bar code.";
            dialogMessage = "You need to specify your product's bar code.";
        }

        // Something went wrong: show the error dialog.
        DialogUtil.showDialog(this, dialogTitle, dialogMessage, "OK", null);

        return null;
    }

    void postAddProduct(String url, JSONObject data) {
        sendHTTPRequest(url, data, Method.POST,
                new RequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response, int status) {
                        if (status == 1) {
                            setResult(ResultCodes.PRODUCT_ADDED);
                            finish();

                            runOnUiThread(() ->
                                    Toast.makeText(getApplicationContext(),
                                            "Product successfully added.",
                                            Toast.LENGTH_LONG).show());
                        } else if (status == 0) {
                            runOnUiThread(() ->
                                    Toast.makeText(getApplicationContext(),
                                            "erreur  !!!!", Toast.LENGTH_LONG).show());
                        }
                    }

                    @Override
                    public void onRequestFailed() {
                        runOnUiThread(() -> Toast.makeText(AddProductActivity.this,
                                "Unknown error.", Toast.LENGTH_LONG).show());
                    }
                });
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
}
