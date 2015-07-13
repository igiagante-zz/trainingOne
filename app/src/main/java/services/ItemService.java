package services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
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

    // TODO: Rename actions, choose action names that describe tasks that this
    public static final String ACTION_GET_ITEM = "ACTION_GET_ITEM";

    //Notificacions
    public static final String NOTIFICATION_ITEM_READY = "ITEM_READY";

    public ItemService() {
        super("ItemService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_ITEM.equals(action)) {
                final Item item = intent.getParcelableExtra(ITEM);
                setImageUrl(item);
                setDescription(item);
                publishResults(item);
            }
        }
    }

    private void publishResults(Item result) {
        Intent intent = new Intent(NOTIFICATION_ITEM_READY);
        intent.putExtra(ITEM, result);
        sendBroadcast(intent);
    }

    private void setImageUrl(Item item){

        try{
            String queryEncoded = URLEncoder.encode(item.getItemId(), "UTF-8");
            String dir = "https://api.mercadolibre.com/items/"+ queryEncoded;
            URL url = new URL(dir);

            String response = Helper.get(url);
            Log.d("response", response);

            JSONObject resultObject = new JSONObject(response);
            JSONArray pictures = resultObject.getJSONArray("pictures");
            JSONObject first = pictures.getJSONObject(0);
            String urlJSON = first.getString("url");
            item.setImageUrl(urlJSON);

        }catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException ioe){
            System.out.println("Error: " + ioe.getMessage());
        }
    }

    private void setDescription(Item item){

        try{
            String queryEncoded = URLEncoder.encode(item.getItemId(), "UTF-8");
            String dir = "https://api.mercadolibre.com/items/"+ queryEncoded + "/description";
            URL url = new URL(dir);

            String response = Helper.get(url);
            Log.d("response", response);

            JSONObject resultObject = new JSONObject(response);
            item.setDescription(resultObject.get("text").toString());

        }catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException ioe){
            System.out.println("Error: " + ioe.getMessage());
        }
    }
}
