package com.example.ismailamrani.comptable.LocalData.ToDelete;

import com.example.ismailamrani.comptable.Models.ClientModel;

import java.util.ArrayList;

/**
 * Created by Redouane on 31/03/2016.
 */
public class GetClient {

    public ArrayList<ClientModel> GetClient(){

        ArrayList<ClientModel> List = new ArrayList<>();

        ClientModel m = new ClientModel();
        m.setId("1");
        m.setNomPrenom("redouane azzouzi");
        m.setAdresse("Fes,21AV");
        m.setImage("social");



        List.add(m);

        ClientModel m2 = new ClientModel();
        m2.setId("2");
        m2.setNomPrenom("Brahim saddik");
        m2.setAdresse("fes,454");

        List.add(m2);

        return List;

    }

}
