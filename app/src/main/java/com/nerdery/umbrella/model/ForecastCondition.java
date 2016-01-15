package com.nerdery.umbrella.model;

import java.util.Date;

/**
 * Represents a forecast weather condition returned from Weather Underground
 *
 * Does not include all available only data- only potentially useful fields are included
 *
 * @author bherbst
 */
public class ForecastCondition {
    /**
     * Formatted time suitable for display
     */
    public String displayTime;

    /**
     * Date representation of the time associated with this forecast
     */
    public Date time;

    /**
     * The icon to use for this reading
     */
    public String icon;

    /**
     * The human-readable name of the condition
     */
    public String condition;

    /**
     * The temperature that is forecast (in degrees Fahrenheit)
     */
    public float tempFahrenheit;

    /**
     * The temperature that is forecast (in degrees Celsius)
     */
    public float tempCelsius;

}
