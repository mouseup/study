package org.techtown.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SmsReceiver extends BroadcastReceiver {
    public static final String TAG = "SmsReceiver";

    public SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    // sms를 받으면 onReceive() 메서드가 자동 호출
    // 파라미터로 전달되는 Intent 객체 안에  sms 데이타가 들어 있다.
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "onReceive() 메소드 호출됨.");

        // 인텐트 객체 안에 드어 있는 bundle 객체를 getExtras() 메서드로 참조
        // 이 bundle 객체 안에는 부가 데이터가 들어 있으며
        // parseSmsMessage() 메서드를 호출하여 sms 메시지 객체를 만들도록 한다.
        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = parseSmsMessage(bundle);

        if (messages != null && messages.length > 0) {

            // getOriginatingAddress() - 발신자 번호 확인
            String sender = messages[0].getOriginatingAddress();
            Log.i(TAG, "SMS sender : " + sender);

            // getMessageBody() - 문자 내용 확인
            String contents = messages[0].getMessageBody().toString();
            Log.i(TAG, "SMS contents : " + contents);

            // getTimestampMillis() - sms 받은 시각 확인
            Date receivedDate = new Date(messages[0].getTimestampMillis());
            Log.i(TAG, "SMS received date : " + receivedDate.toString());

            sendToActivity(context, sender, contents, receivedDate);
        }
    }

    // parseSmsMessage() 메서드는 직접 정의한 메서드로
    // SmsMessage라는 자료형으로 된 배열 객체를 반환하도록 되어 있다.
    // SmsMessage 객체에는 sms 데이터를 확인할 수 있는 메서드들이 정의
    private SmsMessage[] parseSmsMessage(Bundle bundle) {

        // 번들 객체에 들어가 있는 부가 데이터 중에서 pdus 가져오기
        Object[] objs = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[objs.length];

        int smsCount = objs.length;
        for (int i = 0; i < smsCount; i++) {

            // 단말 os 버전에 따라 다른 방식으로 메서드 호출하기
            // Build.VERSION.SDK_INT 는 단말의 os 버전 확인
            // Build.VERSION_CODES 에는 안드로이드 os 버전별로 상수가 정의
            // OS 가 마시멜로(첫 글자 M) 버전과 같거나 그 이후 버전일 때
            // String format = bundle.getString("format"); 수행
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String format = bundle.getString("format");

                // 인텐트 객체 안에 부가 데이터로 들어 있는 sms 데이터를 확인하려면
                // SmsMessage 클래스의 createFromPdu() 메서드를 사용하여
                // SmsMessage 객체로 변환하면 sms 데이터를 확인할수 있다
                messages[i] = SmsMessage.createFromPdu((byte[]) objs[i], format);
            } else {
                messages[i] = SmsMessage.createFromPdu((byte[]) objs[i]);
            }
        }

        return messages;
    }

    //SmsActivity로 인텐트를 보냄
    private void sendToActivity(Context context, String sender, String contents, Date receivedDate) {

        // Intent 객체를 만들 때 두 번째 파라미터로 SmsActivity 객체(SmsActivity.class)를 전달했으므로
        // startActivity() 메서드를 사용해 이 인텐트를 시스템으로 전달하면 시스템이
        // 시스템이 그 인텐트를 SmsActivity 쪽으로 전달한다.
        // 브로드캐스트 수신자는 화면이 없으므로
        // 인텐트의 플래스로 FLAG_ACTIVITY_NEW_TASK를 추가해야 한다.
        // 그리고 이미 메모리에 만든 SmsActivity 가 있을 때 액티비티를 중복 생성하지 않도록
        // FLAG_ACTIVITY_SINGLE_TOP 플래그 추가
        Intent myIntent = new Intent(context, SmsActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        myIntent.putExtra("sender", sender);
        myIntent.putExtra("contents", contents);
        myIntent.putExtra("receivedDate", format.format(receivedDate));
        context.startActivity(myIntent);
    }
}
