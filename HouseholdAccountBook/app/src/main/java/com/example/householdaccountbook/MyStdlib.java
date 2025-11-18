package com.example.householdaccountbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

public class MyStdlib {
    public static <T> T[] mergeSort(T[] dataArray, Comparator<T> comp) {
        T[] array = Arrays.copyOf(dataArray, dataArray.length);
        T[] bufArray = Arrays.copyOf(dataArray, dataArray.length);
        sort(array, bufArray, comp, 0, array.length - 1);
        return array;
    }
    private static <T> void sort(T[] array, T[] buf, Comparator<T> comp, int leftIndex, int rightIndex) {
        if (leftIndex >= rightIndex) return;
        int mid = (leftIndex + rightIndex) / 2;
        sort(array, buf, comp, leftIndex, mid);
        sort(array, buf, comp, mid + 1, rightIndex);
        merge(array, buf, comp,leftIndex, mid, rightIndex);
        return;
    }
    private static <T> void merge(T[] array, T[] buf, Comparator<T> comp, int leftIndex, int mid, int rightIndex) {
        int leftPointer = leftIndex;
        int rightPointer = mid + 1;
        int pointer = leftIndex;
        while (leftPointer <= mid || rightPointer <= rightIndex) {
            if (leftPointer > mid) {
                buf[pointer++] = array[rightPointer++];
            }
            else if (rightPointer > rightIndex) {
                buf[pointer++] = array[leftPointer++];
            }
            else if (comp.compare(array[leftPointer], array[rightPointer]) <= 0) {
                buf[pointer++] = array[leftPointer++];
            }
            else {
                buf[pointer++] = array[rightPointer++];
            }
        }
        if (rightIndex + 1 - leftIndex >= 0)
            System.arraycopy(buf, leftIndex, array, leftIndex, rightIndex + 1 - leftIndex);
    }
    public static String convertCalendarToString(Integer year, Integer month, Integer day, Integer week){
        String formatedDate = "";
        if (year != null) {
            formatedDate += String.valueOf(year) + "年";
        }
        if (month != null) {
            formatedDate += String.valueOf(month + 1) + "月";

        }
        if (day != null) {
            formatedDate += String.valueOf(day) + "日";
        }
        if (week != null) {
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
            formatedDate += "(" + weekText + ")";
        }

        return formatedDate;
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
    public static boolean isSameDay(Calendar c1, Calendar c2) {
        if (c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR)) return false;
        if (c1.get(Calendar.MONTH) != c2.get(Calendar.MONTH)) return false;
        if (c1.get(Calendar.DATE) != c2.get(Calendar.DATE)) return false;
        return true;
    }

    public static boolean canConvertToInteger(String str) {
        try {
            int tmp = Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
    public static int dpToPx(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }
}
