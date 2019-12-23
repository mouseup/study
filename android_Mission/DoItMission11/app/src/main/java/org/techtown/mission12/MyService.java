package org.techtown.mission12;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {
    public MyService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    // 서비스가 startServie 메서드에 의하여 메모리에 만들어어져 있는 상태에는
    // onCreat 가 아니라 onStartCommand 메서드 실행
    // 서비스로 전달된 인텐트 객체 처리
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 서비스는 시스템에 의해 자동으로 다시 시작될수 있기 때문에
        // 인텐트 객체가 널인지 검사하여 널이 아니면 인텐트에서 부가 데이터 가져오기
        if (intent != null) {
            String command = intent.getStringExtra("command");
            if (command != null) {
                if (command.equals("show")) {
                    String data = intent.getStringExtra("data");

                    sendToActivity(data);
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    // 브로드캐스트 수신자 정의하여 sendBroadcast 메서드로 메시지 보내기
    public void sendToActivity(String data) {
        Intent activityIntent = new Intent();
        activityIntent.setAction("org.techtown.broadcast.SHOW");
        activityIntent.putExtra("command", "show");
        activityIntent.putExtra("data", data);
        sendBroadcast(activityIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // 서비스가 서버 역할을 하면서 액티비티와 연결될 수 있도록 만드는 것을 바인딩이라 한다.
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
