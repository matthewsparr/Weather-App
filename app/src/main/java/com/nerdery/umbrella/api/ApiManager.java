package com.nerdery.umbrella.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nerdery.umbrella.BuildConfig;
import com.nerdery.umbrella.api.parser.ForecastParser;
import com.nerdery.umbrella.model.ForecastCondition;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Manages access to the various APIs we are using
 *
 * @author bherbst
 */
public class ApiManager {
    private static WeatherApi sApi;

    /**
     * Get a WeatherApi instance for access to Weather Underground's APIs
     */
    public static WeatherApi getWeatherApi() {
        if (sApi == null) {
            sApi = buildWeatherApi();
        }

        return sApi;
    }

    /**
     * Get an IconApi instance
     */
    public static IconApi getIconApi() {
        return new IconApi();
    }

    /**
     * Build a new WeatherApi
     */
    private static WeatherApi buildWeatherApi() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ForecastCondition.class, new ForecastParser())
                .create();

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BuildConfig.API_URL)
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .setConverter(new GsonConverter(gson))
                .build();

        return adapter.create(WeatherApi.class);
    }
}
