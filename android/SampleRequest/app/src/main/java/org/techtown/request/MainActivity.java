package org.techtown.request;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;

    // 요청 큐는 한번만 만들어 계속 사용할수 있기 때문에 static 으로 클래스 변수 할당
    static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editText = findViewById(R.id.editText);
        editText.setText("http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=430156241533f1d058c603178cc3ca0e&targetDt=20120101");
        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRequest();
            }
        });


        if (requestQueue == null) {
            // Volley.newRequestQueue - requestQueue 객체 생성
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

    }

    public void makeRequest() {
        String url = editText.getText().toString();

        // 요청을 보내기 위한 StringRequest 객체 생성
        // StringRequest - 문자열을 주고 받기 위해 사용하는 요청 객체
        // 네 개의 파라미터 전달
        // 첫 번째 파라미터 - 요청방식 지정 - GET, POST
        // 두 번째 파라미터 - 웹사이트 주소 전달
        // 세 번째 파라미터 - 응답받을 리스너 객체 전달 - 이 리스너의 onResponse 메서드는 응답을 받을때 자동호출
        // 네 번째 파라미터 - 에러가 발생했을 때 호출될 리스너 객체 전달
        // -> POST 방식을 사용하면서 요청 파라미터를 전달하고자 한다면 getParams 메서드에서 반환하는
        // -> HashMap 객체에 파라미터 값들을 넣어주면 된다.
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        println("응답 -> " + response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        println("에러 -> " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();

                return params;
            }
        };

        // 요청 객체는 cache 매너키즘을 지원하는데 만약 이전 응답 결과를 사용하지 않겠다면
        // setShouldCache를 false
        request.setShouldCache(false);
        // 요청 객체를 만들었다면 이 객체는 요청 큐에 넣어준다.
        // add 메서드로 요청 객체를 넣으면 요청 큐가 자동으로 요청과 응답 과정을 진행한다.
        requestQueue.add(request);
        println("요청 보냄.");
    }

    public void println(String data) {
        textView.append(data + "\n");
    }

}
