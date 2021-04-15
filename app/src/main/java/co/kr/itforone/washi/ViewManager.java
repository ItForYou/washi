package co.kr.itforone.washi;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ViewManager extends WebViewClient {
    MainActivity mainActivity;
    Activity activity;

    public ViewManager(MainActivity mainActivity, Activity activity) {
        this.mainActivity = mainActivity;
        this.activity  = activity;
    }
    public ViewManager(Activity activity) {
        this.activity  = activity;
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if(!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("javascript:"))  {

            Log.d("package_intent_url",url);

            try {
                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                Log.d("package_intent_1",intent.getScheme());
                Intent existPackage = activity.getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                if (existPackage != null) {
                    activity.startActivity(intent);
                } else {
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                    marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
                    Log.d("package_intent_2",intent.getScheme());
                    activity.startActivity(marketIntent);
                }


                return true;
            } catch (Exception e) {
                Log.d("error1",e.toString());
                e.printStackTrace();
            }

        }
        else {
            view.loadUrl(url);
        }


        mainActivity.progressBar.setVisibility(View.VISIBLE);
        mainActivity.progressBar.setIndeterminate(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mainActivity.progressBar.getIndeterminateDrawable().setColorFilter(mainActivity.getColor(R.color.blue), PorterDuff.Mode.MULTIPLY);
        }
        else{
            mainActivity.progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#0091ff"), PorterDuff.Mode.MULTIPLY);
        }

        return false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.d("pushurl4",mainActivity.pushurl
        );
        view.loadUrl("javascript:setToken('"+mainActivity.token+"')");
        if(!mainActivity.pushurl.isEmpty() && !mainActivity.pushurl.equals("")){
            view.loadUrl(mainActivity.pushurl);
            mainActivity.pushurl="";
        }
        mainActivity.progressBar.setVisibility(View.GONE);
    }
}
