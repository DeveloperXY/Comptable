package com.example.ismailamrani.comptable.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.ismailamrani.comptable.utils.http.PhpAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Redouane on 24/03/2016.
 */
public class Client implements Parcelable {

    String id;
    String nomPrenom;
    String tel;
    String adresse;
    String url;
    String image;
    String email;

    public Client() {

    }

    public Client(String id, String nomPrenom, String tel, String adresse,
                  String url, String image, String email) {
        this.id = id;
        this.nomPrenom = nomPrenom;
        this.tel = tel;
        this.adresse = adresse;
        this.url = url;
        this.image = image;
        this.email = email;
    }

    public Client(JSONObject object) throws JSONException {
        this(
                object.getString("idclient"),
                object.getString("nom"),
                object.getString("tel"),
                object.getString("adresse"),
                "",
                PhpAPI.IpBackend + "clients/client.png",
                object.getString("email")
        );
    }

    private Client(Parcel in) {
        id = in.readString();
        nomPrenom = in.readString();
        tel = in.readString();
        adresse = in.readString();
        url = in.readString();
        image = in.readString();
        email = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nomPrenom);
        dest.writeString(tel);
        dest.writeString(adresse);
        dest.writeString(url);
        dest.writeString(image);
        dest.writeString(email);
    }

    public static final Parcelable.Creator<Client> CREATOR = new Parcelable.Creator<Client>() {

        @Override
        public Client createFromParcel(Parcel source) {
            return new Client(source);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (id != null ? !id.equals(client.id) : client.id != null) return false;
        if (nomPrenom != null ? !nomPrenom.equals(client.nomPrenom) : client.nomPrenom != null)
            return false;
        if (tel != null ? !tel.equals(client.tel) : client.tel != null) return false;
        if (adresse != null ? !adresse.equals(client.adresse) : client.adresse != null)
            return false;
        if (url != null ? !url.equals(client.url) : client.url != null) return false;
        if (image != null ? !image.equals(client.image) : client.image != null) return false;
        return email != null ? email.equals(client.email) : client.email == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nomPrenom != null ? nomPrenom.hashCode() : 0);
        result = 31 * result + (tel != null ? tel.hashCode() : 0);
        result = 31 * result + (adresse != null ? adresse.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    public static List<Client> parseClients(JSONArray array) {
        List<Client> clients = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject client = array.getJSONObject(i);
                clients.add(new Client(client));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return clients;
    }
}
