package org.techtown.location;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
    private static final String TAG = "MainActivity";

    SupportMapFragment mapFragment;
    GoogleMap map;

    MarkerOptions myLocationMarker;

    MarkerOptions friendMarker1;
    MarkerOptions friendMarker2;
    private Drawable pictureDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // xml 레이아웃에 추가했던 프래그먼트 객체 참조하고 getMapAsync 메서드 호출
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // getMapAsync - 비동기방식으로 동작하기 때문에 지도가 사용 가능하게 된 후에 onMapReady 메서드 자동 호출
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "GoogleMap is ready.");

                map = googleMap;

                try {
                    map.setMyLocationEnabled(true);
                } catch(SecurityException e) {e.printStackTrace();}

            }
        });

        try {
            MapsInitializer.initialize(this);
        } catch(Exception e) {
            e.printStackTrace();
        }

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMyLocation();
            }
        });

        AutoPermissions.Companion.loadAllPermissions(this, 101);
    }


    private void requestMyLocation() {

        // LocationManager 객체 참조
        // LOCATION_SERVICE - 위치관리자를 위해 정의한 상수이름
        LocationManager manager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            // 최소시간 10초
            long minTime = 10000;
            // 최소 거리 0
            float minDistance = 0;

            // 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {

                        // 위치 관리자가 위치 정보를 전달할 때 호출
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    }
            );

            // 이전에 확인했던 위치 정보 가져오기
            // GPS_PROVIDER - 위치제공자
            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                showCurrentLocation(lastLocation);
            }

            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);

                            addPictures(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    }
            );


        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    // location.getLatitude(), location.getLongitude() - 위도, 경도
    private void showCurrentLocation(Location location) {

        // 현재 위치의 좌표로 LatLng 객체 생성
        LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());

        try {
            // 지정한 위치의 지도 영역 보여주기 - 15(축척)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));

            showMyLocationMarker(location);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void showMyLocationMarker(Location location) {
        if (myLocationMarker == null) {

            // 마커 객체 생성
            myLocationMarker = new MarkerOptions();
            myLocationMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
            myLocationMarker.title("● 내 위치\n");
            myLocationMarker.snippet("● GPS로 확인한 위치");
            myLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation));

            // 지도에 마커 추가
            map.addMarker(myLocationMarker);
        } else {
            myLocationMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }


    private void addPictures(Location location) {
        int pictureResId = R.drawable.friend03;
        String msg = "● 김성수\n"
                + "● 010-7788-1234";

        if (friendMarker1 == null) {
            friendMarker1 = new MarkerOptions();
            friendMarker1.position(new LatLng(location.getLatitude()+3000, location.getLongitude()+3000));
            friendMarker1.title("● 친구 1\n");
            friendMarker1.snippet(msg);
            friendMarker1.icon(BitmapDescriptorFactory.fromResource(pictureResId));
            map.addMarker(friendMarker1);
        } else {
            friendMarker1.position(new LatLng(location.getLatitude()+3000, location.getLongitude()+3000));
        }

        pictureResId = R.drawable.friend04;
        msg = "● 이현수\n"
                + "● 010-5512-4321";


        if (friendMarker2 == null) {
            friendMarker2 = new MarkerOptions();
            friendMarker2.position(new LatLng(location.getLatitude()+2000, location.getLongitude()-1000));
            friendMarker2.title("● 친구 2\n");
            friendMarker2.snippet(msg);
            friendMarker2.icon(BitmapDescriptorFactory.fromResource(pictureResId));
            map.addMarker(friendMarker2);
        } else {
            friendMarker2.position(new LatLng(location.getLatitude()+2000, location.getLongitude()-1000));
        }

    }


    @Override
    protected void onPause() {
        super.onPause();

        if (map != null) {
            try {

                // 액티비티가 중지될 때 내 위치 표시 비활성화
                map.setMyLocationEnabled(false);
            } catch(SecurityException e) {e.printStackTrace();}
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (map != null) {
            try {

                // 액티비티가 화면에 보일 때 내 위치 표시 활성화
                map.setMyLocationEnabled(true);
            } catch(SecurityException e) {e.printStackTrace();}
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
