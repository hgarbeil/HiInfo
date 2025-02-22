package com.hg.hiinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler ;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private String weatherString, tideString, buoyString, surfString ;
    private MyWeather myWeather ;
    private MyBuoy myBuoy ;
    private MyTide tdn ;
    private MySurf mySurf ;
    private WeatherData myWeatherData ;
    private ImageView weather_icon ;
    private TextView tv_astro_0, tv_astro_1 ;
    private TextView tv_temp, tv_weather, tv_tide_0, tv_tide_1 ;
    private TextView tv_buoy_0, tv_buoy_1, tv_surf_0 ;
    private CardView card_weather, card_tide, card_buoy ;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm") ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weather_icon = findViewById(R.id.weather_icon) ;
        tv_temp = findViewById(R.id.tv_temp);
        tv_weather = findViewById(R.id.tv_weather);
        tv_astro_0 = findViewById(R.id.tv_astro) ;
        tv_astro_1 = findViewById(R.id.tv_astro1);
        tv_tide_0 = findViewById(R.id.tv_tide_0);
        tv_tide_1 = findViewById(R.id.tv_tide_1);
        tv_buoy_0 = findViewById(R.id.tv_buoy_0);
        tv_buoy_1 = findViewById(R.id.tv_buoy_1);
        tv_surf_0 = findViewById(R.id.tv_surf_0);
        card_weather = findViewById(R.id.card_weather);
        card_tide = findViewById(R.id.card_tide);
        card_buoy = findViewById(R.id.card_buoy);
        weatherString ="" ;
        tideString="" ;


        Handler s_handler = new Handler (Looper.getMainLooper()) {

            @Override
            public void handleMessage (Message msg) {
                Bundle bundle = msg.getData();
                surfString = bundle.getString("MySurf");
                tv_surf_0.setText(surfString);
            }
        };

        mySurf = new MySurf(s_handler) ;
        Thread t_surf = new Thread (mySurf) ;
        t_surf.start();


        Handler b_handler = new Handler (Looper.getMainLooper()) {

            @Override
            public void handleMessage (Message msg) {
                Bundle bundle = msg.getData();
                buoyString = bundle.getString("MyBuoy");
                BuoyData buoyData = new BuoyData(buoyString) ;
                updateBuoy(buoyData);


//                myWeatherData = new WeatherData(weatherString);
//                System.out.println(weatherString);
            }
        };
        Handler w_handler = new Handler (Looper.getMainLooper()){
            @Override
            public void handleMessage (Message msg){

                Bundle bundle = msg.getData() ;
                weatherString = bundle.getString("MyWeather") ;
                myWeatherData = new WeatherData(weatherString) ;
                System.out.println(weatherString);
                myWeatherData.parse();
                Picasso.with(MainActivity.this).load(myWeatherData.cur_icon).into(weather_icon);
                String outstring = String.format ("%d℉", Math.round(myWeatherData.cur_temp));
                tv_temp.setText(outstring);
                outstring = String.format("Wind: %dMPH\nWind Dir: %d",
                        Math.round(myWeatherData.cur_wind),Math.round(myWeatherData.cur_windDir));
                tv_weather.setText(outstring);
                outstring = String.format ("Sunrise : %s AM\nSunset : %s PM\nMoon Phase : %4.2f",
                        myWeatherData.sunrise,
                        myWeatherData.sunset,
                        myWeatherData.moonphase) ;
                tv_astro_1.setText(outstring);

                // astro
            }


        };

        myWeather = new MyWeather(w_handler);
        myWeather.makeURL() ;
        Thread t_weather = new Thread (myWeather) ;
        t_weather.start();

        myBuoy = new MyBuoy(b_handler);
        myBuoy.makeURL() ;
        Thread t_buoy = new Thread (myBuoy) ;
        t_buoy.start();

        Handler tide_handler = new Handler (Looper.getMainLooper()){
            @Override
            public void handleMessage (Message msg){

                Bundle bundle = msg.getData() ;
                tideString = bundle.getString("MyTides") ;
                TideData tideData = new TideData(tideString);
                tideData.parseHILO() ;
                updateTides (tideData);

            }


        };

        tdn = new MyTide(tide_handler);
        tdn.setInterval ("hilo");
        tdn.makeURL() ;
        Thread t_tide = new Thread (tdn) ;

        t_tide.start();

        card_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                Intent intent = new Intent(MainActivity.this, WeatherGraph.class);
                intent.putExtra ("WeatherData", weatherString);
                startActivity(intent);
            }

        });
        card_tide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                Intent intent = new Intent(MainActivity.this, TideActivity.class);
                intent.putExtra ("TideData", tideString);
                startActivity(intent);
            }

        });
        card_buoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                Intent intent = new Intent(MainActivity.this,BuoyActivity.class);
                intent.putExtra ("BuoyData", buoyString);
                startActivity(intent);
            }

        });
    }

    void updateTides (TideData tideData){
        String stationName = tdn.stationName ;
        tv_tide_0.setText("Station : "+stationName) ;
        String tidestr = new String("");
        for (int i=0; i<4; i++ ){
            tidestr += String.format("%s\t\t%4.1f\t\t%s\n",
                    tideData.timeArr[i].substring(5), tideData.values[i],
                    tideData.typeArr[i]);

        }
        tv_tide_1.setText (tidestr);


    }

    void updateBuoy (BuoyData buoyData){
        tv_buoy_0.setText ("Station : "+myBuoy.stationName);
        String outstring = "Time \tHt DPD  APD DIR\n" ;
        for (int i=0;i<3; i++){
//            outstring += String.format ("%3.1f\t\t%3.0f\t%3.0f\t%5.0f\n",
//                    buoyData.height[i], buoyData.domPeriod, buoyData.avgPeriod, buoyData.dir) ;
            outstring += String.format("%s\t\t%3.1f\t%3.0f\t%3.0f\t%4.0f\n", buoyData.timeString[i],
                    buoyData.height[i],
                    buoyData.domPeriod[i],buoyData.avgPeriod[i],buoyData.dir[i]);
        }
        tv_buoy_1.setText(outstring) ;


    }

}