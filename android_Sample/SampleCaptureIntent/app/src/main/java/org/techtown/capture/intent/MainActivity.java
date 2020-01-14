package org.techtown.capture.intent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

// AutoPermissionsListener 인터페이스 구현
public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
    ImageView imageView;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        //
        AutoPermissions.Companion.loadAllPermissions(this, 101);
    }

    public void takePicture() {
        if (file == null) {
            file = createFile();
        }

        // file 객체로부터 uri 객체 만들기
        // 카메라 앱에서 공유하여 사용할수 있는 파일 정보를 uri 객체로 만든다
        Uri fileUri = FileProvider.getUriForFile(this,"org.techtown.capture.intent.fileprovider", file);

        // MediaStore.ACTION_IMAGE_CAPTURE - 단말의 카메라 앱을 띄워달라는 액션 정보
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 단말에서 사진을 찍었을경우 저장하는 방식
        // uri 객체는 MediaStore.EXTRA_OUTPUT 키를 사용해서 인텐트에 부가 데이터로 추가
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // resolveActivity - 인텐트에 지정한 액티비티가 있는지 확인(카메라 앱 있는지 확인)
        if (intent.resolveActivity(getPackageManager()) != null) {

            // 사진 찍기 화면 띄우기
            // 시스템으로 인텐트 객체 전달
            startActivityForResult(intent, 101);
        }
    }

    private File createFile() {
        String filename = "capture.jpg";
        // sd 카드 폴더 참조
        File storageDir = Environment.getExternalStorageDirectory();
        File outFile = new File(storageDir, filename);

        return outFile;
    }

    // 카메라 앱의 액티비티를 닫을때 응답 받기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 정상적으로 넘어오면
        if (requestCode == 101 && resultCode == RESULT_OK) {

            // 비트맵 로딩 - 이미지 파일을 비트맵 객체로 만들기(메모리에 만들어짐)
            BitmapFactory.Options options = new BitmapFactory.Options();

            // 1/8 크기로 만들기
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

            // 이미지뷰에 보여주기 - 이미지뷰에 비트맵 설정
            imageView.setImageBitmap(bitmap);
        }
    }



//  권한 부여시 필요한 콜백 메서드 3개 추가가
    @Override
    public void onRequestPermissionsResult(int requesCode, String permissions[],
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }

    @Override
    public void onDenied(int requestCode, @NotNull String[] permissions) {
        Toast.makeText(this, "permissions denied : " + permissions.length,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGranted(int requestCode, @NotNull String[] permissions) {
        Toast.makeText(this, "permissions granted : " + permissions.length, Toast.LENGTH_LONG).show();
    }

}
