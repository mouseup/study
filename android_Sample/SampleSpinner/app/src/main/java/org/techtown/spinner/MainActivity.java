package org.techtown.spinner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    String[] items = {"mike", "angel", "crow", "john", "ginnie", "sally", "cohen", "rice"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        Spinner spinner = findViewById(R.id.spinner);

        // simple_spinner_item - API에서 제공하는 레이아웃 / 문자열을 아이템으로 보여주는 단순 스피너 아이템 레이아웃
        // items - 아이템으로 보일 문자열 데이터들의 배열
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);

        // setDropDownViewResource - 스피너는 항목을 선택하기 위한 창이 따로 있기 때문에 항목을 선택하는 창을 위한 레이아웃을 설정한다.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 스피너에 어댑터 설정
        spinner.setAdapter(adapter);

        // 스피너에 리스너 설정
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            // AdapterView 는 ListView , GridView , Spinner 등이 될 수 있습니다.
            // 꺾쇠 괄호 안에있는 물음표는 그 중 하나 일 수 있음을 나타냅니다. 이것은 Java에서 generics 이라고합니다.
            // Event 발생한 AdapterView (제네릭이 ?인 이유는 뭐가 넘어올지 모르기 때문)
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                textView.setText(items[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                textView.setText("");
            }
        });
    }
}
