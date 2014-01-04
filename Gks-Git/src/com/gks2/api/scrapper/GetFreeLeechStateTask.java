package com.gks2.api.scrapper;

import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;
import android.webkit.DateSorter;

public class GetFreeLeechStateTask extends HttpAsyncTask<String, String[]>{
	
	public final static String rulesUrl = "https://gks.gs/rules";
	
	@Override
	protected String[] doInBackground(String... params) {
		
		Log.v(this.getClass().getSimpleName(), String.format("begin (%s)", rulesUrl));
		final HttpGet httpRequest = new HttpGet(rulesUrl);
		final HttpResponse httpResp = browser.execute(httpRequest);	 
		final String content = getStringContent(httpResp);
		final Document htmlResponse = Jsoup.parse(content);
		Element resultEntrie = htmlResponse.select("li.fl-activated").first();
		
		if(resultEntrie != null) return parse(resultEntrie);
		
		return null;
	}
	
	protected String[] parse(Element p){
		String datestr = "";
		String flstr = "";
		String datestr2 = "";
		try{
		Elements el = p.getAllElements();
		Element typefl = el.get(0);
		flstr = typefl.text(); 
		Element e = typefl.select("span#countdown_fl").first();
		String timeStamp = e.attr("data-time");

		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(Long.parseLong( timeStamp)* 1000);
		Date date = cal.getTime();
		datestr = date.toString();
		datestr2 = ""+(Long.parseLong( timeStamp)* 1000);
		}catch(Exception e){ e.printStackTrace();}
		finally{
			
			return new String[]{flstr,datestr2};
			
		}
	}
}
