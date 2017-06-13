package com.example.android.brewbee;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.graphics.Color;

import com.example.android.brewbee.utils.BreweryUtils;

import java.util.ArrayList;

/**
 * Created by anish on 6/8/17.
 */

public class BrewAdapter extends RecyclerView.Adapter<BrewAdapter.SearchResultViewHolder> {
    private ArrayList<BreweryUtils.BrewItem> mSearchResultsList;
    private OnSearchResultClickListener mSearchResultClickListener;

    public BrewAdapter(OnSearchResultClickListener clickListener){
        mSearchResultClickListener = clickListener;
    }

    public void updateSearchResults(ArrayList<BreweryUtils.BrewItem> searchResultsList){
        mSearchResultsList = searchResultsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mSearchResultsList != null) {
            return mSearchResultsList.size();
        }
        else {
            return 0;
        }

    }
    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.search_result_item, parent, false);
        return new SearchResultViewHolder(view);
    }


    @Override
    public void onBindViewHolder(SearchResultViewHolder holder, int position) {
        holder.bind(mSearchResultsList.get(position));
        holder.mSearchResultTV.setTextColor(Color.parseColor("#ffffff"));
    }


    public interface OnSearchResultClickListener {
        void onSearchResultClick(BreweryUtils.BrewItem searchResult);
    }

    class SearchResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mSearchResultTV;

        public SearchResultViewHolder(View itemView){
            super(itemView);
            mSearchResultTV = (TextView)itemView.findViewById(R.id.tv_search_result);
            itemView.setOnClickListener(this);
        }

        public void bind(BreweryUtils.BrewItem searchResult) {
            mSearchResultTV.setText(searchResult.fullname);
        }

        public void onClick(View v){
            BreweryUtils.BrewItem searchResult = mSearchResultsList.get(getAdapterPosition());
            mSearchResultClickListener.onSearchResultClick(searchResult);
        }
    }
}
