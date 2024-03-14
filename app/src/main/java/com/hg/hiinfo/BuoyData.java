package com.hg.hiinfo;

import java.util.StringTokenizer;

public class BuoyData {
    int stationNum ;
    String timeString[], dayString[] ;
    String buoyString ;
    float height[] ;
    float dir[], domPeriod[], avgPeriod[] ;
    String stationNames []= {"Pearl Harbor","Barbers Point","Mokapu","Waimea"} ;

    BuoyData(String bstring){
        stationNum = 0 ;
        buoyString = bstring ;
        dir = new float[4] ;
        domPeriod = new float[4] ;
        avgPeriod = new float[4] ;
        height = new float[4] ;
        dayString = new String [4] ;
        timeString = new String[4] ;
        parseData() ;

    }



    void parseData() {
        String time, valstr, hrtime, mintime ;
        StringTokenizer stok = new StringTokenizer(buoyString, "\n") ;
        String strline = stok.nextToken () ;
        String lstr = strline.substring(10) ;
        stationNum = Integer.parseInt(lstr) ;
        stok.nextToken() ;
        stok.nextToken() ;
        for (int i=0; i<4; i++){
            strline = stok.nextToken() ;
            StringTokenizer fields = new StringTokenizer(strline, " \t") ;
            time = fields.nextToken();
            time += "/"+fields.nextToken() ;
            time += "/"+fields.nextToken() ;
            hrtime = fields.nextToken() ;
            mintime = fields.nextToken() ;
            time += " "+hrtime+":"+mintime ;
            timeString[i]=hrtime+":"+mintime;
            dayString[i] = time ;
            for (int icol=0; icol<3; icol++) {
                fields.nextToken() ;
            }
            valstr = fields.nextToken() ;
            height[i] = Float.parseFloat(valstr) ;
            valstr = fields.nextToken() ;
            domPeriod[i] = Float.parseFloat(valstr) ;
            valstr = fields.nextToken() ;
            avgPeriod[i] = Float.parseFloat(valstr) ;
            valstr = fields.nextToken();
            dir[i] = Float.parseFloat(valstr) ;

        }

    }
}
