package org.techtown.samplerecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // 리싸이클러뷰에 레이아웃 매니저 설정
        // 레이아웃 메니저는 리싸이클러뷰가 보일 기본적인 형태를 설정할 때 사용한다.
        LinearLayoutManager layoutManager = new LinearLayoutManager
                (this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);



        PersonAdapter adapter = new PersonAdapter();

        adapter.addItem(new Person("김민수", "010-1000-1000"));
        adapter.addItem(new Person("김하늘", "010-2000-2000"));
        adapter.addItem(new Person("홍길동", "010-3000-3000"));

        // 리싸이클러뷰에 어댑터 설정
        recyclerView.setAdapter(adapter);

    }
}
