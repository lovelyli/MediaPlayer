package com.example.android_mediaplayer_download_demo.util;

import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;

public class Utils {

    public static final String SDCardDir    = Environment
                                                    .getExternalStorageDirectory ()
                                                    .getAbsolutePath ();
    public static final String DOWNLOAD_DIR = SDCardDir
                                                    + java.io.File.separator
                                                    + "MusicDownload";

    public static boolean idSDCardAviable(){
        boolean isaviable = false;
        String SDCardPath = "";
        StatFs statFs;
        if (Environment.getExternalStorageState ().equals (
                Environment.MEDIA_MOUNTED)) {
            SDCardPath = Environment.getExternalStorageDirectory ().getPath ();
            if (!SDCardPath.equals (null)) {
                statFs = new StatFs (SDCardPath);
                long SDFreeSize = (statFs.getAvailableBlocks () * statFs
                        .getBlockSize ()) / (1024 * 1024);
                if (SDFreeSize > 10) {
                    isaviable = true;
                }
            }
        }
        return isaviable;
    }
    public static String getFileName(String mDownloadUrl){
        Uri uri = Uri.parse (mDownloadUrl);
        return uri.getLastPathSegment ();
    }
}
