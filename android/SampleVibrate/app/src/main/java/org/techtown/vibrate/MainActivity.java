package org.techtown.vibrate;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Parcel;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 진동 울리기
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                // 버전 26 이상이면 VibrationEffect.createOneShot 메서드 호출하여 반환된 객체를 파라미터로 전달
                // 1000,10 - 지속 시간과 음량
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(VibrationEffect.createOneShot(1000,10));
                } else {
                // 26 미만이면 지속시간만 전달
                    vibrator.vibrate(1000);
                }
            }
        });

        // 기본 음원 재생
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                // uri 객체를 전달하면 지정한 음원을 rindon 객체를 참조한다.
                Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), uri);
                ringtone.play();
            }
        });


        // 직접 추가한 음원 파일 재생
        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // raw 폴더 안에 beep.wav 파일 지정
                MediaPlayer player = MediaPlayer.create(getApplicationContext(), R.raw.beep);
                player.start();
            }
        });
    }

}
