package co.kr.itforone.washi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mwebview)    WebView webView;
    @BindView(R.id.progressbar)    ProgressBar progressBar;

    //@BindView(R.id.refreshlayout)    SwipeRefreshLayout refreshlayout;
    WebSettings settings;
    String token = "";
    String[] PERMISSIONS = {

            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.VIBRATE

    };

    static final int PERMISSION_REQUEST_CODE = 1;
    private long backPrssedTime = 0;
    String user_id="", user_pwd="", pushurl="",pushurl2="";

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

        Log.d("Create!!!","ok");

        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("D", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        token = task.getResult().getToken();
                    }
        });

        SharedPreferences pref = getSharedPreferences("logininfo", MODE_PRIVATE);
        user_id = pref.getString("id", "");
        user_pwd = pref.getString("pwd", "");

        Intent push_i = getIntent();
        if(push_i!=null){
            if(push_i.hasExtra("goUrl")){
                Log.d("pushurl1",push_i.getExtras().toString());
            }
            Log.d("pushurl2",push_i.toString());
        }
        if (push_i.getStringExtra("goUrl") != null)
            pushurl = push_i.getStringExtra("goUrl");

        Log.d("pushurl3",pushurl);

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
        settings.setUserAgentString("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Mobile Safari/537.36"+"/washi");
        webView.setWebContentsDebuggingEnabled(true);

        if(!user_id.isEmpty() && !user_pwd.isEmpty()){
            webView.loadUrl(getString(R.string.login) + "mb_id=" + user_id + "&mb_password=" + user_pwd);
        }
        else {
            webView.loadUrl(getString(R.string.index));
        }

    }

    @Override
    public void onBackPressed() {
      //  WebBackForwardList historyList = webView.copyBackForwardList();
            Log.d("now_url",webView.getUrl());

        if(webView.getUrl().equals(getString(R.string.driverindex))){
            return;
        }
        if(webView.getUrl().equals(getString(R.string.index)) || webView.getUrl().equals(getString(R.string.intro))
                || webView.getUrl().equals(getString(R.string.shopindex)) || webView.getUrl().equals(getString(R.string.cus_index))
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