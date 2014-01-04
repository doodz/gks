package com.gks2.api.scrapper;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

public class GetChartRatioTask  extends HttpAsyncTask<String , String>{

	public final static String ChartRatioUrl = "https://gks.gs/m/account/ratio";
	public final static String RatiohistoryUrl = "https://gks.gs/ajax.php?action=genjs&ratiohistory=%s&ak=%s";
	
	@Override
	protected String doInBackground(String... params) {
		
		String forgedUrl = buildUrl(profile);
		Log.v(this.getClass().getSimpleName(), String.format("begin (%s)", forgedUrl));
		HttpGet searchRequest = new HttpGet(forgedUrl);
    	HttpResponse searchResponse = browser.execute(searchRequest);
    	String str = getStringContent(searchResponse);
    	String scripts = "<script src=\"file:///android_asset/amstock.js?v=11\" type=\"text/javascript\"></script>"
    			+ "<script type=\"text/javascript\">"+ str +"</script>";
		String pagecontent = "<html><head>"
                + scripts
                + "</head><body>"
                + "<div id=\"chartratio\" style=\"width: 100%; height: 400px; overflow: hidden;\"/></div>"
                + "</body></html>";

    	return pagecontent;
	}
	
	public static String buildUrl(UserProfile user) {
		
		return String.format(RatiohistoryUrl, user.userid, user.authKey);
	}
	
}
