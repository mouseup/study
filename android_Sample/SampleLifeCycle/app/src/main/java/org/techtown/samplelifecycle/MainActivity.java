package org.techtown.samplelifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText nameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toast.makeText(this, "onCreate 호출됨", Toast.LENGTH_LONG).show();
        println("onCreate 호출됨");

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
            }
        });

    }

        @Override
    protected void onStart() {

        super.onStart();
//        Toast.makeText(this, "onStart 호출됨", Toast.LENGTH_LONG).show();
        println("onStart 호출됨");
    }

    @Override
    protected void onStop() {

        super.onStop();
//        Toast.makeText(this, "onStop 호출됨", Toast.LENGTH_LONG).show();
        println("onStop 호출됨");
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
//        Toast.makeText(this, "onDestroy 호출됨", Toast.LENGTH_LONG).show();
        println("onDestroy 호출됨");
    }

    @Override
    protected void onPause() {

        super.onPause();
//        Toast.makeText(this, "onPause 호출됨", Toast.LENGTH_LONG).show();
        println("onPause 호출됨");
        saveState();

    }

    @Override
    protected void onResume() {

        super.onResume();
//        Toast.makeText(this, "onResume 호출됨", Toast.LENGTH_LONG).show();
        println("onResume 호출됨");
//        restoreState();

    }

//  logcat 창에서 Main 태크로 검색
    public void println(String data) {
        Log.d("Main", data);
    }

//  설정 정보에 저장된 데이터를 가져와서
    protected void restoreState() {
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if ((pref != null) && (pref.contains("name"))) {
            String name = pref.getString("name", "");
            Log.d("Main", name);
            nameInput.setText(name);
        }
    }

//  현재 입력상자에 입력된 데이터를 저장
//  데이터를 저장할 때는 SharedPreferences 사용 - 데이터를 저장할 수 있도록 edit() 메서드 제공
//  SharedPreferences 객체를 사용하려면 getSharedPreferences() 메서드 참조
//  edit() 메서드를 호출한후 putString()메서드로 저장하려는 데이터를 설정
//  데이터를 저장한 후에는 commit()를 호출해야 실제로 저장
    protected void saveState() {

        println("saveState 호출됨");

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("name", nameInput.getText().toString());
        editor.commit();
        println(nameInput.getText().toString());

    }


}
