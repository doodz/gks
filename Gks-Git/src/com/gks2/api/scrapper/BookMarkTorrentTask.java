package com.gks2.api.scrapper;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;

import android.util.Log;

public class BookMarkTorrentTask  extends HttpAsyncTask<String, Integer>{
	
	public final static String BookMarkUrl = "https://gks.gs/ajax.php?action=add&type=booktorrent&tid=%s";
	public final static int SUCCES_BOOKMARK_CODE = 2;
	public final static int ERROR_BOOKMARK_CODE = 4;
	
	@Override
	protected Integer doInBackground(String... params) {
		
		String forgedUrl = buildUrl(params[0]);
		Log.v(this.getClass().getSimpleName(), String.format("begin (%s)", forgedUrl));
		HttpGet searchRequest = new HttpGet(forgedUrl);
    	HttpResponse searchResponse = browser.execute(searchRequest);
    	StatusLine stl =  searchResponse.getStatusLine();
    	if(stl != null){
    		if(stl.toString().equals("200 OK"))
    			return SUCCES_BOOKMARK_CODE;
    		else
    		{
    			String str = stl.toString();
    			str = str+"1";
    			return ERROR_BOOKMARK_CODE;
    		}
    	}
    	
		return ERROR_BOOKMARK_CODE;
	}
	
	public static String buildUrl(String tid) {
		
		return String.format(BookMarkUrl, tid);
	}
}
