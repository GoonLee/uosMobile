package com.uosmobile.team1.stamp;

import android.content.Context;
import android.widget.Toast;
import com.uosmobile.team1.common.Constant;
import com.uosmobile.team1.common.DBHelper;
import com.uosmobile.team1.common.StampDBManager;

import java.util.ArrayList;

public class StampService {
    public static int ACHIEVEMENT_READALL = 0;
    public static int ACHIEVEMENT_QUIZ = 1;

    Context context;
    StampDBManager stampDBManager;

    public StampService(Context context){
        this.context = context;
        this.stampDBManager = new StampDBManager(new DBHelper(context, Constant.NAME_DB, null, Constant.VERSION_DB));
    }

    public void addStampIfEligible(String bookTitle, int achievementCode){
        if (achievementCode == ACHIEVEMENT_READALL || achievementCode == ACHIEVEMENT_QUIZ){
            if (stampDBManager.insertDataIfNotExists(bookTitle, achievementCode) != -1){
                Toast.makeText(context, (achievementCode == ACHIEVEMENT_READALL ? "컨텐츠 완독을" : "퀴즈 풀기를") + " 달성하여 스탬프를 지급합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public ArrayList<StampData> getAllStampData(){
        return stampDBManager.getAllStampFromDB();
    }
}
