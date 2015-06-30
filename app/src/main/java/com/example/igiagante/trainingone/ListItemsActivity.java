package com.example.igiagante.trainingone;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import model.Item;
import model.Search;
import services.SearchService;


public class ListItemsActivity extends Activity {

    private ArrayList<Item> items;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Search search = (Search) bundle.getParcelable(SearchService.RESULT);
                if (search != null) {

                    Log.d("Title", search.getItems().get(0).getTitle());

                    items = search.getItems();

                    setContentView(R.layout.activity_list_items);
                    mRecyclerView = (RecyclerView) findViewById(R.id.list_item_view);

                    mAdapter = new MyAdapter(items);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    Log.d("items", "not found");
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get param from search activity
        Intent intent = getIntent();
        String query = intent.getStringExtra(SearchService.SEARCH_PARAM);

        Intent intentService = new Intent(this, SearchService.class);
        intentService.setAction(SearchService.ACTION_SEARCH);
        intentService.putExtra(SearchService.SEARCH_PARAM, query);
        startService(intentService);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}
