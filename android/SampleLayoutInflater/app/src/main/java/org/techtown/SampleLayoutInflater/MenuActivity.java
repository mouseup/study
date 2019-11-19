package org.techtown.SampleLayoutInflater;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class MenuActivity extends AppCompatActivity {

    // activity_menu.xml 의 안쪽 리니어 레이아웃
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    container = findViewById(R.id.container);

    Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            // getSystemService() 메서드를 사용해 LAYOUT_INFLATER 객체 참조
            //LayoutInflater 객체는 시스템 서비스로 제공되므로 getSystemService()메서드를 호출하거나
            //LayoutInflater 클래스의 from() 메서드를 호출하여 참조할 수도 있다.
            //static LayoutInflater LayoutInflater.from(Context context)
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //inflate() 메서드의 파라미터로 R.layout.sub1(xml 레이아웃 리소스), container(부모 컨테이너) 객체 전달
            // - container를 id 로 갖는 리니어 레이아웃 객체에 sub1.xml 파일의 레이아웃을 설정
            // sub1. xml에 정의된 뷰들이 메모리에 로딩되며 객체화 과정
            inflater.inflate(R.layout.sub1, container, true);

            //sub1.xml 이 객체화 되었으므로 부분 레이아웃에 들어있던 텍스트뷰와 체크박스를
            //findViewById() 메서드로 참조할수 있음
            CheckBox checkBox = container.findViewById(R.id.checkBox);
            checkBox.setText("로딩되었어요.");
        }
    });
}


}
