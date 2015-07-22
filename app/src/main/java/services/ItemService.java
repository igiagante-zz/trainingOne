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

import dao.ItemDao;
import utils.HttpClient;
import model.Item;

/**
 * Created by igiagante on 10/7/15.
 */
public class ItemService extends IntentService {

    private Item item;
    private ItemDao itemDao;

    // TODO: Rename parameters
    public static final String ITEM = "ITEM";
    public static final String ITEM_ID = "ITEM_ID";

    // TODO: Rename actions, choose action names that describe tasks that this
    public static final String ACTION_ADD_EXTRA_DATA = "ACTION_ADD_EXTRA_DATA";
    public static final String ACTION_GET_ITEM = "ACTION_GET_ITEM";

    //Notificacions
    public static final String NOTIFICATION_ITEM_READY = "ITEM_READY";

    public ItemService() {
        super("ItemService");
        itemDao = new ItemDao(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_ADD_EXTRA_DATA.equals(action)) {
                item = intent.getParcelableExtra(ITEM);
                setImageUrl(item);
                setDescription(item);
                publishResults(item);
            }
            if(ACTION_GET_ITEM.equals(action)){
                String itemId = intent.getStringExtra(ITEM_ID);
                JSONObject resultObject = getItem(itemId);
                item = parseJson(resultObject);
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
            JSONObject resultObject = getItem(item.getItemId());
            JSONArray pictures = resultObject.getJSONArray("pictures");
            JSONObject first = pictures.getJSONObject(0);
            String urlJSON = first.getString("url");

            item.setImageUrl(urlJSON);

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setDescription(Item item){

        try{
            JSONObject resultObject = getItem(item.getItemId());
            item.setDescription(resultObject.get("text").toString());

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject getItem(String itemId){

        try{
            String queryEncoded = URLEncoder.encode(itemId, "UTF-8");
            String dir = "https://api.mercadolibre.com/items/"+ queryEncoded;
            URL url = new URL(dir);

            String response = HttpClient.get(url);
            Log.d("response", response);

            return new JSONObject(response);

        }catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException ioe){
            System.out.println("Error: " + ioe.getMessage());
        }
        return null;
    }

    private Item parseJson(JSONObject resultObject){
        Item item = new Item();
        try{
            item.setItemId(resultObject.getString("id"));
            item.setPrice(resultObject.getString("price"));
            item.setExpirationDate(resultObject.getString("stop_time"));
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return item;
    }

    private String checkPrice(){
        Item itemDB = itemDao.getItem(item.getItemId());
        if(!item.getPrice().equals(itemDB.getPrice()))
            return "Price was modified";
        return "";
    }

    private String checkExpirationDate(){
        Item itemDB = itemDao.getItem(item.getItemId());
        if(!item.getExpirationDate().equals(itemDB.getExpirationDate()))
            return "Expiration Date was modified";
        return "";
    }
}
