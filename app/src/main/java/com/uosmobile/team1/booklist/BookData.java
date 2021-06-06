package com.uosmobile.team1.booklist;

/**
 * 책의 제목을 필드로 하는 Immutable 객체입니다.
 */
public class BookData {
    private final String title;

    public BookData(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
