package com.example.android.brewbee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.brewbee.R;
import com.example.android.brewbee.utils.BreweryUtils;

/**
 * Created by Bradley on 6/10/2017.
 */

public class SearchResultDetailActivity extends AppCompatActivity {
    private TextView mSearchResultNameTV;
    private TextView mSearchResultDescriptionTV;
    private TextView mSearchResultStarsTV;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_search_result_detail);

        mSearchResultNameTV = (TextView)findViewById(R.id.tv_search_result_name);
        mSearchResultDescriptionTV = (TextView)findViewById(R.id.tv_search_result_description);
 //       mSearchResultStarsTV = (TextView)findViewById(R.id.tv_search_result_stars);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(BreweryUtils.BrewItem.EXTRA_SEARCH_RESULT)){
            BreweryUtils.BrewItem searchResult = (BreweryUtils.BrewItem)intent.getSerializableExtra(BreweryUtils.BrewItem.EXTRA_SEARCH_RESULT);
            mSearchResultNameTV.setText(searchResult.fullname);
            mSearchResultDescriptionTV.setText(searchResult.description);
  //          mSearchResultStarsTV.setText(Integer.toString(searchResult.stars));
        }
    }
}
