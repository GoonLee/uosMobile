package com.uosmobile.team1.booklist;

import android.os.Bundle;
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
import com.uosmobile.team1.R;
import com.uosmobile.team1.common.Constant;

import java.io.File;
import java.util.ArrayList;

public class DownloadedBookFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_downloaded_list, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        RecyclerView recyclerView = view.findViewById(R.id.BookList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.scrollToPosition(0);
        DownloadedBookAdapter downloadedBookAdapter = new DownloadedBookAdapter(loadDownloadedBooks());
        recyclerView.setAdapter(downloadedBookAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    private ArrayList<BookData> loadDownloadedBooks(){
        ArrayList<BookData> bookDataList = new ArrayList<>();

        File downloadedContentsDirectory = new File(Constant.NAME_CONTENTS_ABSOLUTE_PATH);
        try{
            for(File f : downloadedContentsDirectory.listFiles()){
                bookDataList.add(new BookData(f.getName()));
            }
        }
        catch(Exception e){
            Log.e("Exception", "loadDownloadedBooks: Error occurred while loading contents");
        }

        return bookDataList;
    }
}
