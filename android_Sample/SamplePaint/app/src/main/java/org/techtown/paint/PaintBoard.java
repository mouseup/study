package org.techtown.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PaintBoard extends View {

    Canvas mCanvas;
    Bitmap mBitmap;
    Paint mPaint;

    // 이전점 위치
    int lastX;
    int lastY;

    public PaintBoard(Context context) {
        super(context);

        init(context);
    }

    public PaintBoard(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        // 선그리기 객체 생성
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.BLACK);

        this.lastX = -1;
        this.lastY = -1;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // 메모리에 비트맵 객체 만들기
        Bitmap img = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(img);
        // 하얀색 배경
        canvas.drawColor(Color.WHITE);

        mBitmap = img;
        mCanvas = canvas;

    }

    protected void onDraw(Canvas canvas) {
        if (mBitmap != null) {
            // 메모리에 만들어진 이미지 그려주기
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {

        // 액션 정보(손가락 눌렀을때, 누른 상태로 움직일때, 떼었을 때)
        int action = event.getAction();

        // 현재 좌표값
        int X = (int) event.getX();
        int Y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_UP:
                lastX = -1;
                lastY = -1;

                break;

            case MotionEvent.ACTION_DOWN:
                if (lastX != -1) {
                    if (X != lastX || Y != lastY) {
                        // 선그리기
                        mCanvas.drawLine(lastX, lastY, X, Y, mPaint);
                    }
                }
                // 이전좌표를 현재좌표로 설정
                lastX = X;
                lastY = Y;

                break;

            // ACTION_MOVE 상태에서 이전의 좌표 값과 현재의 좌표 값을 연결하여 선을 그림
            case MotionEvent.ACTION_MOVE:
                if (lastX != -1) {
                    mCanvas.drawLine(lastX, lastY, X, Y, mPaint);
                }

                lastX = X;
                lastY = Y;

                break;
        }

        // 화면을 다시 그리도록 하면 이동 시에도 지속적으로 화면 갱신 -onDraw 호출
        invalidate();

        return true;
    }

}
