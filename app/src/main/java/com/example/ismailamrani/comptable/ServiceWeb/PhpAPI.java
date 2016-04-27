package com.example.ismailamrani.comptable.ServiceWeb;

/**
 * Created by Redouane on 24/03/2016.
 */
public class PhpAPI {

    public static String IpBackend = "http://octagen-it.com/comptable/";

    public static final String login = IpBackend + "login.php";

    public static final String getProduit = IpBackend + "getProduit.php";
    public static final String addProduit = IpBackend + "addProduit.php";
    public static final String removeProduit = IpBackend + "removeProduit.php";
    public static final String getProduitById = IpBackend + "getProduitByID.php";
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

    public static final String addccharge = IpBackend + "addCharge.php";
    public static final String addlocal = IpBackend + "addLocal.php";
    public static final String addSociete = IpBackend + "addSociete.php";
}
