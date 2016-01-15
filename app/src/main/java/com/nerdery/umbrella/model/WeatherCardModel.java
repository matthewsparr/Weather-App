package com.nerdery.umbrella.model;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by Matthew on 11/21/2015.
 */
public class WeatherCardModel {

    public List<ForecastCondition> forecastData;
    public String dayHeader;

    public WeatherCardModel(List<ForecastCondition> forecastData, String dayHeader) {
        this.forecastData = forecastData;
        this.dayHeader = dayHeader;
    }
}
