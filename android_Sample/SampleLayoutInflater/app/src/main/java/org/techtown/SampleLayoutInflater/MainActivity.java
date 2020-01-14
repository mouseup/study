package org.techtown.SampleLayoutInflater;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "버튼이 눌렸어요.",
                        Toast.LENGTH_LONG).show();
            }
        });

//        setContentView(R.layout.activity_main);
//        setContentView - 화면에 표시할 xml 레이아웃을 지정하거나 레이아웃 내용을 메모리에 객체화
//        setContentView로 레이아웃 객체화전에 레이아웃 버튼 참조시 NullPointerException 발생

    }
}
