package com.uosmobile.team1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder>{

    private ArrayList<BookData> bookList;

    public BookAdapter(ArrayList<BookData> bookList){
        this.bookList = bookList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.book);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        BookData book = bookList.get(pos);
                        String bookTitle = book.getTitle();
                        Intent intent = new Intent(view.getContext(), ShowContentsActivity.class);
                        intent.putExtra("contentsName", bookTitle);
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.books, parent, false);
        ViewHolder vh = new BookAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
        holder.getTextView().setText(bookList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

}
