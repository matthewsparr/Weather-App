package com.nerdery.umbrella.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.nerdery.umbrella.R;
import com.nerdery.umbrella.api.ApiManager;
import com.nerdery.umbrella.model.CurrentObservation;
import com.nerdery.umbrella.model.DisplayLocation;
import com.nerdery.umbrella.model.ForecastCondition;
import com.nerdery.umbrella.model.WeatherCardHolder;
import com.nerdery.umbrella.model.WeatherCardModel;
import com.nerdery.umbrella.model.WeatherData;
import com.nerdery.umbrella.widget.DynamicGridLayoutManager;
import com.skocken.efficientadapter.lib.adapter.EfficientAdapter;
import com.skocken.efficientadapter.lib.adapter.EfficientRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity {

    private DisplayLocation displayLocation;
    private List<ForecastCondition> forecastConditionList;
    private CurrentObservation currentObservation;

    List<ForecastCondition> todayForecast = new ArrayList<>();
    List<ForecastCondition> tomorrowForecast = new ArrayList<>();
    List<ForecastCondition> thirdForecast = new ArrayList<>();

    List<WeatherCardModel> weatherCardList = new ArrayList<>();

    List<List<ForecastCondition>> forecastCollection = new ArrayList<>();

    private String thirdDayOfWeek;

    private String zipCode;
    public static Boolean metricUnits;

    private static Boolean resumed = false;


    @Bind(R.id.recyclerViewWeatherCards)
    RecyclerView weatherCardsRecyclerView;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getSettings();

        setWeatherCardsAdapter();

        getWeatherData();

        setActionBar();

    }

    @Override
    protected void onResume() {
        super.onResume();

        final SharedPreferences prefs = getSharedPreferences("UmbrellaSettings", Context.MODE_PRIVATE);
        if (prefs.getBoolean("Refresh", false)) {
            recreate();
            prefs.edit().putBoolean("Refresh", false).apply();
        }
    }
    private void clearAllLists() {
        if (forecastCollection != null) {
            forecastCollection.clear();
        }
        if (todayForecast != null) {
            todayForecast.clear();
        }
        if (tomorrowForecast != null) {
            tomorrowForecast.clear();
        }
        if (thirdForecast != null) {
            thirdForecast.clear();
        }
        if (weatherCardList != null) {
            weatherCardList.clear();
        }
        if (forecastConditionList != null) {
            forecastConditionList.clear();
        }
    }

    private void setActionBar() {
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

    }

    private void getSettings() {
        SharedPreferences prefs = getSharedPreferences("UmbrellaSettings", Context.MODE_PRIVATE);
        zipCode = prefs.getString("Zipcode", "46250");
        metricUnits = prefs.getBoolean("Metric", false);
    }

    private void setCurrentConditions() {
        View actionBarView = (View)getSupportActionBar().getCustomView();

        RelativeLayout actionBarBackground = (RelativeLayout)actionBarView.findViewById(R.id.actionBarBackground);
        if (metricUnits) {
            if (currentObservation.tempCelsius >= 15.5) {
                actionBarBackground.setBackgroundColor(getResources().getColor(R.color.weather_warm));
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.weather_warm)));
            } else {
                actionBarBackground.setBackgroundColor(getResources().getColor(R.color.weather_cool));
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.weather_cool)));
            }
        } else {
            if (currentObservation.tempFahrenheit >= 60) {
                actionBarBackground.setBackgroundColor(getResources().getColor(R.color.weather_warm));
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.weather_warm)));
            } else {
                actionBarBackground.setBackgroundColor(getResources().getColor(R.color.weather_cool));
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.weather_cool)));
            }
        }

        TextView locationLabel = (TextView)actionBarView.findViewById(R.id.locationLabel);
        String location = currentObservation.displayLocation.city + ", " + currentObservation.displayLocation.state;
        locationLabel.setText(location);

        TextView temperatureLabel = (TextView)actionBarView.findViewById(R.id.currentTemperatureLabel);
        String currentTemperature = " ";
        if (metricUnits) {
            currentTemperature = String.valueOf(Math.round(currentObservation.tempCelsius)) + (char) 0x00B0;
        } else {
            currentTemperature = String.valueOf(Math.round(currentObservation.tempFahrenheit)) + (char) 0x00B0;
        }
        temperatureLabel.setText(currentTemperature);

        TextView conditionsLabel = (TextView)actionBarView.findViewById(R.id.currentConditionsLabel);
        conditionsLabel.setText(currentObservation.weather);

        ImageView settingsButton = (ImageView)actionBarView.findViewById(R.id.action_settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSettings();
            }
        });
    }


    private void setWeatherCardsAdapter() {
        EfficientRecyclerAdapter<WeatherCardModel> efficientAdapter =
                new EfficientRecyclerAdapter<WeatherCardModel>(R.layout.weather_card_layout, WeatherCardHolder.class, weatherCardList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        weatherCardsRecyclerView.setLayoutManager(layoutManager);
        weatherCardsRecyclerView.setAdapter(efficientAdapter);

    }

    private void goToSettings() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        MainActivity.this.startActivity(intent);
    }


    private void getWeatherData() {
        ApiManager.getWeatherApi().getForecastForZip(Integer.parseInt(zipCode), new Callback<WeatherData>() {
            @Override
            public void success(WeatherData weatherData, Response response) {

                displayLocation = weatherData.currentObservation.displayLocation;
                forecastConditionList = weatherData.forecast;
                currentObservation = weatherData.currentObservation;

                try {
                    getSupportActionBar().setTitle(displayLocation.city + ", " + displayLocation.state);
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }

                setCurrentConditions();
                setForecastLists();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void setForecastLists() {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date thirdDay = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");     // getting day of week for third day
        thirdDayOfWeek = sdf.format(thirdDay);


        if (forecastConditionList != null) {
            Iterator<ForecastCondition> iterator = forecastConditionList.iterator();
            while (iterator.hasNext()) {
                ForecastCondition forecast = iterator.next();
                int forecastDate = forecast.time.getDate();
                int todayDate = today.getDate();
                int tomorrowDate = tomorrow.getDate();
                int thirdDayDate = thirdDay.getDate();

                if (forecastDate == todayDate) {
                    todayForecast.add(forecast);
                } else if (forecastDate == tomorrowDate) {
                    tomorrowForecast.add(forecast);
                } else if (forecastDate == thirdDayDate) {
                    thirdForecast.add(forecast);
                }
            }

            forecastCollection.add(todayForecast);
            forecastCollection.add(tomorrowForecast);
            forecastCollection.add(thirdForecast);
        }
        weatherCardList.add(new WeatherCardModel(todayForecast, "Today"));
        weatherCardList.add(new WeatherCardModel(tomorrowForecast, "Tomorrow"));
        weatherCardList.add(new WeatherCardModel(thirdForecast, thirdDayOfWeek));


        setWeatherCardsAdapter();

    }



}
