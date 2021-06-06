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

/**
 * 사용자가 터치를 통해 그림을 그릴 수 있는 프래그먼트입니다.
 * 액티비티의 중계를 통해 TextFragment와 값을 주고받기 위해 내부 인터페이스를 갖습니다.
 */
public class DrawingFragment extends Fragment {
    int page;
    String bookTitle;
    PaintView paintView;
    ImageDBManager manager;
    DrawingFragmentResultListener fragmentResultListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // context 정보의 활용이 필요한 초기화를 진행
        manager = new ImageDBManager(new DBHelper(context, Constant.NAME_DB, null, Constant.VERSION_DB));
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

        // 액티비티로부터 프래그먼트 전환 시 전달된 책 이름과 페이지 번호
        Bundle bundle = getArguments();

        // page 번호의 default value는 -1이지만 정상적인 실행 흐름에서는 텍스트 프래그먼트가 반드시 먼저 실행되기 때문에 -1 값을 가질 일은 없음
        bookTitle = bundle.getString("bookTitle");
        page = bundle.getInt("page", -1);

        // 실제로 사용자의 터치에 따라 그림이 그려질 custom view
        paintView = v.findViewById(R.id.paintView);

        // 프래그먼트의 버튼들에 대해 onClickListener 세팅
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
                // 오픈소스 ColorPicker 라이브러리 사용
                ColorPicker colorPicker = new ColorPicker(getActivity());
                // 색상이 선택되면 paintView의 paint 색상을 그 색으로 변경
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
                // 기본 제공 색상이 파스텔톤에 색 다양성이 부족해 직접 지정
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
        /*
        내부 custom view의 생성자가 실행되고 onDraw는 실행되기 이전에 초기 값 지정
        자세한 사항은 프래그먼트 생명주기와 custom view 생성 실행 흐름 확인
         */
        paintView.setPaintViewStatus(manager.getPaintViewInfoOrNullFromDB(bookTitle, page));
        paintView.setBackgroundImage(BitmapFactory.decodeFile(Constant.NAME_CONTENTS_ABSOLUTE_PATH + "/" + bookTitle + "/" + Constant.NAME_DIRECTORY_IMAGE + "/" + page + ".bmp"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /*
        프래그먼트가 전환될 때 page 값을 액티비티로 전달
        만약 사용자가 paintView에 그림을 그렸다면 다음 실행시 해당 상태 복원을 위해 이미지 비트맵과 배경 활성화 여부를 DB에 저장
         */
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

    /**
     * 액티비티의 중계를 통해 프래그먼트간 데이터 잔달을 하기 위해 생성한 인터페이스
     */
    public interface DrawingFragmentResultListener{
        void onDrawingFragmentDestroyView(int page);
    }
}
