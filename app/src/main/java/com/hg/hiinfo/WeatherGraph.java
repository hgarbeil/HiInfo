package com.hg.hiinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherGraph extends AppCompatActivity {

    private GraphView gv_weather ;
    private String weatherString ;
    private WeatherData myWeatherData ;
    Intent intent ;
    private LineGraphSeries<DataPoint> minseries ;
    private LineGraphSeries<DataPoint> maxseries ;
    TextView tv_day0, tv_dayweather_0 ;
    TextView tv_day1, tv_dayweather_1 ;
    TextView tv_day2, tv_dayweather_2 ;
    ImageView iv_icon_0, iv_icon_1, iv_icon_2;
    SimpleDateFormat sdf = new SimpleDateFormat ("MMM-dd") ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_graph);
        tv_day0 = findViewById(R.id.tv_day0);
        tv_dayweather_0 = findViewById(R.id.tv_dayweather_0);
        iv_icon_1 = findViewById(R.id.iv_icon_1);
        tv_day1 = findViewById(R.id.tv_day1);
        tv_dayweather_1 = findViewById(R.id.tv_dayweather_1);
        iv_icon_0 = findViewById(R.id.iv_icon_0);
        intent = getIntent() ;
        gv_weather = findViewById(R.id.graphWeather) ;
        weatherString = intent.getStringExtra("WeatherData");
        myWeatherData = new WeatherData(weatherString) ;
        myWeatherData.parseDaily() ;

        String daystring = String.format("Weather: %s\nMin: %3.0f ℉   \t\t\t\tMax: %3.0f ℉\nWind: %3.0f MPH\t\tDir : %3.0f Deg",
                myWeatherData.dData[0].weather_desc,
                myWeatherData.dData[0].min, myWeatherData.dData[0].max,
                myWeatherData.dData[0].wind_speed, myWeatherData.dData[0].wind_dir);
        daystring+= String.format("\nSunrise : %s\t\tSunset : %s", myWeatherData.dData[0].sunrise, myWeatherData.dData[0].sunset);

        tv_day0.setText(myWeatherData.dData[0].datestring);
        tv_dayweather_0.setText(daystring);
        Picasso.with(WeatherGraph.this).load(myWeatherData.dData[0].iconURL).into(iv_icon_0);

        daystring = String.format("Weather: %s\nMin: %3.0f ℉   \t\t\t\tMax: %3.0f ℉\nWind: %3.0f MPH\t\tDir : %3.0f Deg",
                myWeatherData.dData[1].weather_desc,
                myWeatherData.dData[1].min, myWeatherData.dData[1].max,
                myWeatherData.dData[1].wind_speed, myWeatherData.dData[1].wind_dir);
        daystring+= String.format("\nSunrise : %s\t\tSunset : %s", myWeatherData.dData[1].sunrise, myWeatherData.dData[1].sunset);

        tv_day1.setText(myWeatherData.dData[1].datestring);
        tv_dayweather_1.setText(daystring);
        Picasso.with(WeatherGraph.this).load(myWeatherData.dData[1].iconURL).into(iv_icon_1);


        DataPoint[] values = getData(myWeatherData.dData, true);
        DataPoint[] mxvalues = getData(myWeatherData.dData, false);
        gv_weather.getViewport().setScrollable(true);

        minseries = new LineGraphSeries<>(values);
        minseries.setColor(Color.BLUE) ;
        minseries.setDrawDataPoints(true);
        gv_weather.addSeries(minseries);
        maxseries = new LineGraphSeries<>(mxvalues);
        maxseries.setDrawDataPoints(true);
        maxseries.setColor(Color.RED) ;
        maxseries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series2, DataPointInterface dataPoint) {
                long pointX = (long) dataPoint.getX();
                float yval = (float) dataPoint.getY() ;
                Date newdate = new Date(pointX) ;

                Double d = new Double(pointX);
                int pointXX = d.intValue();
                Toast.makeText(WeatherGraph.this,sdf.format(newdate) +" "+yval+" F", Toast.LENGTH_SHORT).show();
//                Toast.makeText(WeatherGraph.this, "Series1: On Data Point clicked: "+dataPoint, Toast.LENGTH_SHORT).show();
            }
        });
        minseries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series2, DataPointInterface dataPoint) {
                long pointX = (long) dataPoint.getX();
                float yval = (float) dataPoint.getY() ;
                Date newdate = new Date(pointX) ;

                Double d = new Double(pointX);
                int pointXX = d.intValue();
                Toast.makeText(WeatherGraph.this,sdf.format(newdate) +" \t"+yval+" F", Toast.LENGTH_SHORT).show();
//                Toast.makeText(WeatherGraph.this, "Series1: On Data Point clicked: "+dataPoint, Toast.LENGTH_SHORT).show();
            }
        });
        gv_weather.addSeries(maxseries);
        gv_weather.getViewport().setXAxisBoundsManual(true);
        gv_weather.getGridLabelRenderer().setHorizontalLabelsAngle(90);
        gv_weather.getGridLabelRenderer().setLabelsSpace(1) ;


        gv_weather.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel (double value, boolean isValueX){
                if (isValueX){
                    return sdf.format (new Date((long)(value*1000)) );
                }
                return super.formatLabel(value, isValueX);

            }

        }) ;
        gv_weather.getViewport().setMinX((double)myWeatherData.dData[0].date.getTime());
        gv_weather.getViewport().setMaxX((double)myWeatherData.dData[4].date.getTime());
        gv_weather.setTitle("5 Day Min and Max Temperatures");
        gv_weather.setTitleTextSize(40.f);
        gv_weather.getGridLabelRenderer().setHumanRounding(true, true);
        //gv_weather.getGridLabelRenderer().setNumHorizontalLabels(8);
        String [] labels = new String [5];
        for (int i=0; i<5; i++){
            labels[i] = sdf.format(myWeatherData.dData[i].date) ;
        }
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(gv_weather);
        staticLabelsFormatter.setHorizontalLabels(labels);
        gv_weather.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
//        gv_weather.getGridLabelRenderer().
        gv_weather.getViewport();
    }

    DataPoint[] getData (WeatherData.DailyData ddata[], boolean minFlag){
        double yval ;
        DataPoint[] values = new DataPoint[5];
        for (int i=0; i<5; i++){
//            DataPoint v = new DataPoint (myWeatherData.dData[i].date, myWeatherData.dData[i].min);
            if (minFlag){
                yval = ddata[i].min ;
            } else{
                yval = ddata[i].max ;
            }
            DataPoint v = new DataPoint (ddata[i].date, yval);
            values[i]=v ;
        }
        return values ;
    }
}