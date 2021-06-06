package com.uosmobile.team1.common;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.uosmobile.team1.bookcontents.PaintViewInfo;

import java.io.ByteArrayOutputStream;

/**
 * 이미지 관련 DB 처리를 담당하는 클래스입니다.
 */
public class ImageDBManager {
    SQLiteDatabase database;
    DBHelper dbHelper;

    public ImageDBManager(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        database = dbHelper.getWritableDatabase();
    }

    /**
     * 매개변수로 전달받은 책 제목과 페이지 번호에 해당하는 PaintView 정보가 있으면 update, 없으면 insert를 수행하는 메소드입니다.
     * @param bookTitle PaintView 정보가 전달된 책 이름입니다. page 매개변수와 함께 composite key로 사용합니다.
     * @param page PaintView 정보가 전달된 페이지 번호입니다. bookTitle 매개변수와 함께 composite key로 사용합니다.
     * @param paintViewInfo PaintView에서 DB에 저장할 정보를 필드로 갖는 객체입니다.
     */
    public void insertOrUpdatePaintViewInfoToDB(String bookTitle, int page, PaintViewInfo paintViewInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.NAME_COLUMN_BOOK_TITLE_OF_IMAGE, bookTitle);
        contentValues.put(Constant.NAME_COLUMN_PAGE_OF_IMAGE, page);
        contentValues.put(Constant.NAME_COLUMN_ENCODED_IMAGE_OF_IMAGE, encodeBitmapToByteArray(paintViewInfo.getFrontBitmap()));
        contentValues.put(Constant.NAME_COLUMN_BACKGROUND_TOGGLED_OF_IMAGE, paintViewInfo.isBackgroundToggled() ? 1 : 0);

        // conflict algorithm으로 CONFLICT_REPLACE를 사용하여 primary key가 tuple이 DB에 존재하는 경우 update와 동일한 결과를 얻음
        database.insertWithOnConflict(Constant.NAME_TABLE_IMAGE,null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    /**
     * 매개변수로 전달받은 책 제목과 페이지 번호에 해당하는 PaintView 정보를 DB에서 읽어 반환하는 메소드입니다.
     * @param bookTitle PaintView 정보를 찾을 책 제목입니다. page 매개변수와 함께 composite key로 사용합니다.
     * @param page PaintView 정보를 찾을 페이지 번호입니다. bookTitle 매개변수와 함께 composite key로 사용합니다.
     * @return
     */
    public PaintViewInfo getPaintViewInfoOrNullFromDB(String bookTitle, int page){
        String[] columns = {Constant.NAME_COLUMN_ENCODED_IMAGE_OF_IMAGE, Constant.NAME_COLUMN_BACKGROUND_TOGGLED_OF_IMAGE};
        String selection = Constant.NAME_COLUMN_BOOK_TITLE_OF_IMAGE + " Like ? AND " + Constant.NAME_COLUMN_PAGE_OF_IMAGE + " LIKE ?";
        String[] selectionArgs = {bookTitle, String.valueOf(page)};

        Cursor cursor  = database.query(Constant.NAME_TABLE_IMAGE, columns, selection, selectionArgs, null,null, null);

        //primary key가 일치하는 튜플을 찾기 때문에 cursor는 항상 0또는 1개의 값을 반환
        if(cursor.moveToNext()){
            byte[] encodedBitmap = cursor.getBlob(cursor.getColumnIndex(Constant.NAME_COLUMN_ENCODED_IMAGE_OF_IMAGE));
            boolean backgroundToggled = cursor.getInt(cursor.getColumnIndex(Constant.NAME_COLUMN_BACKGROUND_TOGGLED_OF_IMAGE)) == 1;

            cursor.close();

            return new PaintViewInfo(BitmapFactory.decodeByteArray(encodedBitmap, 0, encodedBitmap.length), backgroundToggled);
        }
        return null;
    }

    /**
     * DB에 비트맵을 저장하기 위해 byte array로 변환하는 메소드입니다.
     * @param bitmap byte array로 변환할 비트맵입니다.
     * @return 비트맵을 변환한 byte array를 반환합니다.
     */
    private byte[] encodeBitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        return stream.toByteArray();
    }
}
