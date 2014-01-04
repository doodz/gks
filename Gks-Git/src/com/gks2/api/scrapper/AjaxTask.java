package com.gks2.api.scrapper;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;

import android.net.Uri;
import android.util.Log;

public class AjaxTask  extends HttpAsyncTask<AjaxRequest, Integer> {
	
	public final static String AjaxUrl = "https://gks.gs/ajax.php";
	public final static int SUCCES_BOOKMARK_CODE = 2;
	public final static int ERROR_BOOKMARK_CODE = 4;
	public final static int SUCCES_AUTOGET_CODE = 8;
	public final static int ERROR_AUTOGET_CODE = 12;
	
	@Override
	protected Integer doInBackground(AjaxRequest... params) {
		
		String forgedUrl = buildUrl(params[0]);
		Log.v(this.getClass().getSimpleName(), String.format("begin (%s)", forgedUrl));
		HttpGet searchRequest = new HttpGet(forgedUrl);
    	HttpResponse searchResponse = browser.execute(searchRequest);
    	StatusLine stl =  searchResponse.getStatusLine();
    	if(stl != null){
    		
    		if(stl.getStatusCode() == 200)
    			if(params[0].type.equals(SortTypeAjax.autoget))
    				return SUCCES_AUTOGET_CODE;
    			else
    				return SUCCES_BOOKMARK_CODE;
    		else
    		{
    			String str = stl.toString();
    			if(params[0].type.equals(SortTypeAjax.autoget))
    				return ERROR_AUTOGET_CODE;
    			else
    			return ERROR_BOOKMARK_CODE;
    		}
    	}
    	
		return ERROR_BOOKMARK_CODE;
	}

	public static String buildUrl(AjaxRequest request) {
		String uri = Uri.parse(AjaxUrl)
                .buildUpon()
                .appendQueryParameter("action", request.action.toString())
                .appendQueryParameter("type", request.type.toString())
                .appendQueryParameter("tid", ""+request.tid)
                .build().toString();
		return uri;
	}
}
