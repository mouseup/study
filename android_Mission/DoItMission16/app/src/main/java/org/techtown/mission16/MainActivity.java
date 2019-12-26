package org.techtown.mission16;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    WebView webview;
    EditText urlInput;

    ImageView handleButton;
    RelativeLayout urlLayout;

    Animation translateUpsideAnim;
    Animation translateDownsideAnim;

    boolean isUrlInputOpen = false;

    boolean loadRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview = findViewById(R.id.webview);

        // 웹뷰 설정 수정
        WebSettings webSettings = webview.getSettings();
        webSettings.setSaveFormData(false);

        // 자바스크립트 동작
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);

        webview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "onPageFinished() called.");

                // start animation
                if (loadRequested) {
                    urlLayout.startAnimation(translateUpsideAnim);
                    loadRequested = false;
                }
            }
        });


        urlInput = findViewById(R.id.urlInput);
        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String urlStr = urlInput.getText().toString().trim();
                if (urlStr.length() < 1) {
                    Toast.makeText(getApplicationContext(), "사이트 주소를 먼저 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!urlStr.startsWith("http://")) {
                    urlStr = "http://" + urlStr;
                    urlInput.setText(urlStr);
                }

                loadRequested = true;

                // 버튼 클릭시 사이트 로딩
                webview.loadUrl(urlStr);

            }
        });

        handleButton = findViewById(R.id.handleButton);
        handleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleButton.startAnimation(translateUpsideAnim);
            }
        });

        // 주소검색 패널
        urlLayout = findViewById(R.id.urlLayout);

        // 액션정보 로딩
        translateUpsideAnim = AnimationUtils.loadAnimation(this, R.anim.translate_upside);
        translateDownsideAnim = AnimationUtils.loadAnimation(this, R.anim.translate_downside);

        // 애니메이션 리스너 설정
        SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
        translateUpsideAnim.setAnimationListener(animListener);
        translateDownsideAnim.setAnimationListener(animListener);

        isUrlInputOpen = true;
    }

    // 핸들버튼은 보이고 상단 패널은 안보임
    private void showHandleButton() {
        handleButton.setVisibility(View.VISIBLE);
        urlLayout.setVisibility(View.GONE);

        isUrlInputOpen = false;
    }

    // 핸들버튼 안보이고 상단패널은 보임
    private void showUrlInput() {
        handleButton.setVisibility(View.GONE);
        urlLayout.setVisibility(View.VISIBLE);

        isUrlInputOpen = true;
    }

    private class SlidingPageAnimationListener implements Animation.AnimationListener {

        // 애니메이션이 끝났을 때 호출
        public void onAnimationEnd(Animation animation) {
            if (isUrlInputOpen) {
                // 핸들버튼 보임
                showHandleButton();
            } else {
                // 상단패널보임
                showUrlInput();
            }
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

}
