package com.example.android_mediaplayer_download_demo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.HttpStatus;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadAsyncTask extends AsyncTask<Object, Object, Long> implements MyPlayerListener{

//    private Context mContext;
	private boolean justDown;
    private String  mFileName;
    private String  mDownLoadUrl;
    private int     mStartId;
    private File    mDownloadDir;
    private File    mDownloadFile;
    int total_kb;
    int current_kb;

    public DownloadAsyncTask(Context context, String fileName,boolean justDown) {
//      this.mContext = context;
        this.mFileName = fileName;
        this.justDown = justDown;
    }

    @Override
    protected Long doInBackground(Object... params){
        getDataFromService (params);
        downLoadFile ();
        return null;
    }

    private void downLoadFile(){
        if (mDownLoadUrl != null && mDownloadDir != null&& mDownloadFile != null) {
            InputStream inputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
            	HttpURLConnection conn = (HttpURLConnection) new URL(mDownLoadUrl).openConnection();
                conn.setConnectTimeout(5*1000);
                conn.setRequestMethod("GET");
                conn.connect();
            	if (conn.getResponseCode()==HttpStatus.SC_OK) {
                    fileOutputStream = new FileOutputStream (mDownloadFile,false);
                    inputStream = conn.getInputStream();
                    byte[] buff = new byte[1024];
                    int len = 0;
                    total_kb = conn.getContentLength();
                    current_kb = 0;
                    boolean flag = true;
                    while ((len = inputStream.read (buff)) > 0) {
                        fileOutputStream.write (buff, 0, len);
                        current_kb +=len;
                        if (!justDown&&flag&&current_kb>=200*1024) {
                        	flag = false;
                        	startPlayer();
						}
                    }
                    fileOutputStream.flush ();
                    fileOutputStream.close ();
                    if (currentPosition<total_kb) {
                    	currentPosition = VoiceUtils.getInstance().getCurrentPosition();
                    	startPlayer();
					}
                }
                Log.e("完成了：",mDownloadFile.getAbsolutePath()+"-----"+mDownloadFile.getName());
            } catch (Exception e) {
            } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close ();
                        } catch (IOException e) {
                            e.printStackTrace ();
                        }
                    }
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.flush ();
                            fileOutputStream.close ();
                        } catch (Exception e2) {
                        }
                    }
            }
        }
    }

    private void getDataFromService(Object... params){
        mDownLoadUrl = (String) params[0];
        mStartId = (Integer) params[1];
    }

    @Override
    protected void onPreExecute(){
        mDownloadDir = new File (Utils.DOWNLOAD_DIR);
        mDownloadFile = new File (mDownloadDir,mFileName);
        if (mDownloadDir != null && !mDownloadDir.exists ()) {
            mDownloadDir.mkdirs ();
        }
        if (mDownloadFile != null && !mDownloadFile.exists ()) {
            try {
                mDownloadFile.createNewFile ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
        }
        super.onPreExecute ();
    }
    public void startPlayer(){
    	VoiceUtils.getInstance().startVoice(true,currentPosition, mDownloadFile, mStartId, this, null);
    }
    int currentPosition = 0;
	@Override
	public void onCompletion(int id,int position) {
		currentPosition = position;
		if (position<current_kb) {
			startPlayer();
			//TODO加载条开启
		}
	}

	@Override
	public void onError(int id) {
		
	}

	@Override
	public void onPause(int id) {
		
	}

	@Override
	public void onStop(int id) {
		
	}

	@Override
	public void onStart(int id) {
		//TODO加载条关闭
	}

	@Override
	public void onUpDate(int percent, int id) {
		
	}
//  public void startPlayer(){
//	if (mediaPlayer!=null) {
//		currentPosition = mediaPlayer.getCurrentPosition();
//	}else {
//		mediaPlayer = new MediaPlayer();
//	}
//	mediaPlayer.reset();
//	FileInputStream fis;
//	try {
//		fis = new FileInputStream(mDownloadFile);
//		mediaPlayer.setDataSource(fis.getFD());
//		mediaPlayer.prepareAsync();
//		mediaPlayer.setOnPreparedListener(this);
//		mediaPlayer.setOnCompletionListener(this);
//		mediaPlayer.setOnSeekCompleteListener(this);
//	} catch (FileNotFoundException e) {
//		e.printStackTrace();
//	} catch (IllegalArgumentException e) {
//		e.printStackTrace();
//	} catch (IllegalStateException e) {
//		e.printStackTrace();
//	} catch (IOException e) {
//		e.printStackTrace();
//	}
//}
}
