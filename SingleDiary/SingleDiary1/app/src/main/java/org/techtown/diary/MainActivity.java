package org.techtown.diary;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;
import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

import org.jetbrains.annotations.NotNull;

// 날씨 데이터 파싱 클래스
import org.techtown.diary.data.GeocodeItem;
import org.techtown.diary.data.GeocodeResult;
import org.techtown.diary.data.WeatherItem;
import org.techtown.diary.data.WeatherResult;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;



public class MainActivity extends AppCompatActivity implements OnTabItemSelectedListener, OnRequestListener, AutoPermissionsListener, MyApplication.OnResponseListener {

    private static final String TAG = "MainActivity";

    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;

    BottomNavigationView bottomNavigation;

    // 현재위치
    Location currentLocation;
    // 위치정보수신
    GPSListener gpsListener;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
    SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH시");
    SimpleDateFormat dateFormat3 = new SimpleDateFormat("MM월 dd일");
    SimpleDateFormat dateFormat4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 위치정보확인 횟수 - 위치를 한번 확인한 후에는 위치 요청 취소
    int locationCount = 0;

    String currentWeather;
    String currentAddress;
    String currentDateString;
    Date currentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 각각 객체로 만들어 변수할당
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();

        // FragmentManager 객체를 이용해 첫번째 프레임 레이아웃 안에 추가
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();

        bottomNavigation = findViewById(R.id.bottom_navigation);

