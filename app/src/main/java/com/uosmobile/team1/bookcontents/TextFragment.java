package com.uosmobile.team1.bookcontents;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
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
import com.uosmobile.team1.common.MetaAndCachedDBManager;
import com.uosmobile.team1.quiz.QuizActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TextFragment extends Fragment {

    int page;
    String bookTitle;
    boolean pageMoved;
    MediaPlayer mediaPlayer;
    MetaAndCachedDBManager manager;
    TextFragmentResultListener fragmentResultListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        manager = new MetaAndCachedDBManager(new DBHelper(context, Constant.NAME_DB, null, Constant.VERSION_DB));
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

        bookTitle = getArguments().getString("bookTitle");
        page = getArguments().getInt("page", -1);
        if(page == -1){
            page = manager.getLastPageFromDB(bookTitle);
        }
        int contentsTotalPage = manager.getTotalPageFromDB(bookTitle);
        Log.d("DEBUGTAG", "contentsTotalPage: "+contentsTotalPage);

        TextView contentsTextTextView = v.findViewById(R.id.ContentsTextTextView);

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
                }
                if(page == 2){
                    contentsTextPrevButton.setVisibility(View.VISIBLE);
                }
                contentsTextTextView.setText(getTextFromFile(page));
                playSoundTrack(page);
            }
        });

        if(page == 1) contentsTextPrevButton.setVisibility(View.INVISIBLE);

        if(page == contentsTotalPage){
            contentsTextNextButton.setVisibility(View.INVISIBLE);
            goToQuizButton.setVisibility(View.VISIBLE);

            //DB 스탬프 업데이트
            manager.updateStampGivenByComplete(bookTitle);
        }

        contentsTextTextView.setText(getTextFromFile(page));
        playSoundTrack(page);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mediaPlayer.release();
        fragmentResultListener.onTextFragmentDestroyView(page);
        if(pageMoved) manager.updateLastPageToDB(bookTitle, page);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentResultListener = null;
    }

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

    private void playSoundTrack(int page){
        try{
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(Constant.NAME_CONTENTS_ABSOLUTE_PATH + "/" + bookTitle + "/" + Constant.NAME_DIRECTORY_SOUND + "/" + page + ".mp3");
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch(FileNotFoundException ignored){

        } catch(IOException e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Sound 재생 오류",Toast.LENGTH_SHORT).show();
        }
    }

    public interface TextFragmentResultListener{
        void onTextFragmentDestroyView(int page);
    }
}
