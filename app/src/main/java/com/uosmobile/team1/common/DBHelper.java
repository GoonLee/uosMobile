package com.uosmobile.team1.common;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * SQLite를 사용하기 위해 SQLiteOpenHelper 클래스를 상속한 DBHelper 클래스입니다.
 */
public class DBHelper extends SQLiteOpenHelper {
    // 테이블 생성 쿼리
    private static final String QUERY_CREATE_META_AND_CACHED_DATA_TABLE = "create table if not exists " +
            Constant.NAME_TABLE_PAGE_INFO + "(" +
            Constant.NAME_COLUMN_BOOK_TITLE_OF_PAGE_INFO + " TEXT primary key, " +
            Constant.NAME_COLUMN_LAST_PAGE_OF_PAGE_INFO + " INTEGER, " +
            Constant.NAME_COLUMN_TOTAL_PAGE_OF_PAGE_INFO + " INTEGER);";

    private static final String QUERY_CREATE_IMAGE_TABLE = "create table if not exists " +
            Constant.NAME_TABLE_IMAGE + " (" +
            Constant.NAME_COLUMN_BOOK_TITLE_OF_IMAGE + " TEXT, " +
            Constant.NAME_COLUMN_PAGE_OF_IMAGE + " INTEGER, " +
            Constant.NAME_COLUMN_ENCODED_IMAGE_OF_IMAGE + " BLOB, " +
            Constant.NAME_COLUMN_BACKGROUND_TOGGLED_OF_IMAGE + " INTEGER, " +
            "PRIMARY KEY (" + Constant.NAME_COLUMN_BOOK_TITLE_OF_IMAGE + ", " + Constant.NAME_COLUMN_PAGE_OF_IMAGE + ") );";

    private static final String QUERY_CREATE_STAMP_TABLE = "create table if not exists " +
            Constant.NAME_TABLE_STAMP + " (" +
            Constant.NAME_COLUMN_BOOK_TITLE_OF_STAMP + " TEXT, " +
            Constant.NAME_COLUMN_ACHIEVEMENT_CODE_OF_STAMP + " INTEGER, " +
            Constant.NAME_COLUMN_EARN_DATETIME_OF_STAMP + " DATETIME default (datetime('now', 'localtime')), " +
            "PRIMARY KEY (" + Constant.NAME_COLUMN_BOOK_TITLE_OF_STAMP + ", " + Constant.NAME_COLUMN_ACHIEVEMENT_CODE_OF_STAMP + ") );";

    // 테이블 삭제 쿼리
    private static final String QUERY_DROP_META_AND_CACHED_DATA_TABLE_IF_EXISTS = "drop table if exists " + Constant.NAME_TABLE_PAGE_INFO;
    private static final String QUERY_DROP_IMAGE_TABLE_IF_EXISTS = "drop table if exists " + Constant.NAME_TABLE_IMAGE;
    private static final String QUERY_DROP_STAMP_TABLE_IF_EXISTS = "drop table if exists " + Constant.NAME_TABLE_STAMP;

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
        sqLiteDatabase.execSQL(QUERY_CREATE_STAMP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(QUERY_DROP_META_AND_CACHED_DATA_TABLE_IF_EXISTS);
        sqLiteDatabase.execSQL(QUERY_DROP_IMAGE_TABLE_IF_EXISTS);
        sqLiteDatabase.execSQL(QUERY_DROP_STAMP_TABLE_IF_EXISTS);
        onCreate(sqLiteDatabase);
    }
}
