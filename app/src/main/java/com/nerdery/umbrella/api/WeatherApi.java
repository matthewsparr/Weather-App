package com.nerdery.umbrella.api;

import com.nerdery.umbrella.BuildConfig;
import com.nerdery.umbrella.model.WeatherData;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Retrofit interface for fetching weather data
 *
 * @author bherbst
 */
public interface WeatherApi {

    /**
     * Get the forecast for a given zip code
     */
    @GET("/api/" + BuildConfig.API_KEY + "/conditions/hourly/q/{zip}.json")
    public void getForecastForZip(@Path("zip") int zipCode, Callback<WeatherData> callback);
}
