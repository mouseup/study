package org.techtown.graphics.custom.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

// view 를 상속하여 새로운 뷰 정의
public class CustomViewImage extends View {

    // 메모리에 만들어질 Bitmap 객체 선언
    Bitmap cacheBitmap;
    // 메모리에 만들어질 Bitmap 객체에 그리기 위한 Canvas 객체 선언
    Canvas cacheCanvas;
    Paint mPaint;

    // 생성자
    public CustomViewImage(Context context) {
        super(context);

        init(context);
    }

    public CustomViewImage(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
    }

    // 뷰가 화면에 보이기 전에 Bitmap 객체를 만들고 그 위에 그리기
    // 뷰의 크기가 결정이 되는 시점에 자동 호출
    // 뷰의 크기가 변결될때도 호출
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        createCacheBitmap(w, h);
        // 실제 그래픽이 그려지는 시점
        testDrawing();
    }

    // 메모리에 Bitmap 객체를 만들고 Canvas 객체 설정
    private void createCacheBitmap(int w, int h) {
        // createBitmap 은 onSizeChanged 메서드가 호출되었을때 초기화 된후 testDrawing 메서드에 의해 그려지게 된다
        cacheBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas();
        cacheCanvas.setBitmap(cacheBitmap);
    }

    // 빨간 사각형 그리기
    private void testDrawing() {
        cacheCanvas.drawColor(Color.WHITE);

        mPaint.setColor(Color.RED);
        cacheCanvas.drawRect(100, 100, 200, 200, mPaint);

        // 비트맵객체 만들기
        // drawable/waterdrop.png - 리소스의 이미지 파일을 읽어 들여 화면에 그리기
        Bitmap srcImg = BitmapFactory.decodeResource(getResources(), R.drawable.waterdrop);
        cacheCanvas.drawBitmap(srcImg, 30, 30, mPaint);

        // setScale - 매트릭스 객체를 이용해 좌우 대칭이 되는 비트맵 이미지를 만들어 그리기
        // (-1, 1) - 좌우 대칭, (1,-1) 상하 대칭
        Matrix horInverseMatrix = new Matrix();
        horInverseMatrix.setScale(-1, 1);
        Bitmap horInverseImg = Bitmap.createBitmap(srcImg, 0, 0,
                srcImg.getWidth(), srcImg.getHeight(), horInverseMatrix, false);
        cacheCanvas.drawBitmap(horInverseImg, 30, 130, mPaint);

        // 매트릭스 객체를 이용해 상하 대칭이 되는 비트맵 이미지를 만들어 그리기
        Matrix verInverseMatrix = new Matrix();
        verInverseMatrix.setScale(1, -1);
        Bitmap verInverseImg = Bitmap.createBitmap(srcImg, 0, 0,
                srcImg.getWidth(), srcImg.getHeight(), verInverseMatrix, false);
        cacheCanvas.drawBitmap(verInverseImg, 30, 230, mPaint);

        // BlurMaskFilter - 번짐효과
        mPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL));
        // createScaledBitmap - 이미지 확대
        Bitmap scaledImg = Bitmap.createScaledBitmap(srcImg,
                srcImg.getWidth()*3, srcImg.getHeight()*3, false);
        cacheCanvas.drawBitmap(scaledImg, 30, 300, mPaint);
    }

    // 메모리의 Bitmap을 이용해 화면에 그리기
    // 뷰가 새로 그려질 때 호출
    protected void onDraw(Canvas canvas) {
        if (cacheBitmap != null) {
            canvas.drawBitmap(cacheBitmap, 0, 0, null);
        }
    }

}
