package org.techtown.mission10;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentCallback {

    Toolbar toolbar;

    Fragment fragment1;
    Fragment fragment2;
    Fragment fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);

        // 툴바를 액티비티의 액션바로 사용
        // ActionBarDrawerToggle 사용을 위해 Toolbar 를 Actionbar 로 설정
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        // ActionBarDrawerToggle 은 Navigation Drawer 를 ActionBar 에서 콘트롤하기 쉽도록 제공되는 class 이다.
        // ActionBarDrawerToggle 의 생성자에서는 DrawerLayout 과 Toolbar 를 입력받는다.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // addDrawerListener 사용해 DrawerLayout에 추가
        drawer.addDrawerListener(toggle);
        // 네비게이션 뷰가 열리고 닫힐 때 툴바의 햄버거 버튼 모양이 회전되는데 이를 네비게이션 상태에 따라 처리해주는 역할
        toggle.syncState();

        // 레이아웃에 선언된 네비게이션뷰에 선택 리스너 설정
        // 이때 this로 선언하기 위해서는 현재 클래스인 MainActivity 가
        // NavigationView.OnNavigationItemSelectedListener 인터페이스를 구현하고 있어야 한다.
        NavigationView navigationView = findViewById(R.id.nav_view);

        // setNavigationItemSelectedListener 메소드를 통해 사용자가 네비게이션 뷰의 아이템을 클릭하면
        // onNavigationItemSelected 메서드가 호출된다
        navigationView.setNavigationItemSelectedListener(this);

        // 첫 화면에 fragment1 보여주기
        fragment1 = new Fragment1();
        getSupportActionBar().setTitle("첫번째 화면");
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment1).commit();


        // 하단 탭 선택시 이벤트 처리
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab1:
                        Toast.makeText(getApplicationContext(), "첫 번째 탭 선택됨", Toast.LENGTH_LONG).show();

                        fragment1 = new Fragment1();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment1).commit();

                        toolbar.setTitle("첫번째 화면");

                        return true;
                    case R.id.tab2:
                        Toast.makeText(getApplicationContext(), "두 번째 탭 선택됨", Toast.LENGTH_LONG).show();

                        fragment2 = new Fragment2();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment2).commit();

                        toolbar.setTitle("두번째 화면");

                        return true;
                    case R.id.tab3:
                        Toast.makeText(getApplicationContext(), "세 번째 탭 선택됨", Toast.LENGTH_LONG).show();

                        fragment3 = new Fragment3();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment3).commit();

                        toolbar.setTitle("세번째 화면");

                        return true;
                }

                return false;
            }
        });

    }

    @Override
    public void onFragmentSelected(int position, Bundle bundle) {
        Fragment curFragment = null;
        if (position == 0) {
            curFragment = new Fragment1();
            toolbar.setTitle("첫번째 화면");
        } else if (position == 1) {
            curFragment = new Fragment2();
            toolbar.setTitle("두번째 화면");
        } else if (position == 2) {
            curFragment = new Fragment3();
            toolbar.setTitle("세번째 화면");
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.container, curFragment).commit();

    }

    // 시스템 back 키를 눌렀을때 호출
    // 바로가기 메뉴가 열려있을 경우 닫는 코드 추가
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // 액션바 - settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // 액션바 클릭됐을때 자동 호출
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // main.xml 의 <item android:id="@+id/action_settings"
        if (id == R.id.action_settings) {
            Toast.makeText(this, "설정 클릭", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // 바로가기 메뉴 안에서 메뉴 선택시 자동 호출
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_0) {
            onFragmentSelected(0, null);
        } else if (id == R.id.nav_1) {
            onFragmentSelected(1, null);
        } else if (id == R.id.nav_2) {
            onFragmentSelected(2, null);
        } else if (id == R.id.nav_user_setting) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
