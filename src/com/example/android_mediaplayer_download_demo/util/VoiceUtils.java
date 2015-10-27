package com.example.android_mediaplayer_download_demo.util;


import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;

import com.example.android_mediaplayer_download_demo.service.DownLoadService;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

public class VoiceUtils implements OnCompletionListener,OnBufferingUpdateListener{
	public static MediaPlayer mPlayer;//唯一播放器
	private int id;
	private MyPlayerListener listener;
	private boolean isVedio;
	
	private static VoiceUtils utils;
	private VoiceUtils(){}
	public static VoiceUtils getInstance(){
		if (utils==null) {
			utils = new VoiceUtils();
		}
		return utils;
	}
	//播放本地文件
		public void startVoice(boolean isFileChange,final int seekposition,File file,final int id,final MyPlayerListener listener,SurfaceView surfaceView){
			if (this.id==id&&!isFileChange) {
				if (mPlayer!=null) {
					if (mPlayer.getCurrentPosition()>0) {
						mPlayer.start();
						if (listener!=null) 
							listener.onStart(id);
						return;
					}
				}
			}
			if (mPlayer!=null) {
				if (mPlayer.isPlaying()) {
					mPlayer.pause();
				}
			}
			relaseData();
			this.id = id;
			this.listener = listener;
			try {
				mPlayer = new MediaPlayer();
				mPlayer.reset();
				FileInputStream fis=new FileInputStream(file);  
				mPlayer.setDataSource(fis.getFD());  
				mPlayer.setLooping(false);// 设置不循环播放  
				if (surfaceView!=null) {
					mPlayer.setDisplay(surfaceView.getHolder());
					isVedio = true;
				}else {
					isVedio = false;
				}
				mPlayer.prepareAsync();
				mPlayer.setOnCompletionListener(this);
				mPlayer.setOnBufferingUpdateListener(this);
				mPlayer.setOnPreparedListener(new OnPreparedListener() {
					@Override
					public void onPrepared(MediaPlayer mp) {
						if (mp!=null) {
							if (seekposition==mp.getDuration()) {
								return;
							}
							if (seekposition!=0) {
								mp.seekTo(seekposition);
							}
							mp.start();
							if (listener!=null) 
								listener.onStart(id);
						}
					}
				});
			} catch (Exception e) {
				System.out.println("音乐播放异常");
				e.printStackTrace();
			}
		}
	//播放网络
	public void startVoice(String url,final int id,final MyPlayerListener listener,SurfaceView surfaceView){
		if (this.id==id) {
			if (mPlayer!=null) {
				if (mPlayer.getCurrentPosition()>0) {
					mPlayer.start();
					if (listener!=null) 
						listener.onStart(id);
					return;
				}
			}
		}
		if (mPlayer!=null) {
			if (mPlayer.isPlaying()) {
				mPlayer.pause();
			}
		}
		relaseData();
		this.id = id;
		this.listener = listener;
		try {
			mPlayer = new MediaPlayer();
			mPlayer.reset();
			mPlayer.setDataSource(url);
			mPlayer.setLooping(false);// 设置不循环播放  
			if (surfaceView!=null) {
				mPlayer.setDisplay(surfaceView.getHolder());
				isVedio = true;
			}else {
				isVedio = false;
			}
			mPlayer.prepareAsync();
			mPlayer.setOnCompletionListener(this);
			mPlayer.setOnBufferingUpdateListener(this);
			mPlayer.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					if (mp!=null) {
						mp.start();
						if (listener!=null) 
							listener.onStart(id);
					}
				}
			});
		} catch (Exception e) {
			System.out.println("音乐播放异常");
			e.printStackTrace();
		}
	}
	//暂停音乐
	public void pauseVoice(int id) {
		if (this.id==id) {
			if (mPlayer!=null) {
				if (mPlayer.isPlaying()) {
					mPlayer.pause();
					if (listener!=null)
						listener.onPause(id);
				}
			}
		}
	}
	//停止播放
	public void stopVoice(int id){
		if (this.id==id) {
			if (mPlayer!=null) {
				if (isVedio) {
					mPlayer.seekTo(0);
				}
				mPlayer.stop();
				mPlayer.release();
				mPlayer = null;
				if (listener!=null)
					listener.onStop(id);
			}
		}
	}
	public void relaseData(){
		if (mPlayer!=null) {
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
		}
	}
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		if (listener!=null)
			listener.onUpDate(percent,id);
	}
	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.e("onCompletion", "调用");
		int p = mp.getCurrentPosition();
		relaseData();
		if (listener!=null)
			listener.onCompletion(id,p);
	}
	public int getCurrentPosition(){
		if (mPlayer!=null) {
			return mPlayer.getCurrentPosition();
		}
		return 0;
	}
	Intent mIntent;
	 public void playMusic(Context context,int id,String url,boolean justdown) {
	    	String pathString = Utils.DOWNLOAD_DIR+java.io.File.separator+Utils.getFileName(url);
	    	File file = new File(pathString);
	    	if (!file.exists()) {
				 mIntent = new Intent (context,
	                     DownLoadService.class);
				 Bundle mBundle = new Bundle ();
	             mBundle.putString ("downloadUrl", url);
	             mBundle.putBoolean("justdown",justdown);
	             mBundle.putInt("id",id);
	             mIntent.putExtras (mBundle);
	             context.startService (mIntent);
			}else{
				startVoice(false,0,file,id,null,null);
			}
		}
}