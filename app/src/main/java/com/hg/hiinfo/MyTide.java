package com.hg.hiinfo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class MyTide implements Runnable {

    String stationNames [] = {"Honolulu", "Kaneohe", "Haleiwa"} ;
    String stationIDs [] ={"1612340","1612480","1612668"};
    String[] intervals = {"h","h","hilo"} ;
    String fullURL = "https://api.tidesandcurrents.noaa.gov/api/prod/datagetter?begin_date=20250801&end_date=20250831&station=8557863&product=predictions&datum=MLLW&time_zone=lst_ldt&interval=hilo&units=english&application=DataAPI_Sample&format=xml";
    private int index ;
    public String stationName, interval, stationID ;
    private String dateString[] ;
    private Handler handler ;



    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    MyTide(Handler h) {
        handler = h ;
        index = 0 ;
        dateString = getDates(3);
        setStationIndex(index) ;


    }


    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    void setStationIndex (int index){
        if (index > 2){
            return ;
        }
        stationName = stationNames[index];
        stationID = stationIDs[index] ;
        interval = intervals[index];
        makeURL() ;
    }

    void setInterval(String interv){
        interval = interv ;
    }

    void makeURL () {
        fullURL = String.format(
                "https://api.tidesandcurrents.noaa.gov/api/prod/datagetter?begin_date=%s&end_date=%s&station=%s&product=predictions&datum=MLLW&time_zone=lst_ldt&interval=%s&units=english&application=DataAPI_Sample&format=csv",
                dateString[0], dateString[1], stationID, interval);

    }

    String [] getDates(int ndays){
        String outarr[] = new String [2];
        Date date0 = new Date() ;
        outarr[0] = sdf.format(date0);
        outarr[1] = sdf.format(date0.getTime() + 86400L * ndays * 1000L);
        return outarr ;
    }


    @Override
    public void run() {
        HttpURLConnection con = null;
        InputStream is = null;

        try {
            con = (HttpURLConnection) (new URL(fullURL)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
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
            bundle.putString("MyTides", buffer.toString());
            msg.setData (bundle);
            handler.sendMessage(msg);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
