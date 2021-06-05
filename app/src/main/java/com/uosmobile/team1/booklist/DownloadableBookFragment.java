package com.uosmobile.team1.booklist;

import android.os.Bundle;
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
import java.util.ArrayList;

public class DownloadableBookFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_downloadable_list, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        RecyclerView recyclerView = view.findViewById(R.id.BookList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.scrollToPosition(0);
        DownloadableBookAdapter downloadableBookAdapter = new DownloadableBookAdapter(loadDownloadableBooks());
        recyclerView.setAdapter(downloadableBookAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    private ArrayList<BookData> loadDownloadableBooks(){
        ArrayList<BookData> bookDataList = new ArrayList<>();

        bookDataList.add(new BookData("콩쥐팥쥐"));
        bookDataList.add(new BookData("은혜 갚은 호랑이"));
        bookDataList.add(new BookData("해와 달이 된 오누이"));

        return bookDataList;
    }
}
