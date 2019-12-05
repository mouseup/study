package org.techtown.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class CustomView extends View {

    private Paint paint;

    // 생성자
    public CustomView(Context context) {
        super(context);

        init(context);
    }

    // 생성자 - xml 레이아웃에 태그로 추가해서 인플레이션 할경우
    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    // 초기화
    private void init(Context context) {

        // Paint - 그래픽을 그리기 위한 속성을 담아둠
        paint = new Paint();
        paint.setColor(Color.RED);
    }

    // 뷰가 화면에 보여지기 전에 자동호출되어 Canvas에 그리도록
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 사각형 그리기
        canvas.drawRect(100, 100, 200, 200, paint);
    }

    // 뷰를 터치했을때 호출됨
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 손가락이 눌렸을때
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Toast.makeText(super.getContext(), "MotionEvent.ACTION_DOWN : " +
                    event.getX() + ", " + event.getY(), Toast.LENGTH_LONG).show();
        }

        return super.onTouchEvent(event);
    }
}
