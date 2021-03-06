package com.example.ismailamrani.comptable.ui.startup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.transition.Fade;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Local;
import com.example.ismailamrani.comptable.models.User;
import com.example.ismailamrani.comptable.ui.AccountingDetailsActivity;
import com.example.ismailamrani.comptable.ui.AccountingHomeActivity;
import com.example.ismailamrani.comptable.ui.ChargesActivity;
import com.example.ismailamrani.comptable.ui.ClientsActivity;
import com.example.ismailamrani.comptable.ui.OrdersActivity;
import com.example.ismailamrani.comptable.ui.ProductsActivity;
import com.example.ismailamrani.comptable.ui.StockActivity;
import com.example.ismailamrani.comptable.ui.SuppliersActivity;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.ui.dialogs.LocalChooserDialog;
import com.example.ismailamrani.comptable.ui.dialogs.base.ChooserDialog;
import com.example.ismailamrani.comptable.utils.parsing.Orders;
import com.example.ismailamrani.comptable.utils.ui.ActivityTransition;
import com.example.ismailamrani.comptable.utils.ui.CalculateScreenSize;
import com.example.ismailamrani.comptable.utils.ui.DialogUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ismail Amrani on 17/03/2016.
 * Altered by Mohammed Aouf ZOUAG on 01/05/2016.
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CalculateScreenSize().CalculateScreenSize(this);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setupWindowAnimations();
        setupLocale();

        if (mDatabaseAdapter.getUserType().startsWith("e"))
            logoutImage.setVisibility(View.VISIBLE);
        else
            settingsImage.setVisibility(View.VISIBLE);
    }

    private void setupLocale() {
        updateCurrentLocaleLabel();

        int currentLocaleID = mDatabaseAdapter.getCurrentLocaleID();
        Log.i("LOCALE", "Current locale ID is: " + currentLocaleID);
    }

    private void updateCurrentLocaleLabel() {
        localeLabel.setText(mDatabaseAdapter.getUserAddress());
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
            R.id.ventes, R.id.achat, R.id.fournis, R.id.comptabilite})
    public void OnClick(View view) {
        Class<?> targetActivity;
        @IdRes int clickedImageID; // the ID of the clicked ImageView
        @DrawableRes int clickedImageResID; // The resource ID of the clicked image
        String orderType = "";
        String userType = mDatabaseAdapter.getUserType();

        switch (view.getId()) {
            case R.id.produit:
                targetActivity = ProductsActivity.class;
                clickedImageID = R.id.productsMenuImage;
                clickedImageResID = R.mipmap.ic_produit;
                break;
            case R.id.client:
                targetActivity = ClientsActivity.class;
                clickedImageID = R.id.clientsMenuImage;
                clickedImageResID = R.mipmap.ic_client;
                break;
            case R.id.chargee:
                targetActivity = ChargesActivity.class;
                clickedImageID = R.id.chargesMenuImage;
                clickedImageResID = R.mipmap.ic_charge;
                break;
            case R.id.fournis:
                targetActivity = SuppliersActivity.class;
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
            case R.id.comptabilite:
                targetActivity = userType.startsWith("e") ?
                        AccountingDetailsActivity.class : AccountingHomeActivity.class;
                clickedImageID = R.id.comptabiliteMenuImage;
                clickedImageResID = R.mipmap.ic_comp;
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
        DialogUtil.showMutliDialog(this, getString(R.string.question_log_out),
                getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDatabaseAdapter.logout();

                        activityShouldFinish();
                        ActivityTransition.startActivityWithSharedElement(
                                HomeActivity.this, LoginActivity.class, headerImageView, "header");
                    }
                },
                getString(R.string.no), null);
    }

    public void onSettingsPressed(final View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_change_locale:
                        showLocaleChooserDialog();
                        break;
                    case R.id.action_logout:
                        onLogoutPressed(view);
                        break;
                }

                return true;
            }
        });
        popupMenu.inflate(R.menu.options_menu);
        popupMenu.show();
    }

    private void showLocaleChooserDialog() {
        List<Local> locales = mDatabaseAdapter.retrieveCurrentLocales();

        new LocalChooserDialog(this)
                .whoseItemsAre(locales)
                .whoseSearchHintIs(getString(R.string.search_locales))
                .runWhenItemSelected(new ChooserDialog.OnItemSelectionListener<Local>() {
                    @Override
                    public void onItemSelected(Local selectedLocal) {
                        if (mDatabaseAdapter.getCurrentLocaleID() != selectedLocal.getId()) {
                            final User user = mDatabaseAdapter.getLoggedUser();
                            User copy = new User(user);
                            copy.setLocaleID(selectedLocal.getId());
                            copy.setAddress(selectedLocal.getAddress());
                            copy.setCity(selectedLocal.getCity());
                            copy.setCountry(selectedLocal.getCountry());
                            copy.setTelephone(selectedLocal.getTelephone());
                            mDatabaseAdapter.updateUser(copy);

                            updateCurrentLocaleLabel();
                            Snackbar.make(getWindow().getDecorView(),
                                    R.string.locale_changed, Snackbar.LENGTH_LONG)
                                    .setAction(R.string.undo, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mDatabaseAdapter.updateUser(user);
                                            updateCurrentLocaleLabel();
                                        }
                                    })
                                    .show();
                        }
                    }
                })
                .show();
    }
}
