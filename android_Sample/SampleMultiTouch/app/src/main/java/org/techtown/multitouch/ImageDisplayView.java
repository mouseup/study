package org.techtown.multitouch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


// 뷰를 상속하면서 OnTouchListener 인터페이스를 구현하는 클래스 정의
// 터치했을 때의 이벤트를 이용해 이미지를 확대/축소하거나 이동하기 위해 일반 view 클래스 상속
// 터치 이벤트를 처리하므로 OnTouchListener 인터페이스 구현
public class ImageDisplayView extends View implements View.OnTouchListener {
	private static final String TAG = "ImageDisplayView";
	
	Context mContext;
	Canvas mCanvas;
	Bitmap mBitmap;
	Paint mPaint;

	int lastX;
	int lastY;

	Bitmap sourceBitmap;
	
	Matrix mMatrix;
	
	float sourceWidth = 0.0F;
	float sourceHeight = 0.0F;
	 
	float bitmapCenterX;
	float bitmapCenterY;
	
	float scaleRatio;
	float totalScaleRatio;
	 
	float displayWidth = 0.0F;
	float displayHeight = 0.0F;
	
	int displayCenterX = 0;
	int displayCenterY = 0;

	public float startX;
    public float startY;
    
    public static float MAX_SCALE_RATIO = 5.0F;
    public static float MIN_SCALE_RATIO = 0.1F;
    
	float oldDistance = 0.0F;
	 
	int oldPointerCount = 0;
	boolean isScrolling = false;
	float distanceThreshold = 3.0F;

	// 생성자
	public ImageDisplayView(Context context) {
		super(context);
		
		mContext = context;

		init();
	}

	public ImageDisplayView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mContext = context;

