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

public class BookContentsActivity extends AppCompatActivity implements DrawingFragment.DrawingFragmentResultListener, TextFragment.TextFragmentResultListener{
    Bundle fragmentArgument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contents);

        String bookTitle = getIntent().getStringExtra("bookTitle");

        TextFragment textFragment = new TextFragment();
        DrawingFragment drawingFragment = new DrawingFragment();

        fragmentArgument = new Bundle();
        fragmentArgument.putString("bookTitle", bookTitle);

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

        PageInfoDBManager manager = new PageInfoDBManager(new DBHelper(this, Constant.NAME_DB, null, Constant.VERSION_DB));
        manager.insertDataIfNotExists(bookTitle, getTotalPageFromFile(bookTitle));

        textFragment.setArguments(fragmentArgument);
        getSupportFragmentManager().beginTransaction().replace(R.id.ContentsContainer, textFragment).commit();
    }

    @Override
    public void onTextFragmentDestroyView(int page) {
        fragmentArgument.putInt("page", page);
    }

    @Override
    public void onDrawingFragmentDestroyView(int page) {
        fragmentArgument.putInt("page", page);
    }

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
