package com.example.igiagante.trainingone;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import connections.Connection;
import model.Item;
import model.Search;
import services.ItemService;
import services.SearchService;
import views.EndlessRecyclerOnScrollListener;


public class ListItemsActivity extends Activity {

    private ArrayList<Item> items;

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    private Integer offset = 0;
    private Integer limit = 10;

    private ProgressBar pb;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                if(intent.getAction().equals(SearchService.NOTIFICATION)){
                    Search search = (Search) bundle.getParcelable(SearchService.RESULT);
                    if (search != null) {
                        addMoreItems(search.getItems());
                        mAdapter.setItems(items);
                        mAdapter.notifyDataSetChanged();

                        ProgressBar pb = (ProgressBar) findViewById(R.id.progress_bar);
                        pb.setVisibility(View.INVISIBLE);
                    } else {
                        Log.d("items", "not found");
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        pb = (ProgressBar) findViewById(R.id.progress_bar);

        if( savedInstanceState != null ) {
            items = savedInstanceState.getParcelableArrayList("items");
            pb.setVisibility(View.INVISIBLE);
        }else{
            pb.setVisibility(View.VISIBLE);
            getData(String.valueOf(offset), String.valueOf(limit));
        }

        mAdapter = new MyAdapter(items, this);
        mRecyclerView.setAdapter(mAdapter);

        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(mLayoutManager) {

            @Override
            public void onLoadMore(int current_page) {
                if(Connection.checkInternet(getApplicationContext())){
                    offset += 10 * current_page;
                    getData(String.valueOf(offset), String.valueOf(limit));
                }else{
                    Toast.makeText(getApplicationContext(), "Internet is not avialable", Toast.LENGTH_LONG).show();
                }
            }
        };

        mRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    private void addMoreItems(ArrayList<Item> items){
        if(this.items == null){
            this.items = new ArrayList<>();
        }
        this.items.addAll(items);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar card_view clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(SearchService.NOTIFICATION));
        endlessRecyclerOnScrollListener.reset(0, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("items", items);
    }

    private void getData(String offset, String limit){
        Intent intent = getIntent();
        String query = intent.getStringExtra(SearchService.SEARCH_PARAM);

        Intent intentService = new Intent(this, SearchService.class);
        intentService.setAction(SearchService.ACTION_SEARCH);
        intentService.putExtra(SearchService.SEARCH_PARAM, query);
        intentService.putExtra(SearchService.OFFSET_PARAM, offset);
        intentService.putExtra(SearchService.LIMIT_PARAM, limit);
        startService(intentService);
    }
}
