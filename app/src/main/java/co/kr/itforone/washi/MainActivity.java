package co.kr.itforone.washi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mwebview)    WebView webView;
    //@BindView(R.id.refreshlayout)    SwipeRefreshLayout refreshlayout;
    WebSettings settings;
    String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    static final int PERMISSION_REQUEST_CODE = 1;
    private long backPrssedTime = 0;

    private boolean hasPermissions(String[] permissions){
        // 퍼미션 확인해
        int result = -1;
        for (int i = 0; i < permissions.length; i++) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[i]);
            if(result!= PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }

        Log.d("per_result",String.valueOf(result));

        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else {
            return false;
        }

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (!hasPermissions(PERMISSIONS)){

                }else{

                }
                return;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        settings = webView.getSettings();
        webView.setWebChromeClient(new ChromeManager(this,this));
        webView.setWebViewClient(new ViewManager(this, this));
        webView.addJavascriptInterface(new WebviewJavainterface(this, this),"Android");
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);//웹에서 파일 접근 여부
        settings.setAppCacheEnabled(true);//캐쉬 사용여부
        settings.setDatabaseEnabled(true);//HTML5에서 db 사용여부 -> indexDB
        settings.setDomStorageEnabled(true);//HTML5에서 DOM 사용여부
        settings.setSupportMultipleWindows(false);
        settings.setUseWideViewPort(true);//웹에서 view port 사용여부
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);//캐시 사용모드 LOAD_NO_CACHE는 캐시를 사용않는다는 뜻
        settings.setTextZoom(100);       // 폰트크기 고정
        settings.setUserAgentString("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Mobile Safari/537.36"+"/AGold");
        webView.setWebContentsDebuggingEnabled(true);
        webView.loadUrl(getString(R.string.index));

    }

    @Override
    public void onBackPressed() {
      //  WebBackForwardList historyList = webView.copyBackForwardList();
            Log.d("now_url",webView.getUrl());
        if(webView.getUrl().equals(getString(R.string.index)) || webView.getUrl().equals(getString(R.string.intro))
                || webView.getUrl().equals(getString(R.string.shopindex)) || webView.getUrl().equals(getString(R.string.driverindex))
                || webView.getUrl().equals(getString(R.string.cus_index))
        ){

            long tempTime = System.currentTimeMillis();
            long intervalTime = tempTime - backPrssedTime;

            if (0 <= intervalTime && 2000 >= intervalTime){
                finish();
            }
            else
            {
                webView.clearCache(true);
                webView.clearHistory();
                backPrssedTime = tempTime;
                Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 누를시 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
            }
        }else{
            if(webView.canGoBack()){
                webView.goBack();
            }
            else {
                long tempTime = System.currentTimeMillis();
                long intervalTime = tempTime - backPrssedTime;
                if (0 <= intervalTime && 2000 >= intervalTime) {
                    finish();
                } else {
                    webView.clearCache(true);
                    webView.clearHistory();
                    backPrssedTime = tempTime;
                    Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 누를시 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}