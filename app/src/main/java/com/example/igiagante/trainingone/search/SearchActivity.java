package com.example.igiagante.trainingone.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.igiagante.trainingone.R;
import com.example.igiagante.trainingone.item.ListItemsActivity;

import services.SearchService;


public class SearchActivity extends Activity implements SearchFragment.SearchFragmentListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    public void onClickSearchButton(String query) {
        search(query);
    }

    /**
     * Initialize ListItemActivity in order to list the result obtained through the searched.
     * @param query
     */
    public void search(String query) {
        Intent intent = new Intent(this, ListItemsActivity.class);
        intent.putExtra(SearchService.SEARCH_PARAM, query);
        startActivity(intent);
    }
}
