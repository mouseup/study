package com.aso.shoppinghistory;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements OnTabItemSelectedListener, AutoPermissionsListener {
    private static final String TAG = "MainActivity";

    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;
    Fcategory fcategory;

    BottomNavigationView bottomNavigation;

    /**
     * 데이터베이스 인스턴스
     */
    public static NoteDatabase mDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fcategory = new Fcategory();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab1:
                        Toast.makeText(getApplicationContext(), "첫 번째 탭 선택됨", Toast.LENGTH_LONG).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();

                        return true;
                    case R.id.tab2:
                        Toast.makeText(getApplicationContext(), "두 번째 탭 선택됨", Toast.LENGTH_LONG).show();

                        fragment2 = new Fragment2();

                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment2).commit();

                        return true;
                    case R.id.tab3:
                        Toast.makeText(getApplicationContext(), "세 번째 탭 선택됨", Toast.LENGTH_LONG).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment3).commit();

                        return true;
                }

                return false;
            }
        });


        AutoPermissions.Companion.loadAllPermissions(this, 101);
        setPicturePath();


        // 데이터베이스 열기
        openDatabase();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_top, menu);
        try{
            Method method=menu.getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            method.setAccessible(true);
            method.invoke(menu, true);
        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        switch(id) {
            case R.id.menuSutup:
                Toast.makeText(getApplicationContext(), "카테고리", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fcategory).commit();
                return true;

            case R.id.menuHelp:
                Toast.makeText(getApplicationContext(), "도움말", Toast.LENGTH_SHORT).show();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void onTabSelected(int position) {
        if (position == 0) {
            bottomNavigation.setSelectedItemId(R.id.tab1);
        } else if (position == 1) {
            bottomNavigation.setSelectedItemId(R.id.tab2);
        } else if (position == 2) {
            bottomNavigation.setSelectedItemId(R.id.tab3);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }
    }

    /**
     * 데이터베이스 열기 (데이터베이스가 없을 때는 만들기)
     */
    public void openDatabase() {
        // open database
        if (mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }

        mDatabase = NoteDatabase.getInstance(this);
        boolean isOpen = mDatabase.open();
        if (isOpen) {
            Log.d(TAG, "Note database is open.");
        } else {
            Log.d(TAG, "Note database is not open.");
        }
    }



    public void setPicturePath() {
        String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        AppConstants.FOLDER_PHOTO = sdcardPath + File.separator + "photo";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }

    @Override
    public void onDenied(int requestCode, @NotNull String[] permissions) {
        Toast.makeText(this, "permissions denied : " + permissions.length, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGranted(int requestCode, @NotNull String[] permissions) {
        Toast.makeText(this, "permissions granted : " + permissions.length, Toast.LENGTH_LONG).show();
    }



    public void showFragment2(Note item) {

        fragment2 = new Fragment2();
        fragment2.setItem(item);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment2).commit();

    }

    private void println(String data) {
        Log.d(TAG, data);
    }

}
