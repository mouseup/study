package org.techtown.push;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);

        // 등록 id 확인을 위한 리스너 설정
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {

            // 등록 id 가 확인되었을때 onSuccess 자동 호출
            @Override
            public void onSuccess(InstanceIdResult result) {
                String newToken = result.getToken();

                println("등록 id : " + newToken);
            }
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 인스턴스 id 값 확인을 위한 메서드 호출 - 등록 id의 일부분
                String instanceId = FirebaseInstanceId.getInstance().getId();

                println("확인된 인스턴스 id : " + instanceId);
            }
        });
    }

    public void println(String data) {
        textView2.append(data + "\n");
        Log.d("FMS", data);
    }

    // 서비스로부터 인텐트를 받았을 때 처리
    @Override
    protected void onNewIntent(Intent intent) {
        println("onNewIntent 호출됨");
        if (intent != null) {
            processIntent(intent);
        }

        super.onNewIntent(intent);
    }

    private void processIntent(Intent intent) {
        String from = intent.getStringExtra("from");
        if (from == null) {
            println("from is null.");
            return;
        }

        // 보낸 데이터는 contents 키를 사용해 확인
        String contents = intent.getStringExtra("contents");
        println("DATA : " + from + ", " + contents);
        textView.setText("[" + from + "]로부터 수신한 데이터 : " + contents);
    }

}
