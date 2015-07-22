package services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Parcel;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import utils.HttpClient;
import model.Item;
import model.Paging;
import model.Search;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SearchService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    public static final String ACTION_SEARCH = "services.action.SEARCH";

    // TODO: Rename parameters
    public static final String SEARCH_PARAM = "services.extra.SEARCH_PARAM";
    public static final String OFFSET_PARAM = "services.extra.OFFSET_PARAM";
    public static final String LIMIT_PARAM = "services.extra.LIMIT_PARAM";

    public static final String RESULT = "result";
    public static final String NOTIFICATION = "training.service.receiver";

    boolean success;
    boolean stopped;

    public SearchService() {
        super("SearchService");
        success = false;
        stopped = false;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if(stopped){
            return;
        }

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SEARCH.equals(action)) {
                final String query = intent.getStringExtra(SEARCH_PARAM);
                final String offset = intent.getStringExtra(OFFSET_PARAM);
                final String limit = intent.getStringExtra(LIMIT_PARAM);
                handleActionSearch(query, offset, limit);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopped = true;
    }

    /**
     * Handle action Search in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSearch(String query, String offset, String limit) {
        Search result = get(query, offset, limit);
        publishResults(result);
    }

    private void publishResults(Search result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
        success = true;
    }

    private Search get(String query, String offset, String limit){

        Search search = null;

        try{
            String queryEncoded = URLEncoder.encode(query, "UTF-8");
            String dir = "https://api.mercadolibre.com/sites/MLA/search?q="+ queryEncoded +"&offset="+ offset +"&limit="+ limit;

            Log.d("url", dir);

            URL url = new URL(dir);

            String response = HttpClient.get(url);
            Log.d("response", response);
            search = parseJsonResult(response);

        }catch (IOException ioe){
            System.out.println("Error: " + ioe.getMessage());
            System.out.println(ioe.getStackTrace());
        }

        return search;
    }

    public static Search parseJsonResult(String result){

        Search search = new Search();
        ArrayList<Item> items = new ArrayList<>();

        try {

            JSONObject resultObject = new JSONObject(result);
            JSONObject pagingJson = resultObject.getJSONObject("paging");

            Integer total = Integer.parseInt(pagingJson.get("total").toString());
            Integer offset = Integer.parseInt(pagingJson.get("offset").toString());
            Integer limit = Integer.parseInt(pagingJson.get("limit").toString());

            Paging paging = new Paging(total, offset, limit);

            JSONArray jsonArray = resultObject.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject itemJson = jsonArray.getJSONObject(i);
                String itemId = itemJson.getString("id");
                String title = itemJson.getString("title");
                Locale locale = new Locale("ar", "AR");
                NumberFormat format = NumberFormat.getCurrencyInstance(locale);
                String price = format.getCurrency() + " " + itemJson.getString("price");
                String thumbnail = itemJson.getString("thumbnail");
                JSONObject shippingJson = itemJson.getJSONObject("shipping");
                Boolean shipping = shippingJson.getBoolean("free_shipping");
                String expirationDate = itemJson.getString("stop_time");

                items.add(new Item(itemId, title, price, thumbnail, shipping, expirationDate));
            }

            search.setItems(items);
            search.setPaging(paging);

            final Parcel parcel = Parcel.obtain();
            search.writeToParcel(parcel, 0);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return search;
    }

}
