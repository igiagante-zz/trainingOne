package services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import helper.Helper;
import model.Item;

/**
 * Created by igiagante on 10/7/15.
 */
public class ItemService extends IntentService {

    // TODO: Rename parameters
    public static final String ITEM = "ITEM";
    public static final String ITEM_DESCRIPTION = "ITEM_DESCRIPTION";

    // TODO: Rename actions, choose action names that describe tasks that this
    public static final String ACTION_GET_ITEM_DESCRIPTION = "ACTION_GET_ITEM_DESCRIPTION";

    //Notificacions
    public static final String NOTIFICATION_ITEM_READY = "ITEM_READY";

    public ItemService() {
        super("ItemService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_ITEM_DESCRIPTION.equals(action)) {
                final Item item = intent.getParcelableExtra(ITEM);
                String result = get(item.getItemId());
                publishResults(result);
            }
        }
    }

    private void publishResults(String result) {
        Intent intent = new Intent(NOTIFICATION_ITEM_READY);
        intent.putExtra(ITEM_DESCRIPTION, result);
        sendBroadcast(intent);
    }

    private String get(String itemId){

        try{
            String queryEncoded = URLEncoder.encode(itemId, "UTF-8");
            String dir = "https://api.mercadolibre.com/items/"+ queryEncoded + "/description";
            URL url = new URL(dir);

            String response = Helper.get(url);
            Log.d("response", response);

            JSONObject resultObject = new JSONObject(response);
            return resultObject.get("text").toString();

        }catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException ioe){
            System.out.println("Error: " + ioe.getMessage());
        }

        return "";
    }
}
