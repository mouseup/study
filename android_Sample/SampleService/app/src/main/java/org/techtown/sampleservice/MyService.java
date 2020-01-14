package org.techtown.sampleservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    private static final String TAG = "MyService";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    // 서비스는 시스템에 의해 자동으로 다시 시작될수 있기 때문에 onStartCommand 메서드로 전달되는
    // 인텐트 객체가 null인 경우도 검사
    // 인텐트 객체가 null이면 onStartCommand 메서드는 Service.START_STICKY을 반환
    // 이 값을 반환하면 서비스가 비정상 종료되었다는 의미이므로 시스템이 자동으로 재시작
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand() 호출됨.");

        if (intent == null) {
            return Service.START_STICKY;
        } else {
            processCommand(intent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    // 5초동안 1초에 한 번씩 로그 출력
    private void processCommand(Intent intent) {

        // 인텐트에서 부가데이터 가져오기
        String command = intent.getStringExtra("command");
        String name = intent.getStringExtra("name");
        Log.d(TAG, "command : " + command + ", name : " + name);

        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }

            Log.d(TAG, "Waiting " + i + " seconds.");
        }

        // 액티비티 쪽으로 인텐트 전달
        Intent showIntent = new Intent(getApplicationContext(), MainActivity.class);

        // 서비스에서 startActivity 메서드를 호출 할때는 새로운 태스크를 생성하도록
        // FLAG_ACTIVITY_NEW_TASK 플래그를 인텐트에 추가해야 한다
        // 서비스는 화면이 없기 때문에 화면이 없는 서비스에서 화면이 있는 액티비티를 띄우려면
        // 새로운 태스크를 만들어야 하기 때문이다.
        // MainActivity 객체가 이미 메모리에 만들어져 있을 때 재사용하도록
        // FLAG_ACTIVITY_SINGLE_TOP 과 FLAG_ACTIVITY_CLEAR_TOP 플래그도 인텐트에 추가
        // 서비스에서 5초후에 메인 액티비티에 전달한 인텐트는 메인 엑티비티에서 받아 처리할수 있다.
        showIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        showIntent.putExtra("command", "show");
        showIntent.putExtra("name", name + " from service.");
        startActivity(showIntent);
    }


    // 바인딩 - 서비스가 서버 역할을 하면서 액티비티와 연결 될 수 있도록 만드는것
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
