package com.nerdery.umbrella.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nerdery.umbrella.R;
import com.nerdery.umbrella.activity.MainActivity;
import com.nerdery.umbrella.widget.DynamicGridLayoutManager;
import com.skocken.efficientadapter.lib.adapter.EfficientRecyclerAdapter;
import com.skocken.efficientadapter.lib.viewholder.EfficientViewHolder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Matthew on 11/21/2015.
 */
public class WeatherCardHolder extends EfficientViewHolder<WeatherCardModel> {

    public List<WeatherCellModel> weatherCellsList = new ArrayList<>();

    public WeatherCardHolder(View itemView) {
        super(itemView);
    }
    @Override
    protected void updateView(Context context, WeatherCardModel weatherCard) {
        TextView dayHeader = (TextView)findViewByIdEfficient(R.id.sectionHeaderLabel);
        dayHeader.setText(weatherCard.dayHeader);

        List<Integer> tempsList = new ArrayList<>();

        Iterator<ForecastCondition> iterator = weatherCard.forecastData.iterator();
        while (iterator.hasNext()) {
            ForecastCondition forecastCondition = iterator.next();
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
            String time = sdf.format(forecastCondition.time);
            String icon = forecastCondition.icon;
            DecimalFormat df = new DecimalFormat("0");
            String temp = " ";
            if(MainActivity.metricUnits) {
                temp = String.valueOf(df.format(forecastCondition.tempCelsius)) + (char) 0x00B0;
            } else {
                temp = String.valueOf(df.format(forecastCondition.tempFahrenheit)) + (char) 0x00B0;
            }

            tempsList.add(Math.round(forecastCondition.tempFahrenheit));

            weatherCellsList.add(new WeatherCellModel(time, icon, temp, false, false));
        }

        int highTemp = Collections.max(tempsList);
        int lowTemp = Collections.min(tempsList);

        tempsList.clear();

        Boolean lowTempFound = false;
        Boolean highTempFound = false;

        int position = 0;
        int lowPosition = 0;
        int highPosition = 0;

        Iterator<WeatherCellModel> tempIterator = weatherCellsList.iterator();
        while (tempIterator.hasNext()) {
            if (highTempFound && lowTempFound) {
                break;
            } else {
                WeatherCellModel current = tempIterator.next();
                Log.d("HIGH", String.valueOf(lowTemp));
                Log.d("HIGH", current.temp);
                if (current.temp.substring(0,2).equals(String.valueOf(lowTemp)) && !lowTempFound) {
                    Log.d("LOW", current.temp + current.time);
                    lowTempFound = true;
                    lowPosition = position;
                } else if (current.temp.substring(0,2).equals(String.valueOf(highTemp)) && !highTempFound) {
                    highTempFound = true;
                    highPosition = position;
                }
                position ++;
            }
        }

        WeatherCellModel lowModel = weatherCellsList.get(lowPosition);
        String lowTime = lowModel.time;
        String lowIcon = lowModel.icon;
        String lowTempRef = lowModel.temp;
        weatherCellsList.remove(lowPosition);
        weatherCellsList.add(lowPosition, new WeatherCellModel(lowTime, lowIcon, lowTempRef, false, true));

        WeatherCellModel highModel = weatherCellsList.get(highPosition);
        String highTime = highModel.time;
        String highIcon = highModel.icon;
        String highTempRef = highModel.temp;
        weatherCellsList.remove(highPosition);
        weatherCellsList.add(highPosition, new WeatherCellModel(highTime, highIcon, highTempRef, true, false));

        setRecyclerView();


    }

    public void setRecyclerView() {
        RecyclerView recyclerView = (RecyclerView)findViewByIdEfficient(R.id.recyclerViewNested);
        recyclerView.setLayoutManager(new DynamicGridLayoutManager(getContext(), 4));
        EfficientRecyclerAdapter<WeatherCellModel> adapter =
                new EfficientRecyclerAdapter<>(R.layout.weather_cell_layout, WeatherCellHolder.class, weatherCellsList);
        recyclerView.setAdapter(adapter);
    }
}