        // 하단탭에 리스너 설정
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            // 하단탭 버튼 클릭시 자동호출
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab1:
                        Toast.makeText(getApplicationContext(), "첫 번째 탭 선택됨", Toast.LENGTH_LONG).show();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment1).commit();

                        return true;
                    case R.id.tab2:
                        Toast.makeText(getApplicationContext(), "두 번째 탭 선택됨", Toast.LENGTH_LONG).show();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment2).commit();

                        return true;
                    case R.id.tab3:
                        Toast.makeText(getApplicationContext(), "세 번째 탭 선택됨", Toast.LENGTH_LONG).show();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment3).commit();

                        return true;
                }

                return false;
            }
        });

        AutoPermissions.Companion.loadAllPermissions(this, 101);

        setPicturePath();

    }

    public void setPicturePath() {
        String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        AppConstants.FOLDER_PHOTO = sdcardPath + File.separator + "photo";
    }

    // MainActivity가 OnTabItemSelectedListener 인터페이스를 구현하고 있으므로 그 안에 onTabSelected 정의
    public void onTabSelected(int position) {
        if (position == 0) {
            bottomNavigation.setSelectedItemId(R.id.tab1);
        } else if (position == 1) {
            bottomNavigation.setSelectedItemId(R.id.tab2);
        } else if (position == 2) {
            bottomNavigation.setSelectedItemId(R.id.tab3);
        }
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


    // fragment2 클래스에서 호출
    public void onRequest(String command) {
        if (command != null) {
            if (command.equals("getCurrentLocation")) {
                getCurrentLocation();
            }
        }
    }


    // 위치확인 시작
    public void getCurrentLocation() {
        // set current time
        // 현재 일자를 확인하여 두 번째 프래그먼트에 설정
        currentDate = new Date();
        currentDateString = dateFormat3.format(currentDate);
        if (fragment2 != null) {
            fragment2.setDateString(currentDateString);
        }

        // 현재 위치 요청
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            currentLocation = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (currentLocation != null) {
                double latitude = currentLocation.getLatitude();
                double longitude = currentLocation.getLongitude();
                String message = "Last Location -> Latitude : " + latitude + "\nLongitude:" + longitude;
                println(message);

                // 위치 확인되면 호출
                getCurrentWeather();
                getCurrentAddress();
            }

            // 요청된 위치 수신
            gpsListener = new GPSListener();
            long minTime = 10000;
            float minDistance = 0;

            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime, minDistance, gpsListener);

            println("Current location requested.");

        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    public void stopLocationService() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            manager.removeUpdates(gpsListener);

            println("Current location requested.");

        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    class GPSListener implements LocationListener {

        // 위치가 확인되었을 때 자동으로 호출됨
        public void onLocationChanged(Location location) {
            currentLocation = location;

            locationCount++;

            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            String message = "Current Location -> Latitude : "+ latitude + "\nLongitude:"+ longitude;
            println(message);

            getCurrentWeather();
            getCurrentAddress();
        }

        public void onProviderDisabled(String provider) { }

        public void onProviderEnabled(String provider) { }

        public void onStatusChanged(String provider, int status, Bundle extras) { }
    }



    // 현재 위치를 이용해 주소확인
    public void getCurrentAddress() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude(),
                    1);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);

            currentAddress = address.getLocality();
            if (address.getSubLocality() != null) {
                currentAddress += " " + address.getSubLocality();
            }

            String adminArea = address.getAdminArea();
            String country = address.getCountryName();
            println("Address : " + country + " " + adminArea + " " + currentAddress);

            if (fragment2 != null) {
                fragment2.setAddress(currentAddress);
            }
        }

    }

    // 현재 위치를 이용해 날씨 확인
    public void getCurrentWeather() {

        // 경위도 좌표를 격자번호로 변환
        Map<String, Double> gridMap = GridUtil.getGrid(currentLocation.getLatitude(), currentLocation.getLongitude());
        double gridX = gridMap.get("x");
        double gridY = gridMap.get("y");
        println("x -> " + gridX + ", y -> " + gridY);

        // 기상청 날씨 서버로 요청 전송
        // 응답을 받으면 processResponse() 메서드 호출
        sendLocalWeatherReq(gridX, gridY);

    }

    public void sendLocalWeatherReq(double gridX, double gridY) {
        String url = "http://www.kma.go.kr/wid/queryDFS.jsp";
        url += "?gridx=" + Math.round(gridX);
        url += "&gridy=" + Math.round(gridY);

        Map<String,String> params = new HashMap<String,String>();

        MyApplication.send(AppConstants.REQ_WEATHER_BY_GRID, Request.Method.GET, url, params, this);


    }


    // xml 응답 데이터를 자바 객체로 만들어준다.
    public void processResponse(int requestCode, int responseCode, String response) {
        if (responseCode == 200) {
            if (requestCode == AppConstants.REQ_WEATHER_BY_GRID) {
                // Grid 좌표를 이용한 날씨 정보 처리 응답
                //println("response -> " + response);

                XmlParserCreator parserCreator = new XmlParserCreator() {
                    @Override
                    public XmlPullParser createParser() {
                        try {
                            return XmlPullParserFactory.newInstance().newPullParser();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                };

                GsonXml gsonXml = new GsonXmlBuilder()
                        .setXmlParserCreator(parserCreator)
                        .setSameNameLists(true)
                        .create();

                WeatherResult weather = gsonXml.fromXml(response, WeatherResult.class);

                // 현재 기준 시간
                try {
                    Date tmDate = AppConstants.dateFormat.parse(weather.header.tm);
                    String tmDateText = AppConstants.dateFormat2.format(tmDate);
                    println("기준 시간 : " + tmDateText);

                    for (int i = 0; i < weather.body.datas.size(); i++) {
                        WeatherItem item = weather.body.datas.get(i);
                        println("#" + i + " 시간 : " + item.hour + "시, " + item.day + "일째");
                        println("  날씨 : " + item.wfKor);
                        println("  기온 : " + item.temp + " C");
                        println("  강수확률 : " + item.pop + "%");

                        println("debug 1 : " + (int)Math.round(item.ws * 10));
                        float ws = Float.valueOf(String.valueOf((int)Math.round(item.ws * 10))) / 10.0f;
                        println("  풍속 : " + ws + " m/s");
                    }

                    // set current weather
                    WeatherItem item = weather.body.datas.get(0);
                    currentWeather = item.wfKor;
                    if (fragment2 != null) {
                        fragment2.setWeather(item.wfKor);
                    }

                    // stop request location service after 2 times
                    // LocationManager에게 위치를 요청했던 것은 stopLocationService 메서드 호출하여 취소
                    if (locationCount > 1) {
                        stopLocationService();
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                }


            } else {
                // Unknown request code
                println("Unknown request code : " + requestCode);

            }

        } else {
            println("Failure response code : " + responseCode);

        }

    }

    private void println(String data) {
        Log.d(TAG, data);
    }



}
