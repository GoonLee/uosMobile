package com.uosmobile.team1.stamp;

import android.content.Context;
import android.widget.Toast;
import com.uosmobile.team1.common.Constant;
import com.uosmobile.team1.common.DBHelper;
import com.uosmobile.team1.common.StampDBManager;

import java.util.ArrayList;

/**
 * 스탬프 부여와 관련된 기능을 제공하는 클래스입니다.
 */
public class StampService {
    // 사용자가 스탬프를 부여받을 수 있는 행동 코드를 지정
    public static int ACHIEVEMENT_READALL = 0;
    public static int ACHIEVEMENT_QUIZ = 1;

    Context context;
    StampDBManager stampDBManager;

    public StampService(Context context){
        this.context = context;
        this.stampDBManager = new StampDBManager(new DBHelper(context, Constant.NAME_DB, null, Constant.VERSION_DB));
    }

    /**
     * 사용자가 최로로 스탬프를 부여받을 수 있는 행동을 수행하면 스탬프를 지급합니다.
     * @param bookTitle 스탬프를 부여받을 수 있는 행동을 수행한 책 이름입니다.
     * @param achievementCode 사용자가 수행한 행동 코드입니다.
     */
    public void addStampIfEligible(String bookTitle, int achievementCode){
        if (achievementCode == ACHIEVEMENT_READALL || achievementCode == ACHIEVEMENT_QUIZ){
            if (stampDBManager.insertDataIfNotExists(bookTitle, achievementCode) != -1){
                Toast.makeText(context, (achievementCode == ACHIEVEMENT_READALL ? "컨텐츠 완독을" : "퀴즈 풀기를") + " 달성하여 스탬프를 지급합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * DB에 저장된 모든 stamp 정보를 반환하는 메소드입니다.
     * @return DB에 저장된 전체 스탬프 정보를 ArrayList로 반환합니다.
     */
    public ArrayList<StampData> getAllStampData(){
        return stampDBManager.getAllStampFromDB();
    }
}
