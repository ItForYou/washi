package co.kr.itforone.washi;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
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
        return false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {

        super.onPageFinished(view, url);
    }
}
