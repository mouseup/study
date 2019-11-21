package org.techtown.samplepager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = findViewById(R.id.pager);

        // 미리 로딩해 놓을 아이템의 개수를 세 개로 늘림
        // 뷰페이저는 어댑터가 가지고 있는 아이템 중에서 몇 개를 미리 로딩해 두었다가
        // 좌우 스크롤할 때 빠르게 보여줄 수 있다.
        pager.setOffscreenPageLimit(3);

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());

        // 프래그먼트를 객체로 만든 후 어댑터 객체에 추가
        // addItem 메서드를 호출하면서 프래그먼트 객체를 파라미터로 넘겨주면
        // 어댑터 안에 들어 있는 ArrayList에 추가
        Fragment1 fragment1 = new Fragment1();
        adapter.addItem(fragment1);

        Fragment2 fragment2 = new Fragment2();
        adapter.addItem(fragment2);

        Fragment3 fragment3 = new Fragment3();
        adapter.addItem(fragment3);

        // ViewPager 객체에 어댑터 객체를 설정
        // 이때부터 뷰페이저는 어댑터에 있는 프래그먼트들을 화면에 보여줄 수 있게 된다.
        pager.setAdapter(adapter);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(1);
            }
        });



    }

    // MyPagerAdapter - 뷰페이저에 보여줄 각 프래그먼트를 관리하는 역할
    class MyPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> items = new ArrayList<Fragment>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // ArrayList 객체 안에 프래그먼트를 추가
        public void addItem(Fragment item) {
            items.add(item);
        }

        // ArrayList 객체 안에 프래그먼트를 가져갈수 있음
        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        // 프래그먼트 개수 확인
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "페이지 " + position;
        }

    }

}
