package org.techtown.permission2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 위험 권한 자동부여 요청하기
        // AndroidManifest 파일 안에 넣은 권한 중에서 위험권한을 자동으로 체크한 후 권한 부여 요청
        AutoPermissions.Companion.loadAllPermissions(this, 101);
    }


    // 위험 권한 부여에 대한 응답 처리하기
    // 권한 부여 요청 결과가 넘어오게 되는데 그 결과는 onRequestPermissionsResult 메서드로 전달 받는다.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // parsePermissions() - 이 메서드를 호출하면 권한 결과가 승인 또는 거부로 나뉘면서
        // onGranted() 또는 onDenied() 메서드가 호출
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
