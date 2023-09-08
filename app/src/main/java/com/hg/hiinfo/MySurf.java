package com.hg.hiinfo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.StringTokenizer;


public class MySurf implements Runnable {

    String surfStrings[] ;
    private Element surfTable ;
    //String url = "https://www.weather.gov/source/hfo/xml/SurfState.xml";
    String url = "https://www.weather.gov/source/hfo/xml/Oahu.OMR.HFO.xml";
    Handler handler ;
    MySurf(Handler h) {
        surfStrings = new String [4] ;
        handler = h ;
    }

    @Override
    public void run() {
        try {
            URL myURL = new URL(url);
            StringBuffer buffer = new StringBuffer() ;
            Document doc = Jsoup.connect(url).get();

            //Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", "", Parser.xmlParser());
            String title = doc.title() ;
            //surfTable = doc.select("item").get(0);
            //for (Element e : doc.select("item")) {
            for (Element e: doc.select("title")){
                if (e.text().contains("DIAMOND HEAD")) {
                    buffer.append("\n"+e.text()+"\n") ;
                    surfStrings[0] = e.text() ;
                    System.out.println(e.text());

                    //System.out.println(e.text());
                }
                if (e.text().contains("WAIKIKI")){
                    surfStrings[1]= e.text() ;
                    buffer.append(e.text()+"\n");
                }
                Bundle bundle = new Bundle();
                Message msg = new Message() ;
                bundle.putString("MySurf", buffer.toString());
                msg.setData (bundle);
                handler.sendMessage(msg);
            }


        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
