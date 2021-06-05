package com.uosmobile.team1.common;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.uosmobile.team1.bookcontents.PaintViewInfo;

import java.io.ByteArrayOutputStream;

public class ImageDBManager {
    SQLiteDatabase database;
    DBHelper dbHelper;

    public ImageDBManager(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        database = dbHelper.getWritableDatabase();
    }

    public void insertOrUpdatePaintViewInfoToDB(String bookTitle, int page, PaintViewInfo paintViewInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.NAME_COLUMN_BOOK_TITLE_OF_IMAGE, bookTitle);
        contentValues.put(Constant.NAME_COLUMN_PAGE_OF_IMAGE, page);
        contentValues.put(Constant.NAME_COLUMN_ENCODED_IMAGE_OF_IMAGE, encodeBitmapToByteArray(paintViewInfo.getFrontBitmap()));
        contentValues.put(Constant.NAME_COLUMN_BACKGROUND_TOGGLED_OF_IMAGE, paintViewInfo.isBackgroundToggled() ? 1 : 0);

        database.insertWithOnConflict(Constant.NAME_TABLE_IMAGE,null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public PaintViewInfo getFrontPaintViewInfoOrNullFromDB(String bookTitle, int page){
        String[] columns = {Constant.NAME_COLUMN_ENCODED_IMAGE_OF_IMAGE, Constant.NAME_COLUMN_BACKGROUND_TOGGLED_OF_IMAGE};
        String selection = Constant.NAME_COLUMN_BOOK_TITLE_OF_IMAGE + " Like ? AND " + Constant.NAME_COLUMN_PAGE_OF_IMAGE + " LIKE ?";
        String[] selectionArgs = {bookTitle, String.valueOf(page)};

        Cursor cursor  = database.query(Constant.NAME_TABLE_IMAGE, columns, selection, selectionArgs, null,null, null);

        if(cursor.moveToNext()){
            byte[] encodedBitmap = cursor.getBlob(cursor.getColumnIndex(Constant.NAME_COLUMN_ENCODED_IMAGE_OF_IMAGE));
            boolean backgroundToggled = cursor.getInt(cursor.getColumnIndex(Constant.NAME_COLUMN_BACKGROUND_TOGGLED_OF_IMAGE)) == 1;

            cursor.close();

            return new PaintViewInfo(BitmapFactory.decodeByteArray(encodedBitmap, 0, encodedBitmap.length), backgroundToggled);
        }
        return null;
    }

    private byte[] encodeBitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        return stream.toByteArray();
    }
}
