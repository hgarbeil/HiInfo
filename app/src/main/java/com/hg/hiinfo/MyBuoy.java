package com.hg.hiinfo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyBuoy implements Runnable {
    int index ;
    String fullURL ;
    Handler handler ;
    String stationIDs []=  {"51212","51202","51202"} ;
    String stationNames []= {"Barbers Point","Mokapu","Waimea"} ;
    String stationID, stationName ;
    MyBuoy (Handler h) {
        handler = h ;
        index = 0 ;
        setStationIndex(index) ;


    }

    void setStationIndex (int ind){
        index = ind ;
        stationID = stationIDs[ind];
        stationName = stationNames[ind];
    }

    void makeURL () {
        fullURL = String.format ("https://www.ndbc.noaa.gov/data/realtime2/%s.txt",
                stationID) ;
    }

    @Override
    public void run() {
        HttpURLConnection con = null;
        InputStream is = null;

        try {
            con = (HttpURLConnection) (new URL(fullURL)).openConnection();
            con.setRequestMethod("GET");
            //con.setDoInput(true);
//            con.setDoOutput(true);
//            con.setDoInput(false) ;
            con.connect();
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            Bundle bundle = new Bundle();
            while ((line = br.readLine()) != null)
                buffer.append(line + "\n");
            is.close();
            Message msg = new Message() ;
            bundle.putString("MyBuoy", buffer.toString());
            msg.setData (bundle);
            handler.sendMessage(msg);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
