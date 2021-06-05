package com.uosmobile.team1.booklist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.uosmobile.team1.R;
import com.uosmobile.team1.common.Constant;

import java.io.File;
import java.io.FileNotFoundException;
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
        try{
            File downloadedContentsDirectory = new File(Constant.NAME_CONTENTS_ABSOLUTE_PATH);
            for(File f : downloadedContentsDirectory.listFiles()){
                bookDataList.add(new BookData(f.getName()));
            }
        }
        catch(Exception e){
            Toast.makeText(getContext(), "파일을 읽을 수 없습니다." +
                    "\n함께 첨부된 contents 파일을 보고서를 참조하여 AVD에 업로드 하였는지 확인하세요.", Toast.LENGTH_SHORT).show();
        }

        return bookDataList;
    }
}
