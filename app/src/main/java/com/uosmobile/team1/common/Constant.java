package com.uosmobile.team1.common;

/**
 * 프로그램에서 자주 사용되는 상수들을 정의한 클래스입니다.
 */
public class Constant {
    // 파일 읽고 쓰기 관련 상수
    public static final String NAME_DIRECTORY_TEXT = "text";
    public static final String NAME_DIRECTORY_SOUND = "sound";
    public static final String NAME_DIRECTORY_IMAGE = "image";
    public static final String NAME_DIRECTORY_QUIZ = "quiz";

    // DB 파일 이름
    public static final String NAME_DB = "uosmobile.db";

    // DB 버전 번호
    public static final int VERSION_DB = 4;

    // 책의 페이지 관련 DB 테이블 이름
    public static final String NAME_TABLE_PAGE_INFO = "BOOK_PAGE_INFO";

    // 페이지 관련 DB 테이블 column 이름
    public static final String NAME_COLUMN_BOOK_TITLE_OF_PAGE_INFO = "book_title";
    public static final String NAME_COLUMN_LAST_PAGE_OF_PAGE_INFO = "last_page";
    public static final String NAME_COLUMN_TOTAL_PAGE_OF_PAGE_INFO = "total_page";

    // 이미지 관련 DB 테이블 이름
    public static final String NAME_TABLE_IMAGE = "USER_IMAGE";

    // 이미지 관련 DB 테이블 column 이름
    public static final String NAME_COLUMN_BOOK_TITLE_OF_IMAGE = "book_title";
    public static final String NAME_COLUMN_PAGE_OF_IMAGE = "page";
    public static final String NAME_COLUMN_ENCODED_IMAGE_OF_IMAGE = "encoded_image";
    public static final String NAME_COLUMN_BACKGROUND_TOGGLED_OF_IMAGE = "background_toggled";

    // 스탬프 관련 DB 테이블 이름
    public static final String NAME_TABLE_STAMP = "STAMP";

    // 스탬프 관련 DB 테이블 column 이름
    public static final String NAME_COLUMN_BOOK_TITLE_OF_STAMP = "book_title";
    public static final String NAME_COLUMN_ACHIEVEMENT_CODE_OF_STAMP = "achievement";
    public static final String NAME_COLUMN_EARN_DATETIME_OF_STAMP = "earn_datetime";
}