package org.techtown.mission07;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MENU = 1001;

    EditText usernameInput;
    EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();

                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("password", password);

                // Activity의 결과를 받으려면 호출할 때 startActivity() 대신 startActivityForResult() 메소드를 사용
                // 새로 호출된 Activity에서는 setResult()를 통해 돌려줄 결과를 저장하고 finish()로 Activity를 종료
                // 이후 그 결과는 호출했던 Activity의 onActivityResult() 메소드를 통해 전달
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });

        usernameInput = (EditText) findViewById(R.id.usernameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);

    }

    // LoginActivity에서 MenuActivity로 갔다가 다시 LoginActivity로 넘어올 때 사용하는, 안드로이드에서 제공하는 기본 메소드이다.
    // requestCode는 처음 startActivityForResult()의 두번째 인수 값이며,
    // resultCode는 호출된 Activity에서 설정한 성공(RESULT_OK)/실패(RESULT_CANCEL) 값이다.
    // 이를 통해 어떤 호출 (REQUST_TEST) 이었는지와 결과가 어떠한지 (RESULT_OK)를 알 수 있으므로
    // 그에 맞는 동작을 진행할 수 있다.
    // 마지막으로 세번째 인수 Intent는 호출된 Activity에서 저장한 결과이다.
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE_MENU) {
            if (intent != null) {
                String menu = intent.getStringExtra("menu");
                String message = intent.getStringExtra("message");

                Toast toast = Toast.makeText(getBaseContext(), "result code : " + resultCode + ", menu : " + menu + ", message : " + message, Toast.LENGTH_LONG);
                toast.show();
            }
        }

    }

}
