package org.techtown.samplelayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Layout1 layout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // xml 레이아웃에 추가한 뷰 참조하기
        layout1 = findViewById(R.id.layout1);

        // 뷰의 메서드 호출하여 데이터 설정
        layout1.setImage(R.drawable.profile1);
        layout1.setName("김민수");
        layout1.setMobile("010-1000-1000");

        // 버튼을 눌렀을때 다른 이미지 보이기
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout1.setImage(R.drawable.profile1);
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout1.setImage(R.drawable.profile2);
            }
        });

    }
}
