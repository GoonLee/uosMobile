package com.uosmobile.team1.common;

import android.os.Environment;

public class Constant {
    public static final String NAME_CONTENTS_ABSOLUTE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/Contents";
    public static final String NAME_DIRECTORY_TEXT = "text";
    public static final String NAME_DIRECTORY_SOUND = "sound";
    public static final String NAME_DIRECTORY_IMAGE = "image";
    public static final String NAME_DIRECTORY_QUIZ = "quiz";

    public static final String NAME_DB = "uosmobile.db";

    public static final int VERSION_DB = 3;

    public static final String NAME_TABLE_META_AND_CACHED_DATA = "BOOK_META_DATA";
    public static final String NAME_TABLE_IMAGE = "USER_MODIFIED_IMAGE";
    public static final String NAME_TABLE_STAMP = "STAMP";

    public static final String NAME_COLUMN_BOOK_TITLE_OF_META_AND_CACHED_DATA = "book_title";
    public static final String NAME_COLUMN_LAST_PAGE_OF_META_AND_CACHED_DATA = "last_page";
    public static final String NAME_COLUMN_TOTAL_PAGE_OF_META_AND_CACHED_DATA = "total_page";

    public static final String NAME_COLUMN_BOOK_TITLE_OF_IMAGE = "book_title";
    public static final String NAME_COLUMN_PAGE_OF_IMAGE = "page";
    public static final String NAME_COLUMN_ENCODED_IMAGE_OF_IMAGE = "encoded_image";
    public static final String NAME_COLUMN_BACKGROUND_TOGGLED_OF_IMAGE = "background_toggled";

    public static final String NAME_COLUMN_BOOK_TITLE_OF_STAMP = "book_title";
    public static final String NAME_COLUMN_ACHIEVEMENT_CODE_OF_STAMP = "achievement";
    public static final String NAME_COLUMN_EARN_DATETIME_OF_STAMP = "earn_datetime";
}