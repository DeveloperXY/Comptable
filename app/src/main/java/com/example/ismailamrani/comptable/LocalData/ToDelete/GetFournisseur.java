package com.example.ismailamrani.comptable.LocalData.ToDelete;

import com.example.ismailamrani.comptable.Models.ClientModel;
import com.example.ismailamrani.comptable.Models.Fournisseur;

import java.util.ArrayList;

/**
 * Created by Redouane on 08/04/2016.
 */
public class GetFournisseur {

    public ArrayList<Fournisseur> GetFournisseur(){

        ArrayList<Fournisseur> List = new ArrayList<>();

        Fournisseur m = new Fournisseur();
        m.setId("1");
        m.setNom("redouane azzouzi");
        m.setAdresse("Fes,21AV");
        m.setImage("social");
        List.add(m);

        Fournisseur m2 = new Fournisseur();
        m2.setId("2");
        m2.setNom("Shell");
        m2.setAdresse("Rabat,412-New Castel");
        List.add(m2);

        Fournisseur m3 = new Fournisseur();
        m3.setId("2");
        m3.setNom("OctaGen");
        m3.setAdresse("New Yorck,412-Wall Street");
        List.add(m3);

        return List;

    }
}
