package com.gks2.api.scrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.util.Log;

import com.gks2.app.NotifyManager;
import com.gks2.app.SaveFileManager;

public class DownloadTorrentTask extends HttpAsyncTask<String, byte[]> {
	public static final String downloadUrl = "https://gks.gs";
	public static final int MESSAGE_DOWNLOAD_COMPLETE = 1001;
	
	
	@Override
	protected byte[] doInBackground(String... params) {
		
		
		String forgedUrl = params[0];
		try {
			URI uri = new URI(downloadUrl+forgedUrl);
			//HttpGet dlRequest = new HttpGet(downloadUrl+forgedUrl);
			HttpGet dlRequest = new HttpGet(uri);
			HttpResponse dlResponse = browser.execute(dlRequest);
			InputStream input = getUngzipedInputStream(dlResponse);
			
			Log.v("DownloadTorrentTask", "getUngzipedInputStream from "+dlRequest.getURI());
			return convertInputStreamToByteArray(input);
			//return MESSAGE_DOWNLOAD_COMPLETE;
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		Log.v("DownloadTorrentTask", "finished MESSAGE_DOWNLOAD_CANCELED");
		return null;
	}
	
	public byte[] convertInputStreamToByteArray(InputStream inputStream)
	 {
	 byte[] bytes= null;
	 
	 try
	 {
	 ByteArrayOutputStream bos = new ByteArrayOutputStream();
	 
	 byte data[] = new byte[1024];
	 int count;
	 
	 while ((count = inputStream.read(data)) != -1)
	 {
	 bos.write(data, 0, count);
	 }
	 
	bos.flush();
	 bos.close();
	 inputStream.close();
	 
	bytes = bos.toByteArray();
	 }
	 catch (IOException e)
	 {
	 e.printStackTrace();
	 }
	 return bytes;
	 }
}
