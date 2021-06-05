package com.uosmobile.team1;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uosmobile.team1.booklist.BookListFragment;
import com.uosmobile.team1.common.PermissionSupport;
import com.uosmobile.team1.stamp.StampFragment;

public class MainActivity extends AppCompatActivity {
    PermissionSupport permission;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment bookListFragment = new BookListFragment();
        Fragment stampFragment = new StampFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.mainBottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.goToContents:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, bookListFragment).commit();
                        break;
                    case R.id.goToStamps:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, stampFragment).commit();
                        break;
                }
                return true;
            }
        });

        permissionCheck();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, bookListFragment).commit();
    }

    private void permissionCheck(){
        if(Build.VERSION.SDK_INT>=23){
            permission = new PermissionSupport(this, this);
            if(!permission.checkPermission()){
                permission.requestPermisssion();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(!permission.permissionResult(requestCode, permissions, grantResults)){
            permission.requestPermisssion();
        }
    }
}
