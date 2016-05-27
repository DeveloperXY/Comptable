package com.example.ismailamrani.comptable.ui.startup;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Local;
import com.example.ismailamrani.comptable.sqlite.DatabaseAdapter;
import com.example.ismailamrani.comptable.ui.ChargesActivity;
import com.example.ismailamrani.comptable.ui.ClientListActivity;
import com.example.ismailamrani.comptable.ui.FournisseurListActivity;
import com.example.ismailamrani.comptable.ui.OrdersActivity;
import com.example.ismailamrani.comptable.ui.ProductsActivity;
import com.example.ismailamrani.comptable.ui.StockActivity;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.ui.dialogs.LocalChooserDialog;
import com.example.ismailamrani.comptable.utils.ActivityTransition;
import com.example.ismailamrani.comptable.utils.CalculateScreenSize;
import com.example.ismailamrani.comptable.utils.Orders;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ismail Amrani on 17/03/2016.
 */
public class HomeActivity extends ColoredStatusBarActivity {

    @Bind(R.id.localeLabel)
    TextView localeLabel;
    @Bind(R.id.headerImageView)
    ImageView headerImageView;
    @Bind(R.id.logoutImage)
    ImageView logoutImage;
    @Bind(R.id.settingsImage)
    ImageView settingsImage;

    private DatabaseAdapter mDatabaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CalculateScreenSize().CalculateScreenSize(this);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        mDatabaseAdapter = DatabaseAdapter.getInstance(this);

        setupWindowAnimations();
        setupLocale();

        if (mDatabaseAdapter.getUserType().startsWith("e"))
            logoutImage.setVisibility(View.VISIBLE);
        else
            settingsImage.setVisibility(View.VISIBLE);
    }

    private void setupLocale() {
        String currentLocale = mDatabaseAdapter.getUserAddress();
        int currentLocaleID = mDatabaseAdapter.getCurrentLocaleID();
        Log.i("LOCALE", "ID: " + currentLocaleID);
        localeLabel.setText(currentLocale);
    }

    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.setDuration(1000);
            getWindow().setEnterTransition(fade);
        }
    }

    /**
     * Starts a new activity based on the clicked-upon layout.
     *
     * @param view that was clicked.
     */
    @OnClick({R.id.produit, R.id.client, R.id.chargee, R.id.stock,
            R.id.ventes, R.id.achat, R.id.fournis})
    public void OnClick(View view) {
        Class<?> targetActivity;
        @IdRes int clickedImageID; // the ID of the clicked ImageView
        @DrawableRes int clickedImageResID; // The resource ID of the clicked image
        String orderType = "";

        switch (view.getId()) {
            case R.id.produit:
                targetActivity = ProductsActivity.class;
                clickedImageID = R.id.productsMenuImage;
                clickedImageResID = R.mipmap.ic_produit;
                break;
            case R.id.client:
                targetActivity = ClientListActivity.class;
                clickedImageID = R.id.clientsMenuImage;
                clickedImageResID = R.mipmap.ic_client;
                break;
            case R.id.chargee:
                targetActivity = ChargesActivity.class;
                clickedImageID = R.id.chargesMenuImage;
                clickedImageResID = R.mipmap.ic_charge;
                break;
            case R.id.fournis:
                targetActivity = FournisseurListActivity.class;
                clickedImageID = R.id.supplierMenuImage;
                clickedImageResID = R.mipmap.ic_fournisseur;
                break;
            case R.id.stock:
                targetActivity = StockActivity.class;
                clickedImageID = R.id.stockMenuImage;
                clickedImageResID = R.mipmap.ic_stock;
                break;
            case R.id.achat:
                targetActivity = OrdersActivity.class;
                clickedImageID = R.id.purchasesMenuImage;
                clickedImageResID = R.mipmap.ic_buy;
                orderType = Orders.PURCHASE;
                break;
            case R.id.ventes:
                targetActivity = OrdersActivity.class;
                clickedImageID = R.id.salesMenuImage;
                clickedImageResID = R.mipmap.ic_sell;
                orderType = Orders.SALE;
                break;
            default:
                targetActivity = null;
                clickedImageID = -1;
                clickedImageResID = -1;
        }

        View clickedImage = findViewById(clickedImageID);

        Intent intent = new Intent(this, targetActivity);
        intent.putExtra("imageRes", clickedImageResID);
        if (targetActivity == OrdersActivity.class)
            intent.putExtra("orderType", orderType);

        ActivityTransition.startActivityWithSharedElement(this, intent,
                clickedImage, "menuAnim");
    }

    public void onLogoutPressed(View view) {
        mDatabaseAdapter.logout();

        activityShouldFinish();
        ActivityTransition.startActivityWithSharedElement(this, LoginActivity.class,
                headerImageView, "header");
    }

    public void onSettingsPressed(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_change_locale:
                    showLocaleChooserDialog();
                    break;
                case R.id.action_logout:
                    onLogoutPressed(view);
                    break;
            }

            return true;
        });
        popupMenu.inflate(R.menu.options_menu);
        popupMenu.show();
    }

    private void showLocaleChooserDialog() {
        List<Local> locales = mDatabaseAdapter.retrieveCurrentLocales();
        List<String> stringLocales = Stream.of(locales)
                .map(Local::getAddress)
                .collect(Collectors.toList());

        new LocalChooserDialog(this)
                .whoseItemsAre(stringLocales)
                .whoseSearchHintIs("Search for locales...")
                .runWhenItemSelected(item -> {
                    Toast.makeText(HomeActivity.this, item, Toast.LENGTH_SHORT).show();
                })
                .show();
    }
}
