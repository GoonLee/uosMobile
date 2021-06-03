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


public class Fragment1 extends Fragment {

    private ArrayList<BookData> bookList;
    private BookAdapter bookAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment1, container, false);

        recyclerView = view.findViewById(R.id.BookList);
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

    public void load(){
        bookList = new ArrayList<BookData>();
        String sdPath;
        String title;
        sdPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/Contents";
        try {
            File bookTitle = new File(sdPath);

            Log.d("file", bookTitle.getAbsolutePath());

            for (File f : bookTitle.listFiles()) {
                title = f.getName();
                System.out.println("f = " + f.getName());
                bookList.add(new BookData(title));
            }
        } catch (Exception e) {
            Log.i("에러: ", e.getMessage());
        }
    }
}
