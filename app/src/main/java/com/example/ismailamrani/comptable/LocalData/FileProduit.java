package com.example.ismailamrani.comptable.LocalData;

import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Redouane on 29/03/2016.
 */
public class FileProduit {
    String nomFile;
    public FileProduit(String nomfile) {
        this.nomFile = nomfile;
    }

    public Boolean write(String fcontent){
        try {

            //String encryptedText = encryption.encryptOrNull(fcontent);

            String fpath = "/storage/emulated/0/Comptable/" + nomFile;
            java.io.File file = new java.io.File(fpath);




            // If file does not exists, then create it
            if (!file.exists()) {

                java.io.File myDir = new java.io.File("/storage/emulated/0/","Comptable");
                myDir.mkdir();

                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fcontent);
            bw.close();

            Log.d("Suceess", "Sucess");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public String read(){

        System.out.println(nomFile);
        BufferedReader br = null;
        String response = null;

        try {

            StringBuffer output = new StringBuffer();
            String fpath = "/storage/emulated/0/Comptable/" + nomFile;

            br = new BufferedReader(new FileReader(fpath));
            String line = "";
            while ((line = br.readLine()) != null) {
                output.append(line +"\n");
            }
            response = output.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }

        //String decryptedText = encryption.decryptOrNull(response);

        return response;

    }



}
