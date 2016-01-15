package com.nerdery.umbrella.model;

/**
 * Represents a "display_location" returned from Weather Underground
 *
 * Does not include all available only data- only potentially useful fields are included
 *
 * @author bherbst
 */
public class DisplayLocation {
    public String full;
    public String city;
    public String state;
    public String state_name;
    public String country;
    public String zip;
}
