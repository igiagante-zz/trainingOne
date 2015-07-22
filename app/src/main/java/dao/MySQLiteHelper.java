package dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by igiagante on 22/7/15.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_ITEMS = "Items";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ITEM_ID = "item_id";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_EXPIRATION_DATE = "stop_time";

    private static final String DATABASE_NAME = "items.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_ITEMS + "("
            + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_ITEM_ID
            + " text not null, "
            + COLUMN_PRICE
            + " text not null, "
            + COLUMN_EXPIRATION_DATE
            + " text not null);";


    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }
}
