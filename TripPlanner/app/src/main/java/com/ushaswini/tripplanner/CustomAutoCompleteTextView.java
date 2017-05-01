package com.ushaswini.tripplanner;


import android.content.Context;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.Log;

import java.util.HashMap;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * CustomAutoCompleteTextView
 * 29/04/2017
 */

public class CustomAutoCompleteTextView extends AppCompatAutoCompleteTextView {

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** Returns the place description corresponding to the selected item */
    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        /** Each item in the autocompetetextview suggestion list is a hashmap object */
        HashMap<String, String> hm = (HashMap<String, String>) selectedItem;

        Log.d("demo",hm.toString());

        return hm.get("description");
    }
}