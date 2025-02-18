package com.hg.hiinfo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class MyWeather implements Runnable {
    String myApiKey = BuildConfig.OW_APIKEY;
    String fullURL = "https://api.open-meteo.com/v1/forecast?wind_speed_unit=mph&timezone=auto&weather_code,precipitation_unit=inch&temperature_unit=fahrenheit&latitude=21.31&longitude=-157.858&current=precipitation,temperature_2m,relative_humidity_2m,cloud_cover,wind_speed_10m,wind_direction_10m&daily=weather_code,temperature_2m_max,temperature_2m_min,sunrise,sunset,precipitation_sum,wind_speed_10m_max,wind_direction_10m_dominant&timeformat=unixtime";
    // String fullURL = "https://api.open-meteo.com/v1/forecast?wind_speed_unit=mph&timezone=auto&weather_code,precipitation_unit=inch&temperature_unit=fahrenheit&latitude=21.31&longitude=-157.858&current=precipitation,temperature_2m,relative_humidity_2m,cloud_cover,wind_speed_10m,wind_direction_10m&daily=weather_code,temperature_2m_max,temperature_2m_min,sunrise,sunset,precipitation_sum,wind_speed_10m_max,wind_direction_10m_dominant";
    double lat, lon;


    public class DailyData {
        double min, max, wind_speed, wind_dir ;
        public DailyData(float minT, float maxT, float windSp, float windDir){
            min = minT ;
            max = maxT ;
            wind_dir = windDir ;
            wind_speed = windSp ;

        }

    }
    private Handler handler ;
    private DailyData [] dailyData ;
    public MyWeather(Handler h) {
        lat = 21.31;
        lon = -157.86;
        handler = h ;
    }

    // if string has already been received, used for parsing etc
    public MyWeather() {
        lat = 21.31;
        lon = -157.86;
        // fullURL = "";
        dailyData = new DailyData[4];
    }


    public void makeURL() {
//        fullURL = String.format(
//                "https://api.openweathermap.org/data/2.5/onecall?lat=%f&lon=%f&units=imperial&exclude=hourly,minutely&appid=%s",
//                lat, lon, myApiKey);
        System.out.println(fullURL);

    }


    @Override
    public void run() {

        HttpURLConnection con = null;
        InputStream is = null;

        try {
            con = (HttpURLConnection) (new URL(fullURL)).openConnection();
            System.out.println(con.getResponseCode());
            con.setRequestMethod("GET");
            //          con.setDoOutput(true);
//            con.connect();
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            Bundle bundle = new Bundle();
            while ((line = br.readLine()) != null)
                buffer.append(line + "rn");
            is.close();
            Message msg = new Message() ;
            bundle.putString("MyWeather", buffer.toString());
            msg.setData (bundle);
            handler.sendMessage(msg);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
