package com.example.android_mediaplayer_download_demo.util;

import android.R.integer;


public abstract interface MyPlayerListener {
	public abstract void onCompletion(int id,int position);
	public abstract void onError(int id);
	public abstract void onPause(int id);
	public abstract void onStop(int id);
	public abstract void onStart(int id);
	public abstract void onUpDate(int percent,int id);
}
