package com.example.ismailamrani.comptable.ServiceWeb;

import com.example.ismailamrani.comptable.utils.Method;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Redouane on 24/03/2016.
 */
public class PhpAPI {
    // Setup MIME type
    public static final MediaType JSON =
            MediaType.parse("application/json; charset=utf-8");
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

    /**
     * @param param  the request's body that contains the request's params to the server
     * @param url    target
     * @param method HTTP method (POST/GET)
     * @return a fully setup request
     */
    public static Request createHTTPRequest(JSONObject param, String url, Method method) {
        if (param == null)
            param = new JSONObject();

        return method == Method.POST ? new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, param.toString()))
                .build() :
                new Request.Builder()
                        .url(url + buildGETRequestParams(param))
                        .build();
    }

    /**
     * @param params JSONObject with the key/value params
     * @return ?key=value&
     */
    public static String buildGETRequestParams(JSONObject params) {
        StringBuilder sb = new StringBuilder("?");

        Iterator<String> keys = params.keys();
        while (keys.hasNext()) {
            try {
                String key = keys.next();
                sb.append(key)
                        .append("=")
                        .append(params.get(key));

                if (keys.hasNext())
                    sb.append("&");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
