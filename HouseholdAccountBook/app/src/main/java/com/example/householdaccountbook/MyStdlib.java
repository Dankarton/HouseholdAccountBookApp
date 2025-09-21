package com.example.householdaccountbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

public class MyStdlib {

    public static String convertCalendarToString(Calendar calendar){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        String weekText = "";
        switch (week){
            case Calendar.SUNDAY:
                weekText = "日";
                break;
            case Calendar.MONDAY:
                weekText = "月";
                break;
            case Calendar.TUESDAY:
                weekText = "火";
                break;
            case Calendar.WEDNESDAY:
                weekText = "水";
                break;
            case Calendar.THURSDAY:
                weekText = "木";
                break;
            case Calendar.FRIDAY:
                weekText = "金";
                break;
            case Calendar.SATURDAY:
                weekText = "土";
                break;
        }
        return String.valueOf(year) + "年" + String.valueOf(month) + "月" + String.valueOf(day) + "日(" + weekText + ")";
    }
    public static Calendar convertToCalendar(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        // 時刻情報をクリア
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal;
    }

    public static boolean canConvertToInteger(String str) {
        try {
            int tmp = Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
}
