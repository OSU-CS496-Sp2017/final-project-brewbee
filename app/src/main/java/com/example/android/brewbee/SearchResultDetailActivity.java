package com.example.android.brewbee;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.brewbee.R;
import com.example.android.brewbee.data.BrewSearchContract;
import com.example.android.brewbee.data.BrewSearchDBHelper;
import com.example.android.brewbee.utils.BreweryUtils;

/**
 * Created by Bradley on 6/10/2017.
 */

public class SearchResultDetailActivity extends AppCompatActivity {
    private TextView mSearchResultNameTV;
    private TextView mSearchResultDescriptionTV;
    private TextView mSearchResultStarsTV;
    private BreweryUtils.BrewItem mBrewItem;
    private SQLiteDatabase mDB;
    private boolean mFavorited;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_search_result_detail);

        mSearchResultNameTV = (TextView)findViewById(R.id.tv_search_result_name);
        mSearchResultDescriptionTV = (TextView)findViewById(R.id.tv_search_result_description);
 //       mSearchResultStarsTV = (TextView)findViewById(R.id.tv_search_result_stars);

        BrewSearchDBHelper dbHelper = new BrewSearchDBHelper(this);
        mDB = dbHelper.getWritableDatabase();

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(BreweryUtils.BrewItem.EXTRA_SEARCH_RESULT)){

            mBrewItem = (BreweryUtils.BrewItem)intent.getSerializableExtra(BreweryUtils.BrewItem.EXTRA_SEARCH_RESULT);
            mSearchResultNameTV.setText(mBrewItem.fullname);
            mSearchResultDescriptionTV.setText(mBrewItem.description);
            //mSearchResultStarsTV.setText(Integer.toString(mBrewItem.stars));

            mFavorited = checkBrewIsInDB();
        }
    }

    private boolean checkBrewIsInDB(){
        boolean isInDB = false;
        if(mBrewItem  != null){
            String sqlSelection = BrewSearchContract.FavoriteBrew.COLUMN_BEER_NAME + " = ?";
            String[] sqlSelectionArgs = {mBrewItem.fullname};
            Cursor cursor = mDB.query(
                    BrewSearchContract.FavoriteBrew.TABLE_NAME,
                    null,
                    sqlSelection,
                    sqlSelectionArgs,
                    null,
                    null,
                    null
            );
            isInDB = cursor.getCount() > 0;
            cursor.close();

        }
        return isInDB;
    }
}
