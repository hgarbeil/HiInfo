package com.hg.hiinfo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherData implements Runnable {

    int ndays ;
    double cur_temp, cur_wind, cur_windDir ;
    double moon_phase ;
    String cur_desc;
    String cur_icon ;
    Handler handler ;

    public void setHandler(Handler h) {
        this.handler = h;
    }



    @Override
    public void run() {
        parse() ;
        parseDaily() ;
        if (handler != null){
            Message msg = new Message() ;
            Bundle bundle = new Bundle() ;
            bundle.putString("msg", "parse complete");
            msg.setData(bundle) ;
            handler.sendMessage (msg);
        }
    }

    public class DailyData {
        Date date ;
        double min, max, wind_speed, wind_dir ;
        String datestring, sunrise, sunset, weather_desc, icon, iconURL;
        public class DailyData90{

        }

    }

    DailyData [] dData ;

    String jsonString ;

    public WeatherData (String jstring){
        jsonString = jstring ;
        ndays = 5 ;
        handler = null ;

    }



    public void parse (){

        try {
            JSONObject jobj = new JSONObject(jsonString);
            JSONObject jcurr = jobj.getJSONObject("current") ;
            cur_wind = jcurr.getDouble("wind_speed");
            cur_windDir = jcurr.getDouble ("wind_deg");
            cur_temp = jcurr.getDouble("temp");

            JSONArray jarr = jcurr.getJSONArray("weather");
            JSONObject weather = jarr.getJSONObject(0);
            cur_desc = weather.getString("description");
            cur_icon = String.format ("http://openweathermap.org/img/w/%s.png", weather.getString("icon"));



        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public  void parseDaily (){
        dData = new DailyData [ndays] ;
        for (int iday=0; iday<ndays; iday++) {
            dData[iday]= new DailyData() ;
            try {
                JSONObject jobj = new JSONObject(jsonString);
                JSONArray jdaily = jobj.getJSONArray("daily");
                JSONObject today = jdaily.getJSONObject(iday);
                Long tstring = today.getLong("dt");
                dData[iday].date = new Date(tstring*1000);
                dData[iday].datestring = MyDate.dateToDayString(new Date(tstring*1000L));
                tstring = today.getLong("sunrise");
                dData[iday].sunrise = MyDate.dateToTimeString(new Date(tstring*1000L));
                tstring = today.getLong("sunset");
                dData[iday].sunset = MyDate.dateToTimeString(new Date(tstring*1000L));
                JSONObject ttemp = today.getJSONObject("temp");
                dData[iday].min = ttemp.getDouble("min");
                dData[iday].max = ttemp.getDouble("max");
                dData[iday].wind_dir = Math.round(today.getDouble("wind_deg"));
                dData[iday].wind_speed = Math.round(today.getDouble("wind_speed")) ;
                JSONArray jarr = today.getJSONArray("weather");
                JSONObject weather = jarr.getJSONObject(0);
                dData[iday].iconURL = String.format ("http://openweathermap.org/img/w/%s.png", weather.getString("icon"));

                dData[iday].weather_desc = weather.getString("main");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
