package com.uosmobile.team1.quiz;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.uosmobile.team1.MainActivity;
import com.uosmobile.team1.R;
import com.uosmobile.team1.common.Constant;
import com.uosmobile.team1.common.DBHelper;
import com.uosmobile.team1.common.MetaAndCachedDBManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    String bookTitle;
    TextView quizTitleTextView, quizQuestion;
    RadioButton quizRadioButton[];
    RadioGroup quizRadioGroup;
    int radioButtonId[] = {R.id.quizRadioButton1, R.id.quizRadioButton2, R.id.quizRadioButton3, R.id.quizRadioButton4};
    int answerNum, totalQuizNum, quizNum = 1;
    MetaAndCachedDBManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        manager = new MetaAndCachedDBManager(new DBHelper(getApplicationContext(), Constant.NAME_DB, null, 1));

        //intent로 bookTitle 전달받음
        bookTitle = getIntent().getStringExtra("bookTitle");

        //상단 카드뷰 텍스트 변경
        quizTitleTextView = findViewById(R.id.quizTitleTextView);
        quizTitleTextView.setText("QUIZ: " + bookTitle);

        //퀴즈 문제 텍스트 초기화
        quizQuestion = findViewById(R.id.quizQuestion);

        //라디오 그룹 초기화
        quizRadioGroup = findViewById(R.id.quizRadioGroup);
        quizRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.quizRadioButton1:
                        checkAnswer(1);
                        break;
                    case R.id.quizRadioButton2:
                        checkAnswer(2);
                        break;
                    case R.id.quizRadioButton3:
                        checkAnswer(3);
                        break;
                    case R.id.quizRadioButton4:
                        checkAnswer(4);
                        break;
                }
            }
        });

        //라디오 버튼 초기화
        quizRadioButton = new RadioButton[4];
        for(int i=0;i<4;i++){
            quizRadioButton[i] = findViewById(radioButtonId[i]);
        }
        
        //필요 변수 초기화
        totalQuizNum = getTotalQuizNumFromFile(bookTitle);

        setQuiz();
    }

    private void setQuiz(){
        String quizPath = Constant.NAME_CONTENTS_ABSOLUTE_PATH + "/" + bookTitle + "/" + Constant.NAME_DIRECTORY_QUIZ + "/" + quizNum + ".txt";
        Log.d("TAG", "quizPath: " + quizPath);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(quizPath));

            //문제, 항목, 답 READ
            quizQuestion.setText(reader.readLine());
            for(int i=0;i<4;i++){
                String line = reader.readLine();
                quizRadioButton[i].setText((i+1)+". "+line);
                Log.d("TAG", line);
            }
            answerNum = Integer.valueOf(reader.readLine());
            Log.d("TAG", "answerNum: " + answerNum);

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "주어진 문제가 모두 끝났습니다!",Toast.LENGTH_SHORT).show();

            if(quizNum>1){
                //DB 스탬프 업데이트
                manager.updateStampGivenByQuiz(bookTitle);
            }

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    private int getTotalQuizNumFromFile(String bookTitle){
        File f = new File(Constant.NAME_CONTENTS_ABSOLUTE_PATH + "/" + bookTitle + "/" + Constant.NAME_DIRECTORY_QUIZ);
        File[] files = f.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase(Locale.US).endsWith(".txt");
            }
        });
        Log.d("DEBUGTAG", "files.length: "+files.length);
        return files != null? files.length : 0;
    }

    private void checkAnswer(int radioButtonNum){
        Boolean isRight = false;
        if(answerNum == radioButtonNum){
            Toast.makeText(getApplicationContext(), "맞았어요!",Toast.LENGTH_SHORT).show();
            isRight = true;
        }else{
            Toast.makeText(getApplicationContext(), "다시 한 번 생각해보세요!",Toast.LENGTH_SHORT).show();
        }

        if(isRight){
            quizNum++;
            quizRadioGroup.clearCheck();
            setQuiz();
        }
    }
}
