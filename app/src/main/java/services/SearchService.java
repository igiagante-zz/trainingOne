package services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Parcel;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

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

    public static final String RESULT = "result";
    public static final String NOTIFICATION = "training.service.receiver";

    public SearchService() {
        super("SearchService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SEARCH.equals(action)) {
                final String query = intent.getStringExtra(SEARCH_PARAM);
                handleActionSearch(query);
            }
        }
    }

    /**
     * Handle action Search in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSearch(String query) {
        Search result = get(query);
        Log.d("result ", "todo ok");
        publishResults(result);
    }

    private void publishResults(Search result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }

    private Search get(String query){

        InputStream inputStream = null;
        HttpsURLConnection urlConnection = null;

        String dir = "https://api.mercadolibre.com/sites/MLA/search?q="+ query +"&offset=0&limit=10";
        Search search = null;
        try{
            URL url = new URL(dir);

            /* forming th java.net.URL object */
            urlConnection = (HttpsURLConnection) url.openConnection();
            /* optional request header */
            urlConnection.setRequestProperty("Content-Type", "application/json");

            /* optional request header */
            urlConnection.setRequestProperty("Accept", "application/json");

            /* for Get request */
            urlConnection.setRequestMethod("GET");
            int statusCode = urlConnection.getResponseCode();

            /* 200 represents HTTP OK */
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                String response = convertInputStreamToString(inputStream);
                Log.d("response", response);
                search = parseJsonResult(response);
                Log.d("Title", search.getItems().get(0).getTitle());
                return search;
            } else {
                Log.d("error", "todo mal");
                //throw new SearchException("Failed to fetch data!!");
            }

        }catch (IOException ioe){
            System.out.println("Error: " + ioe.getMessage());
            System.out.println(ioe.getStackTrace());
        }

        return search;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

            /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }

        return result;
    }

    private static Search parseJsonResult(String result){

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
                String title = itemJson.getString("title");
                String price = itemJson.getString("price");
                String thumbnail = itemJson.getString("thumbnail");
                JSONObject shippingJson = itemJson.getJSONObject("shipping");
                Boolean shipping = shippingJson.getBoolean("free_shipping");

                items.add(new Item(title, price, thumbnail, shipping));
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

    private static String getFieldData(String result, String field) {
        try {
            JSONObject json= (JSONObject) new JSONTokener(result).nextValue();
            JSONObject json2 = json.getJSONObject("results");
            String test = (String) json2.get(field);
            Log.d("test", test);
            return test;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
