package com.uosmobile.team1;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class Fragment0 extends Fragment {

    private ArrayList<BookData> bookList;
    private BookAdapter bookAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private String ext = Environment.getExternalStorageState();
    private String sdPath;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment0, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.BookList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.scrollToPosition(0);
        bookAdapter = new BookAdapter(bookList);
        recyclerView.setAdapter(bookAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        load();
    }

    private void initDataset(){
        //for Test
        bookList = new ArrayList<BookData>();
        bookList.add(new BookData("개구리 소년"));
        bookList.add(new BookData("신데렐라"));
        bookList.add(new BookData("촉법소년"));
    }

    public void load(){
        bookList = new ArrayList<BookData>();
        String sdPath;
        String title;
        String ext = Environment.getExternalStorageState();
        if(ext.equals(Environment.MEDIA_MOUNTED)){
            sdPath = Environment.getExternalStoragePublicDirectory("Download").getAbsolutePath();
            Log.d("dd",sdPath);
        } else {
            sdPath = Environment.MEDIA_UNMOUNTED;
        }
        try {
            File bookTitle = new File(sdPath, "Contents");

            Log.d("file", bookTitle.getAbsolutePath());

            for (File f : bookTitle.listFiles()) {
                if (f.isFile()) {
                    title = f.getName();
                    bookList.add(new BookData(title));
                }
            }
        } catch (Exception e) {
            Log.i("에러: ", e.getMessage());
        }
    }
}