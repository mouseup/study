package org.techtown.sampleintent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

//  새 액티비티를 띄울 때 보낼 요청 코드 - 어떤 액티비티로부터 온 응답인지 구분
    public static final int REQUEST_CODE_MENU = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//              Intent - 액티비티를 띄울 목적, 액티비티 간에 데이터 전달
//              getApplicationContext() - 전역적인 어플리케이션 정보에 접근하거나 어플리케이션 연관된 시스템 기능을 수행
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);

//              새 액티비티로부터 응답을 받을 수 있다
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });

    }

//  새로 띄운 MenuActivity 로부터 받은 응답을 처리하는 메서드
//  int requestCode - 액티비티를 띄울 때 전달했던 요청 코드
//  int resultCode - 새액티비티로부터 전달된 응답 코드 - 새 액티비티에서 처리한 결과가 정상인지 아닌지 구분(RESULT_OK - 정상)
//  Intent data - 새액티비티로부터 전달 받은 인텐트 - 새 액티비티의 데이터 전달(putExtra() 사용)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_MENU) {
            Toast.makeText(getApplicationContext(),
                    "onActivityResult 메소드 호출됨. 요청 코드 : " + requestCode +
                            ", 결과 코드 : " + resultCode, Toast.LENGTH_LONG).show();
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra("name");
                Toast.makeText(getApplicationContext(), "응답으로 전달된 name : " + name,
                        Toast.LENGTH_LONG).show();
            }
        }

    }

}
