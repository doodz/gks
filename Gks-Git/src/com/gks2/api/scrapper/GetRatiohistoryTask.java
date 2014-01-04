package com.gks2.api.scrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.util.Log;


public class GetRatiohistoryTask extends HttpAsyncTask<String , List<DataRatioEntry>>{
	public final static String RatiohistoryUrl = "https://gks.gs/ajax.php?action=genjs&ratiohistory=%s&ak=%s";
	//public static final Pattern FindPattern = Pattern.compile("(added|ul|dl|aura): \"?(\\d*)\"?,",Pattern.CASE_INSENSITIVE);
	public static final Pattern FindPattern = Pattern.compile("added: \"?(\\d*)\"?,ul: (\\d*),dl: (\\d*),aura: (\\d*),",Pattern.CASE_INSENSITIVE);
	@Override
	protected List<DataRatioEntry> doInBackground(String... params) {
		String forgedUrl = buildUrl(profile);
		Log.v(this.getClass().getSimpleName(), String.format("begin (%s)", forgedUrl));
		HttpGet searchRequest = new HttpGet(forgedUrl);
    	HttpResponse searchResponse = browser.execute(searchRequest);
    	String str = getStringContent(searchResponse);
    	//Document searchWebPage = Jsoup.parse(str);
    	
    	//HeaderFooterScrapper.readPage(searchWebPage, profile);
    	
    	//Elements resultEntries = searchWebPage.select("pre");
    	
    	List<DataRatioEntry> result = this.parse(str);
    	
    	Log.v(this.getClass().getSimpleName(), String.format("finish (%d results)", result.size()));
		return result;
	}
	
	public static String buildUrl(UserProfile user) {
				
		return String.format(RatiohistoryUrl, user.userid, user.authKey);
	}
	
	public List<DataRatioEntry> parse(String s){
		final Matcher m = FindPattern.matcher(s);
		List<DataRatioEntry> lst =  new ArrayList<DataRatioEntry>();
		while(m.find()) {
			DataRatioEntry data = new DataRatioEntry();
			data.added =  Integer.parseInt(m.group(1));
			data.ul = Integer.parseInt(m.group(2));
			data.dl =  Integer.parseInt(m.group(3));
			data.aura=  Integer.parseInt(m.group(4));
			lst.add(data);
			
		}
		//throw new Error(String.format("Impossible d'extraire les valeurs '%s'", s));
		return lst;
	}

}
