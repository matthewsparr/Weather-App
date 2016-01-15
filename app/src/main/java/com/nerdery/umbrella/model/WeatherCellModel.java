package com.nerdery.umbrella.model;

import java.util.List;

/**
 * Created by Matthew on 11/21/2015.
 */
public class WeatherCellModel {


    public String time;
    public String icon;
    public String temp;
    public Boolean highTemp;
    public Boolean lowTemp;

    public void setHighTemp(Boolean highTemp) {
        this.highTemp = highTemp;
    }

    public void setLowTemp(Boolean lowTemp) {
        this.lowTemp = lowTemp;
    }


    public WeatherCellModel(String time, String icon, String temp, Boolean highTemp, Boolean lowTemp) {
        this.time = time;
        this.icon = icon;
        this.temp = temp;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
    }
}
