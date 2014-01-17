package com.github.awerem.aweremandroid.web;

import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;

public class RemoteWebChrome extends WebChromeClient
{
    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage)
    {
        Log.i("RemoteWebChrome",
                consoleMessage.messageLevel().toString() + ":"
                        + consoleMessage.message() + " at "
                        + consoleMessage.lineNumber() + " from "
                        + consoleMessage.sourceId());
        return super.onConsoleMessage(consoleMessage);
    }
}
