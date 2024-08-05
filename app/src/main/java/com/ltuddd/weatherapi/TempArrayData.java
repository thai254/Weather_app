package com.ltuddd.weatherapi;

public class TempArrayData {
    public static String[] TimeList;
    public static String[] TempList;
    public static String[] WaterList;
    public static String[] RainList;
    public static String[] WindList;
    public static String TempChild;
    public static String TimeCurrent;
    public static int isDay;

    public static void setTempChild(String value) {
        TempChild = value;
    }
    public void TimeNow(String timeCurrent) {
        this.TimeCurrent= timeCurrent;
    }
    public void isDay(int isDay){
        this.isDay = isDay;
    }
    public static String getTimeCurrent() {
        return TimeCurrent;
    }
    public static int getIsDay(){
        return isDay;
    }

}
