package org.techtown.thread;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    // api 의 기본 핸들러 객체 생성하기
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundThread thread = new BackgroundThread();
                thread.start();
            }
        });

    }

    class BackgroundThread extends Thread {
        int value = 0;

        public void run() {
            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(1000);
                } catch(Exception e) {}

                value += 1;
                Log.d("Thread", "value : " + value);

                // 핸들러의 post() 메서드 호출하며 Runnable 객체를 만든다
                // Runnable 객체는 스레드의 작업 결과물로 만들어지는 데이터를 처리해야 한다.
                // 따라서 결과물을 화면에 보여주어야 하는 부분이 있을 경우 new 연산자로
                // Runnable 인터페이스를 구현하는 새로운 객체를 만들어 사용하는 것이 일반적이다.
                handler.post(new Runnable() {

                    // ui 접근 가능
                    public void run() {
                        textView.setText("value 값 : " + value);
                    }
                });
            }
        }
    }

}
