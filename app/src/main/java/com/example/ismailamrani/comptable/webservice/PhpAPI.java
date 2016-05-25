package com.example.ismailamrani.comptable.webservice;

/**
 * Created by Redouane on 24/03/2016.
 */
public class PhpAPI {
    private static final String PRODUCTION_HOST = "http://idowny.com/comptable/";
    private static final String DEV_HOST = "http://192.168.137.1/comptablephpapi/";
    public static final String IpBackend = PRODUCTION_HOST;
    public static final String IpBackend_IMAGES = IpBackend + "produits/";
    public static final String login = IpBackend + "login.php";

    public static final String getStock = IpBackend + "getStock.php";
    public static final String getProduit = IpBackend + "getProduit.php";
    public static final String addProduit = IpBackend + "addProduit.php";
    public static final String removeProduit = IpBackend + "removeProduit.php";
    public static final String getProduitById = IpBackend + "getProduitByID.php";
    public static final String getProduitByBarcode = IpBackend + "getProduitByBarcode.php";
    public static final String editproduit = IpBackend + "editProduit.php";

    public static final String addClient = IpBackend + "addClient.php";
    public static final String editClient = IpBackend + "editClient.php";
    public static final String getClient = IpBackend + "getClient.php";
    public static final String getClientById = IpBackend + "getClientByID.php";
    public static final String removeClient = IpBackend + "removeClient.php";

    public static final String getFournisseur = IpBackend + "getFournisseur.php";
    public static final String getFournisseurByID = IpBackend + "getFournisseurByID.php";
    public static final String addFournisseur = IpBackend + "addFournisseur.php";
    public static final String editFournisseur = IpBackend + "editFournisseur.php";
    public static final String removeFournisseur = IpBackend + "removeFournisseur.php";

    public static final String addCharge = IpBackend + "addCharge.php";
    public static final String getChargeByLocaleID = IpBackend + "getChargeByLocaleID.php";
    public static final String addlocal = IpBackend + "addLocal.php";
    public static final String getLocal = IpBackend + "getLocal.php";
    public static final String addSociete = IpBackend + "addSociete.php";

    public static final String createSaleOrder = IpBackend + "createSaleOrder.php";
    public static final String createPurchaseOrder = IpBackend + "createPurchaseOrder.php";
    public static final String getSaleOrder = IpBackend + "getSaleOrder.php";
    public static final String getPurchaseOrder = IpBackend + "getPurchaseOrder.php";
    public static final String getPurchaseDetails = IpBackend + "getPurchaseDetails.php";
    public static final String getSaleDetails = IpBackend + "getSaleDetails.php";
    public static final String chargePurchaseOrder = IpBackend + "chargePurchaseOrder.php";
    public static final String chargeSaleOrder = IpBackend + "chargeSaleOrder.php";

    public static final String getActivationStatus = IpBackend + "getActivationStatus.php";
    public static final String activateApp = IpBackend + "activateApp.php";
    public static final String getServerTime = IpBackend + "getServerTime.php";
}
