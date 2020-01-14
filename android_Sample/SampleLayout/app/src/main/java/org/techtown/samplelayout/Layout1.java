package org.techtown.samplelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

// LinearLayout 클래스 상속
public class Layout1 extends LinearLayout {
    ImageView imageView;
    TextView textView;
    TextView textView2;

    public Layout1(Context context) {
        super(context);

        init(context);
    }

    public Layout1(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {

        // layout1.xml 매칭
        // LayoutInflater 객체는 시스템 서비스로 제공되므로 getSystemService() 메서드를 호출하면서
        // 파라미터로 Context.LAYOUT_INFLATER_SERVICE 상수를 전달하면 객체가 반환된다
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflater 의 inflate() 메서드를 호출하면서 xml 레이아웃 파일을 파라미터로 전달하면
        // 인플레이션이 진행되면서 이 소스 파일에 설정된다
        inflater.inflate(R.layout.layout1, this, true);

        // 인플레이션 과정이 끝나면 xml 레이아웃 파일 안에 넣어둔 이미지뷰나 텍스트뷰를 찾아서 참조할 수 있다.
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
    }

    // 새로 정의한 Layout1 이라는 이름의 뷰는 메인 레이아웃에서 추가되어 사용할 것이므로
    // 소스 코드에서 이미지뷰의 이미지나 텍스트뷰의 글자를 바꿀 수 있도록
    // setImage, setName, setMobile 이라는 이름의 메서드를 정의
    public void setImage(int resId) {
        imageView.setImageResource(resId);
    }
    public void setName(String name) {
        textView.setText(name);
    }
    public void setMobile(String mobile) {
        textView2.setText(mobile);
    }

}
