package com.example.igiagante.trainingone.item;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.igiagante.trainingone.DescriptionActivity;
import com.example.igiagante.trainingone.MyAdapter;
import com.example.igiagante.trainingone.R;

import connections.Connection;
import model.Item;
import model.Search;
import services.SearchService;


public class ListItemsActivity extends Activity implements ListItemsFragment.ListItemsListener,
        MyAdapter.OnItemSelectedListener, ItemDetailFragment.ItemDetailListener
{

    private Integer limit = 10;
    private ListItemsFragment listItemsFragment;
    private ItemDetailFragment itemDetailFragment;
    private boolean mTwoPane;

    public static final String ITEM_PARAM = "ITEM_PARAM";

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                if(intent.getAction().equals(SearchService.NOTIFICATION)){
                    Search search = (Search) bundle.getParcelable(SearchService.RESULT);
                    if (search != null) {
                        listItemsFragment = (ListItemsFragment) getFragmentManager().findFragmentById(R.id.list_items);
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
    public void itemSelected(Item item) {
        if(mTwoPane){
            itemDetailFragment = (ItemDetailFragment) getFragmentManager().findFragmentById(R.id.item_detail);
            itemDetailFragment.setItem(item);
        }else{
            if(Connection.checkInternet(this)){
                Intent intent = new Intent(this, ItemActivity.class);
                intent.putExtra(ItemActivity.ITEM_PARAM, item);
                startActivity(intent);
            }else{
                Toast.makeText(this, "Internet is not avialable", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void getItemDescription(Item item) {
        Intent intent = new Intent(this, DescriptionActivity.class);
        intent.putExtra(ITEM_PARAM, item);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(SearchService.NOTIFICATION));

        itemDetailFragment = (ItemDetailFragment) getFragmentManager().findFragmentById(R.id.item_detail);

        if (itemDetailFragment == null || !itemDetailFragment.isInLayout()) {
            mTwoPane = false;
        }else{
            mTwoPane = true;
        }
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
