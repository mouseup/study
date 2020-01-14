package org.techtown.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    SensorManager manager;
    List<Sensor> sensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 센서 리스트 화면 출력
                getSensorList();
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 첫번째 센서의 값을 확인하여 화면에 출력
                registerFirstSensor();
            }
        });
    }

    public void getSensorList() {

        // 센서 매니저 객체 참조
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // 모든 센서 정보
        sensors = manager.getSensorList(Sensor.TYPE_ALL);

        int index = 0;
        for (Sensor sensor : sensors) {
            println("#" + index + " : " + sensor.getName());
        }
    }

    public void registerFirstSensor() {

        // 센서를 위한 리스너 설정
        manager.registerListener(
                new SensorEventListener() {

                    // 센서의 데이터 값이 변할 때마다 호출
                     @Override
                     public void onSensorChanged(SensorEvent event) {

                         // timestamp - 값을 확인한 시간
                         String output = "Sensor Timestamp : " + event.timestamp + "\n\n";
                         for(int index = 0; index < event.values.length; ++index) {
                             output += ("Sensor Value #" + (index + 1) + " : " + event.values[index] + "\n");
                         }
                         println(output);
                     }

                     // 센서의 정확도 값이 변할 때마다 호출
                     @Override
                     public void onAccuracyChanged(Sensor sensor, int accuracy) {

                     }
                },
                sensors.get(0),
                SensorManager.SENSOR_DELAY_UI);
    }

    public void println(String data) {
        textView.append(data + "\n");
    }
}
