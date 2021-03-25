package co.kr.itforone.washi;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebviewActivity extends AppCompatActivity {
    @BindView(R.id.mwebview)    WebView webView;
    WebSettings settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        settings = webView.getSettings();
        webView.setWebChromeClient(new ChromeManager(this));
        webView.setWebViewClient(new ViewManager( this));
        webView.addJavascriptInterface(new WebviewJavainterface(this),"Android");
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);//웹에서 파일 접근 여부
        settings.setAppCacheEnabled(true);//캐쉬 사용여부
        settings.setDatabaseEnabled(true);//HTML5에서 db 사용여부 -> indexDB
        settings.setSupportMultipleWindows(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDomStorageEnabled(true);//HTML5에서 DOM 사용여부
        settings.setUseWideViewPort(true);//웹에서 view port 사용여부
        settings.setUserAgentString("washi");
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);//캐시 사용모드 LOAD_NO_CACHE는 캐시를 사용않는다는 뜻
        settings.setTextZoom(100);       // 폰트크기 고정
        webView.setWebContentsDebuggingEnabled(true);

        Intent i = getIntent();
        String url="";
        if(i!=null){
            url = i.getStringExtra("url");
        }
        if(!url.isEmpty() && !url.equals("")) {
            webView.loadUrl(url);
        }

    }


}
