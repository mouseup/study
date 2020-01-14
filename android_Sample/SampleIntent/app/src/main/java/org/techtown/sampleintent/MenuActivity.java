package org.techtown.sampleintent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();

//              키 + 값
                intent.putExtra("name", "mike");

//              새로 띄운 액티비티에서 이전 액티비티로 인텐트를 전달
                setResult(RESULT_OK, intent);

//              액티비티를 화면에서 없앰
                finish();
            }
        });


    }
}
