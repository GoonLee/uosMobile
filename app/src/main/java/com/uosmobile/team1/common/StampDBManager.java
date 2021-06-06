package com.uosmobile.team1.common;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.uosmobile.team1.stamp.StampData;
import com.uosmobile.team1.stamp.StampService;

import java.util.ArrayList;

/**
 * 스탬프 관련 DB 처리를 담당하는 클래스입니다.
 */
public class StampDBManager {
    SQLiteDatabase database;
    DBHelper dbHelper;

    public StampDBManager(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        database = dbHelper.getWritableDatabase();
    }

    /**
     * 매개변수로 전달받은 책 제목과 달성한 행동 코드를 DB에 없을 경우 insert하는 메소드입니다.
     * @param bookTitle 사용자가 특정 행동을 달성한 책 이름입니다. achievementCode와 함께 composite key로 사용됩니다.
     * @param achievementCode 사용자가 달성한 행동입니다. bookTitle과 함께 composite key로 사용됩니다.
     */
    public long insertDataIfNotExists(String bookTitle, int achievementCode){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.NAME_COLUMN_BOOK_TITLE_OF_STAMP, bookTitle);
        contentValues.put(Constant.NAME_COLUMN_ACHIEVEMENT_CODE_OF_STAMP, achievementCode);

        // conflict algorithm으로 CONFLICT_IGNORE를 사용하여 primary key가 tuple이 insert 하지 않음
        return database.insertWithOnConflict(Constant.NAME_TABLE_STAMP,null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
    }

    /**
     * DB에 저장된 스탬프 전체를 read하는 메소드입니다.
     * @return DB에 저장된 전체 스탬프 정보를 ArrayList로 반환합니다.
     */
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
