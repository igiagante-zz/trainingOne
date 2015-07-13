package com.example.igiagante.trainingone;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import model.Item;

/**
 * Created by igiagante on 13/7/15.
 */
public class DescriptionActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Item item = getIntent().getParcelableExtra(ItemActivity.ITEM_PARAM);

        setContentView(R.layout.activity_description);

        WebView webView = (WebView)findViewById(R.id.item_description);
        webView.loadDataWithBaseURL("", item.getDescription(), "text/html", "UTF-8", "");
    }
}
