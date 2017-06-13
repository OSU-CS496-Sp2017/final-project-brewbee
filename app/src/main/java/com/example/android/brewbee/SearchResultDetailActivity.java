package com.example.android.brewbee;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.method.LinkMovementMethod;

import com.example.android.brewbee.R;
import com.example.android.brewbee.data.BrewSearchContract;
import com.example.android.brewbee.data.BrewSearchDBHelper;
import com.example.android.brewbee.utils.BreweryUtils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * Created by Bradley on 6/10/2017.
 */

public class SearchResultDetailActivity extends AppCompatActivity {
    private ImageView mFavoriteIV;
    private TextView mSearchResultNameTV;
    private TextView mSearchResultDescriptionTV;
    private TextView mAbvTV;
    private TextView mSearchResultsLinkTV;
    private BreweryUtils.BrewItem mBrewItem;
    private SQLiteDatabase mDB;
    private boolean mIsFavorited;
    private ImageView mLogo;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_search_result_detail);

        mLogo = (ImageView)findViewById(R.id.iv_beer_logo);
        mFavoriteIV = (ImageView)findViewById(R.id.iv_search_result_favorite);
        mSearchResultNameTV = (TextView)findViewById(R.id.tv_search_result_name);
        mAbvTV = (TextView)findViewById(R.id.tv_search_result_abv);
        mSearchResultsLinkTV = (TextView)findViewById(R.id.tv_search_result_link);
        mSearchResultDescriptionTV = (TextView)findViewById(R.id.tv_search_result_description);
        mSearchResultDescriptionTV.setMovementMethod(new ScrollingMovementMethod());
        BrewSearchDBHelper dbHelper = new BrewSearchDBHelper(this);
        mDB = dbHelper.getWritableDatabase();
        Log.d("Intent", "outside if");
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(BreweryUtils.BrewItem.EXTRA_SEARCH_RESULT)){
            Log.d("Intent", "inside if");
            mBrewItem = (BreweryUtils.BrewItem)intent.getSerializableExtra(BreweryUtils.BrewItem.EXTRA_SEARCH_RESULT);
            mSearchResultNameTV.setText(mBrewItem.fullname);
            mSearchResultDescriptionTV.setText(mBrewItem.description);
            mAbvTV.setText("ABV: " + mBrewItem.abvMin + " - " + mBrewItem.abvMax + "%");

            if(mBrewItem.website != "") {
                mSearchResultsLinkTV.setText("Website: " + mBrewItem.website);
            }

            if(mBrewItem.logo != "") {
                Picasso.with(this).load(mBrewItem.logo).into(mLogo);
            }
            mIsFavorited = checkBrewIsInDB();
            updateFavoriteIconState();
        }

        mFavoriteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mIsFavorited = !mIsFavorited;
                updateFavoriteInDB();
                updateFavoriteIconState();
            }
        });
    }

    private void updateFavoriteIconState(){
        if(mIsFavorited){
            mFavoriteIV.setImageResource(R.drawable.ic_star_yellow);
        }
        else{
            mFavoriteIV.setImageResource(R.drawable.ic_star_border_pink);
        }
    }

    private void updateFavoriteInDB(){
        if(mIsFavorited){
            addFavoriteToDB();
        }
        else{
            deleteFavoriteFromDB();
        }
    }

    private long addFavoriteToDB() {
        if(mBrewItem != null){
            ContentValues values = new ContentValues();
            values.put(BrewSearchContract.FavoriteBrew.COLUMN_BEER_NAME, mBrewItem.fullname);
            values.put(BrewSearchContract.FavoriteBrew.COLUMN_DESCRIPTION, mBrewItem.description);
            values.put(BrewSearchContract.FavoriteBrew.COLUMN_API_ID, mBrewItem.brewID);
            values.put(BrewSearchContract.FavoriteBrew.COLUMN_ABV_MIN, mBrewItem.abvMin);
            values.put(BrewSearchContract.FavoriteBrew.COLUMN_ABV_MAX, mBrewItem.abvMax);
            values.put(BrewSearchContract.FavoriteBrew.COLUMN_BREWERY, mBrewItem.brewery);
            return mDB.insert(BrewSearchContract.FavoriteBrew.TABLE_NAME, null, values);
        }
        else{
            Log.d("addFavoriteToDB says: ", "Beer not added");
            return -1;
        }
    }

    private void deleteFavoriteFromDB(){
        if(mBrewItem != null){
            String sqlSelection = BrewSearchContract.FavoriteBrew.COLUMN_BEER_NAME + " = ?";
            String[] sqlSelectionArgs = { mBrewItem.brewID };
            mDB.delete(BrewSearchContract.FavoriteBrew.TABLE_NAME, sqlSelection, sqlSelectionArgs);
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


    //share location stuff
    public void showForecastLocation() {
        /*Uri geoUri = Uri.parse("geo:0,0").buildUpon()
                .appendQueryParameter("q","temp" )
                .build();
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoUri);*/

        /*if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }*/

        if (mBrewItem != null) {
            //Log.d(TAG, "UnitSendOut inside shareForecast value: " + mUnitValue);
            String shareText = mBrewItem.fullname +
                    ": ABV: " + mBrewItem.abvMin +
                    //WeatherPreferences.getDefaultTemperatureUnitsAbbr() +
                    " - " +
                    mBrewItem.abvMax +
                    "%\n \n" + mBrewItem.description
                    ;
            ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText(shareText)
                    .setChooserTitle(R.string.share_chooser_title)
                    .startChooser();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.brew_item_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                showForecastLocation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
