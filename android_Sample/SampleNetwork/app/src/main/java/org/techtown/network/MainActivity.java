package org.techtown.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    WiFiReceiver wifiReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnectivity();
            }
        });

        wifiReceiver = new WiFiReceiver();
    }

    public void checkConnectivity() {

        // ConnectivityManager 객체 확인
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // getActiveNetworkInfo - 인터넷 연결 여부와 연결 방식에 대한 정보
        NetworkInfo info = manager.getActiveNetworkInfo();

        // getType - 무선랜인지 일반망인지 구분
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                println("WiFi로 설정됨");
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                println("일반망으로 설정됨");
            }

            // isConnected - 연결상태 확인
            println("연결 여부 : " + info.isConnected());
        } else {
            println("데이터통신 불가");
        }

    }

    public void println(String data) {
        textView.append(data + "\n");
    }

    // 수신자 등록 해제
    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(wifiReceiver);
    }

    // 브로드캐스트 수신자 등록
    @Override
    protected void onResume() {
        super.onResume();

        // 무선랜의 상태와 네트워크 상태를 전달 받을 수 있다.
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);

        // 코드에서 수신자 등록 - wifiReceiver 클래스로부터 인스턴스 객체를 생성하여 변수에 할당
        registerReceiver(wifiReceiver, filter);

    }

    class WiFiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // wifi 상태 체크
            if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
                if (state == WifiManager.WIFI_STATE_ENABLED) {
                    println("WiFi enabled");
                } else if (state == WifiManager.WIFI_STATE_DISABLED) {
                    println("WiFi disabled");
                }
            } else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                String ssid = manager.getConnectionInfo().getSSID();

                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    println("Connected : " + ssid);
                } else if (info.getState() == NetworkInfo.State.DISCONNECTED) {
                    println("Disconnected : " + ssid);
                }
            }
        }
    }

}
