package org.techtown.web;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        webView = findViewById(R.id.webView);

        // getSettings 사용해 WebSettings 객체 참조
        // 웹뷰 설정 수정하기 - 웹의 자바스크립트 동작 가능하게
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //webView.setWebChromeClient(new BrowserClient());
        // 화면에 추가된 WebView 객체에 웹 페이지를 보여주기 위해서는 WebViewClient 객체를 상속한 객체를 만들어
        // WebView 에 설정해야한다.
        webView.setWebViewClient(new ViewClient());

        //webView.addJavascriptInterface(new JavaScriptMethods(), "sample");

        // app -> new -> folder -> assets folder 만든후 html 만들었을 경우
        //webView.loadUrl("file:///android_asset/www/sample.html");

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 버튼 클릭시 사이트 로딩하기
                webView.loadUrl(editText.getText().toString());
            }
        });
    }

    final class BrowserClient extends WebChromeClient {
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            result.confirm();

            return true;
        }
    }

    // 화면에 추가된 WebView 객체에 웹 페이지를 보여주기 위해서는 WebViewClient 객체를 상속한 객체를 만들어
    //  WebView 에 설정해야한다.
    private class ViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            view.loadUrl(url);

            return true;
        }
    }

}
