package com.uosmobile.team1.booklist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.uosmobile.team1.R;
import com.uosmobile.team1.bookcontents.BookContentsActivity;

import java.util.ArrayList;

/**
 * DownloadedBookFragment에서 사용할 Recyclerview의 Adapter입니다.
 */
public class DownloadedBookAdapter extends RecyclerView.Adapter<DownloadedBookAdapter.ViewHolder>{
    private ArrayList<BookData> bookList;

    public DownloadedBookAdapter(ArrayList<BookData> bookList){
        this.bookList = bookList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.book_title);

            // Recyclerview의 각 아이텥의 onClickListener 세팅
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        // 선택된 책을 보여주는 BookContentsActivity 실행
                        Intent intent = new Intent(view.getContext(), BookContentsActivity.class);
                        intent.putExtra("bookTitle", bookList.get(pos).getTitle());
                        view.getContext().startActivity(intent);
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
    public DownloadedBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.books, parent, false);
        return new DownloadedBookAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadedBookAdapter.ViewHolder holder, int position) {
        holder.getTextView().setText(bookList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}
