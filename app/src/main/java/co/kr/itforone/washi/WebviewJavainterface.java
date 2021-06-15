package co.kr.itforone.washi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.MediaStore;
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

        Intent notiservice_stop = new Intent(mainActivity,Notiservice.class);
        notiservice_stop.putExtra("flg", mainActivity.StopForegroundService);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mainActivity.startForegroundService(notiservice_stop);
        }
        else {
            mainActivity.startService(notiservice_stop);
        }

    }

    @JavascriptInterface
    public void startCamera() {

        Intent captureIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        mainActivity.startActivity(captureIntent);

    }

}
