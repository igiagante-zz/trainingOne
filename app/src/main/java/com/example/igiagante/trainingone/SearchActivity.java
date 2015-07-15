package com.example.igiagante.trainingone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import services.SearchService;


public class SearchActivity extends Activity {

    public static final String PREFS_NAME = "MyPrefsFile";
    private StringBuilder queryList; //String list with comma
    private String [] queries; //list
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_view);

        if( savedInstanceState != null ) {
            TextView textView = (TextView) findViewById(R.id.search_query);
            textView.setText(savedInstanceState.getString("search_query"));
        }

        initSearchQueryList();

        Button button = (Button) findViewById(R.id.button_search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) findViewById(R.id.search_query);
                String text = textView.getText().toString();
                if (text.equals("")) {
                    textView.setError("Please enter a valid query");
                } else {
                    if (!existQuery(text)) {
                        queryList.append(text);
                        queryList.append(",");
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("list", queryList.toString());
                        editor.apply();
                    }
                    if(isNetworkStatusAvialable(getApplicationContext())) {
                        search();
                    } else {
                        Toast.makeText(getApplicationContext(), "Internet is not avialable", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSearchQueryList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar card_view clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TextView textView = (TextView) findViewById(R.id.search_query);
        outState.putString("search_query", textView.getText().toString());
    }

    public void search() {
        EditText editText = (EditText) findViewById(R.id.search_query);
        String query = editText.getText().toString();

        Intent intent = new Intent(this, ListItemsActivity.class);
        intent.putExtra(SearchService.SEARCH_PARAM, query);
        startActivity(intent);
    }

    public static boolean isNetworkStatusAvialable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
                if(netInfos.isConnected())
                    return true;
        }
        return false;
    }

    private void initSearchQueryList(){

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String list = settings.getString("list", "");

        queryList = new StringBuilder(list);

        if(list != null && !list.isEmpty()){
            queries = list.split(",");
        }else{
            queries = new String[1];
            queries[0] = "";
        }

        // Get a reference to the AutoCompleteTextView in the layout
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.search_query);

        // Create the adapter and set it to the AutoCompleteTextView
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, queries);
        textView.setAdapter(adapter);
        textView.setThreshold(1);

    }

    private boolean existQuery(String query){
        if(queries != null && queries.length > 0 ){
            for(int i = 0; i < queries.length; i++){
                if(queries[i].equals(query))
                    return true;
            }
        }
        return false;
    }
}
