package com.example.ismailamrani.comptable.LocalData.ToDelete;

import com.example.ismailamrani.comptable.Models.ProduitModel;

import java.util.ArrayList;

/**
 * Created by Ismail Amrani on 23/03/2016.
 */
public class GetProduit {

    public ArrayList<ProduitModel> GetProduit(){

        ArrayList<ProduitModel> List = new ArrayList<>();

        ProduitModel m = new ProduitModel();
        m.setID(1);
        m.setLibelle("Iphone 6 Plus");
        m.setQte(125);
        m.setPrixTTC(7000.00);
        m.setPhoto("http://idowny.com/comptable/photo/iphone.png");

        List.add(m);

        ProduitModel m2 = new ProduitModel();
        m2.setID(1);
        m2.setLibelle("Samsung Galaxy S6");
        m2.setQte(85);
        m2.setPrixTTC(6500.00);
        m2.setPhoto("http://idowny.com/comptable/photo/s6.png");

        List.add(m2);

        return List;

    }

}
