package com.uosmobile.team1.common;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class DBHelper extends SQLiteOpenHelper {
    private static final String QUERY_CREATE_META_AND_CACHED_DATA_TABLE = "create table if not exists " +
            Constant.NAME_TABLE_META_AND_CACHED_DATA + "(" +
            Constant.NAME_COLUMN_BOOK_TITLE_OF_META_AND_CACHED_DATA + " TEXT primary key, " +
            Constant.NAME_COLUMN_LAST_PAGE_OF_META_AND_CACHED_DATA + " INTEGER, " +
            Constant.NAME_COLUMN_TOTAL_PAGE_OF_META_AND_CACHED_DATA + " INTEGER, " +
            Constant.NAME_COLUMN_STAMP_EARNED_BY_COMPLETE_OF_META_AND_CACHED_DATA + " INTEGER, " +
            Constant.NAME_COLUMN_STAMP_EARNED_BY_QUIZ_OF_META_AND_CACHED_DATA + " INTEGER);";

    private static final String QUERY_CREATE_IMAGE_TABLE = "create table if not exists " +
            Constant.NAME_TABLE_IMAGE + " (" +
            Constant.NAME_COLUMN_BOOK_TITLE_OF_IMAGE + " TEXT, " +
            Constant.NAME_COLUMN_PAGE_OF_IMAGE + " INTEGER, " +
            Constant.NAME_COLUMN_ENCODED_IMAGE_OF_IMAGE + " BLOB, " +
            Constant.NAME_COLUMN_BACKGROUND_TOGGLED_OF_IMAGE + " INTEGER, " +
            "PRIMARY KEY (" + Constant.NAME_COLUMN_BOOK_TITLE_OF_IMAGE + ", " + Constant.NAME_COLUMN_PAGE_OF_IMAGE + ") );";

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public DBHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(QUERY_CREATE_META_AND_CACHED_DATA_TABLE);
        sqLiteDatabase.execSQL(QUERY_CREATE_IMAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
