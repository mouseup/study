package org.techtown.push;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

// FirebaseMessagingService 상속 - 푸시 메시지 전달 받는 역할
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FMS";

    public MyFirebaseMessagingService() {

    }

    // 앱이 Firebase 서버에 등록되었을때 호출
    // String token - 앱의 등록 id(메시지를 전달하고 싶은 쪽에서 이 등록 id를 사용할 수 있다)
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        Log.d(TAG, "onNewToken 호출됨 : " + token);
    }

    // 새로운 메시지를 받았을때 호출됨
    // remoteMessage - 상대방이 클라우드 서버를 통해 보낸 푸시 메시지의 데이터 확인
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived 호출됨.");

        // 발신자 코드 확인
        String from = remoteMessage.getFrom();

        // 데이터 확인
        Map<String, String> data = remoteMessage.getData();
        String contents = data.get("contents");
        Log.d(TAG, "from : " + from + ", contents : " + contents);

        // 액티비티 쪽으로 보냄
        sendToActivity(getApplicationContext(), from, contents);
    }

    // 액티비티 쪽으로 데이터를 보내기 위한 인텐트 객체를 만들고 startActivity 호출
    private void sendToActivity(Context context, String from, String contents) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("contents", contents);

        // 서비스에서 액티비티를 띄울 때는 인텐트에 플래그를 주어야 하며
        // 메인 액티비티가 이미 메모리에 만들어져 있는 경우에는 메인 액티비티의 onNewIntent() 메서드로 데이터가 전달
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(intent);
    }

}
