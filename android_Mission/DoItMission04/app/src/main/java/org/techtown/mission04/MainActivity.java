package org.techtown.mission04;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    EditText inputMessage;
    TextView inputCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputMessage = findViewById(R.id.inputMessage);
        inputCount = findViewById(R.id.inputCount);


        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message = inputMessage.getText().toString();
                Toast.makeText(getApplicationContext(), "전송할 메시지\n\n" + message, Toast.LENGTH_LONG).show();
            }
        });

        Button closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        TextWatcher watcher = new TextWatcher() {

            // 텍스트가 변경되는 동시에 동작
            public void onTextChanged(CharSequence str, int start, int before, int count) {
                byte[] bytes = null;
                try {
                    bytes = str.toString().getBytes("KSC5601");
                    int strCount = bytes.length;
                    inputCount.setText(strCount + " / 80바이트");
                } catch(UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
            }

            // 텍스트가 변경되기 바로 이전에 동작
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            // EditText의 텍스트가 변경되면 호출된다.
            public void afterTextChanged(Editable strEditable) {
                String str = strEditable.toString();
                try {
                    // "KSC5601" - 한글 완성형 표준
                    byte[] strBytes = str.getBytes("KSC5601");
                    if(strBytes.length > 80) {
                        strEditable.delete(strEditable.length()-2, strEditable.length()-1);
                    }
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        // 에디트텍스트는 사용자가 입력한 문자열의 상태가 바뀌는 것을 에디트텍스트 밖으로 알리기 위해
        // TextWatcher 객체를 addTextChangedListener()메서드의 인자로 사용한다.
        // addTextChangeListener는 EditText에 추가적인 글자 변화가 있는지 항상 듣고 있는 리스너이다
        // TextWatcher는 인터페이스로써 3단계(글자변화 전, 중, 후)로 구성된 글자 변화의 시점의 메서드를 갖고있다.
        inputMessage.addTextChangedListener(watcher);
    }

}
