package com.example.igiagante.trainingone.item;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.example.igiagante.trainingone.R;

import model.Search;
import services.SearchService;


public class ListItemsActivity extends Activity implements ListItemsFragment.ListItemsListener{

    private Integer limit = 10;
    private ListItemsFragment listItemsFragment;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                if(intent.getAction().equals(SearchService.NOTIFICATION)){
                    Search search = (Search) bundle.getParcelable(SearchService.RESULT);
                    if (search != null) {
                        listItemsFragment = (ListItemsFragment) getFragmentManager().findFragmentByTag("fragment_list_items");
                        if(listItemsFragment != null){
                            listItemsFragment.addMoreItems(search.getItems());
                        }
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
        setContentView(R.layout.activity_list_items);

        Log.d("start", "ListItemsActivity");

        listItemsFragment = (ListItemsFragment) getFragmentManager().findFragmentByTag("fragment_list_items");

        if(listItemsFragment == null){
            Log.d("fragment", "is not null");
            listItemsFragment = new ListItemsFragment();
            getFragmentManager().beginTransaction().add(R.id.container_list_items, listItemsFragment, "fragment_list_items").commit();
            Log.d("fragment", "readey");
        }
    }

    @Override
    public void getItemListData(String offset) {
        getData(offset, String.valueOf(limit));
    }

    @Override
    public void loadMoreItems(String offset) {
        getData(offset, String.valueOf(limit));
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
