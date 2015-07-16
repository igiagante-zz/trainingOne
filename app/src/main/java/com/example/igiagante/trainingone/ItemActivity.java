package com.example.igiagante.trainingone;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import connections.Connection;
import imageloader.ImageLoader;
import model.Item;
import services.ItemService;

/**
 * Created by igiagante on 10/7/15.
 */
public class ItemActivity extends Activity{

    private Item item;

    static final String ITEM_PARAM = "ITEM_PARAM";

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, Intent intent) {
            if(intent.getAction().equals(ItemService.NOTIFICATION_ITEM_READY)){

                item = intent.getParcelableExtra(ItemService.ITEM);

                ImageView imageView = (ImageView) findViewById(R.id.item_image);
                ImageLoader.INSTANCE.displayImage(item.getImageUrl(), imageView, true);

                ProgressBar pb = (ProgressBar) findViewById(R.id.item_progress_bar);
                pb.setVisibility(View.INVISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item);

        Button button = (Button) findViewById(R.id.item_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DescriptionActivity.class);
                intent.putExtra(ITEM_PARAM, item);
                startActivity(intent);
            }
        });

        if(savedInstanceState != null){

            item = savedInstanceState.getParcelable("item");

            ImageView imageView = (ImageView) findViewById(R.id.item_image);
            ImageLoader.INSTANCE.displayImage(item.getImageUrl(), imageView, true);

            ProgressBar pb = (ProgressBar) findViewById(R.id.item_progress_bar);
            pb.setVisibility(View.INVISIBLE);

        }else{

            ProgressBar pb = (ProgressBar) findViewById(R.id.item_progress_bar);
            pb.setVisibility(View.VISIBLE);

            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                item = (Item) bundle.getParcelable(ITEM_PARAM);
            }

            getItemDescription(item);
        }

        TextView textView = (TextView)findViewById(R.id.item_title);
        textView.setText(item.getTitle());
    }

    public void getItemDescription(Item item){
        Intent intentService = new Intent(this, ItemService.class);
        intentService.setAction(ItemService.ACTION_GET_ITEM);
        intentService.putExtra(ItemService.ITEM, item);
        startService(intentService);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(ItemService.NOTIFICATION_ITEM_READY));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("item", item);
    }
}
