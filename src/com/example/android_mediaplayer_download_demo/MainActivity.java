package com.example.android_mediaplayer_download_demo;

import com.example.android_mediaplayer_download_demo.util.VoiceUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

    private static final String PATH1    = "http://192.168.0.247:8080/MyLogin/abc.mp3";
    private static final String PATH2    = "http://192.168.0.247:8080/MyLogin/cde.mp3";
    private static final String PATH3    = "http://192.168.0.247:8080/MyLogin/ggg.mp3";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
    }


    @Override
    public void onClick(View v){
        switch (v.getId ()) {
            case R.id.download_btn1:
            	VoiceUtils.getInstance().playMusic(this, R.id.download_btn1,PATH1,false);
                break;
            case R.id.download_btn2:
            	VoiceUtils.getInstance().playMusic(this,R.id.download_btn2,PATH2,false);
                break;
            case R.id.download_btn3:
            	VoiceUtils.getInstance().playMusic(this,R.id.download_btn3,PATH3,false);
                break;
            case R.id.stop_btn:
            	VoiceUtils.getInstance().stopVoice(R.id.download_btn1);
            	VoiceUtils.getInstance().stopVoice(R.id.download_btn2);
            	VoiceUtils.getInstance().stopVoice(R.id.download_btn3);
            	break;
        }
    }
   
    
}
