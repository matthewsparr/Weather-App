package com.nerdery.umbrella.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nerdery.umbrella.R;
import com.nerdery.umbrella.activity.MainActivity;
import com.nerdery.umbrella.api.ApiManager;
import com.skocken.efficientadapter.lib.viewholder.EfficientViewHolder;
import com.squareup.picasso.Picasso;

/**
 * Created by Matthew on 11/21/2015.
 */
public class WeatherCellHolder extends EfficientViewHolder<WeatherCellModel> {

    public WeatherCellHolder(View itemView) {
        super(itemView);
    }
    @Override
    protected void updateView(Context context, WeatherCellModel weatherData) {
        TextView time = (TextView)findViewByIdEfficient(R.id.weatherCellTimeLabel);
        time.setText(weatherData.time);

        ImageView icon = (ImageView)findViewByIdEfficient(R.id.weatherCellIcon);
        String iconString = weatherData.icon;

        String iconURL = ApiManager.getIconApi().getUrlForIcon(iconString, false);
        Picasso.with(getContext()).load(iconURL).into(icon);

        TextView temp = (TextView)findViewByIdEfficient(R.id.weatherCellTempLabel);
        temp.setText(weatherData.temp);



        String highlightColor = "#000000";

        if (weatherData.lowTemp) {
            highlightColor = "#03A9F4";
            time.setTextColor(Color.parseColor(highlightColor));
            icon.setColorFilter(Color.parseColor(highlightColor));
            temp.setTextColor(Color.parseColor(highlightColor));
        } else if (weatherData.highTemp) {
            highlightColor = "#FF9800";
            time.setTextColor(Color.parseColor(highlightColor));
            icon.setColorFilter(Color.parseColor(highlightColor));
            temp.setTextColor(Color.parseColor(highlightColor));
        } else {
            icon.setColorFilter(Color.parseColor(highlightColor));
            time.setTextColor(Color.parseColor(highlightColor));
            temp.setTextColor(Color.parseColor(highlightColor));
        }


    }

}
