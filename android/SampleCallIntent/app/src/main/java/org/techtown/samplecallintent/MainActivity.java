package org.techtown.samplecallintent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
            public void onClick(View view) {
                String data = editText.getText().toString();

//              인텐트의 기본 구성 요소 - action, data
//              action - 수행할 기능 /  data - 액션이 수행될 대상의 데이터 의미
//              ACTION_VIEW - 주어진 전화번호를 이용해 전화걸기 화면을 보여줌
//              uri 값의 유형에 따라 View 액션이 다른 기능을 수행함
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
                startActivity(intent);

            }
        });


        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

//              인텐트에 사용될 컴포넌트 클래스 이름을 명시적으로 지정
//              패키지 이름 - "org.techtown.samplecallintent"
//              클래스 이름 - "org.techtown.samplecallintent.MenuActivity"
                ComponentName name = new ComponentName("org.techtown.samplecallintent",
                        "org.techtown.samplecallintent.MenuActivity");

                intent.setComponent(name);

                startActivityForResult(intent, 101);
            }
        });


    }
}
