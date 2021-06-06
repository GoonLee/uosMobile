package com.uosmobile.team1.bookcontents;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.tabs.TabLayout;
import com.uosmobile.team1.R;
import com.uosmobile.team1.common.Constant;
import com.uosmobile.team1.common.DBHelper;
import com.uosmobile.team1.common.PageInfoDBManager;

import java.io.File;
import java.io.FileFilter;
import java.util.Locale;

/**
 * 책의 내용을 보여주기 위한 액티비티입니다.
 * 하위 컴포넌트로 TextFragment, DrawingFragment를 갖습니다.
 * 또한 두 프래그먼트간 데이터 전달을 위해 각 프래그먼트에서 정의한 리스너를 상속받습니다.
 */
public class BookContentsActivity extends AppCompatActivity implements DrawingFragment.DrawingFragmentResultListener, TextFragment.TextFragmentResultListener{
    Bundle fragmentArgument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contents);


        // 액티비티 실행시 Intent를 통해 전달된 책 이름
        String bookTitle = getIntent().getStringExtra("bookTitle");

        TextFragment textFragment = new TextFragment();
        DrawingFragment drawingFragment = new DrawingFragment();

        // 프래그먼트에 책 이름을 그대로 전달
        fragmentArgument = new Bundle();
        fragmentArgument.putString("bookTitle", bookTitle);

        // 상단의 TabLayout을 통해 프래그먼트 전환
        TabLayout tabLayout = findViewById(R.id.ShowContentsTabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if(position == 0){
                    textFragment.setArguments(fragmentArgument);
                    getSupportFragmentManager().beginTransaction().replace(R.id.ContentsContainer, textFragment).commit();
                } else if(position == 1){
                    drawingFragment.setArguments(fragmentArgument);
                    getSupportFragmentManager().beginTransaction().replace(R.id.ContentsContainer, drawingFragment).commit();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        /*
        액티비티 실행 시 DB에 해당 책의 전체 페이지 정보와 사용자가 마지막으로 본 페이지를 이미 저장하지 않은 경우 저장
        이 부분은 다운로드 기능의 미구현으로 추가된 코드입니다.
        다운로드 기능 구현시 전체 페이지 정보를 같이 다운받고, 사용자가 마지막으로 본 페이지만 TextFragment에서 저장하는 방식의 사용이 가능합니다.
         */
        PageInfoDBManager manager = new PageInfoDBManager(new DBHelper(this, Constant.NAME_DB, null, Constant.VERSION_DB));
        manager.insertDataIfNotExists(bookTitle, getTotalPageFromFile(bookTitle));

        // 최초 액티비티 실행시 TextFragment가 화면에 나타나도록 설정
        textFragment.setArguments(fragmentArgument);
        getSupportFragmentManager().beginTransaction().replace(R.id.ContentsContainer, textFragment).commit();
    }

    /**
     * 프래그먼트간 페이지 정보 공유를 위해 프래그먼트가 가지고 있던 페이지 정보를 bundle에 저장하는 메소드입니다.
     * @param page 프래그먼트 이동 시 프래그먼트의 page 값
     */
    @Override
    public void onTextFragmentDestroyView(int page) {
        fragmentArgument.putInt("page", page);
    }

    /**
     * 프래그먼트간 페이지 정보 공유를 위해 프래그먼트가 가지고 있던 페이지 정보를 bundle에 저장하는 메소드입니다.
     * @param page 프래그먼트 이동 시 프래그먼트의 page 값
     */
    @Override
    public void onDrawingFragmentDestroyView(int page) {
        fragmentArgument.putInt("page", page);
    }

    /**
     * 컨텐츠 폴더에서 매개변수로 주어진 책의 텍스트 파일 수를 확인하여 리턴하는 메소드입니다.
     * 이 메소드는 다운로드 기능의 미구현으로 추가된 메소드입니다.
     * 다운로드 기능 구현시 전체 페이지 정보를 같이 다운로드받고 해당 파일을 읽는 방식으로 변경이 가능합니다.
     * @param bookTitle 현재 액티비티에서 사용할 책 이름입니다.
     * @return 해당 책의 텍스트 파일 수를 리턴합니다.
     */
    private int getTotalPageFromFile(String bookTitle){
        File f = new File(Constant.NAME_CONTENTS_ABSOLUTE_PATH + "/" + bookTitle + "/" + Constant.NAME_DIRECTORY_TEXT);
        File[] files = f.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase(Locale.US).endsWith(".txt");
            }
        });
        return files != null? files.length : 0;
    }
}
