package com.example.android.brewbee;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.brewbee.utils.BreweryUtils;
import com.example.android.brewbee.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements BrewAdapter.OnSearchResultClickListener, LoaderManager.LoaderCallbacks<String>, NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SEARCH_RESULTS_LIST_KEY = "searchResultsList";
    private static final String SEARCH_URL_KEY = "brewSearchURL";
    private static final int BREW_SEARCH_LOADER_ID = 0;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private RecyclerView mSearchResultsRV;
    private EditText mSearchBoxET;
    private ProgressBar mLoadingIndicatorPB;
    private TextView mLoadingErrorMessageTV;
    private BrewAdapter mBrewSearchAdapter;

    private ArrayList<BreweryUtils.BrewItem> mSearchResultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    //    setContentView(R.layout.activity_main);
        mSearchResultsList = null;

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mSearchBoxET = (EditText)findViewById(R.id.et_search_box);
        mLoadingIndicatorPB = (ProgressBar)findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = (TextView)findViewById(R.id.tv_loading_error_message);
        mSearchResultsRV = (RecyclerView)findViewById(R.id.rv_search_results);

        mSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultsRV.setHasFixedSize(true);

        mBrewSearchAdapter = new BrewAdapter(this);
        mSearchResultsRV.setAdapter(mBrewSearchAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        getSupportLoaderManager().initLoader(BREW_SEARCH_LOADER_ID, null, this);

        Button searchButton = (Button)findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String searchQuery = mSearchBoxET.getText().toString();
                if(!TextUtils.isEmpty(searchQuery)){
                    doBrewSearch(searchQuery);
                }
            }
        });
    }

    private void doBrewSearch(String searchQuery) {
        String brewSearchURL = BreweryUtils.buildForecastURL(searchQuery);

        Bundle argsBundle = new Bundle();
        argsBundle.putString(SEARCH_URL_KEY, brewSearchURL);
        getSupportLoaderManager().restartLoader(BREW_SEARCH_LOADER_ID, argsBundle, this);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSearchResultClick(BreweryUtils.BrewItem searchResult) {
        Intent intent = new Intent(this, SearchResultDetailActivity.class);
        intent.putExtra(BreweryUtils.BrewItem.EXTRA_SEARCH_RESULT, searchResult);
        startActivity(intent);
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this){
            String mSearchResultsJSON;

            @Override
            protected void onStartLoading(){
                if (args != null){
                    if(mSearchResultsJSON != null){
                        Log.d(TAG, "AsyncTaskLoader delivering cahced results");
                        deliverResult(mSearchResultsJSON);
                    } else {
                        mLoadingIndicatorPB.setVisibility(View.VISIBLE);
                        forceLoad();
                    }
                }
            }

            @Override
            public String loadInBackground() {
                if(args != null){
                    String brewSearchURL = args.getString(SEARCH_URL_KEY);
                    Log.d(TAG, "AsyncTaskLoader making network call:" + brewSearchURL);
                    String searchResults = null;
                    try{
                        searchResults = NetworkUtils.doHTPPGet(brewSearchURL);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return searchResults;

                } else {
                    return null;
                }
            }

            @Override
            public void deliverResult(String data){
                mSearchResultsJSON = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, "AsyincTaskLoader's onLoadFinished called");
        mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
        if(data != null){
            mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
            mSearchResultsRV.setVisibility(View.VISIBLE);
            mSearchResultsList = BreweryUtils.parseBrewSearchResultsJSON(data);
            mBrewSearchAdapter.updateSearchResults(mSearchResultsList);
        } else {
            mSearchResultsRV.setVisibility(View.INVISIBLE);
            mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.nav_search:
                mDrawerLayout.closeDrawers();
                return true;
            case R.id.nav_saved_results:
                mDrawerLayout.closeDrawers();
                Intent savedResultsIntent = new Intent(this, SavedSearchResultActivity.class);
                startActivity(savedResultsIntent);
                return true;
            default:
                return false;
        }
    }

}
