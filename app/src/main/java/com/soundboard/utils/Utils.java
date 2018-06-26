package com.soundboard.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Utils {
    public static Uri resourceToUri(Context context, String resName, String resExtension, int resID) {
        File file = new File(Environment.getExternalStorageDirectory() + "/Temp/" + resName + "." + resExtension);
        File directory = new File(Environment.getExternalStorageDirectory() + "/Temp/");
        try{
            directory.mkdirs();
            file.createNewFile();
            InputStream in = context.getResources().openRawResource(resID);
            FileOutputStream out = new FileOutputStream(file);
            byte[] buff = new byte[1024];
            int read = 0;
            try {
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            } finally {
                in.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Uri.fromFile(file);
    }

    public static String capitalizeString(String s){
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static int ObjectPosition(Object[] arrayToSearch, Object objectToSearch)
    {
        for (int i=0; i<arrayToSearch.length; i++) {
            if(arrayToSearch[i].equals(objectToSearch))
                return i;
        }

        return -1;
    }

    public static int getDrawableIdByName(Context context, String name){
        Resources resources = context.getResources();
        return resources.getIdentifier(name.toLowerCase(), "drawable", context.getPackageName());
    }

    public static Drawable getDrawableByName(Context context, String name){
        return ContextCompat.getDrawable(context, getDrawableIdByName(context, name));
    }
}
