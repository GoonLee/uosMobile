package com.uosmobile.team1.common;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 페이지 정보 관련 DB 처리를 담당하는 클래스입니다.
 */
public class PageInfoDBManager {
    SQLiteDatabase database;
    DBHelper dbHelper;

    public PageInfoDBManager(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        database = dbHelper.getWritableDatabase();
    }

    /**
     * 매개변수로 전달받은 책 제목에 해당하는 페이지 정보가 없으면 insert를 수행하는 메소드입니다.
     * @param bookTitle 페이지 정보가 전달된 책 이름입니다. primary key로 사용합니다.
     * @param totalPage 해당 책의 마지막 페이지 번호입니다.
     */
    public void insertDataIfNotExists(String bookTitle, int totalPage){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.NAME_COLUMN_BOOK_TITLE_OF_PAGE_INFO, bookTitle);
        contentValues.put(Constant.NAME_COLUMN_LAST_PAGE_OF_PAGE_INFO, 1);
        contentValues.put(Constant.NAME_COLUMN_TOTAL_PAGE_OF_PAGE_INFO, totalPage);

        // conflict algorithm으로 CONFLICT_IGNORE를 사용하여 primary key가 tuple이 insert 하지 않음
        database.insertWithOnConflict(Constant.NAME_TABLE_PAGE_INFO,null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
    }

    /**
     * 매개변수로 전달받은 책의 사용자가 마지막으로 본 페이지 번호를 DB에서 읽는 메소드입니다.
     * @param bookTitle 사용자가 마지막으로 본 페이지 번호를 확인할 책의 제목입니다.
     * @return 해당 책에서 사용자가 마지막으로 본 페이지 번호를 반환합니다.
     */
    public int getLastPageFromDB(String bookTitle){
        String[] columns = {Constant.NAME_COLUMN_LAST_PAGE_OF_PAGE_INFO};
        String selection = Constant.NAME_COLUMN_BOOK_TITLE_OF_PAGE_INFO + " Like ?";
        String[] selectionArgs = {bookTitle};

        Cursor cursor  = database.query(Constant.NAME_TABLE_PAGE_INFO, columns, selection, selectionArgs, null,null, null);

        // 정상적인 실행 흐름에서 항상 insertDataIfNotExists가 실행된 후에 실행되므로 cursor는 항상 1개의 값을 반환
        cursor.moveToNext();

        int ret = cursor.getInt(cursor.getColumnIndex(Constant.NAME_COLUMN_LAST_PAGE_OF_PAGE_INFO));

        cursor.close();

        return ret;
    }

    /**
     * 매개변수로 전달받은 책의 마지막 페이지 번호를 DB에서 읽는 메소드입니다.
     * @param bookTitle 마지막 페이지 번호를 확인할 책의 제목입니다.
     * @return 해당 책의 마지막 페이지 번호를 반환합니다.
     */
    public int getTotalPageFromDB(String bookTitle){
        String[] columns = {Constant.NAME_COLUMN_TOTAL_PAGE_OF_PAGE_INFO};
        String selection = Constant.NAME_COLUMN_BOOK_TITLE_OF_PAGE_INFO + " Like ?";
        String[] selectionArgs = {bookTitle};

        Cursor cursor  = database.query(Constant.NAME_TABLE_PAGE_INFO, columns, selection, selectionArgs, null,null, null);

        // 정상적인 실행 흐름에서 항상 insertDataIfNotExists가 실행된 후에 실행되므로 cursor는 항상 1개의 값을 반환
        cursor.moveToNext();

        int ret = cursor.getInt(cursor.getColumnIndex(Constant.NAME_COLUMN_TOTAL_PAGE_OF_PAGE_INFO));

        cursor.close();

        return ret;
    }

    /**
     * 매개변수로 전달받은 책의 사용자가 마지막으로 본 페이지 번호를 DB에 Update하는 메소드입니다.
     * @param bookTitle 사용자가 마지막으로 본 페이지 번호를 Update할 책의 제목입니다.
     * @param page 사용자가 마지막으로 본 페이지 번호입니다.
     */
    public void updateLastPageToDB(String bookTitle, int page){
        String where = Constant.NAME_COLUMN_BOOK_TITLE_OF_PAGE_INFO + " = ?";
        String[] whereArgs = {bookTitle};

        ContentValues contentValues = new ContentValues();

        contentValues.put(Constant.NAME_COLUMN_LAST_PAGE_OF_PAGE_INFO, page);

        database.update(Constant.NAME_TABLE_PAGE_INFO, contentValues, where, whereArgs);
    }
}
