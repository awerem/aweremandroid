package com.github.awerem.aweremandroid.web;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class RemoteWebClient extends WebViewClient
{
    private String ip;

    public RemoteWebClient(String ip)
    {
        super();
        this.ip = ip;
    }
    
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url)
    {
        if (Uri.parse(url).getHost().equals(this.ip))
        {
            return false;
        }
        else
        {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(intent);
            return true;

        }
    }
}
