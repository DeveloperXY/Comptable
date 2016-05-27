package com.example.ismailamrani.comptable.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Mohammed Aouf ZOUAG on 27/04/2016.
 * <p>
 * Represents a user in possession of a user name & a password.
 */
public class User {
    private int id;
    private String firstname;
    private String lastname;
    private String type;
    private Date creationDate;
    private Date expirationDate;
    private String username;
    private String password;
    private int companyID;
    private int localeID;
    private String address;
    private String city;
    private String country;
    private String telephone;

    public static class Builder {
        private int id;
        private String firstname;
        private String lastname;
        private String type;
        private Date creationDate;
        private Date expirationDate;
        private String username;
        private String password;
        private int companyID;
        private int localeID;
        private String address;
        private String city;
        private String country;
        private String telephone;

        public User createUser() {
            return new User(this);
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder creationDate(Date creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public Builder expirationDate(Date expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder companyID(int companyID) {
            this.companyID = companyID;
            return this;
        }

        public Builder localeID(int localeID) {
            this.localeID = localeID;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder telephone(String telephone) {
            this.telephone = telephone;
            return this;
        }
    }

    public User(Builder builder) {
        this.id = builder.id;
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.type = builder.type;
        this.creationDate = builder.creationDate;
        this.expirationDate = builder.expirationDate;
        this.username = builder.username;
        this.password = builder.password;
        this.companyID = builder.companyID;
        this.localeID = builder.localeID;
        this.address = builder.address;
        this.city = builder.city;
        this.country = builder.country;
        this.telephone = builder.telephone;
    }

    public User(User user) {
        this.id = user.id;
        this.firstname = user.firstname;
        this.lastname = user.lastname;
        this.type = user.type;
        this.creationDate = user.creationDate;
        this.expirationDate = user.expirationDate;
        this.username = user.username;
        this.password = user.password;
        this.companyID = user.companyID;
        this.localeID = user.localeID;
        this.address = user.address;
        this.city = user.city;
        this.country = user.country;
        this.telephone = user.telephone;
    }

    /**
     * Overloaded constructor.
     * Builds a new instance whose informations are extracted from the passed-in
     * JSON object.
     *
     * @param user parameter
     */
    public User(JSONObject user) {
        try {
            this.id = user.getInt("iduser");
            this.firstname = user.getString("prenom");
            this.lastname = user.getString("nom");
            this.type = user.getString("type");
            this.username = user.getString("Username");
            this.password = user.getString("Password");
            this.companyID = user.getInt("SocieteID");
            this.localeID = user.getInt("LocaleID");
            this.address = user.getString("Adresse");
            this.city = user.getString("Ville");
            this.country = user.getString("Pays");
            this.telephone = user.getString("Tel");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public int getLocaleID() {
        return localeID;
    }

    public void setLocaleID(int localeID) {
        this.localeID = localeID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * @return a JSON object representing this user object.
     */
    public JSONObject toJSON() {
        JSONObject object = new JSONObject();
        try {
            object.put("Username", username);
            object.put("Password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", type='" + type + '\'' +
                ", creationDate=" + creationDate +
                ", expirationDate=" + expirationDate +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", companyID=" + companyID +
                ", localeID=" + localeID +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
