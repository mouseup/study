package org.techtown.asynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    BackgroundTask task;
    int value;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 태스크 객체 만들어 실행하기
                task = new BackgroundTask();
                // doInBackground 실행
                task.execute();
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.cancel(true);
            }
        });
    }


    // AsyncTask<Integer , Integer , Integer> 세개의 자료형 명시되어 있는데
    // 이 자료형은 이 클래스를 상속하면서 재정의할 새로운 클래스의 메서드가
    // 어떤 자료형을 파라미터를 가질 것인지를 알려주는 역할
    // 첫번째 파라미터가 Integer이기 때문에 - protected Integer doInBackground(Integer ... values)
    // 두번째 파라미터가 Integer이기 때문에 - protected void onProgressUpdate(Integer ... values)
    // 세번째 파라미터가 Integer이기 때문에 - protected void onPostExecute(Integer result)
    class BackgroundTask extends AsyncTask<Integer , Integer , Integer> {
        protected void onPreExecute() {
            value = 0;
            progressBar.setProgress(value);
        }

        // 태스크 객체 안에서 백그라운드 작업 수행
        // AsyncTask - 추상클래스이므로 새로 정의한 클래스에서는 필요한 메서드를 다시 재정의 하여 사용
        // implements methods - doInBackground
        // BackgroundTask 실행시 doInBackground 자동 실행
        // doInBackground 완료시 onPostExecute 수행
        protected Integer doInBackground(Integer ... values) {
            while (isCancelled() == false) {
                value++;
                if (value >= 100) {
                    break;
                } else {
                    // onProgressUpdate 자동 호출됨
                    publishProgress(value);
                }

                try {
                    // 0.1초마다 쉼
                    Thread.sleep(100);
                } catch (InterruptedException ex) {}
            }

            return value;
        }

        // ui update
        protected void onProgressUpdate(Integer ... values) {
            // values[0]을 int값으로 변경
            progressBar.setProgress(values[0].intValue());
        }


        // doInBackground 의 반환값이 Integer = onPostExecute 매개변수 타입
        protected void onPostExecute(Integer result) {
            Toast.makeText(getApplicationContext(), "완료됨", Toast.LENGTH_LONG).show();
            progressBar.setProgress(0);
        }

        protected void onCancelled() {
            progressBar.setProgress(0);
        }
    }
}
