package org.techtown.sampleservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editText = findViewById(R.id.editText);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();

                // 인텐트 객체 만들고 부가 데이터 넣기
                Intent intent = new Intent(getApplicationContext(), MyService.class);
                // command -  서비스 쪽으로 전달한 인텐트 객체의 데이타가 어떤 목적으로 사용되는지 구별
                intent.putExtra("command", "show");
                // 입력상자에서 가져온 문자열 전달
                intent.putExtra("name", name);

                // startService 메서드에 담은 인텐트 객체는 MyService 클래스의
                // onStartCommand 메서드로 전달
                startService(intent);
            }
        });

        //액티비티가 새로 만들어질 때 전달된 인텐트 처리하기
        Intent passedIntent = getIntent();
        processIntent(passedIntent);

    }

    // 액티비티가 이미 만들어져 있을때 전달된 인텐트 처리하기
    @Override
    protected void onNewIntent(Intent intent) {
        processIntent(intent);

        super.onNewIntent(intent);
    }

    private void processIntent(Intent intent) {
        if (intent != null) {
            String command = intent.getStringExtra("command");
            String name = intent.getStringExtra("name");
            Toast.makeText(this, "command : " + command + ", name : " + name,
                    Toast.LENGTH_LONG).show();
        }
    }

}
