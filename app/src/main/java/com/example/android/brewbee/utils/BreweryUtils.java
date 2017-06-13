package com.example.android.brewbee.utils;

/**
 * Created by anish on 6/8/17.
 */

import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

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
    private final static String BREWERY_SEARCH = "search";
    private final static String BREWERY_HAS = "hasBreweries";

    private final static String BREWERY_APPID = "7c166ab511e0516204c3a689b0f53de2";



    public static class BrewItem implements Serializable {
        public static final String EXTRA_SEARCH_RESULT = "BreweryUtils.SearchResult";
        public String brewID = "";
        public String fullname = "";
        public String description = "";
        public Float abvMin = Float.valueOf(0);
        public Float abvMax = Float.valueOf(0);
        public String brewery = "";
        public String logo = "";
        public String website = "";
    }

    public static String buildForecastURL(String brewSearch) {

        Log.d("BrewUtil", "We got a query - " + brewSearch);

        return Uri.parse(BREWERY_BASE_URL).buildUpon()
                    .appendPath(BREWERY_SEARCH)
                    .appendQueryParameter(BREWERY_QUERY_PARAM, brewSearch)
                    .appendQueryParameter(BREWERY_KEYWORD, BREWERY_APPID)
   //                 .appendQueryParameter(BREWERY_HAS, "Y")
                    .appendQueryParameter("format", "json" )
                    .build()
                    .toString();
    }

    public static ArrayList<BrewItem> parseBrewSearchResultsJSON(String searchResultsJSON) {
        try {
            JSONObject searchResultsObj = new JSONObject(searchResultsJSON);
            JSONArray searchResultsItems = searchResultsObj.getJSONArray("data");

            String websiteURL = "";
            JSONObject searchResultItem = searchResultsItems.getJSONObject(0);
            if(searchResultItem.has("website")) {
                websiteURL = searchResultItem.getString("website");
                Log.d("Web", "Got website - " + websiteURL);
            }

            ArrayList<BrewItem> searchResultsList = new ArrayList<BrewItem>();
            for (int i = 1; i < searchResultsItems.getJSONObject(0).length(); i++) {
                BrewItem searchResult = new BrewItem();
                searchResultItem = searchResultsItems.getJSONObject(i);

                if (searchResultItem.has("description")) {
                    searchResult.description = searchResultItem.getString("description");
                }

                Log.d("BrewDesc", "Got description " + searchResult.description);

                if (searchResultItem.has("id")) {
                    searchResult.brewID = searchResultItem.getString("id");
                }
                Log.d("BrewID", "Got ID " + searchResult.brewID);

                if (searchResultItem.has("name")) {
                    searchResult.fullname = searchResultItem.getString("name");
                }
                Log.d("BrewName", "Got Name " + searchResult.fullname);

                if(searchResultItem.has("labels")) {
                    JSONObject iconInfo = searchResultItem.getJSONObject("labels");

                    if (iconInfo.has("medium")) {
                        searchResult.logo = iconInfo.getString("medium");
                    }

                    Log.d("BrewImg", "Got link to logo - " + searchResult.logo);

                    if (searchResultItem.has("style")) {
                        JSONObject styleInfo = searchResultItem.getJSONObject("style");

                        if (styleInfo.has("abvMin")) {
                            searchResult.abvMin = Float.valueOf(styleInfo.getString("abvMin"));
                        }
                        Log.d("AbvMin", "Got MinABV " + searchResult.abvMin);

                        if (styleInfo.has("abvMax")) {
                            searchResult.abvMax = Float.valueOf(styleInfo.getString("abvMax"));
                        }
                        Log.d("AbvMax", "Got MaxABV " + searchResult.abvMax);
                    }

                }

                searchResult.website = websiteURL;



                searchResultsList.add(searchResult);
            }
            return searchResultsList;
        } catch (JSONException e) {
            return null;
        }
    }


}


