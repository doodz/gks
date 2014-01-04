package com.gks2.app;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotifyManager {
	final int notifyID = 1;
	NotificationManager mNotifyManager;
	NotificationCompat.Builder mBuilder;
	
	public NotifyManager(){}
	public void NotifyDownloadComplete(){
		 // When the loop is finished, updates the notification
        mBuilder.setContentText("Download complete")
        // Removes the progress bar
                .setProgress(0,0,false);
        mNotifyManager.notify(notifyID, mBuilder.build());
	}
	
	public void NotifyDownload(Activity a){
		mNotifyManager = (NotificationManager) a.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(a);
		mBuilder.setContentTitle("Torrent Download")
		    .setContentText("Download in progress")
		    .setSmallIcon( android.R.drawable.ic_notification_overlay);
	}
	
}
