package org.codelibs.elasticsearch.minhash.index.mapper;


import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class test {

    public static void main(String[] args) {

        String value="2013-09-18T20:40:00+0000";


        PrintStream psObj = new PrintStream(System.out);
        String outputValue = "";

    /*    Date formatter = null;
        try {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value2.replace("T"," ").substring(0,19));
            psObj.println(formatter);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } */



        if (value.contains(",")) {
            if (value.split(",")[0].length() > 10) {
                try {
                    Date formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value.split(",")[0].replace("T"," ").substring(0,19));
                    ShamsiDate pdate = new ShamsiDate(formatter);
                    ShamsiDateFormat pdformater1 = new ShamsiDateFormat(value.split(",")[1]);
                    outputValue = (pdformater1.format(pdate));

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                try {
                Date formatter = new SimpleDateFormat("yyyy-MM-dd").parse(value.split(",")[0].replace("/","-"));
                ShamsiDate pdate = new ShamsiDate(formatter);
                ShamsiDateFormat pdformater1 = new ShamsiDateFormat(value.split(",")[1]);
                outputValue = (pdformater1.format(pdate));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            if (value.length() > 10) {
                try {
                    Date formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value.replace("T", " ").substring(0, 19));
                    ShamsiDate pdate = new ShamsiDate(formatter);
                    ShamsiDateFormat pdformater1 = new ShamsiDateFormat("Y/m/j H:i:s");
                    outputValue = (pdformater1.format(pdate));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    Date formatter = new SimpleDateFormat("yyyy-MM-dd").parse(value.replace("/","-"));
                    ShamsiDate pdate = new ShamsiDate(formatter);
                    ShamsiDateFormat pdformater1 = new ShamsiDateFormat("Y/m/j");
                    outputValue = (pdformater1.format(pdate));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        psObj.println(outputValue);
        // Flushing the Stream
        psObj.flush();
    }
}
