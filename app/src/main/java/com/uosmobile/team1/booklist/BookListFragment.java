package com.uosmobile.team1.booklist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.tabs.TabLayout;
import com.uosmobile.team1.R;

/**
 * 메인 액티비티에서 책의 리스트를 사용자에게 보여주는 프래그먼트입니다.
 * 하위 프래그먼트로 DownloadableBookFragment, DownloadedBookFragment를 가져 중첩 프래그먼트 구조를 사용합니다.
 */
public class BookListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        Fragment downloadableBookFragment = new DownloadableBookFragment();
        Fragment downloadedBookFragment = new DownloadedBookFragment();

        // 상단의 TabLayout을 통해 프래그먼트 전환
        TabLayout tabLayout = view.findViewById(R.id.BookList);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if(position == 0){
                    getChildFragmentManager().beginTransaction().replace(R.id.frame, downloadableBookFragment).commit();
                } else if (position == 1){
                    getChildFragmentManager().beginTransaction().replace(R.id.frame, downloadedBookFragment).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // 최초 실행 시 프래그먼트를 DownloadableBookFragment로 세팅
        getChildFragmentManager().beginTransaction().add(R.id.frame, downloadableBookFragment).commit();

        return view;
    }
}
