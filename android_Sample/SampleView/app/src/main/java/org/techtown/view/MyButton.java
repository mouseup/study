package org.techtown.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

// AppCompatButton에는 버튼을 위한 기능이 미리 정의되어 있다.
public class MyButton extends AppCompatButton {

    // 안드로이는 ui 객체를 만들 때 Context 객체를 전달 받도록 되어 있으므로
    // 생성자에는 항상 Context 객체가 전달되어야 한다.
    // 이 뷰를 소스 코드에서 new 연산자로 생성하는 경우에 사용된다.
    public MyButton(Context context) {
        super(context);

        init(context);
    }

    // AttributeSet 객체는 xml 레이아웃에서 태그에 추가되는 속성을 전달받기 위한 것으로
    // 이 뷰를 xml 레이아웃에 추가하는 경우 이 두 번째 생성자가 사용된다.
    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    // 초기화 메서드
    private void init(Context context) {
        setBackgroundColor(Color.CYAN);
        setTextColor(Color.BLACK);

        // setTextSize() 메서드는 픽셀 단위만 설정
        // 글자 크기는 화면 크기별로 다르게 표현되는 sp 단위를 사용하는 것을 권장
        // getResources() 메서드를 사용하여 dimens.xml 파일에 정의되 있는 크기 값이 픽셀 값으로 자동 변환된다.
        float textSize = getResources().getDimension(R.dimen.text_size);
        setTextSize(textSize);
    }

    // 뷰가 그려질 때 호출되는 함수에 기능 추가하기
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.d("MyButton", "onDraw 호출됨");
    }

    // 뷰가 터치될 때 호출되는 함수에 기능 추가하기
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("MyButton", "onTouchEvent 호출됨");

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundColor(Color.BLUE);
                setTextColor(Color.RED);

                break;
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                setBackgroundColor(Color.CYAN);
                setTextColor(Color.BLACK);

                break;
        }

        // invalidate() 메서드가 호출되면 자동으로 onDraw() 메서드가 다시 호출되어
        // 이동한 좌표의 뷰의 그래픽을 다시 그리도록 만든다.
        invalidate();
        Log.d("MyButton", "invalidate()");
        return true;
    }

}
