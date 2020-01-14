package org.techtown.capture;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.jetbrains.annotations.NotNull;

// MainActivity 클래스가 AutoPermissionsListener 인터페이스를 구현하도록
public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
    CameraSurfaceView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout previewFrame = findViewById(R.id.previewFrame);
        cameraView = new CameraSurfaceView(this);
        previewFrame.addView(cameraView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                takePicture();
            }
        });

        AutoPermissions.Companion.loadAllPermissions(this, 101);
    }

    public void takePicture() {

        // CameraSurfaceView 의 capture 메서드 호출
        // PictureCallback 인터페이스 구현 - CameraSurfaceView 의 capture 메서드 호출할때 전달
        cameraView.capture(new Camera.PictureCallback() {

            // 사진이 찍어지면 byte 배열 전달
            // 사진을 찍을 때 자동으로 호출
            public void onPictureTaken(byte[] data, Camera camera) {
                try {

                    // 전달받은 바이트 배열을 비트맵 객체로 만들기
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                    // 이미지를 미디어 앨범에 추가
                    // getContentResolver(), - ContentResolver객체
                    //        bitmap, - 메모리에 만들어진 비트맵 객체
                    //        "Captured Image", - 제목
                    //        "Captured Image using Camera."); - 내용

                    String outUriStr = MediaStore.Images.Media.insertImage(
                            getContentResolver(),
                            bitmap,
                            "Captured Image",
                            "Captured Image using Camera.");

                    if (outUriStr == null) {
                        Log.d("SampleCapture", "Image insert failed.");
                        return;
                    } else {
                        Uri outUri = Uri.parse(outUriStr);
                        sendBroadcast(new Intent(
                                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, outUri));
                    }

                    // 다시 미리보기 시작
                    camera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    // SurfaceView 클래스 상속
    // SurfaceHolder에 정의된 Callback 인터페이스 구현
    private class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

        private SurfaceHolder mHolder;
        private Camera camera = null;

        // 생성자
        public CameraSurfaceView(Context context) {
            super(context);

            // 생성자에서 서피스홀더 객체 참조 후 설정
            mHolder = getHolder();
            // 이 클래스에서 구현된 Callback 객체 지정
            mHolder.addCallback(this);
        }


        // 서피스 뷰의 상태가 변경될 때 자동 호출되는 세가지 메서드 구현
        // Callback - 서피스뷰가 메모리에 만들어지는 시점에 호출
        // 서피스뷰가 만들어질 때 카메라 객체를 참조한 후 미리보기 화면으로 홀더 객체 설정
        public void surfaceCreated(SurfaceHolder holder) {

            // 카메라 오픈
            camera = Camera.open();

            setCameraOrientation();

            try {
                // 카메라 객체에 mHolder 서피스뷰 설정
                camera.setPreviewDisplay(mHolder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Callback - 서피스뷰가 메모리에 변경되는 시점에 호출
        // 서피스뷰의 화면 크기가 바뀌는 등의 변경 시점에 미리보기 시작
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // 미리보기 화면에 영상 뿌리기
            camera.startPreview();
        }

        // Callback - 서피스뷰가 메모리에 없어지는 시점에 호출
        // 서피스뷰가 없어질 때 미리보기 중지
        public void surfaceDestroyed(SurfaceHolder holder) {

            // 카메라 해제
            camera.stopPreview();
            camera.release();
            camera = null;
        }


        // 카메라 객체의 takePicture 메서드를 호출하여 사진 촬영
        // PictureCallback 인터페이스를 구현한 객체를 파라미터로 전달함으로써 사진을 찍었을 때 이 객체의
        // onPictureTaken 메서드가 자동 호출된다
        public boolean capture(Camera.PictureCallback handler) {
            if (camera != null) {

                // 서피스 뷰에서 사진 찍기
                camera.takePicture(null, null, handler);
                return true;
            } else {
                return false;
            }
        }

        // 카메라 미리보기 화면 세로 모드로 보이기
        public void setCameraOrientation() {
            if (camera == null) {
                return;
            }

            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(0, info);

            WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

            // 회전에 대한 정보 확인
            int rotation = manager.getDefaultDisplay().getRotation();

            int degrees = 0;
            switch (rotation) {
                case Surface.ROTATION_0: degrees = 0; break;
                case Surface.ROTATION_90: degrees = 90; break;
                case Surface.ROTATION_180: degrees = 180; break;
                case Surface.ROTATION_270: degrees = 270; break;
            }

            int result;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360;
            } else {
                result = (info.orientation - degrees + 360) % 360;
            }

            // 카메라 객체의 setDisplayOrientation 메서드 호출
            camera.setDisplayOrientation(result);
        }

    }


    // 권한 부여시 콜백 메서드 3개 추가
    @Override  
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }

    @Override  
    public void onDenied(int requestCode, @NotNull String[] permissions) {
        Toast.makeText(this, "permissions denied : " + permissions.length, Toast.LENGTH_LONG).show();
    }

    @Override  
    public void onGranted(int requestCode, @NotNull String[] permissions) {
        Toast.makeText(this, "permissions granted : " + permissions.length, Toast.LENGTH_LONG).show();
    }

}
