package org.techtown.mission26;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;


/**
 * Camera Preview
 *
 * @author Mike
 */

// SurfaceView 클래스를 상속하고 콜백 인터페이스를 구현하는 새로운 Camerasurface view 클래스 정의
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera camera = null;

    public static final String TAG = "CameraSurfaceView";

    int surfaceWidth;
    int surfaceHeight;

    int bitmapWidth = 0;
    int bitmapHeight = 0;


    public CameraSurfaceView(Context context) {
        super(context);

        init();
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    // 생성자에서 서피스홀더 객체 참조 후 설정
    private void init() {

        // 서피스홀더 객체 참조
        mHolder = getHolder();

        // 콜백객체 지정
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    // 서피스뷰의 상태가 변경될 때 자동 호출되는 세가지 메서드
    /**
     * surfaceCreated defined in Callback
     */

   // 서피스뷰가 만들어질 때 카메라 객체를 참조한 후 미리보기 화면으로 홀더 객체 설정
   public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();

        try {

            // 카메라 객체에 서피스홀더 객체 지정
            camera.setPreviewDisplay(mHolder);

        } catch (Exception e) {
            Log.e(TAG, "Camera Preview setting failed.", e);
        }
    }

    /**
     * surfaceChanged defined in Callback
     */

    // 서피스뷰의 화면 크기가 바뀌는 등의 변경 시점에 미리보기 시작
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    	surfaceWidth = width;
    	surfaceHeight = height;

    	// rotate preview display for several devices
    	try {
    		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
    			camera.setDisplayOrientation(90);
    		} else {
    			Parameters parameters = camera.getParameters();
    			parameters.setRotation(90);
    			camera.setParameters(parameters);
    		}

    		camera.setPreviewDisplay(holder);
    	} catch (IOException exception) {
    		camera.release();
    	}

        // 미리보기 시작
    	camera.startPreview();
    }

    /**
     * surfaceDestroyed defined in Callback
     */

    // 서피스뷰가 없어질 때 미리보기 중지
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.setPreviewCallback(null);
        camera.release();

        camera = null;
    }

}

