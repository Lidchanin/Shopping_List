package com.lidchanin.crudindiploma.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lidchanin.crudindiploma.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<String,Integer,String> {

    private Context context;
    private ProgressBar progressBar;
    private String fileName;
    private PowerManager.WakeLock wakeLock;
    public DownloadTask (Context context,ProgressBar progressBar, String fileName){
        this.context = context;
        this.fileName= fileName;
        this.progressBar = progressBar;
    }

    @Override
    protected String doInBackground(String... url) {
        InputStream inputStream=null;
        OutputStream outputStream = null;
        HttpURLConnection connection=null;
        try {
            URL mUrl = new URL(url[0]);
            connection = (HttpURLConnection) mUrl.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            int fileLength = connection.getContentLength();

            inputStream = connection.getInputStream();
            final File folder = new File ( Environment.getExternalStorageDirectory()+Constants.Tessaract.SLASH + Constants.Tessaract.TESSDATA);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            final File outFile = new File(Environment.getExternalStorageDirectory()+Constants.Tessaract.SLASH+Constants.Tessaract.TESSDATA,fileName);
            outFile.createNewFile();
            outputStream = new FileOutputStream(outFile);
            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = inputStream.read(data)) != -1) {
                if (isCancelled()) {
                    inputStream.close();
                    return null;
                }
                total += count;
                if (fileLength > 0)
                    publishProgress((int) (total * 100 / fileLength));
                outputStream.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        wakeLock.acquire();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(false);
        progressBar.setMax(100);
        progressBar.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        wakeLock.release();
        if (result != null)
            Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
    }


}