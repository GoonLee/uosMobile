package com.uosmobile.team1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ContentsDrawingFragment extends Fragment {

    TextView testTextView;

    public static ContentsDrawingFragment newInstance(String param1, String param2) {
        ContentsDrawingFragment fragment = new ContentsDrawingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_contents_drawing, container, false);

        Bundle bundle = getArguments();

        String page = bundle.getString("nowPage");

        testTextView = v.findViewById(R.id.testTextView);

        testTextView.setText(page);

        return v;
    }
}