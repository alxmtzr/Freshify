package de.alxmtzr.freshify.data.local.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

public class FreshifyDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Freshify.db";

    // table name
    public static final String FRESHIFY_TABLE = "items";

    // table column names
    public static final String FRESHIFY_COL_ITEM_NAME = "name";
    public static final String FRESHIFY_COL_ITEM_QUANTITY = "quantity";
    public static final String FRESHIFY_COL_ITEM_CATEGORY_ID = "category_id";
    public static final String FRESHIFY_COL_ITEM_CATEGORY_NAME = "category_name";
    public static final String FRESHIFY_COL_ITEM_EXPIRY_DATE = "expiry_date";
    public static final String FRESHIFY_COL_COMMENT = "comment";

    // SQL: CREATE TABLE items
    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + FRESHIFY_TABLE + "  ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY,"
            + FRESHIFY_COL_ITEM_NAME + " TEXT,"
            + FRESHIFY_COL_ITEM_QUANTITY + " INTEGER,"
            + FRESHIFY_COL_ITEM_CATEGORY_ID + " INTEGER,"
            + FRESHIFY_COL_ITEM_CATEGORY_NAME + " TEXT,"
            + FRESHIFY_COL_ITEM_EXPIRY_DATE + " TEXT,"      // ISO 8601: YYYY-MM-DD
            + FRESHIFY_COL_COMMENT + " TEXT)";

    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + FRESHIFY_TABLE;

    public FreshifyDBHelper(@Nullable Context context) {
        super(context, FRESHIFY_TABLE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // ATTENTION: drops complete table and creates a new one
        sqLiteDatabase.execSQL(SQL_DROP_TABLE);
        onCreate(sqLiteDatabase);
    }
}
