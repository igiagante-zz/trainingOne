package com.example.igiagante.trainingone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import services.SearchService;


public class SearchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.query_view);

        if( savedInstanceState != null ) {
            TextView textView = (TextView) findViewById(R.id.search_query);
            textView.setText(savedInstanceState.getString("search_query"));
        }

        Button button = (Button) findViewById(R.id.button_search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) findViewById(R.id.search_query);
                if(textView.getText().toString().equals("")){
                    textView.setError("Please enter a valid query");
                }else{
                    search();
                }
            }
        });
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
}
