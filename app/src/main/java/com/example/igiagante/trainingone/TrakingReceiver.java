package com.example.igiagante.trainingone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import services.ItemService;

/**
 * Created by igiagante on 23/7/15.
 */
public class TrakingReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intentService = new Intent(context, ItemService.class);
        intentService.setAction(ItemService.ACTION_CHECK_ITEM);
        context.startService(intentService);
    }
}
