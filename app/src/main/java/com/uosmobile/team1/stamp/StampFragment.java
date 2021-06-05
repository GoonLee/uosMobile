package com.uosmobile.team1.stamp;

import android.content.Context;
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

public class StampFragment extends Fragment {
    StampService stampService;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stamp, container, false);

        System.out.println("OCV called");

        stampService = new StampService(getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        RecyclerView recyclerView = view.findViewById(R.id.stampList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.scrollToPosition(0);
        StampAdapter stampAdapter = new StampAdapter(stampService.getAllStampData());
        recyclerView.setAdapter(stampAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }
}
