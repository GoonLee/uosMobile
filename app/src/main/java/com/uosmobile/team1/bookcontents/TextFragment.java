package com.uosmobile.team1.bookcontents;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.uosmobile.team1.R;
import com.uosmobile.team1.common.Constant;
import com.uosmobile.team1.common.DBHelper;
import com.uosmobile.team1.common.PageInfoDBManager;
import com.uosmobile.team1.quiz.QuizActivity;
import com.uosmobile.team1.stamp.StampService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 책의 내용을 사용자에게 보여주는 프래그먼트입니다.
 */
public class TextFragment extends Fragment {

    int page;
    String bookTitle;
    boolean pageMoved;
    MediaPlayer mediaPlayer;
    PageInfoDBManager manager;
    StampService stampService;
    TextFragmentResultListener fragmentResultListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // context 정보의 활용이 필요한 초기화를 진행
        manager = new PageInfoDBManager(new DBHelper(context, Constant.NAME_DB, null, Constant.VERSION_DB));
        stampService = new StampService(context);
        if(context instanceof DrawingFragment.DrawingFragmentResultListener){
            fragmentResultListener = (TextFragmentResultListener) context;
        } else{
            throw new RuntimeException(context + "must implement TextFragment.TextFragmentResultListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contents_text, container, false);

        // 액티비티로부터 프래그먼트 전환 시 전달된 책 이름과 페이지 번호
        Bundle bundle = getArguments();

        bookTitle = bundle.getString("bookTitle");
        page = bundle.getInt("page", -1);
        // 최초 BookContentsActivity가 실행되어 전달받은 page 번호가 없을 경우 DB에서 사용자가 마지막으로 본 page 번호를 read
        if(page == -1){
            page = manager.getLastPageFromDB(bookTitle);
        }
        /*
        Quiz로 이동 및 Stamp 부여를 제어하기 위해 책의 마지막 page 번호를 DB에서 read
        현재 구현에서는 Activity에서 전달하는 것도 가능하지만 다운로드 기능이 존재할 때 별도 파일에서 읽는 것과 유사하게 개발
         */
        int contentsTotalPage = manager.getTotalPageFromDB(bookTitle);

        TextView contentsTextTextView = v.findViewById(R.id.ContentsTextTextView);

        // 프래그먼트의 버튼들에 대해 onClickListener 세팅
        Button goToQuizButton = v.findViewById(R.id.goToQuizButton);
        goToQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), QuizActivity.class);
                intent.putExtra("bookTitle", bookTitle);
                view.getContext().startActivity(intent);
            }
        });

        Button contentsTextPrevButton = v.findViewById(R.id.ContentsTextPrevButton);
        Button contentsTextNextButton = v.findViewById(R.id.ContentsTextNextButton);

        contentsTextPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()) mediaPlayer.stop();
                --page;
                pageMoved = true;
                if(page == 1) contentsTextPrevButton.setVisibility(View.INVISIBLE);
                if (page == contentsTotalPage - 1){
                    contentsTextNextButton.setVisibility(View.VISIBLE);
                    goToQuizButton.setVisibility(View.GONE);
                }
                contentsTextTextView.setText(getTextFromFile(page));
                playSoundTrack(page);
            }
        });

        contentsTextNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()) mediaPlayer.stop();
                ++page;
                pageMoved = true;
                if(page == contentsTotalPage){
                    contentsTextNextButton.setVisibility(View.INVISIBLE);
                    goToQuizButton.setVisibility(View.VISIBLE);
                    //최초로 마지막 페이지에 도달할 경우 스탬프를 부여
                    stampService.addStampIfEligible(bookTitle, StampService.ACHIEVEMENT_READALL);
                }
                if(page == 2){
                    contentsTextPrevButton.setVisibility(View.VISIBLE);
                }
                contentsTextTextView.setText(getTextFromFile(page));
                playSoundTrack(page);
            }
        });

        // 프래그먼트 실행 시 페이지 세팅
        if(page == 1) contentsTextPrevButton.setVisibility(View.INVISIBLE);

        if(page == contentsTotalPage){
            contentsTextNextButton.setVisibility(View.INVISIBLE);
            goToQuizButton.setVisibility(View.VISIBLE);
            //최초로 마지막 페이지에 도달할 경우 스탬프를 부여
            stampService.addStampIfEligible(bookTitle, StampService.ACHIEVEMENT_READALL);
        }

        contentsTextTextView.setText(getTextFromFile(page));
        playSoundTrack(page);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mediaPlayer != null) mediaPlayer.release();
        fragmentResultListener.onTextFragmentDestroyView(page);
        if(pageMoved) manager.updateLastPageToDB(bookTitle, page);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentResultListener = null;
    }

    /**
     * 텍스트 파일을 읽어 책의 해당 페이지의 내용을 반환하는 메소드입니다.
     * @param page 내용을 가져올 페이지 번호입니다.
     * @return 텍스트 파일의 내용을 String으로 반환합니다.
     */
    private String getTextFromFile(int page){
        try {
            FileInputStream inFs = new FileInputStream(Constant.NAME_CONTENTS_ABSOLUTE_PATH + "/" + bookTitle + "/" + Constant.NAME_DIRECTORY_TEXT + "/" + page + ".txt");
            byte[] txt = new byte[inFs.available()];
            inFs.read(txt);
            inFs.close();
            return new String(txt);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "READ 오류",Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    /**
     * 해당 페이지의 사운드 파일이 존재하면 재생하는 메소드입니다.
     * @param page 사운드 파일이 존재하는지 확인할 페이지 번호입니다.
     */
    private void playSoundTrack(int page){
        try{
            if(mediaPlayer != null) mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(Constant.NAME_CONTENTS_ABSOLUTE_PATH + "/" + bookTitle + "/" + Constant.NAME_DIRECTORY_SOUND + "/" + page + ".mp3");
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch(FileNotFoundException ignored){
            // 사운드가 없는 페이지에서 발생하는 예외를 무시합니다.
        } catch(IOException e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Sound 재생 오류",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 액티비티의 중계를 통해 프래그먼트간 데이터 잔달을 하기 위해 생성한 인터페이스
     */
    public interface TextFragmentResultListener{
        void onTextFragmentDestroyView(int page);
    }
}
