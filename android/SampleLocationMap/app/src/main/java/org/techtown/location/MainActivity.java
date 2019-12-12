package org.techtown.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {

    SupportMapFragment mapFragment;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // xml 레이아웃에 추가했던 프래그먼트 객체 참조
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // getMapAsync - 구글맵 객체 사용할수 있다. 내부적으로 지도를 다루는 구글맵 객체를 초기화하는데 비동기 방식으로 처리
        // 따라서 콜백 객체를 파라미터로 전달한 후 초기화가 완료될 때 콜백 객체 안의 함수가 자동으로 호출되도록 만든다.
        mapFragment.getMapAsync(new OnMapReadyCallback() {

            // 구글맵 사용할수 있는 상태가 되면 onMapReady 자동 호출
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d("Map", "지도 준비됨.");
                map = googleMap;

            }
        });

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationService();
            }
        });

        AutoPermissions.Companion.loadAllPermissions(this, 101);
    }

    // 위치 관리자로부터 현재 위치를 전달 받음
    public void startLocationService() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String message = "최근 위치 -> Latitude : " + latitude + "\nLongitude:" + longitude;

                Log.d("Map", message);
            }

            GPSListener gpsListener = new GPSListener();
            long minTime = 10000;
            float minDistance = 0;

            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime, minDistance, gpsListener);

            Toast.makeText(getApplicationContext(), "내 위치확인 요청함",
                    Toast.LENGTH_SHORT).show();

        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    class GPSListener implements LocationListener {

        // 위치가 넘어오면 onLocationChanged 자동 호출
        public void onLocationChanged(Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            String message = "내 위치 -> Latitude : "+ latitude + "\nLongitude:"+ longitude;
            Log.d("Map", message);

            showCurrentLocation(latitude, longitude);
        }

        public void onProviderDisabled(String provider) { }

        public void onProviderEnabled(String provider) { }

        public void onStatusChanged(String provider, int status, Bundle extras) { }
    }

    // 지도 이동
    private void showCurrentLocation(Double latitude, Double longitude) {

        // 현재 위치의 좌표로 LatLng 객체 생성 - 경위도 좌표로 구성된 위치를 지도에 표시할수 있도록 정의
        LatLng curPoint = new LatLng(latitude, longitude);

        // 지정한 위치의 지도 영역 보여주기
        // 15 - 줌 레벨
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
    }

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
