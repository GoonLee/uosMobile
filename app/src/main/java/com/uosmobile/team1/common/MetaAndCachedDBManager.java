package com.uosmobile.team1.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MetaAndCachedDBManager {
    SQLiteDatabase database;
    DBHelper dbHelper;

    public MetaAndCachedDBManager(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        database = dbHelper.getWritableDatabase();
    }

    public void insertDataIfNotExists(String bookTitle, int totalPage){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.NAME_COLUMN_BOOK_TITLE_OF_META_AND_CACHED_DATA, bookTitle);
        contentValues.put(Constant.NAME_COLUMN_LAST_PAGE_OF_META_AND_CACHED_DATA, 1);
        contentValues.put(Constant.NAME_COLUMN_TOTAL_PAGE_OF_META_AND_CACHED_DATA, totalPage);
        contentValues.put(Constant.NAME_COLUMN_STAMP_EARNED_BY_COMPLETE_OF_META_AND_CACHED_DATA, 0);
        contentValues.put(Constant.NAME_COLUMN_STAMP_EARNED_BY_QUIZ_OF_META_AND_CACHED_DATA, 0);

        database.insertWithOnConflict(Constant.NAME_TABLE_META_AND_CACHED_DATA,null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public int getLastPageFromDB(String bookTitle){
        String[] columns = {Constant.NAME_COLUMN_LAST_PAGE_OF_META_AND_CACHED_DATA};
        String selection = Constant.NAME_COLUMN_BOOK_TITLE_OF_META_AND_CACHED_DATA + " Like ?";
        String[] selectionArgs = {bookTitle};

        Cursor cursor  = database.query(Constant.NAME_TABLE_META_AND_CACHED_DATA, columns, selection, selectionArgs, null,null, null);

        cursor.moveToNext();

        int ret = cursor.getInt(cursor.getColumnIndex(Constant.NAME_COLUMN_LAST_PAGE_OF_META_AND_CACHED_DATA));

        cursor.close();

        return ret;
    }

    public int getTotalPageFromDB(String bookTitle){
        String[] columns = {Constant.NAME_COLUMN_TOTAL_PAGE_OF_META_AND_CACHED_DATA};
        String selection = Constant.NAME_COLUMN_BOOK_TITLE_OF_META_AND_CACHED_DATA + " Like ?";
        String[] selectionArgs = {bookTitle};

        Cursor cursor  = database.query(Constant.NAME_TABLE_META_AND_CACHED_DATA, columns, selection, selectionArgs, null,null, null);

        cursor.moveToNext();

        int ret = cursor.getInt(cursor.getColumnIndex(Constant.NAME_COLUMN_TOTAL_PAGE_OF_META_AND_CACHED_DATA));

        cursor.close();

        return ret;
    }

    public void updateLastPageToDB(String bookTitle, int page){
        String where = Constant.NAME_COLUMN_BOOK_TITLE_OF_META_AND_CACHED_DATA + " = ?";
        String[] whereArgs = {bookTitle};

        ContentValues contentValues = new ContentValues();

        contentValues.put(Constant.NAME_COLUMN_LAST_PAGE_OF_META_AND_CACHED_DATA, page);

        database.update(Constant.NAME_TABLE_META_AND_CACHED_DATA, contentValues, where, whereArgs);
    }

    public void updateStampGivenByQuiz(String bookTitle){
        String where = Constant.NAME_COLUMN_BOOK_TITLE_OF_META_AND_CACHED_DATA + " = ?";
        String[] whereArgs = {bookTitle};

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.NAME_COLUMN_STAMP_EARNED_BY_QUIZ_OF_META_AND_CACHED_DATA, 1);
        database.update(Constant.NAME_TABLE_META_AND_CACHED_DATA, contentValues, where, whereArgs);

        Log.d("TAG", "책 문제 풀이 완료 스탬프 업데이트 완료");
    }

    public void updateStampGivenByComplete(String bookTitle){
        String where = Constant.NAME_COLUMN_BOOK_TITLE_OF_META_AND_CACHED_DATA + " = ?";
        String[] whereArgs = {bookTitle};

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.NAME_COLUMN_STAMP_EARNED_BY_COMPLETE_OF_META_AND_CACHED_DATA, 1);
        database.update(Constant.NAME_TABLE_META_AND_CACHED_DATA, contentValues, where, whereArgs);

        Log.d("TAG", "책 완독 스탬프 업데이트 완료");
    }
}
