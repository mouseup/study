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
    MainHandler handler;

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

        // 정의한 MainHandler 핸들러는 onCreate() 메서드에서 액티비티가 초기화될 때 new 연산자를 이용해 객체로 만들어진다.
        handler = new MainHandler();
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

                // message 객체 만들기
                // obtainMessage - 메시지 객체 하나 참조
                Message message = handler.obtainMessage();

                // bundle 객체 만들기
                Bundle bundle = new Bundle();
                bundle.putInt("value", value);

                message.setData(bundle);

                // 핸들러로 메시지 객체 보내기
                // handleMessage 자동 호출
                handler.sendMessage(message);
            }
        }
    }


    class MainHandler extends Handler {

        // 핸들러 안에서 전달받은 메시지 객체 처리하기
        // UI 직접 접근 가능
        // 정의한 핸들러는 onCreate() 메서드에서 액티비티가 초기화될 때 new 연산자를 이용해 객체로 만들어진다.
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // 번들 객체 참조
            Bundle bundle = msg.getData();
            // 스레드 내부에서 만든 value 값을 전달받게 됨
            int value = bundle.getInt("value");
            textView.setText("value 값 : " + value);
        }
    }

}
