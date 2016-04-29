package com.example.ismailamrani.comptable.Models;

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
    }

    /**
     * Overloaded constructor.
     * Builds a new instance whose informations are extracted from the passed-in
     * JSON object.
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
                '}';
    }
}
