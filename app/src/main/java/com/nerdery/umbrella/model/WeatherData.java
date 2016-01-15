package com.nerdery.umbrella.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Represents weather information returned from the Weather Underground API
 *
 * Does not include all available only data- only potentially useful fields are included
 *
 * @author bherbst
 */
public class WeatherData {
    @SerializedName("current_observation")
    public CurrentObservation currentObservation;

    @SerializedName("hourly_forecast")
    public List<ForecastCondition> forecast;
}
