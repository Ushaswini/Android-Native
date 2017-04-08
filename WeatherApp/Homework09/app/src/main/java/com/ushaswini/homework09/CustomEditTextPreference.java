package com.ushaswini.homework09;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.app.AlertDialog;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * CustomEditTextPreference
 * 07/04/2017
 */

public class CustomEditTextPreference extends DialogPreference {

    EditText et_city;
    EditText et_country;

    Button btn_ok;
    Button btn_cancel;

    public CustomEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPersistent(false);
        setDialogLayoutResource(R.layout.alert_dialog_inputs);
    }

    protected View onCreateDialogView(){
        View v = LayoutInflater.from(getContext()).inflate(R.layout.alert_dialog_inputs,null);
        et_city = (EditText) v.findViewById(R.id.et_city);
        et_country = (EditText) v.findViewById(R.id.et_country);
        return v;
    }

    protected void onPrepareDialogBuilder(AlertDialog.Builder builder){
        builder.setTitle("Enter city details");
        builder.setPositiveButton(null,null);
        builder.setNegativeButton(null,null);
        super.onPrepareDialogBuilder(builder);
    }

    @Override
    protected void onBindDialogView(View view) {
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        et_city = (EditText) view.findViewById(R.id.et_city);
        et_country = (EditText) view.findViewById(R.id.et_country);
        btn_ok = (Button)view.findViewById(R.id.btn_ok);
        btn_cancel = (Button)view.findViewById(R.id.btn_cancel);

        if(myPrefs.getString(MainActivity.PREF_CITY_TAG,"").equals("")){
            btn_ok.setText("Set");

        }else{
            btn_ok.setText("Change");
            et_city.setText(myPrefs.getString(MainActivity.PREF_CITY_TAG,""));
            et_country.setText(myPrefs.getString(MainActivity.PREF_COUNTRY_TAG,""));
        }

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getEditor();
                editor.putString(MainActivity.PREF_CITY_TAG,et_city.getText().toString());
                editor.putString(MainActivity.PREF_COUNTRY_TAG,et_country.getText().toString());
                editor.commit();
                getDialog().dismiss();
            }
        });

        super.onBindDialogView(view);

    }
}
