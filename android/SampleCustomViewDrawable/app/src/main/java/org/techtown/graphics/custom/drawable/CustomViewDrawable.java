package org.techtown.graphics.custom.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class CustomViewDrawable extends View {

    private ShapeDrawable upperDrawable;
    private ShapeDrawable lowerDrawable;

    public CustomViewDrawable(Context context) {
        super(context);

        init(context);
    }

    public CustomViewDrawable(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        // 윈도우 매니저를 이용해 뷰의 폭과 높이 확인
        // 뷰가 채워지는 화면의 크기를 알아온다
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        // 리소스에 정의된 색상 값을 변수에 설정
        // /app/res/values/colors.xml
        Resources curRes = getResources();
        int blackColor = curRes.getColor(R.color.color01);
        int grayColor = curRes.getColor(R.color.color02);
        int darkGrayColor = curRes.getColor(R.color.color03);

        // Drawable 객체 생성
        upperDrawable = new ShapeDrawable();

        RectShape rectangle = new RectShape();
        rectangle.resize(width, height*2/3);
        upperDrawable.setShape(rectangle);

        upperDrawable.setBounds(0, 0, width, height*2/3);

        // LinearGradient 객체 생성
        // 뷰 영역의 위쪽 2/3 와 아래쪽 1/3을 따로 채워줌으로써 위쪽에서부터 아래쪽으로 색상이 조금씩 변하는 배경화면 만들기
        // (0, 0, 0, height*2/3, - 그라데이션 축 설정
        LinearGradient gradient = new LinearGradient(0, 0, 0, height*2/3,
                grayColor, blackColor, Shader.TileMode.CLAMP);

        Paint paint = upperDrawable.getPaint();

        // Paint 객체에 새로 생성한 LinearGradient 객체를 Shader로 설정
        paint.setShader(gradient);

        lowerDrawable = new ShapeDrawable();

        RectShape rectangle2 = new RectShape();
        rectangle2.resize(width, height*1/3);
        lowerDrawable.setShape(rectangle2);
        lowerDrawable.setBounds(0, height*2/3, width, height);

        LinearGradient gradient2 = new LinearGradient(0, 0, 0, height*1/3,
                blackColor, darkGrayColor, Shader.TileMode.CLAMP);

        Paint paint2 = lowerDrawable.getPaint();
        paint2.setShader(gradient2);

    }

    // 뷰가 화면에 보여지기 전에 자동호출되어 Canvas에 그리도록
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // onDraw 메서드 안에서 Drawble 객체 그리기
        upperDrawable.draw(canvas);
        lowerDrawable.draw(canvas);

        // Cap.BUTT 와 Join.MITER를 페인트 객체에 적용
        // Paint 객체 - 선 그릴 때 사용
        // 노란선
        Paint pathPaint = new Paint();
        pathPaint.setAntiAlias(true);
        pathPaint.setColor(Color.YELLOW);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(16.0F);
        pathPaint.setStrokeCap(Paint.Cap.BUTT);
        pathPaint.setStrokeJoin(Paint.Join.MITER);

        // Path 객체 생성
        Path path = new Path();
        // moveTo - 좌표값 추가
        // lineTo - 이전 좌표 값과 선으로 연결되는 좌표 값 추가
        // 5개의 점을 이용해 선을 그리는 경우에는 moveTo 메서드를 한번 호출하고 난후
        // lineTo  메서드를 네번 호출하여 Path 객체에 좌표 값을 추가
        path.moveTo(20, 20);
        path.lineTo(120, 20);
        path.lineTo(160, 90);
        path.lineTo(180, 80);
        path.lineTo(200, 120);

        // Path 객체 그리기
        canvas.drawPath(path, pathPaint);

        // Cap.ROUND 와 Join.ROUND 를 페인트 객체에 적용
        // 하얀선
        pathPaint.setColor(Color.WHITE);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);

        // offset을 주어 좌표를 이동한 뒤 Path 객체 그리기
        path.offset(30, 120);
        canvas.drawPath(path, pathPaint);

        // Cap.SQUARE와 Join.BEVEL 를 페인트 객체에 적용
        // 파란선
        pathPaint.setColor(Color.CYAN);
        pathPaint.setStrokeCap(Paint.Cap.SQUARE);
        pathPaint.setStrokeJoin(Paint.Join.BEVEL);

        // offset을 주어 좌표를 이동한 뒤 Path 객체 그리기
        path.offset(30, 120);
        canvas.drawPath(path, pathPaint);

    }

}
