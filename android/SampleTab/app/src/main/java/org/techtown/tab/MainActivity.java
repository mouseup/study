package org.techtown.tab;


import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);

        // xml 레이아웃에서 정의한 toolbar 객체는 코드에서 setSupportActionBar() 메서드를 사용해 액션바로 설정
        // setSupportActionBar() 메서드는 액티비티에 디폴트로 만들어진 액션바가 없을 경우에만 동작
        // 그런데 프로젝트가 만들어 질 때 메인 액티비티에는 자동으로 액션바가 만들어진다.
        // 이것은 테마를 액션바가 들어 있는 테마로 설정했기 때문이다.
        // 액티비티에 설정된 테마를 변경하기 위해 /app/res/valuse 폴더 안에 styles.xml 파일 수정
        // AppTheme 를 NoActionBar 스타일로 바꾸면 이 스타일을 적용한 액티비티에는 액션바가 만들어지지 않는다.
        // 따라서 코드에서 setSupportActionBar 메서드를 호출하여 직접 액션바를 설정해야 한다.
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("통화기록"));
        tabs.addTab(tabs.newTab().setText("스팸기록"));
        tabs.addTab(tabs.newTab().setText("연락처"));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.d("MainActivity", "선택된 탭 : " + position);

                Fragment selected = null;
                if (position == 0) {
                    selected = fragment1;
                } else if (position == 1) {
                    selected = fragment2;
                } else if (position == 2) {
                    selected = fragment3;
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

}