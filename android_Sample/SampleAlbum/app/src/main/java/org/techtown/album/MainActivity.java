package org.techtown.album;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        AutoPermissions.Companion.loadAllPermissions(this, 101);
    }

    public void openGallery() {
        Intent intent = new Intent();
        // MIME 타입이 image 로 시작하는 데이터 가져오기
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        // 인텐트 객체를 파라미터로 전달하면서 앨범에서 사진을 선택할 수 있는 화면을 띄워주게 된다
        startActivityForResult(intent, 101);
    }

    // 앨범 앱에서 사진을 선택한 후에는 onActivityResult 메서드로 그 결과 값을 전달받을 수 있다.
    // onActivityResult 메서드가 자동으로 호출되면 data라는 변수가 참조하는 인텐트 객체를 사용할 수 있다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 101) {
            if(resultCode == RESULT_OK) {

                // getData 메서드를 호출하면 Uri 자료형의 값이 반환된다.
                Uri fileUri = data.getData();

                // ContentResolver 를 이용해 참조할 수 있는 이미지 파일을 가리킨다.
                // 앨범 앱 안에 내용 제공자를 만들어 두었음을 짐작할수 있다.
                ContentResolver resolver = getContentResolver();

                try {
                    // openInputStream 메서드를 호출할 때 파라미터로 전달
                    InputStream instream = resolver.openInputStream(fileUri);
                    // bitmap 객체로 만들기
                    Bitmap imgBitmap = BitmapFactory.decodeStream(instream);
                    // 이미지뷰에 설정
                    imageView.setImageBitmap(imgBitmap);

                    instream.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
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
