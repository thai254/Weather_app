package com.ltuddd.weatherapi;

public class ListData {
    String Time, Temp, Rain, Water, Wind;
    int imageTemp;

    public ListData(String time, String temp, String rain, String water, String wind){
        this.Time = time;
        this.Temp = temp;
        this.Rain = rain;
        this.Water = water;
        this.Wind = wind;
    }
}