		init();
	}

	// 초기화
	private void init() {
		// 그리기
		mPaint = new Paint();
		// 확대, 축소(행열연산)
        mMatrix = new Matrix();
        
		lastX = -1;
		lastY = -1;

		// 리스너 설정
		setOnTouchListener(this);
	}

	// 뷰가 초기화되고 나서 화면에 보이기 전 크기가 정해지면서 호출되는 메서드 안에서 메모리상에 새로운 비트맵 객체 생성
	// 메모리에 만들어지는 비트맵 이미지 초기화하는 부분은 init()메서드가 아닌
	// onSizeChanged 메서드에 들어있는데, 그 이유는 뷰가 화면에 보이기 전에
	// onSizeChanged 메서드가 호출되므로 이 메서드 안에서 비트맵 이미지를 만드는 것이 효율적이기 때문이다.
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (w > 0 && h > 0) {
			// 메모리에 비트맵 만들기
			newImage(w, h);

	        redraw();
		}
	}

	public void newImage(int width, int height) {
		Bitmap img = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas();
		canvas.setBitmap(img);
		
		mBitmap = img;
		mCanvas = canvas;

		displayWidth = (float)width;
		displayHeight = (float)height;
		 
		displayCenterX = width/2;
    	displayCenterY = height/2;
	}	
 
	public void drawBackground(Canvas canvas) {
		if (canvas != null) {
			canvas.drawColor(Color.BLACK);
		}
	}	

	// 뷰가 화면에 그려지는 메서드 안에서 메모리상의 비트맵 객체 그리기
	// 메모리에 만들어져 있는 비트맵 이미지를 화면에 그려주기
	protected void onDraw(Canvas canvas) {
		if (mBitmap != null) {
			canvas.drawBitmap(mBitmap, 0, 0, null);
		}
	}

    public void setImageData(Bitmap image) {
    	recycle();
    	
    	sourceBitmap = image;
    	
    	sourceWidth = sourceBitmap.getWidth();
    	sourceHeight = sourceBitmap.getHeight();
    	 
    	bitmapCenterX = sourceBitmap.getWidth()/2;
    	bitmapCenterY = sourceBitmap.getHeight()/2;
        
        scaleRatio = 1.0F;
        totalScaleRatio = 1.0F;
    }
    
    public void recycle() {
    	if (sourceBitmap != null) {
    		sourceBitmap.recycle();
    	}
    }
    
	public void redraw() {
		if (sourceBitmap == null) {
			Log.d(TAG, "sourceBitmap is null in redraw().");
			return;
		}
		
		drawBackground(mCanvas);
		
		float originX = (displayWidth - (float)sourceBitmap.getWidth()) / 2.0F;
		float originY = (displayHeight - (float)sourceBitmap.getHeight()) / 2.0F;
		
		mCanvas.translate(originX, originY);
        mCanvas.drawBitmap(sourceBitmap, mMatrix, mPaint);
        mCanvas.translate(-originX, -originY);

		invalidate();
	}
	

	// 뷰를 터치할 때 호출되는 메서드 다시 정의
	// 터치할때의 화면 좌표 값을 받아 이미지의 크기를 바꿔야 한다
	public boolean onTouch(View v, MotionEvent ev) {
        final int action = ev.getAction();

        // 터치했을때 몇 개의 손가락으로 터치하는지 개수 확인
        int pointerCount = ev.getPointerCount();
        Log.d(TAG, "Pointer Count : " + pointerCount);

		// 한 손가락을 사용할 때는 이미지 이동 if (pointerCount == 1) {
		// 두 손가락을 사용할 때는 이미지 확대 축소 } else if (pointerCount == 2) {

        switch (action) {

        	// 손가락으로 눌렀을 때의 기능 추가
        	case MotionEvent.ACTION_DOWN:

        		if (pointerCount == 1) {
    	    		float curX = ev.getX();
    	    		float curY = ev.getY();
    	    		
        			startX = curX;
                    startY = curY;

        		} else if (pointerCount == 2) {
        			oldDistance = 0.0F;
        			
        			isScrolling = true;
        		}
        		
        		return true;

        	// 손가락으로 움질일 때의 기능 추가
        	case MotionEvent.ACTION_MOVE:

    	    	if (pointerCount == 1) {

    	    		if (isScrolling) {
    	    			return true;
    	    		}
    	    		
    	    		float curX = ev.getX();
    	    		float curY = ev.getY();
    	    		
    	    		if (startX == 0.0F) {
    	    			startX = curX;
    	                startY = curY;
    	    			
    	    			return true;
    	    		}
    	    		
                    float offsetX = startX - curX;
                    float offsetY = startY - curY;

                	if (oldPointerCount == 2) {
                		
                	} else {
                		Log.d(TAG, "ACTION_MOVE : " + offsetX + ", " + offsetY);

                		if (totalScaleRatio > 1.0F) {
                			// 한 손가락으로 움직이고 있을 때는 moveImage 메서드 호출
                			moveImage(-offsetX, -offsetY);
                		}
                		
    	                startX = curX;
    	                startY = curY;
                	}

                // 두 손가락이 움직이고 있는 상태
				// 이전에 움직였을 때의 두 손가락의 좌표 값과 현재 움직이고 있는 두 손가락의 좌표 값을 확인한후
				// 그 값들의 차이가 더 커지는지 또는 더 작아지는지를 계산
    	    	} else if (pointerCount == 2) {
    	    		
    	    		float x1 = ev.getX(0);
    	    		float y1 = ev.getY(0);
    	    		float x2 = ev.getX(1);
    	    		float y2 = ev.getY(1);
    	    		
    	    		float dx = x1 - x2;
    	    		float dy = y1 - y2;
    	    		float distance = new Double(Math.sqrt(new Float(dx * dx + dy * dy).doubleValue())).floatValue();
    	    		
    	    		float outScaleRatio = 0.0F;
    	    		if (oldDistance == 0.0F) {
    	    			oldDistance = distance;
    	    			
    	    			break;
    	    		}

					// 값들의 차이가 커진다면 두 손가락의 간격이 벌어지는 것을 의미하므로 그 비율만큼 이미지 확대
    	    		if (distance > oldDistance) {
    	    			if ((distance-oldDistance) < distanceThreshold) {
    	    				return true;
    	    			}

    	    			outScaleRatio = scaleRatio + (oldDistance / distance * 0.05F);

    	    		// 값들의 차이가 작아진다면 두 손가락의 간격이 좁아지는 것을 의미하므로 그 비율만큼 이미지 축소
    	    		} else if (distance < oldDistance) { 
    	    			if ((oldDistance-distance) < distanceThreshold) {
    	    				return true;
    	    			}

    	    			outScaleRatio = scaleRatio - (distance / oldDistance * 0.05F);
    	    		}

    	    		if (outScaleRatio < MIN_SCALE_RATIO || outScaleRatio > MAX_SCALE_RATIO) {
                        Log.d(TAG, "Invalid scaleRatio : " + outScaleRatio);
                    } else {
                        Log.d(TAG, "Distance : " + distance + ", ScaleRatio : " + outScaleRatio);
                        // 두 손가락으로 움직이고 있을 때는 scaleImage 호출
                        scaleImage(outScaleRatio);
                    }
    	    		
    	    		oldDistance = distance;
    	    	}
        		
    	    	oldPointerCount = pointerCount;
    	    	
        		break;

        	// 손가락을 떼었을 때의 기능 추가
        	case MotionEvent.ACTION_UP:

        		if (pointerCount == 1) {

    	    		float curX = ev.getX();
    	    		float curY = ev.getY();
    	    		
                    float offsetX = startX - curX;
                    float offsetY = startY - curY;
                    
                    if (oldPointerCount == 2) {
                    		 
                	} else {
                		moveImage(-offsetX, -offsetY);
                	}
	    	 
        		} else {
        			isScrolling = false;
        		}
        		
            	return true;
        }
        
        return true;
	 }

	 // 매트릭스 객체를 사용해 이미지 크기 변경
     private void scaleImage(float inScaleRatio) {
    	Log.d(TAG, "scaleImage() called : " + inScaleRatio);
    	// postScale - 비트맵 이미지를 확대 또는 축소
		// 첫번째 inScaleRatio - x축을 기준으로 확대하는 비율
		// 두번째 inScaleRatio - y축을 기준으로 확대하는 비율
		// bitmapCenterX, bitmapCenterY - 확대 또는 축소할 때 기준이 되는 위치(비트맵 이미지의 중심점)
    	mMatrix.postScale(inScaleRatio, inScaleRatio, bitmapCenterX, bitmapCenterY);
    	// 비트맵 이미지 회전(회전각도)
    	mMatrix.postRotate(0);

    	totalScaleRatio = totalScaleRatio * inScaleRatio;

    	// 화면에 다시 그리기
    	redraw();
     }

     // 매트릭스 객체를 사용해 이미지 이동
     private void moveImage(float offsetX, float offsetY) {
    	Log.d(TAG, "moveImage() called : " + offsetX + ", " + offsetY);

    	// postTranslate - 비트맵 이미지 이동
		// offsetX, offsetY - 이동할 만큼의 x와 y 좌표 값
    	mMatrix.postTranslate(offsetX, offsetY);

    	// 화면에 다시 그리기
    	redraw();
     }

}