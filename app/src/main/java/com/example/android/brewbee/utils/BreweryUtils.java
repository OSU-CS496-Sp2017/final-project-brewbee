package com.example.android.brewbee.utils;

/**
 * Created by anish on 6/8/17.
 */

import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.preference.PreferenceManager;

//import com.example.android.brewbee.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class BreweryUtils {

    private static final String BREWERY_BASE_URL = "http://api.brewerydb.com/v2/";
    private final static String BREWERY_QUERY_PARAM = "q";
    private final static String BREWERY_KEYWORD = "key";
    private final static String BREWERY_BEER = "beer";

    private final static String BREWERY_APPID = "7c166ab511e0516204c3a689b0f53de2";

    public static class BrewItem implements Serializable {

    }

    public static String buildForecastURL(String brewSearch) {

        return Uri.parse(BREWERY_BASE_URL).buildUpon()
                    .appendQueryParameter(BREWERY_QUERY_PARAM, brewSearch)
                    .appendQueryParameter(BREWERY_KEYWORD, BREWERY_APPID)
                    .build()
                    .toString();
    }


}


