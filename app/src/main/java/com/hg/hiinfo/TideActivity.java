package com.hg.hiinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;


import java.text.SimpleDateFormat;
import java.util.Date;

public class TideActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private Spinner spinner_tides ;
    private GraphView gv_tides ;

    private TextView tv_hilo, tv_station ;
    private int stationNumber ;
    private MyTide myTideH, myTide ;
    private Handler tide_handler, tide_handler_h ;
    private LineGraphSeries<DataPoint> maxseries ;
    private Date dayDates[], timeLabels[] ;
    SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd");
    SimpleDateFormat sdf1 = new SimpleDateFormat("EEE HH:mm") ;
    SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tide_graph);
        tv_hilo = findViewById(R.id.tv_hilo) ;
        tv_station = findViewById(R.id.tv_station) ;
        gv_tides = findViewById(R.id.gv_tides);



        // This handles hilo requests for tides
        tide_handler = new Handler (Looper.getMainLooper()){
            @Override
            public void handleMessage (Message msg){

                Bundle bundle = msg.getData() ;
                String tideString = bundle.getString("MyTides") ;
                loadHiLo(tideString) ;
            }


        };

        // This handles h (hourly) requests for tides
        tide_handler_h = new Handler (Looper.getMainLooper()){
            @Override
            public void handleMessage (Message msg){
                gv_tides.removeSeries(maxseries);
                Bundle bundle = msg.getData() ;
                String tidestring = bundle.getString("MyTides") ;
                loadH(tidestring) ;

                plotDataSeries() ;

            }


        };

        myTide = new MyTide(tide_handler) ;

        myTide.setStationIndex(0);
        myTide.setInterval ("hilo");

//        myTide.makeURL() ;
//        Thread t_tide = new Thread (myTide) ;
//
//        t_tide.start();

        spinner_tides = findViewById(R.id.spinner_tides);
        Intent intent = getIntent();
        String tidestring = intent.getStringExtra("TideData");

        ArrayAdapter ad = ArrayAdapter.createFromResource(this,
                R.array.tides_array, android.R.layout.simple_spinner_item);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tides.setAdapter(ad);
        spinner_tides.setOnItemSelectedListener(this);

        TideData tideData = new TideData(tidestring);

        tideData.setStationName("Honolulu");
        tideData.parseHILO() ;
        tv_station.setText ("Station : "+tideData.stationName);
        String tidestr = new String("");
        timeLabels= new Date[12] ;
        try {
            for (int i=0; i<12; i++ ) {
                if (i < 4) {
                    tidestr += String.format("%s\t\t%4.1f\t\t%s\n",
                            tideData.timeArr[i], tideData.values[i],
                            tideData.typeArr[i]);
                }
                timeLabels[i] = sdf0.parse(tideData.timeArr[i]);

            }
        } catch (Exception e){
            e.printStackTrace() ;
        }


        tv_hilo.setText (tidestr);



    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        System.out.println(i);
        if (i!=2) {
            myTideH = new MyTide(tide_handler_h);
            //myTide.setHandler (tide_handler_h) ;
            myTideH.setStationIndex(i);
            myTideH.setInterval("h");
            myTideH.makeURL();
            Thread t_tide = new Thread(myTideH);
            t_tide.start();
        }

        myTide = new MyTide (tide_handler);
        //myTide.setHandler (tide_handler_h) ;
        myTide.setStationIndex (i);
        myTide.setInterval ("hilo");
        myTide.makeURL() ;
        Thread t_tide1 = new Thread (myTideH) ;
        t_tide1.start();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    void loadHiLo (String tidestring){
        TideData tdn = new TideData(tidestring);
        tdn.setStationName(myTide.stationName);
        tdn.parseHILO() ;
        String stationName = tdn.stationName ;
        tv_station.setText("Station : "+stationName) ;
        String tidestr = new String("");
        for (int i=0; i<4; i++ ){
            tidestr += String.format("%s\t\t%4.1f\t\t%s\n",
                    tdn.timeArr[i], tdn.values[i],
                    tdn.typeArr[i]);

        }

        tv_hilo.setText (tidestr);

    }
    void loadH (String tidestring){
        TideData tdn = new TideData(tidestring);
        tdn.setStationName(myTide.stationName);
        tdn.parse() ;
        maxseries = new LineGraphSeries<>(getData(tdn.timeArrH, tdn.valuesH, 96));
        String stationName = tdn.stationName ;
        tv_station.setText("Station : "+stationName) ;
        String tidestr = new String("");
        for (int i=0; i<4; i++ ){
            tidestr += String.format("%s\t\t%4.1f\n",
                    tdn.timeArrH[i], tdn.valuesH[i]);

        }





        //tv_hilo.setText (tidestr);

    }

    DataPoint[] getData (String dates[], float values[], int npts) {
        double yval;
        dayDates = new Date[5] ;
        Date date1 = new Date () ;

        DataPoint[] valuesArr = new DataPoint[npts];
        for (int i = 0; i < npts; i++) {
            try {

                date1 = sdf0.parse(dates[i]);
                if (i%24==0){
                    dayDates[i/24]=date1 ;
                }
                DataPoint v = new DataPoint(date1, values[i]);
                valuesArr[i] = v ;
            } catch (Exception e){
                e.printStackTrace();
            }
            dayDates[4]=date1 ;

        }
        return valuesArr;
    }

    void plotDataSeries (){


//        for (int i=0; i<96; i+=12){
//            maxseries.getValues(i,i). ;
//            dayDates[i/12] = sdf0.parse(myTideH.timeArrH[i]) ;
//        }



        maxseries.setDrawDataPoints(true);
        maxseries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series2, DataPointInterface dataPoint) {
                long pointX = (long) dataPoint.getX();
                float yval = (float) dataPoint.getY();
                Date newdate = new Date(pointX);

                Double d = new Double(pointX);
                int pointXX = d.intValue();
                Toast.makeText(TideActivity.this, sdf0.format(newdate) + " " + yval + " ft.", Toast.LENGTH_SHORT).show();
//                Toast.makeText(WeatherGraph.this, "Series1: On Data Point clicked: "+dataPoint, Toast.LENGTH_SHORT).show();
            }
        });


        gv_tides.addSeries (maxseries);
        gv_tides.getViewport().setXAxisBoundsManual(true);
        gv_tides.getViewport().setYAxisBoundsManual(true);

        gv_tides.getViewport().setMinX(dayDates[0].getTime());

        gv_tides.getViewport().setMaxX(dayDates[4].getTime());
        gv_tides.getViewport().setMaxY(2.5);
        gv_tides.getViewport().setMinY(-0.5);


        //gv_tides.getGridLabelRenderer().setNumHorizontalLabels(6);

        gv_tides.getGridLabelRenderer().setHorizontalLabelsAngle(90);
        gv_tides.getGridLabelRenderer().setNumHorizontalLabels(5);
        DateAsXAxisLabelFormatter xAxisFormat = new DateAsXAxisLabelFormatter(this, sdf1) ;

        //        String [] labels = new String [5];
//        for (int i=0; i<5; i++){
//            labels[i] = sdf1.format(dayDates[i].getTime()) ;
//        }
//

        gv_tides.getGridLabelRenderer().setLabelFormatter(xAxisFormat);


        //gv_tides.getGridLabelRenderer().setLabelsSpace(12) ;
        gv_tides.getGridLabelRenderer().setHumanRounding(true);


    }
}