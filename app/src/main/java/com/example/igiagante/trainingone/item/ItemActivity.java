package com.example.igiagante.trainingone.item;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.igiagante.trainingone.DescriptionActivity;
import com.example.igiagante.trainingone.R;

import model.Item;

/**
 * Created by igiagante on 10/7/15.
 */
public class ItemActivity extends Activity implements ItemDetailFragment.ItemDetailListener{

    private Item item;
    private ItemDetailFragment itemDetailFragment;

    public static final String ITEM_PARAM = "ITEM_PARAM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        itemDetailFragment = (ItemDetailFragment) getFragmentManager().findFragmentByTag("fragment_item_detail");

        if(itemDetailFragment == null){
            itemDetailFragment = new ItemDetailFragment();
            getFragmentManager().beginTransaction().replace(R.id.container_item_detail, itemDetailFragment, "fragment_item_detail").commit();
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            item = (Item) bundle.getParcelable(ITEM_PARAM);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemDetailFragment.setItem(item);
    }

    @Override
    public void getItemDescription(Item item) {
        Intent intent = new Intent(this, DescriptionActivity.class);
        intent.putExtra(ITEM_PARAM, item);
        startActivity(intent);
    }
}
