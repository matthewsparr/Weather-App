package com.nerdery.umbrella.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the "current_observation" data returned from Weather Underground
 *
 * Does not include all available only data- only potentially useful fields are included
 *
 * @author bherbst
 */
public class CurrentObservation {
    @SerializedName("display_location")
    public DisplayLocation displayLocation;

    @SerializedName("temp_f")
    public float tempFahrenheit;

    @SerializedName("temp_c")
    public float tempCelsius;

    public String weather;

    public String icon;
}
