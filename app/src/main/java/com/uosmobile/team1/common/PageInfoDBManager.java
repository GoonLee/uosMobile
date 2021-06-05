package com.uosmobile.team1.common;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PageInfoDBManager {
    SQLiteDatabase database;
    DBHelper dbHelper;

    public PageInfoDBManager(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        database = dbHelper.getWritableDatabase();
    }

    public void insertDataIfNotExists(String bookTitle, int totalPage){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.NAME_COLUMN_BOOK_TITLE_OF_PAGE_INFO, bookTitle);
        contentValues.put(Constant.NAME_COLUMN_LAST_PAGE_OF_PAGE_INFO, 1);
        contentValues.put(Constant.NAME_COLUMN_TOTAL_PAGE_OF_PAGE_INFO, totalPage);

        database.insertWithOnConflict(Constant.NAME_TABLE_PAGE_INFO,null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public int getLastPageFromDB(String bookTitle){
        String[] columns = {Constant.NAME_COLUMN_LAST_PAGE_OF_PAGE_INFO};
        String selection = Constant.NAME_COLUMN_BOOK_TITLE_OF_PAGE_INFO + " Like ?";
        String[] selectionArgs = {bookTitle};

        Cursor cursor  = database.query(Constant.NAME_TABLE_PAGE_INFO, columns, selection, selectionArgs, null,null, null);

        cursor.moveToNext();

        int ret = cursor.getInt(cursor.getColumnIndex(Constant.NAME_COLUMN_LAST_PAGE_OF_PAGE_INFO));

        cursor.close();

        return ret;
    }

    public int getTotalPageFromDB(String bookTitle){
        String[] columns = {Constant.NAME_COLUMN_TOTAL_PAGE_OF_PAGE_INFO};
        String selection = Constant.NAME_COLUMN_BOOK_TITLE_OF_PAGE_INFO + " Like ?";
        String[] selectionArgs = {bookTitle};

        Cursor cursor  = database.query(Constant.NAME_TABLE_PAGE_INFO, columns, selection, selectionArgs, null,null, null);

        cursor.moveToNext();

        int ret = cursor.getInt(cursor.getColumnIndex(Constant.NAME_COLUMN_TOTAL_PAGE_OF_PAGE_INFO));

        cursor.close();

        return ret;
    }

    public void updateLastPageToDB(String bookTitle, int page){
        String where = Constant.NAME_COLUMN_BOOK_TITLE_OF_PAGE_INFO + " = ?";
        String[] whereArgs = {bookTitle};

        ContentValues contentValues = new ContentValues();

        contentValues.put(Constant.NAME_COLUMN_LAST_PAGE_OF_PAGE_INFO, page);

        database.update(Constant.NAME_TABLE_PAGE_INFO, contentValues, where, whereArgs);
    }
}
