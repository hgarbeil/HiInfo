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

public class MainActivity extends AppCompatActivity {

    private String weatherString, tideString, buoyString ;
    private MyWeather myWeather ;
    private MyBuoy myBuoy ;
    private MyTide tdn ;
    private WeatherData myWeatherData ;
    private ImageView weather_icon ;
    private TextView tv_temp, tv_weather, tv_tide_0, tv_tide_1 ;
    private CardView card_weather, card_tide ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weather_icon = findViewById(R.id.weather_icon) ;
        tv_temp = findViewById(R.id.tv_temp);
        tv_weather = findViewById(R.id.tv_weather);
        tv_tide_0 = findViewById(R.id.tv_tide_0);
        tv_tide_1 = findViewById(R.id.tv_tide_1);
        card_weather = findViewById(R.id.card_weather);
        card_tide = findViewById(R.id.card_tide);
        weatherString ="" ;
        tideString="" ;



        Handler b_handler = new Handler (Looper.getMainLooper()) {

            @Override
            public void handleMessage (Message msg) {
                Bundle bundle = msg.getData();
                buoyString = bundle.getString("MyWeather");
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
    }

    void updateTides (TideData tideData){
        String stationName = tdn.stationName ;
        tv_tide_0.setText("Station : "+stationName) ;
        String tidestr = new String("");
        for (int i=0; i<4; i++ ){
            tidestr += String.format("%s\t\t%4.1f\t\t%s\n",
                    tideData.timeArr[i], tideData.values[i],
                    tideData.typeArr[i]);

        }
        tv_tide_1.setText (tidestr);



    }
}