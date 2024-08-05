package com.ltuddd.weatherapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<ListData> {

    public ListAdapter(@NonNull Context context, ArrayList<ListData> dataArrayList) {
        super(context, R.layout.custom_item, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent){
        ListData listData = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.custom_item, parent, false);
        }

        ImageView listImage = view.findViewById(R.id.listImage);
        TextView listTime = view.findViewById(R.id.listTime);
        TextView listTemp = view.findViewById(R.id.listTemp);
        TextView listRain = view.findViewById(R.id.listRain);
        TextView listWater = view.findViewById(R.id.listWater);
        TextView listWind = view.findViewById(R.id.listWind);

        listImage.setImageResource(listData.imageTemp);
        listTime.setText(listData.Time);
        listRain.setText(listData.Rain);
        listWind.setText(listData.Wind);
        listWater.setText(listData.Water);
        listTemp.setText(listData.Temp);

        return view;
    }
}
