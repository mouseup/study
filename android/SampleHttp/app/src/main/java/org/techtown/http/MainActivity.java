package org.techtown.http;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;

    Handler handler = new Handler();

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
                final String urlStr = editText.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        request(urlStr);
                    }
                }).start();
            }
        });
    }

    public void request(String urlStr) {
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            // HttpURLConnection 객체 만들기
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                // 10동안 기다리기
                conn.setConnectTimeout(10000);
                // GET 방식으로 요청
                conn.setRequestMethod("GET");
                // 서버에서 받기 가능
                conn.setDoInput(true);

                // 연결
                int resCode = conn.getResponseCode();

                // 입력 데이터를 받기 위한 Reader 객체 생성
                // conn.getInputStream() - 들어오는 데이터 통로 만듬
                // BufferedReader - 한줄씩 읽어들임
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = null;
                while (true) {
                    line = reader.readLine();
                    // 다 읽었으면
                    if (line == null) {
                        break;
                    }

                    output.append(line + "\n");
                }
                reader.close();
                conn.disconnect();
            }
        } catch (Exception ex) {
            println("예외 발생함 : " + ex.toString());
        }

        println("응답 -> " + output.toString());
    }

    public void println(final String data) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                textView.append(data + "\n");
            }
        });

    }

}
