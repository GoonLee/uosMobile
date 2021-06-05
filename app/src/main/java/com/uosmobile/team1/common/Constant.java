package com.uosmobile.team1.common;

import android.os.Environment;

public class Constant {
    public static final String NAME_CONTENTS_ABSOLUTE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/Contents";
    public static final String NAME_DIRECTORY_TEXT = "text";
    public static final String NAME_DIRECTORY_SOUND = "sound";
    public static final String NAME_DIRECTORY_IMAGE = "image";
    public static final String NAME_DIRECTORY_QUIZ = "quiz";

    public static final String NAME_DB = "uosmobile.db";

    public static final String NAME_TABLE_META_AND_CACHED_DATA = "BOOK_META_DATA";
    public static final String NAME_TABLE_IMAGE = "USER_MODIFIED_IMAGE";

    public static final String NAME_COLUMN_BOOK_TITLE_OF_META_AND_CACHED_DATA = "book_title";
    public static final String NAME_COLUMN_LAST_PAGE_OF_META_AND_CACHED_DATA = "last_page";
    public static final String NAME_COLUMN_TOTAL_PAGE_OF_META_AND_CACHED_DATA = "total_page";
    public static final String NAME_COLUMN_STAMP_EARNED_BY_COMPLETE_OF_META_AND_CACHED_DATA = "stamp_given_by_complete";
    public static final String NAME_COLUMN_STAMP_EARNED_BY_QUIZ_OF_META_AND_CACHED_DATA = "stamp_given_by_quiz";

    public static final String NAME_COLUMN_BOOK_TITLE_OF_IMAGE = "book_title";
    public static final String NAME_COLUMN_PAGE_OF_IMAGE = "page";
    public static final String NAME_COLUMN_ENCODED_IMAGE_OF_IMAGE = "encoded_image";
    public static final String NAME_COLUMN_BACKGROUND_TOGGLED_OF_IMAGE = "background_toggled";

}
