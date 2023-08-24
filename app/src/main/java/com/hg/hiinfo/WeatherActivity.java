package com.hg.hiinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class WeatherActivity extends AppCompatActivity {

    String weatherString ;
    WeatherData myWeatherData ;
    Handler handler ;
    TextView tv_day0, tv_dayweather_0 ;
    TextView tv_day1, tv_dayweather_1 ;
    TextView tv_day2, tv_dayweather_2 ;
    ImageView iv_icon_0, iv_icon_1, iv_icon_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        tv_day0 = findViewById(R.id.tv_day0);
        tv_dayweather_0 = findViewById(R.id.tv_dayweather_0);
        iv_icon_0 = findViewById(R.id.iv_icon_0);
        iv_icon_1 = findViewById(R.id.iv_icon_1);
        tv_day1 = findViewById(R.id.tv_day1);
        tv_dayweather_1 = findViewById(R.id.tv_dayweather_1);
        iv_icon_2 = findViewById(R.id.iv_icon_2);
        tv_day2 = findViewById(R.id.tv_day2);
        tv_dayweather_2 = findViewById(R.id.tv_dayweather_2);
        Intent intent = getIntent();
        weatherString = intent.getStringExtra("WeatherData");
        myWeatherData = new WeatherData(weatherString) ;
        handler = new Handler (Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                try {
                    String daystring = String.format("Min: %3.0f ℉   \t\t\t\tMax: %3.0f ℉\nWind: %3.0f MPH\t\tDir : %3.0f Deg",
                            myWeatherData.dData[0].min, myWeatherData.dData[0].max,
                            myWeatherData.dData[0].wind_speed, myWeatherData.dData[0].wind_dir);
                    daystring+= String.format("\nSunrise : %s\t\tSunset : %s", myWeatherData.dData[0].sunrise, myWeatherData.dData[0].sunset);

                    tv_day0.setText(myWeatherData.dData[0].datestring);
                    tv_dayweather_0.setText(daystring);
                    Picasso.with(WeatherActivity.this).load(myWeatherData.dData[0].iconURL).into(iv_icon_0);

                    daystring = String.format("Min: %3.0f ℉   \t\t\t\tMax: %3.0f ℉\nWind: %3.0f MPH\t\tDir : %3.0f Deg",
                            myWeatherData.dData[1].min, myWeatherData.dData[1].max,
                            myWeatherData.dData[1].wind_speed, myWeatherData.dData[1].wind_dir);
                    daystring+= String.format("\nSunrise : %s\t\tSunset : %s", myWeatherData.dData[1].sunrise, myWeatherData.dData[1].sunset);



                    tv_day1.setText(myWeatherData.dData[1].datestring);
                    tv_dayweather_1.setText(daystring);
                    Picasso.with(WeatherActivity.this).load(myWeatherData.dData[1].iconURL).into(iv_icon_1);


                    daystring = String.format("Min: %3.0f ℉   \t\t\t\tMax: %3.0f ℉\nWind: %3.0f MPH\t\tDir : %3.0f Deg",
                            myWeatherData.dData[2].min, myWeatherData.dData[2].max,
                            myWeatherData.dData[2].wind_speed, myWeatherData.dData[2].wind_dir);
                    daystring+= String.format("\nSunrise : %s\t\tSunset : %s", myWeatherData.dData[2].sunrise, myWeatherData.dData[2].sunset);


                    tv_day2.setText(myWeatherData.dData[2].datestring);
                    tv_dayweather_2.setText(daystring);
                    Picasso.with(WeatherActivity.this).load(myWeatherData.dData[2].iconURL).into(iv_icon_2);

                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        };
        myWeatherData.setHandler (handler) ;
        Thread thread = new Thread(myWeatherData) ;
        thread.start();
        System.out.println(weatherString);
        myWeatherData.parseDaily ();


    }
}