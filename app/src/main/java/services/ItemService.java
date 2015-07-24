package services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.example.igiagante.trainingone.R;
import com.example.igiagante.trainingone.item.ItemActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dao.ItemDao;
import model.Item;
import utils.HttpClient;

/**
 * Created by igiagante on 10/7/15.
 */
public class ItemService extends IntentService {

    private Item item;
    private List<Item> items;
    private ItemDao itemDao;
    private Map<String, Item> itemsChanged = new HashMap<>();
    private Map<String, String> itemsChangedMessages = new HashMap<>();

    // TODO: Rename parameters
    public static final String ITEM = "ITEM";
    public static final String ITEM_ID = "ITEM_ID";

    // TODO: Rename actions, choose action names that describe tasks that this
    public static final String ACTION_ADD_EXTRA_DATA = "ACTION_ADD_EXTRA_DATA";
    public static final String ACTION_CHECK_ITEM = "ACTION_CHECK_ITEM";

    //Notificacions
    public static final String NOTIFICATION_ITEM_READY = "ITEM_READY";

    public static final String PRICE_MODIFIED = "The price of the product has changed.";
    public static final String EXPIRTATION_DATE_MODIFIED = "The expiration date of the product has changed.";
    public static final String ALL_MODIFIED = "Price and expiration date of the product have changed.";

    public static final int NOTIFICATION_ID_ITEM_CHANGED = 0;

    private boolean fake = false;

    public ItemService() {
        super("ItemService");
        itemDao = new ItemDao(this);
        items = new ArrayList<>();
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
            if(ACTION_CHECK_ITEM.equals(action)){
                checkItems();
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
            item.setTitle(resultObject.getString("title"));

            Locale locale = new Locale("ar", "AR");
            NumberFormat format = NumberFormat.getCurrencyInstance(locale);
            item.setPrice(format.getCurrency() + " " + resultObject.getString("price"));

            item.setExpirationDate(resultObject.getString("stop_time"));
            JSONObject shippingJson = resultObject.getJSONObject("shipping");
            item.setShipping(shippingJson.getBoolean("free_shipping"));
            item.setThumbnail(resultObject.getString("thumbnail"));
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return item;
    }

    private void checkItems(){
        items = itemDao.getAllItems();

        for(Item itemDB : items){
            JSONObject resultObject = getItem(itemDB.getItemId());
            Item itemFromRequest = parseJson(resultObject);
            checkItem(itemDB, itemFromRequest);
        }

        if(!itemsChanged.isEmpty()){
            for(Item item : items){
                //get itemId from database item
                sendNotification(item.getItemId());
            }
        }
    }

    private void checkItem(Item itemDB, Item item){

        String itemId = itemDB.getItemId();
        String itemIdRequest = item.getItemId();

        Log.d("Check", "item " +  itemId + " is being checked");
        Log.d("Request", "item from request with" +  itemIdRequest + " is being checked");

        boolean priceChanged = itemDB.getPrice().equals(item.getPrice());
        boolean dateChanged = itemDB.getExpirationDate().equals(item.getExpirationDate());

        if(!priceChanged){
            itemsChangedMessages.put(item.getItemId(), PRICE_MODIFIED);
            itemsChanged.put(item.getItemId(), item);
            if(!dateChanged){
                itemsChangedMessages.put(item.getItemId(), ALL_MODIFIED);
                itemsChanged.put(item.getItemId(), item);
            }
        }else if(!dateChanged){
            itemsChangedMessages.put(item.getItemId(), EXPIRTATION_DATE_MODIFIED);
            itemsChanged.put(item.getItemId(), item);
        }
    }

    private void sendNotification(String itemId){

        Item itemUpdated = itemsChanged.get(itemId);

        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra(ItemActivity.ITEM_PARAM, itemUpdated);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        String msg = itemsChangedMessages.get(itemId);

        // Build notification
        // Actions are just fake
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Product New Info")
                .setContentText(msg).setSmallIcon(R.drawable.icon_notification)
                .setContentIntent(pIntent).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(NOTIFICATION_ID_ITEM_CHANGED, notification);

        itemDao.updateItem(itemUpdated.getItemId(), itemUpdated.getPrice(), itemUpdated.getExpirationDate());
    }

}
