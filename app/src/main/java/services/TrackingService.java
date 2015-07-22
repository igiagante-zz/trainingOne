package services;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by igiagante on 22/7/15.
 */
public class TrackingService extends IntentService {

    public static final String ITEM_ID_PARAM = "ITEM_ID_PARAM";

    public TrackingService(){
        super("TrackingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String itemId = intent.getStringExtra(ITEM_ID_PARAM);
    }


}
