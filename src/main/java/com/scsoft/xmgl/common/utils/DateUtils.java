package com.scsoft.xmgl.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtils {
    public static Map<String,String> getWeekDate() {
        Map<String,String> map = new HashMap();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        String Saturday = null;
        String Friday = null;
        long nowDate = new Date().getTime();
        long SaturdayTime = 0;
        long FridayTime = 0;
        switch (dayWeek) {
            case 1:
                dayWeek = 7;
                SaturdayTime = nowDate-24*60*60*1000;
                FridayTime = nowDate+5*24*60*60*1000;
                break;
            case 2:
                dayWeek = 1;
                SaturdayTime = nowDate-2*24*60*60*1000;
                FridayTime = nowDate+4*24*60*60*1000;
                break;
            case 3:
                dayWeek = 2;
                SaturdayTime = nowDate-3*24*60*60*1000;
                FridayTime = nowDate+3*24*60*60*1000;
                break;
            case 4:
                dayWeek = 3;
                SaturdayTime = nowDate-4*24*60*60*1000;
                FridayTime = nowDate+2*24*60*60*1000;
                break;
            case 5:
                dayWeek = 4;
                SaturdayTime = nowDate-5*24*60*60*1000;
                FridayTime = nowDate+1*24*60*60*1000;
                break;
            case 6:
                dayWeek = 5;
                SaturdayTime = nowDate-6*24*60*60*1000;
                FridayTime = nowDate;
                break;
            case 7:
                dayWeek = 6;
                SaturdayTime = nowDate;
                FridayTime = nowDate+6*24*60*60*1000;
                break;
        }
        Date SaturdayDate = new Date(SaturdayTime);
        Date FridayDate = new Date(FridayTime);
        Saturday = sdf.format(SaturdayDate);
        Friday = sdf.format(FridayDate);
        map.put("Saturday", Saturday);
        map.put("Friday", Friday);
        return map;
    }

    public static String getDateAfterNDays(String dateTime, int days) {
        Calendar calendar = Calendar.getInstance();
        String[] dateTimeArray = dateTime.split("-");
        int year = Integer.parseInt(dateTimeArray[0]);
        int month = Integer.parseInt(dateTimeArray[1]);
        int day = Integer.parseInt(dateTimeArray[2]);
        calendar.set(year, month - 1, day);
        long time = calendar.getTimeInMillis();
        calendar.setTimeInMillis(time + days * 1000 * 60 * 60 * 24);
        return calendar.get(Calendar.YEAR)
                + "-" + (calendar.get(Calendar.MONTH) + 1)
                + "-" + calendar.get(Calendar.DAY_OF_MONTH)
                ;
    }

    public static Date StringToDate(String string) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotBlank(string)){
            return simpleDateFormat.parse(string);
        }else {
            return simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        }
    }

    public static String DateToString(Date date) throws ParseException{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static String yyyyMMdd(String string) throws ParseException {
        return DateToString(StringToDate(string));
    }

    public static Date yyyyMMdd(Date date) throws ParseException {
        return StringToDate(DateToString(date));
    }
    
    public static int getMonth(){
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        return month;
    }
    public static int getMonthByDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        return month;
    }

    public static Date getFirstDayOfMonth() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);
        String first = sdf.format(c.getTime());
        return sdf.parse(first);
    }
    public static Date getEndDayOfMonth() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = sdf.format(ca.getTime());
        return sdf.parse(last);
    }
}
