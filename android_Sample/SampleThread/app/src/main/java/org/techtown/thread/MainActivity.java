package org.techtown.thread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    int value = 0;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 스레드 생성
                BackgroundThread thread = new BackgroundThread();
                // 스레드 시작
                thread.start();
            }
        });

    }

    // 내부 스레드 정의
    class BackgroundThread extends Thread {
        // 스레드 start 시 run 메소드 자동 실행
        public void run() {
            for (int i = 0; i < 100; i++) {

                // 1초동안 쉼
                try {
                    Thread.sleep(1000);
                } catch(Exception e) {}

                // 1초마다 1씩 증가
                value += 1;
                // Logcat 창에 변수 값 출력
                Log.d("Thread", "value : " + value);


                // 스레드 안에서 텍스트뷰의 setText 메서드 호출
                // 에러 - Only the original thread that created a view hierarchy can touch its views.
                // 뷰 계층 구조를 만든 원래 스레드 만 해당 뷰를 만질 수 있습니다
                // 메인 스레드에서 관리하는 ui 객체는 직접 만든 스레드 객체에서는 접근 할수 없다.
                // 핸들러를 사용해 해결해야 함
                // textView.setText("value 값 : " + value);
            }
        }
    }

}
