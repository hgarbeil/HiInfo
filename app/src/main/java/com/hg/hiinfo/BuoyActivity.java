package com.hg.hiinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

public class BuoyActivity extends AppCompatActivity {

    private MyBuoy myBuoy [];
    Handler b_handler ;
    String buoyString ;
    TextView tv_buoy_0_0, tv_buoy_0_1, tv_buoy_0_2 ;
    TextView tv_buoy_1_0, tv_buoy_1_1, tv_buoy_1_2 ;
    TextView tv_buoy_2_0, tv_buoy_2_1, tv_buoy_2_2 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buoy);
        tv_buoy_0_0 = findViewById(R.id.tv_buoy_0_0);
        tv_buoy_0_1 = findViewById(R.id.tv_buoy_0_1);
        tv_buoy_0_2 = findViewById(R.id.tv_buoy_0_2);
        tv_buoy_1_0 = findViewById(R.id.tv_buoy_1_0);
        tv_buoy_1_1 = findViewById(R.id.tv_buoy_1_1);
        tv_buoy_1_2 = findViewById(R.id.tv_buoy_1_2);
        tv_buoy_2_0 = findViewById(R.id.tv_buoy_2_0);
        tv_buoy_2_1 = findViewById(R.id.tv_buoy_2_1);
        tv_buoy_2_2 = findViewById(R.id.tv_buoy_2_2);

        myBuoy = new MyBuoy[3] ;

        Handler b_handler = new Handler (Looper.getMainLooper()) {

            @Override
            public void handleMessage (Message msg) {
                System.out.println("in here again");
                Bundle bundle = msg.getData();
                buoyString = bundle.getString("MyBuoy");
                BuoyData buoyData = new BuoyData(buoyString) ;
                updateBuoy(buoyData);


//                myWeatherData = new WeatherData(weatherString);
//                System.out.println(weatherString);
            }
        };

        for (int i=0; i<3; i++) {
            myBuoy[i] = new MyBuoy(b_handler);
            myBuoy[i].setStationIndex(i);
            myBuoy[i].makeURL();
            Thread t_buoy = new Thread(myBuoy[i]);
            t_buoy.start();
        }





    }

    void updateBuoy (BuoyData buoyData){

        int index = buoyData.stationNum ;
        String outstring ="";
        //tv_buoy_0.setText ("Station : "+myBuoy.stationName);
        for (int i = 0; i < 3; i++) {
//                outstring += String.format("%3.1f\t\t%3.0f\t%3.0f\t%5.0f\n",
//                        buoyData.height[i], buoyData.domPeriod[i], buoyData.avgPeriod[i], buoyData[i].dir);
            outstring += String.format("%s\t\t%3.1f\t%3.0f\t%3.0f\t%4.0f\n", buoyData.timeString[i],
                    buoyData.height[i],
                    buoyData.domPeriod[i], buoyData.avgPeriod[i], buoyData.dir[i]);
        }
        if (index ==0) {
            tv_buoy_0_0.setText(String.format("Station Name: %s", buoyData.stationNames[index]));
            tv_buoy_0_1.setText(outstring);

        }
        else if (index==1){
            tv_buoy_1_0.setText(String.format("Station Name: %s", buoyData.stationNames[index]));
            tv_buoy_1_1.setText(outstring);

        }
        else{
            tv_buoy_2_0.setText(String.format("Station Name: %s", buoyData.stationNames[index]));
            tv_buoy_2_1.setText(outstring);

        }


    }
}