package com.example.igiagante.trainingone.search;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.igiagante.trainingone.R;
import com.example.igiagante.trainingone.TrakingReceiver;
import com.example.igiagante.trainingone.item.ListItemsActivity;

import java.util.Calendar;

import services.SearchService;


public class SearchActivity extends Activity implements SearchFragment.SearchFragmentListener{

    private PendingIntent pendingIntent;
    private static final long REPEAT_TIME = 1000 * 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(this, TrakingReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        start();
    }

    @Override
    public void onClickSearchButton(String query) {
        search(query);
    }

    /**
     * Initialize ListItemActivity in order to list the result obtained through the searched.
     * @param query
     */
    public void search(String query) {
        Intent intent = new Intent(this, ListItemsActivity.class);
        intent.putExtra(SearchService.SEARCH_PARAM, query);
        startActivity(intent);
    }

    private void start() {

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 10);

        // Fetch every 30 seconds
        // InexactRepeating allows Android to optimize the energy consumption
        manager.setRepeating(AlarmManager.RTC, cal.getTimeInMillis(), REPEAT_TIME, pendingIntent);
    }
}
