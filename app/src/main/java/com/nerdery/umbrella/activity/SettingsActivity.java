package com.nerdery.umbrella.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdery.umbrella.R;

import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Matthew on 11/21/2015.
 */
public class SettingsActivity extends ActionBarActivity{

    @Bind(R.id.settingsZipBox)
    LinearLayout zipBox;

    @Bind(R.id.settingsZipCode)
    TextView zipCode;

    @Bind(R.id.settingsUnitBox)
    LinearLayout unitsBox;

    @Bind(R.id.settingsUnitsChoice)
    TextView unitsChoice;

    private String zipCodeEntered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        ButterKnife.bind(this);

        setActionBar();


        final SharedPreferences prefs = getSharedPreferences("UmbrellaSettings", Context.MODE_PRIVATE);
        zipCode.setText(prefs.getString("Zipcode", "46250"));
        if (prefs.getBoolean("Metric", false)) {
            unitsChoice.setText("Celsius");
        } else {
            unitsChoice.setText("Fahrenheit");
        }


        zipBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText contentView = new EditText(SettingsActivity.this);
                final MaterialDialog mMaterialDialog = new MaterialDialog(SettingsActivity.this).setView(contentView);
                mMaterialDialog.setPositiveButton("Save", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        zipCodeEntered = contentView.getText().toString();
                        if (isValidZip(zipCodeEntered)) {
                            prefs.edit().putString("Zipcode", zipCodeEntered).apply();
                            setZip();
                            mMaterialDialog.dismiss();
                        } else {
                            Toast.makeText(SettingsActivity.this, "Please enter a valid zipcode", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                mMaterialDialog.setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMaterialDialog.dismiss();
                    }
                });
                mMaterialDialog.show();


            }
        });

        unitsBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MaterialDialog mMaterialDialog = new MaterialDialog(SettingsActivity.this);
                mMaterialDialog.setPositiveButton("Fahrenheit", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        prefs.edit().putBoolean("Metric", false).apply();
                        setUnits();
                        mMaterialDialog.dismiss();
                    }
                }).setNegativeButton("Celsius", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        prefs.edit().putBoolean("Metric", true).apply();
                        setUnits();
                        mMaterialDialog.dismiss();
                    }
                });
                mMaterialDialog.setMessage("Choose units");
                mMaterialDialog.show();
            }
        });
    }

    private void setActionBar() {
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#757575")));
    }

    private Boolean isValidZip(String zip) {
       Pattern sPattern = Pattern.compile("^\\d{5}(?:[-\\s]\\d{4})?$");
        return sPattern.matcher(zip).matches();
    }

    private void setZip() {
        final SharedPreferences prefs = getSharedPreferences("UmbrellaSettings", Context.MODE_PRIVATE);

        String zip = prefs.getString("Zipcode", "46250");
        zipCode.setText(zip);
    }

    private void setUnits() {
        final SharedPreferences prefs = getSharedPreferences("UmbrellaSettings", Context.MODE_PRIVATE);

        Boolean units = prefs.getBoolean("Metric", false);
        if (units) {
            unitsChoice.setText("Celsius");
        } else {
            unitsChoice.setText("Fahrenheit");
        }
    }

    private void goToMain() {
        final SharedPreferences prefs = getSharedPreferences("UmbrellaSettings", Context.MODE_PRIVATE);
        prefs.edit().putBoolean("Refresh", true).apply();
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        SettingsActivity.this.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        goToMain();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMain();
    }
}
