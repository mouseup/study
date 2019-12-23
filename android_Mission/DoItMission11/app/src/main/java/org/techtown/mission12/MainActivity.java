package org.techtown.mission12;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;

    MyReceiver receiver;

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
                sendToService(input);
            }
        });

        receiver = new MyReceiver();

        // 브로드캐스트 수신자를 매니페스트 파일안에 태그로 추가하지 않고
        // 소스파일에서 registerReceiver 메서드를 사용해 등록
        // 이렇게 하면 화면이 사용자에게 보일 때만 브로드캐스트 수신자에서 메시지를 받도록 만든다
        // 어떤 인텐트를 받을 것인지 지정
        IntentFilter filter = new IntentFilter();
        filter.addAction("org.techtown.broadcast.SHOW");
        registerReceiver(receiver, filter);

    }

    class MyReceiver extends BroadcastReceiver {

        // onReceive -  원하는 브로드캐스트 메시지가 도착하면 자동으로 호출
        // 모든 메시지는 인텐트 안에 넣어 전달되므로 원하는 메시지는 인텐트 필터를 사용해 시스템에 등록
        @Override
        public void onReceive(Context context, Intent intent) {
            processIntent(intent);
        }
    }

    public void processIntent(Intent intent) {
        if (intent != null) {
            String command = intent.getStringExtra("command");
            if (command != null) {
                if (command.equals("show")) {
                    String data = intent.getStringExtra("data");
                    textView.setText("받은 결과 : " + data);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);
    }

    public void sendToService(String input) {

        // 인텐트 객체 만들고 부가 데이터 넣기
        Intent serviceIntent = new Intent(getApplicationContext(), MyService.class);
        serviceIntent.putExtra("command", "show");
        serviceIntent.putExtra("data", input);

        // 서비스를 실행시킨후 인텐트 객체를 서비스에 전달
        startService(serviceIntent);
    }

}
