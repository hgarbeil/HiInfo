package com.hg.hiinfo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDate {
    private  static SimpleDateFormat sdf_day = new SimpleDateFormat("EEE MMM-dd-yyyy") ;
    private  static SimpleDateFormat sdf_time = new SimpleDateFormat("hh:mm aa") ;

    public static String dateToDayString (Date dt){
        String st="";
        try {
            st = sdf_day.format(dt);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return st ;

    }

    public static String dateToTimeString (Date dt){
    String st="";
        try {
        st = sdf_time.format(dt);
        }
        catch (Exception e){
        e.printStackTrace();
        }
        return st ;

    }


}
