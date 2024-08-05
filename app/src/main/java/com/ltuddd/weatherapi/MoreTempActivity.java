package com.ltuddd.weatherapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ltuddd.weatherapi.databinding.ActivityMainBinding;
import com.ltuddd.weatherapi.databinding.ActivityMoreTempBinding;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class MoreTempActivity extends AppCompatActivity {

    ActivityMoreTempBinding binding;
    ListAdapter listAdapter;
    ArrayList<ListData> dataArrayList = new ArrayList<>();
    ListData listData;
    RelativeLayout bg_moretemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMoreTempBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bg_moretemp = findViewById(R.id.background_weather);

        int isDay = TempArrayData.getIsDay();
        Log.v("isday", String.valueOf(isDay));
        if(isDay == 1){
            bg_moretemp.setBackgroundResource(R.drawable.days);
        }
        else if(isDay == 0){
            bg_moretemp.setBackgroundResource(R.drawable.night);
        }

        int[] imageList = {R.drawable.pasta, R.drawable.maggi, R.drawable.cake, R.drawable.pancake, R.drawable.pizza, R.drawable.burger, R.drawable.fries};

        String[] timeList1 = TempArrayData.TimeList;
        String[] tempList1 = TempArrayData.TempList;
        String[] RainList1 = TempArrayData.RainList;
        String[] WaterList1 = TempArrayData.WaterList;
        String[] WindList1 = TempArrayData.WindList;
        String TimeNow = TempArrayData.TimeCurrent;
        String TimeNow2;
        String id_value = TempArrayData.getTimeCurrent();

        String[] timeList2 = new String[timeList1.length];

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        SimpleDateFormat outputFormat = new SimpleDateFormat("'Ngày 'dd -' Tháng 'MM  HH'h'");
        SimpleDateFormat outputTimeFormat = new SimpleDateFormat("HH");

        Date targetTime = null;
        try {
            targetTime = inputFormat.parse(TimeNow);
            TimeNow2 = outputTimeFormat.format(targetTime);
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < timeList1.length; i++) {
            String dateTimeString = timeList1[i];
            String formattedDateTimeString = formatDateTime(dateTimeString, inputFormat, outputFormat);
            timeList2[i] = formattedDateTimeString;
        }

        String[] tempList2 = new String[tempList1.length];

        for (int i = 0; i < tempList1.length; i++) {
            double temperature = Double.parseDouble(tempList1[i]);
            String formattedTemperature = temperature + "℃";
            tempList2[i] = formattedTemperature;
        }

        String[] RainList2 = new String[RainList1.length];

        for (int i = 0; i < RainList1.length; i++) {
            double rain = Double.parseDouble(RainList1[i]);
            String formattedRain = "Xác suất mưa: "+ rain + "%";
            RainList2[i] = formattedRain;
        }

        String[] WaterList2 = new String[WaterList1.length];

        for (int i = 0; i < WaterList1.length; i++) {
            double water = Double.parseDouble(WaterList1[i]);
            String formattedWATER = "Độ ẩm: "+ water + "%";
            WaterList2[i] = formattedWATER;
        }

        String[] WindList2 = new String[WindList1.length];

        for (int i = 0; i < WindList1.length; i++) {
            double wind = Double.parseDouble(WindList1[i]);
            String formattedWind = "Gió: "+ wind + "km/h";
            WindList2[i] = formattedWind;
        }

        Log.v("time", TimeNow2 );
        Log.v("time_ar", Arrays.toString(timeList2));

        for (int i = Integer.parseInt(TimeNow2)+1; i < timeList2.length; i++) {
            ListData listData = new ListData(timeList2[i], tempList2[i], RainList2[i], WaterList2[i], WindList2[i]);
            dataArrayList.add(listData);
        }
        listAdapter = new ListAdapter(MoreTempActivity.this, dataArrayList);
        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);
    }

    private static String formatDateTime(String dateTimeString, SimpleDateFormat inputFormat, SimpleDateFormat outputFormat) {
        try {
            Date date = inputFormat.parse(dateTimeString);
            return outputFormat.format(date);
        } catch (ParseException | java.text.ParseException e) {
            return "";
        }
    }
}