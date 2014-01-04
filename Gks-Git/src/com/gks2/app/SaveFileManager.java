package com.gks2.app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

public class SaveFileManager {
	
	public static final int MESSAGE_DOWNLOAD_STARTED 		= 1000;
	public static final int MESSAGE_SAVE_COMPLETE 			= 1001;
	public static final int MESSAGE_UPDATE_PROGRESS_BAR 	= 1002;
	public static final int MESSAGE_DOWNLOAD_CANCELED 		= 1003;
	public static final int MESSAGE_CONNECTING_STARTED 		= 1004;
	public static final int MESSAGE_ENCOUNTERED_ERROR 		= 1005;
	public static final int DOWNLOAD_BUFFER_SIZE 			= 4096;
	private static Pattern numberNameTorrent 				= Pattern.compile("get/\\d+/(.*)");
	
	BufferedOutputStream outStream 	= null;
	FileOutputStream fileStream  	= null;
	BufferedInputStream inStream 	= null;
	InputStream input;
	byte[] torrentFileContent;
	int id = 0;
	android.content.Context context = null;
	
	public SaveFileManager(byte[] inputS){ torrentFileContent = inputS;}
	
	public int Save(String torrentNameUrl){
		
		try{
			
			String torrentName = "Mytorrent.torrent";
			Matcher m = numberNameTorrent.matcher(torrentNameUrl);
			if (m.find()) torrentName = m.group(1);
			
			String Filepath = PreferenceManager.getDefaultSharedPreferences(context).getString("pref_save_path", 
					Environment.getExternalStorageDirectory() + "/Download/");
			
			File file = new File(Filepath + torrentName);
			 try {
	                if (file.createNewFile()) {
	                    OutputStream fo = new FileOutputStream(file);
	                    fo.write(torrentFileContent);
	                    fo.close();
	                    Intent i = new Intent();
	                    
	                    i.setAction(android.content.Intent.ACTION_VIEW);
	                    i.setDataAndType(Uri.fromFile(file), MimeTypeMap.getSingleton().getMimeTypeFromExtension("torrent"));
	                    PendingIntent pI = PendingIntent.getActivity(context, 0, i, Intent.FLAG_ACTIVITY_NEW_TASK | PendingIntent.FLAG_UPDATE_CURRENT);
	                    doNotify(R.drawable.ic_torrent_download, torrentName, context.getString(R.string.user_message_download_complete) , Integer.valueOf(id), pI);
	                }
	            } catch (IOException e) {
	                Intent i = new Intent();
	                i.setClass(context, SettingsActivity.class);
	                PendingIntent pI = PendingIntent.getActivity(context, 0, i, Intent.FLAG_ACTIVITY_NEW_TASK | PendingIntent.FLAG_UPDATE_CURRENT);
	                doNotify(R.drawable.ic_torrent_download, torrentName, "Le téléchargement a échoué...\nAccès au répertoire choisi impossible.", Integer.valueOf(id), pI);
	            } catch (Exception e) {
	                Intent i = new Intent();
	                i.setClass(context, null);
	                PendingIntent pI = PendingIntent.getActivity(context, 0, i, Intent.FLAG_ACTIVITY_NEW_TASK | PendingIntent.FLAG_UPDATE_CURRENT);
	                if (file.exists() && file.length() == 0) {
	                    doNotify(R.drawable.ic_torrent_download, torrentName, "Le téléchargement a échoué...\nErreur réseau : impossible de télécharger le contenu du fichier.", Integer.valueOf(id), pI);
	                } else if (!file.exists()) {
	                    doNotify(R.drawable.ic_torrent_download, torrentName, "Le téléchargement a échoué...\nImpossible de créer le fichier.", Integer.valueOf(id), pI);
	                } else {
	                    doNotify(R.drawable.ic_torrent_download, torrentName, "Le téléchargement a échoué...\nErreur inconnue.", Integer.valueOf(id), pI);
	                }
	            }
			return MESSAGE_SAVE_COMPLETE;
		}
		catch(Exception e){
			e.printStackTrace();
			}
		return 0;
	}
	
	public void doNotify(int icon, String title, String subtitle, int id, PendingIntent pendingIntent) {
		
        try {
        	
            if (pendingIntent == null)
                pendingIntent = PendingIntent.getActivity(context, 0, new Intent(), 0);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(icon)
                            .setContentTitle(title)
                            .setContentText(subtitle);
            
            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setAutoCancel(true);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(id, mBuilder.build());

        } finally {
            try {
                Toast.makeText(this.context, subtitle, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
            }
        }
    }
	
	
}
