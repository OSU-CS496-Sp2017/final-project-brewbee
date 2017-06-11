package com.example.android.brewbee.data;

import android.provider.BaseColumns;

/**
 * Created by Cameron on 6/10/2017.
 */

public class BrewSearchContract {
    private BrewSearchContract(){}

    public static class FavoriteBrew implements BaseColumns {
        public static final String TABLE_NAME = "favoriteBrew";
        public static final String COLUMN_BEER_NAME = "beerName";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_BREWERY = "brewery";
        public static final String COLUMN_API_ID = "apiId";
        public static final String COLUMN_ABV_MIN = "abvMin";
        public static final String COLUMN_ABV_MAX = "abvMax";
    }
}
