package com.uosmobile.team1.booklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.uosmobile.team1.R;

import java.util.ArrayList;

/**
 * DownloadableBookFragment에서 사용할 Recyclerview의 Adapter입니다.
 */
public class DownloadableBookAdapter extends RecyclerView.Adapter<DownloadableBookAdapter.ViewHolder> {
    private ArrayList<BookData> bookList;

    public DownloadableBookAdapter(ArrayList<BookData> bookList){
        this.bookList = bookList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.book_title);

            // Recyclerview의 각 아이텥의 onClickListener 세팅
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getAdapterPosition() != RecyclerView.NO_POSITION){
                        Toast.makeText(view.getContext(), "다운로드 기능은 미구현입니다.\n" +
                                "함께 첨부된 contents 파일을 보고서를 참조하여 AVD에 업로드 해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        public TextView getTextView() {
            return textView;
        }
    }

    @NonNull
    @Override
    public DownloadableBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.books, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadableBookAdapter.ViewHolder holder, int position) {
        holder.getTextView().setText(bookList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}
