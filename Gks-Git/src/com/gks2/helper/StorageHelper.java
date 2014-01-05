package com.gks2.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class StorageHelper {

	public static String IMG_AVATAR_NAME = "avatar.png";
	
	public static String saveToInternalSorage(Bitmap bitmapImage, Context appContext){
		
        ContextWrapper cw = new ContextWrapper(appContext);
        // path to /data/data/gks/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,IMG_AVATAR_NAME);
        FileOutputStream fos = null;
        try {           

            fos = new FileOutputStream(mypath);
       // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }
	
	public static Bitmap loadImageFromStorage(Context appContext)
	{

	    try {
	    	 ContextWrapper cw = new ContextWrapper(appContext);
	    	// path to /data/data/gks/app_data/imageDir
	        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
	        File f=new File(directory, IMG_AVATAR_NAME);
	        Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
	        return b;
	    } 
	    catch (FileNotFoundException e) 
	    {
	        e.printStackTrace();
	    }
	    return null;

	}
	
}
