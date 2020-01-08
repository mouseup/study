package org.techtown.diary;

import android.app.Application;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

// Volley 라이브러리를 이용해 인터넷으로 날씨 데이터를 요청할 때는 RequestQueue 객체 사용
// Application 클래스 상속하는 새로운 클래스 만들고 요청 처리
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    public static RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();

        // requestQueue 객체 만들어 변수에 할당
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext(), new HurlStack() {
                @Override
                protected HttpURLConnection createConnection(URL url) throws IOException {
                    HttpURLConnection connection = super.createConnection(url);
                    connection.setInstanceFollowRedirects(false);

                    return connection;
                }
            });
        }

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static interface OnResponseListener {
        public void processResponse(int requestCode, int responseCode, String response);
    }

    public static void send(final int requestCode, final int requestMethod, final String url, final Map<String,String> params,
                     final OnResponseListener listener) {

        // 요청 객체를 만들어 요청 처리
        StringRequest request = new StringRequest(
                requestMethod,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response for " + requestCode + " -> " + response);

                        // volley에서 응답을 받는 경우에는 OnResponseListener 인테페이스 안에 정의된
                        // processResponse 메서드 호출
                        // send() 메서드를 호출한 쪽에서 응답 결과를 처리할 수 있도록 만든것
                        if (listener != null) {
                            listener.processResponse(requestCode, 200, response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error for " + requestCode + " -> " + error.getMessage());

                        if (listener != null) {
                            listener.processResponse(requestCode, 400, error.getMessage());
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

        };

        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.requestQueue.add(request);
        Log.d(TAG, "Request sent : " + requestCode);
        Log.d(TAG, "Request url : " + url);
    }

}
