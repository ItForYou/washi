package co.kr.itforone.washi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.webkit.JavascriptInterface;

import static android.content.Context.MODE_PRIVATE;

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
    public void setLogininfo(String id, String password) {

        SharedPreferences pref = mainActivity.getSharedPreferences("logininfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("id",id);
        editor.putString("pwd",password);
        editor.commit();


    }
    @JavascriptInterface
    public void printexcute(String txt) {

        mainActivity.print_excute(txt);

    }

    @JavascriptInterface
    public void setlogout() {
        //   Toast.makeText(mainActivity.getApplicationContext(),"logout",Toast.LENGTH_LONG).show();
        SharedPreferences pref = mainActivity.getSharedPreferences("logininfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

}
