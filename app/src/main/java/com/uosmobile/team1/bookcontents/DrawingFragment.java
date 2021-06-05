package com.uosmobile.team1.bookcontents;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uosmobile.team1.R;
import com.uosmobile.team1.common.Constant;
import com.uosmobile.team1.common.DBHelper;
import com.uosmobile.team1.common.ImageDBManager;
import petrov.kristiyan.colorpicker.ColorPicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DrawingFragment extends Fragment {
    int page;
    String bookTitle;
    PaintView paintView;
    ImageDBManager manager;
    DrawingFragmentResultListener fragmentResultListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        manager = new ImageDBManager(new DBHelper(context, Constant.NAME_DB, null, 1));
        if(context instanceof DrawingFragmentResultListener){
            fragmentResultListener = (DrawingFragmentResultListener) context;
        } else{
            throw new RuntimeException(context + "must implement DrawingFragment.DrawingFragmentResultListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_contents_drawing, container, false);

        Bundle bundle = getArguments();

        bookTitle = bundle.getString("bookTitle");
        page = bundle.getInt("page", -1);

        paintView = v.findViewById(R.id.paintView);

        FloatingActionButton mainFab = v.findViewById(R.id.mainFab);
        FloatingActionButton eraserFab = v.findViewById(R.id.eraserFab);
        FloatingActionButton colorFab = v.findViewById(R.id.colorFab);
        FloatingActionButton saveFab = v.findViewById(R.id.saveFab);
        FloatingActionButton bgFab = v.findViewById(R.id.backgroundFab);

        mainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eraserFab.getVisibility() == View.VISIBLE){
                    mainFab.setImageResource(R.drawable.ic_baseline_add_24);
                    eraserFab.setVisibility(View.GONE);
                    colorFab.setVisibility(View.GONE);
                    saveFab.setVisibility(View.GONE);
                    bgFab.setVisibility(View.GONE);
                } else{
                    mainFab.setImageResource(R.drawable.ic_baseline_clear_24);
                    eraserFab.setVisibility(View.VISIBLE);
                    colorFab.setVisibility(View.VISIBLE);
                    saveFab.setVisibility(View.VISIBLE);
                    bgFab.setVisibility(View.VISIBLE);
                }
            }
        });

        eraserFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintView.toggleEraser();
                eraserFab.setImageResource(paintView.isEraserToggled() ? R.drawable.ic_baseline_edit_24 : R.drawable.ic_baseline_auto_fix_normal_24);
            }
        });

        colorFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPicker colorPicker = new ColorPicker(getActivity());
                colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
                    @Override
                    public void setOnFastChooseColorListener(int position, int color) {
                        colorPicker.setColorButtonTickColor(position);
                        paintView.setPaintColor(color);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                colorPicker.setColumns(5);
                colorPicker.setTitle("색상 선택");
                colorPicker.setRoundColorButton(true);
                colorPicker.setColors(
                        new ArrayList<>(
                                Arrays.asList(
                                        "#000000",
                                        "#ffffff",
                                        "#ff0000",
                                        "#ff007f",
                                        "#ff00ff",
                                        "#ff7f00",
                                        "#ffff00",
                                        "#00ff00",
                                        "#00ff7f",
                                        "#7fff00",
                                        "#7fff7f",
                                        "#0000ff",
                                        "#007fff",
                                        "#00ffff",
                                        "#7f00ff",
                                        "#bd5524",
                                        "#c68642",
                                        "#e0ac69",
                                        "#f1c27d",
                                        "#ffdbac"
                                )
                        )
                );
                colorPicker.show();
            }
        });

        saveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    paintView.saveToGallery();
                } catch (IOException e) {
                    Toast.makeText(getContext(), "이미지 저장 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bgFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintView.toggleBackground();
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        paintView.setPaintViewStatus(manager.getFrontPaintViewInfoOrNullFromDB(bookTitle, page));
        paintView.setBackgroundImage(BitmapFactory.decodeFile(Constant.NAME_CONTENTS_ABSOLUTE_PATH + "/" + bookTitle + "/" + Constant.NAME_DIRECTORY_IMAGE + "/" + page + ".bmp"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentResultListener.onDrawingFragmentDestroyView(page);
        if(paintView.isModified()){
            manager.insertOrUpdatePaintViewInfoToDB(bookTitle, page, paintView.getPaintViewStatus());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentResultListener = null;
    }

    public interface DrawingFragmentResultListener{
        void onDrawingFragmentDestroyView(int page);
    }
}
