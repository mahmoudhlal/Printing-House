package com.mahmoud.printinghouse.Utils;


import android.view.View;

import com.mahmoud.printinghouse.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataBindingUtils {

    /**
     * convert time zone to format date
     *
     * @param publishedAt
     * @return format date
     */
    public static String setFormatToDate(String publishedAt) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat("d MMMM, yy", Locale.ENGLISH);
        Date date = null;
        try {
            date = inputFormat.parse(publishedAt.replace(String.valueOf(publishedAt.charAt(publishedAt.length() - 1)), ""));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert date != null;
        return outputFormat.format(date);
    }

    public static int checkVisibility(String type, View view){
        switch (view.getId()) {
            case R.id.content:
                if (!type.equals("shields"))
                    return View.GONE;
                else
                    return View.VISIBLE;
            case R.id.paperType:
            case R.id.paperSize:
                if (!type.equals("files"))
                    return View.GONE;
                else
                    return View.VISIBLE;
            case R.id.copiesNum:
                if (type.equals("shields"))
                    return View.GONE;
                else
                    return View.VISIBLE;
        }
        return View.VISIBLE;
    }

    public static String setCopiesNum(String num){
        return "Copies Number "+num;
    }

    public static String setPaperSize(String Size){
        return "Paper Size "+Size;
    }

    public static String setPaperType(String Type){
        return "Paper Type "+Type;
    }

    public static String setUserName(String Name){
        return "User Name : "+Name;
    }

    public static String setFormula(String formula){
        return "Formula "+formula;
    }

    public static String setAddress(String Address){
        return "Address : "+Address;
    }

    public static String setPrintingType(String type){
        return "Printing "+type;
    }


}
