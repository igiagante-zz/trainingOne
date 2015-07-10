package com.example.igiagante.trainingone;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import imageloader.ImageLoader;
import model.Item;
import services.ItemService;

/**
 * Created by igiagante on 10/7/15.
 */
public class ItemActivity extends Activity{

    private ImageLoader imageLoader;
    private Context context;
    static final String ITEM_PARAM = "ITEM_PARAM";


    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ItemService.NOTIFICATION_ITEM_READY)){
                String description = intent.getStringExtra(ItemService.ITEM_DESCRIPTION);
                TextView textView = (TextView)findViewById(R.id.item_description);
                textView.setText(description);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        if (bundle != null) {

            Item item = (Item) bundle.getParcelable(ITEM_PARAM);

            TextView textView = (TextView)findViewById(R.id.item_title);
            textView.setText(item.getTitle());

            Bitmap placeholderBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.placeholder);
            imageLoader = new ImageLoader(placeholderBitmap);

            ImageView imageView = (ImageView) findViewById(R.id.item_image);
            imageLoader.displayImage(item.getThumbnail(), imageView);

            getItemDescription(item);
        }
    }

    public void getItemDescription(Item item){
        Intent intentService = new Intent(this, ItemService.class);
        intentService.setAction(ItemService.ACTION_GET_ITEM_DESCRIPTION);
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
}
