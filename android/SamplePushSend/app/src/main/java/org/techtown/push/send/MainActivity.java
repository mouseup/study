package org.techtown.push.send;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;

    static RequestQueue requestQueue;
    static String regId = "dU1rN88iOgQ:APA91bHOZyru__sVr8QkqOQETODsvnE7qfx5BwskjtihfRPaVk0sZydpZhIRkDkBoWLOffMubH1tbzmFKYVAtfK7JbDq3i_N2fTMMQCUwjBa7vfKsDcYs1lngzncqC-S0nKzIZCyrlsR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();

                // 메시지 전송
                send(input);
            }
        });

        if (requestQueue == null) {

            // requestQueue 객체 만들기
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

    }

    // volley 라이브러리는 RequestQueue 객체에 요청 객체(Request Object)를 만들어 추가하면 자동으로 메시지를 전송하는 방식이다.
    public void send(String input) {

        // 전송 정보를 담아둘 JSONObject 객체 생성
        JSONObject requestData = new JSONObject();

        try {

            // 옵션 추가
            requestData.put("priority", "high");

            // 전송할 데이터 추가
            JSONObject dataObj = new JSONObject();
            dataObj.put("contents", input);
            requestData.put("data", dataObj);

            // 푸시 메시지를 수신할 단말의 등록 id를
            // JSONArray 에 추가한 후 requestData 객체에 추가
            JSONArray idArray = new JSONArray();
            idArray.put(0, regId);
            requestData.put("registration_ids", idArray);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 푸시 전송을 위해 정의한 메서드 호출
        // JSONObject 타입의 요청 객체와 리스너를 파라미터로 전달하면 메시지를 전송하면서 그 상태를 리스너로 알려준다
        sendData(requestData, new SendResponseListener() {

            @Override
            public void onRequestCompleted() {
                println("onRequestCompleted() 호출됨.");
            }

            @Override
            public void onRequestStarted() {
                println("onRequestStarted() 호출됨.");
            }

            @Override
            public void onRequestWithError(VolleyError error) {
                println("onRequestWithError() 호출됨.");
            }
        });
    }


    public interface SendResponseListener {
        public void onRequestStarted();
        public void onRequestCompleted();
        public void onRequestWithError(VolleyError error);
    }

    public void sendData(JSONObject requestData, final SendResponseListener listener) {

        // Volley 요청 객체를 만들고 요청을 위한 데이터 설정
        // Request.Method.POST- POST 방식으로 요청
        // "https://fcm.googleapis.com/fcm/send" - 클라우드 서버의 요청 주소
        // requestData - 요청 데이터가 들어 있는 객체
        JsonObjectRequest request = new JsonObjectRequest(

                Request.Method.POST, "https://fcm.googleapis.com/fcm/send", requestData,

                // 성공응답을 받았을때
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onRequestCompleted();
                    }

                },

                // 오류응답을 받았을때
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onRequestWithError(error);
                    }
                }) {

            // 요청을 위한 파라미터 설정 - 비어있는  HashMap 객체만 반환
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            // 요청을 위한 헤더 설정
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                // key = fcm을 사용해 클라우드 서버로 메시지를 보낼 때 사용 되는 키 - fcm 개발자 콘솔 페이지에서 확인
                headers.put("Authorization", "key=AAAAdcL7KKQ:APA91bEEJG1TD3oflII4tsfx_BagFqxQ7Efq6TcV0LphTlIWnOURavLNhMouq2FtFegR3yioq3RggkoWN1fMYSyE00rEb-3BzD3W1oi5u9mygrRFVhW4ToyWJznGDgsL1beT8UC5Uxgu");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        request.setShouldCache(false);
        listener.onRequestStarted();

        // Volley 큐에 요청 객체 추가 - volley 라이브러리에서 자동으로 요청 전송
        requestQueue.add(request);
    }


    public void println(String data) {
        textView.append(data + "\n");
    }
}
