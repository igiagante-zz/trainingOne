package helper;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
/**
 * Created by igiagante on 2/7/15.
 */
public class Helper {

    public static String get(URL url){

        InputStream inputStream = null;
        HttpsURLConnection urlConnection = null;

        try{

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
                return response;
            } else {
                Log.d("error", "todo mal");
                //throw new SearchException("Failed to fetch data!!");
            }

        }catch (IOException ioe){
            System.out.println("Error: " + ioe.getMessage());
            System.out.println(ioe.getStackTrace());
        }
        return "";
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {

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
}
