package com.example.android_mediaplayer_download_demo.service;

import com.example.android_mediaplayer_download_demo.util.DownloadAsyncTask;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

public class DownLoadService extends Service {

    private String mDownloadUrl;
    private boolean justdown;
    private int id ;
    private String mFileName;

    @Override
    public void onCreate(){
        super.onCreate ();
    }

    @Override
    public void onDestroy(){
        super.onDestroy ();
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        Bundle bundle = intent.getExtras ();
        if (bundle != null) {
            mDownloadUrl = bundle.getString ("downloadUrl");
            justdown = bundle.getBoolean("justdown");
            id = bundle.getInt("id");
            addTask (id);
        }
        return super.onStartCommand (intent, flags, startId);
    }

    private void addTask(int startId){
        DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask (this,
                setFileName (),justdown);
        downloadAsyncTask.execute (mDownloadUrl, startId);
    }

    @Override
    public IBinder onBind(Intent arg0){
        // TODO Auto-generated method stub
        return null;
    }

    private String setFileName(){
        Uri uri = Uri.parse (mDownloadUrl);
        mFileName = uri.getLastPathSegment ();
        return mFileName;
    }

}
