package org.techtown.samplefragment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    MainFragment mainFragment;
    MenuFragment menuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 프래그먼트는 뷰가 아니라서 activity 클래스에 있는 findViewById() 메서드로 찾을수 없다.
        // 대신 프래그먼트를 관리하는 FragmentManager 객체의 findFragmentById() 메서드를 사용해서 찾는다
        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.mainFragment);
        menuFragment = new MenuFragment();

    }

    // 0 이면 메인 프래그먼트, 1이면 메뉴 프래그먼트
    // 프래그먼트 메니저는 프래그먼트를 다루는 작업을 해 주는 객체로
    // 프래그먼트 추가, 삭제 또는 교체 등의 작업을 할 수 있게 한다.
    // 그런데 이런 작업들은 프래그먼트를 변경할 때 오류가 생기면 다시 원래 상태로 돌릴수 있어야 하므로
    // 트랜잭션 객체를 만들어 실행한다.
    public void onFragmentChanged(int index) {
        if (index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, menuFragment).commit();
        } else if (index == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
        }
    }


}
