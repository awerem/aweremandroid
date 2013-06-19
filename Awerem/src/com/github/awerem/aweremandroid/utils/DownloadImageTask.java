package com.github.awerem.aweremandroid.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
{
    private static final String DEBUG_TAG = "DownloadImageTask";
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage)
    {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls)
    {
        Bitmap bitmap = null;
        String urldisplay = urls[0];
        Log.d("Download", urldisplay);
        try
        {
            URL url = new URL(urldisplay);
            URLConnection connection = url.openConnection();
            connection.setUseCaches(true);
            bitmap = BitmapFactory.decodeStream((InputStream) connection.getContent());
        }
        catch (FileNotFoundException e)
        {
            Log.w(DEBUG_TAG, "file not found at " + urldisplay);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result)
    {
        bmImage.setImageBitmap(result);
    }
}