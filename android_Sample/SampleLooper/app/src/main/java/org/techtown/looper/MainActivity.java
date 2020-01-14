package org.techtown.looper;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;

    Handler handler = new Handler();

    ProcessThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();

                // Message 객체는  Message.obtain 메서드를 이용해서 참조할수 있다.
                Message message = Message.obtain();
                message.obj = input;

                // 새로 만든 스레드 안에 있는 핸들러로 메시지 전송하기
                thread.processHandler.sendMessage(message);
            }
        });

        thread = new ProcessThread();
    }

    // 새로 정의하는 스레드
    class ProcessThread extends Thread {


        // 핸들러 생성
        ProcessHandler processHandler = new ProcessHandler();

        public void run() {

            // 스레드가 계속 대기상태로 동작하게됨
            Looper.prepare();
            Looper.loop();
        }

        class ProcessHandler extends Handler {

            // 새로 만든 스레드 안에서 전달받은 메시지 처리하기
            public void handleMessage(Message msg) {
                final String output = msg.obj + " from thread.";

                handler.post(new Runnable() {
                    public void run() {
                        textView.setText(output);
                    }
                });
            }
        }
    }

}
