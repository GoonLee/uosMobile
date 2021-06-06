package com.uosmobile.team1.stamp;

/**
 * 스탬프를 받은 책 제목과 달성한 행동, 부여 날짜를 필드로 하는 객체입니다.
 */
public class StampData {
    private String bookTitle;
    private String achievement;
    private String earnedDateTime;

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public String getEarnedDateTime() {
        return earnedDateTime;
    }

    public void setEarnedDateTime(String earnedDateTime) {
        this.earnedDateTime = earnedDateTime;
    }
}
