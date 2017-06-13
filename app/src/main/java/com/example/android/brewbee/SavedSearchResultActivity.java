package com.example.android.brewbee;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.brewbee.data.BrewSearchContract;
import com.example.android.brewbee.data.BrewSearchDBHelper;
import com.example.android.brewbee.utils.BreweryUtils;

import java.util.ArrayList;

/**
 * Created by Cameron on 6/12/2017.
 */

public class SavedSearchResultActivity extends AppCompatActivity implements BrewAdapter.OnSearchResultClickListener {

    private RecyclerView mSavedBeerRV;
    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_search_results);

        BrewSearchDBHelper dbHelper = new BrewSearchDBHelper(this);
        mDB = dbHelper.getReadableDatabase();

        ArrayList<BreweryUtils.BrewItem> searchResultList = getAllSavedSearchResults();
        BrewAdapter adapter = new BrewAdapter(this);
        adapter.updateSearchResults(searchResultList);

        mSavedBeerRV = (RecyclerView)findViewById(R.id.rv_saved_search_results);
        mSavedBeerRV.setLayoutManager(new LinearLayoutManager(this));
        mSavedBeerRV.setHasFixedSize(true);
        mSavedBeerRV.setAdapter(adapter);
    }

    @Override
    protected void onDestroy(){
        mDB.close();
        super.onDestroy();
    }

    public void onSearchResultClick(BreweryUtils.BrewItem brewItem){
        Intent intent = new Intent(this, SearchResultDetailActivity.class);
        intent.putExtra(BreweryUtils.BrewItem.EXTRA_SEARCH_RESULT, brewItem);
        startActivity(intent);
    }

    private ArrayList<BreweryUtils.BrewItem>getAllSavedSearchResults(){
        Cursor cursor = mDB.query(
                BrewSearchContract.FavoriteBrew.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                BrewSearchContract.FavoriteBrew.COLUMN_BEER_NAME + " DESC"
        );

        ArrayList<BreweryUtils.BrewItem> brewItemsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            BreweryUtils.BrewItem brewItem = new BreweryUtils.BrewItem();
            brewItem.fullname = cursor.getString(
              cursor.getColumnIndex(BrewSearchContract.FavoriteBrew.COLUMN_BEER_NAME)
            );
            brewItem.description = cursor.getString(
                    cursor.getColumnIndex(BrewSearchContract.FavoriteBrew.COLUMN_DESCRIPTION)
            );
            brewItem.abvMin = cursor.getFloat(
                    cursor.getColumnIndex(BrewSearchContract.FavoriteBrew.COLUMN_ABV_MIN)
            );
            brewItem.abvMax = cursor.getFloat(
                    cursor.getColumnIndex(BrewSearchContract.FavoriteBrew.COLUMN_ABV_MAX)
            );
            brewItemsList.add(brewItem);
        }
        cursor.close();
        return  brewItemsList;
    }

}
