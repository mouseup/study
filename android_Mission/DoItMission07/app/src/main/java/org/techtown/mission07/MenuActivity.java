package org.techtown.mission07;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class MenuActivity extends AppCompatActivity {
	public static final int RESPONSE_CODE_OK = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // process received intent
        // 자신을 호출한  LoginActivity한테서  인텐트값을  받는다
        Intent receivedIntent = getIntent();
        String username = receivedIntent.getStringExtra("username");
        String password = receivedIntent.getStringExtra("password");

        Toast.makeText(this, "username : " + username + ", password : " + password, Toast.LENGTH_LONG).show();


        Button menu01Button = (Button) findViewById(R.id.menu01Button);
        menu01Button.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Intent resultIntent = new Intent();
        		resultIntent.putExtra("menu", "고객관리 메뉴");
                resultIntent.putExtra("message", "result message is OK!");

                // 호출된 Activity에서 setResult() 메소드로 결과를 저장
                // 이 때 성공인 경우는 RESULT_OK로 실패라면 RESULT_CANCEL을 설정
                setResult(Activity.RESULT_OK, resultIntent);

                // Activity를 종료
                finish();
        	}
        });

        Button menu02Button = (Button) findViewById(R.id.menu02Button);
        menu02Button.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Intent resultIntent = new Intent();
        		resultIntent.putExtra("menu", "매출관리 메뉴");
                resultIntent.putExtra("message", "result message is OK!");

                setResult(Activity.RESULT_OK, resultIntent);
                finish();
        	}
        });

        Button menu03Button = (Button) findViewById(R.id.menu03Button);
        menu03Button.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Intent resultIntent = new Intent();
        		resultIntent.putExtra("menu", "상품관리 메뉴");
                resultIntent.putExtra("message", "result message is OK!");

                setResult(Activity.RESULT_OK, resultIntent);
                finish();
        	}
        });

    }

}
