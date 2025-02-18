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
    String sunrise, sunset ;
    double cur_temp, cur_wind, cur_windDir ;
    double moonphase ;
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
        double min, max, wind_speed, wind_dir, moonphase ;
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
            JSONObject jdaily = jobj.getJSONObject("daily");
            // JSONObject today = jdaily.getJSONObject(0);
            cur_wind = jcurr.getDouble("wind_speed_10m");
            cur_windDir = jcurr.getDouble ("wind_direction_10m");
            cur_temp = jcurr.getDouble("temperature_2m");
            JSONArray sunr = jdaily.getJSONArray("sunrise");
            System.out.println(sunr.get(0));
            JSONArray suns = jdaily.getJSONArray("sunset");
            sunrise = MyDate.dateToTimeString(new Date(sunr.getLong(0)*1000L));
            sunset = MyDate.dateToTimeString(new Date(suns.getLong(0)*1000L)); ;


//            moonphase = today.getDouble("moon_phase") ;
//            JSONArray jarr = jcurr.getJSONArray("weather");
//            JSONObject weather = jarr.getJSONObject(0);
//            cur_desc = weather.getString("description");
//            cur_icon = String.format ("http://openweathermap.org/img/w/%s.png", weather.getString("icon"));



        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public  void parseDaily (){
        try {
            JSONObject jobj = new JSONObject(jsonString);
            JSONObject jdaily = jobj.getJSONObject("daily");
            JSONArray temps_mx = jdaily.getJSONArray("temperature_2m_max");
            JSONArray temps_min = jdaily.getJSONArray("temperature_2m_min");
            JSONArray windspd = jdaily.getJSONArray("wind_speed_10m_max");
            JSONArray winddir = jdaily.getJSONArray("wind_direction_10m_dominant");
            JSONArray timearr = jdaily.getJSONArray("time");
            JSONArray sunr = jdaily.getJSONArray("sunrise");
            JSONArray suns = jdaily.getJSONArray("sunset");

        dData = new DailyData [ndays] ;
        for (int iday=0; iday<ndays; iday++) {
            dData[iday]= new DailyData() ;
            try {

                //dData[iday].date = timearr.get(iday).toString() ;
                dData[iday].date = new Date(timearr.getLong(iday)*1000);
                dData[iday].datestring = MyDate.dateToDayString(new Date(timearr.getLong(iday)*1000L));
                // dData[iday].datestring = timearr.get(iday).toString() ;
                dData[iday].sunrise = MyDate.dateToTimeString(new Date(sunr.getLong(iday)*1000L));
                dData[iday].sunset = MyDate.dateToTimeString(new Date(suns.getLong(iday)*1000L)); ;
                dData[iday].min = temps_min.getDouble(iday) ;
                dData[iday].max = temps_mx.getDouble(iday) ;
                dData[iday].wind_dir = winddir.getDouble(iday);
                dData[iday].wind_speed = windspd.getDouble(iday) ;
//                JSONArray jarr = today.getJSONArray("weather");
//                JSONObject weather = jarr.getJSONObject(0);
//                dData[iday].iconURL = String.format ("http://openweathermap.org/img/w/%s.png", weather.getString("icon"));

                //dData[iday].weather_desc = weather.getString("main");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        } catch (Exception e ){
            e.printStackTrace();
        }

    }
}
