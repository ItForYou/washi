package co.kr.itforone.washi;

import android.app.Activity;
import android.webkit.JavascriptInterface;

public class WebviewJavainterface {

    MainActivity mainActivity;
    Activity activity;

    public WebviewJavainterface(MainActivity mainActivity, Activity activity) {
        this.activity = activity;
        this.mainActivity = mainActivity;
    }

    public WebviewJavainterface(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public void temp_first() {

    }

}
