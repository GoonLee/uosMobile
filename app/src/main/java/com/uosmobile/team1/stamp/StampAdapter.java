package com.uosmobile.team1.stamp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.uosmobile.team1.R;

import java.util.ArrayList;

/**
 * StampFragment에서 사용할 Recyclerview의 Adapter입니다.
 */
public class StampAdapter extends RecyclerView.Adapter<StampAdapter.ViewHolder>{
    ArrayList<StampData> stampList;

    public StampAdapter(ArrayList<StampData> stampList){
        this.stampList = stampList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView achievementTextView;
        private final TextView earnedDateTimeTextView;

        public ViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.bookTitle);
            achievementTextView = view.findViewById(R.id.achievement);
            earnedDateTimeTextView = view.findViewById(R.id.earnedDateTime);
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }

        public TextView getAchievementTextView() {
            return achievementTextView;
        }

        public TextView getEarnedDateTimeTextView() {
            return earnedDateTimeTextView;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.stamp, parent, false);
        return new StampAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTitleTextView().setText(stampList.get(position).getBookTitle());
        holder.getAchievementTextView().setText(stampList.get(position).getAchievement());
        holder.getEarnedDateTimeTextView().setText(stampList.get(position).getEarnedDateTime());
    }

    @Override
    public int getItemCount() {
        return stampList.size();
    }
}
