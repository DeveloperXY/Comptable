package com.example.ismailamrani.comptable.localdata.ToDelete;

import com.example.ismailamrani.comptable.models.Product;

import java.util.ArrayList;

/**
 * Created by Ismail Amrani on 23/03/2016.
 */
public class GetProduit {

    public ArrayList<Product> GetProduit() {

        ArrayList<Product> List = new ArrayList<>();

        Product m = new Product();
        m.setID(1);
        m.setLibelle("Iphone 6 Plus");
        m.setQte(125);
        m.setPrixTTC(7000.00);
        m.setPhoto("http://idowny.com/comptable/photo/iphone.png");

        List.add(m);

        Product m2 = new Product();
        m2.setID(1);
        m2.setLibelle("Samsung Galaxy S6");
        m2.setQte(85);
        m2.setPrixTTC(6500.00);
        m2.setPhoto("http://idowny.com/comptable/photo/s6.png");

        List.add(m2);

        return List;

    }

}
