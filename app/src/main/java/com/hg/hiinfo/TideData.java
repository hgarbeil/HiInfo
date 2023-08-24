package com.hg.hiinfo;

import java.util.StringTokenizer;

public class TideData {
    String tideString, timeArr[], typeArr[], timeArrH[],  stationName  ;
    float values[], valuesH[] ;

    TideData (String instr){
        tideString = instr ;


    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    void parse () {
        int nlines ;
        try {

            StringTokenizer stok = new StringTokenizer(tideString, "\n");
            nlines = stok.countTokens() - 1;
            timeArrH = new String[nlines] ;
            valuesH = new float [nlines] ;
            stok.nextToken() ;
            for (int i=0; i<nlines; i++){
                StringTokenizer sline = new StringTokenizer(stok.nextToken(), ",\n");
                timeArrH[i] = sline.nextToken();
                valuesH[i] = Float.parseFloat(sline.nextToken());

            }



        } catch (Exception e){
            e.printStackTrace();
        }


    }
    void parseHILO () {
        int nlines ;
        try {

            StringTokenizer stok = new StringTokenizer(tideString, "\n");
            nlines = stok.countTokens() - 1;
            timeArr = new String[nlines] ;
            values = new float [nlines] ;
            typeArr = new String [nlines] ;
            stok.nextToken() ;
            for (int i=0; i<nlines; i++){
                StringTokenizer sline = new StringTokenizer(stok.nextToken(), ",/n");
                timeArr[i] = sline.nextToken();
                values[i] = Float.parseFloat(sline.nextToken());
                typeArr[i] = sline.nextToken() ;

            }



        } catch (Exception e){
            e.printStackTrace();
        }


    }
}
