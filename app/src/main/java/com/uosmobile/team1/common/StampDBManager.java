package com.uosmobile.team1.common;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.uosmobile.team1.stamp.StampData;
import com.uosmobile.team1.stamp.StampService;

import java.util.ArrayList;

public class StampDBManager {
    SQLiteDatabase database;
    DBHelper dbHelper;

    public StampDBManager(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        database = dbHelper.getWritableDatabase();
    }

    public long insertDataIfNotExists(String bookTitle, int achievementCode){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.NAME_COLUMN_BOOK_TITLE_OF_STAMP, bookTitle);
        contentValues.put(Constant.NAME_COLUMN_ACHIEVEMENT_CODE_OF_STAMP, achievementCode);

        return database.insertWithOnConflict(Constant.NAME_TABLE_STAMP,null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public ArrayList<StampData> getAllStampFromDB(){
        ArrayList<StampData> stampDataList= new ArrayList<>();
        Cursor cursor = database.query(Constant.NAME_TABLE_STAMP, null, null, null, null, null, null);

        while(cursor.moveToNext()){
            StampData stampData = new StampData();
            stampData.setBookTitle(cursor.getString(cursor.getColumnIndex(Constant.NAME_COLUMN_BOOK_TITLE_OF_STAMP)));
            stampData.setAchievement(cursor.getString(cursor.getColumnIndex(Constant.NAME_COLUMN_ACHIEVEMENT_CODE_OF_STAMP)).equals(String.valueOf(StampService.ACHIEVEMENT_READALL)) ? "완독" : "퀴즈 풀이");
            stampData.setEarnedDateTime(cursor.getString(cursor.getColumnIndex(Constant.NAME_COLUMN_EARN_DATETIME_OF_STAMP)));

            stampDataList.add(stampData);
        }

        cursor.close();
        return stampDataList;
    }
}
