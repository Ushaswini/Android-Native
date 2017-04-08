package com.ushaswini.homework09;


import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import static com.ushaswini.homework09.MainActivity.DEGREE_CELECIUS_SIGN_UNICODE;
import static com.ushaswini.homework09.MainActivity.DEGREE_FAHRENHEIT_SIGN_UNICODE;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * PreferenceActivity
 * 07/04/2017
 */

public class PreferenceActivity extends android.preference.PreferenceActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.preference_fragment);

        ListPreference pref= (ListPreference) findPreference("temperatureUnit");

        pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.d("new value",newValue.toString());
                String output ="";
                if(newValue.toString().equals("0")){
                    output = "Temperature Unit has been changed from" + DEGREE_FAHRENHEIT_SIGN_UNICODE +
                                        " to "+ DEGREE_CELECIUS_SIGN_UNICODE;
                }else{
                    output = "Temperature Unit has been changed from" + DEGREE_CELECIUS_SIGN_UNICODE +" to " + DEGREE_FAHRENHEIT_SIGN_UNICODE;
                }
                Toast.makeText(PreferenceActivity.this,output,Toast.LENGTH_SHORT).show();
                return true;
            }
        });


    }
}
