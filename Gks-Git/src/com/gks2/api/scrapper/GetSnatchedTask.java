package com.gks2.api.scrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class GetSnatchedTask extends HttpAsyncTask<String, List<SnatchedEntry>> {

	public final static String snatchedUrl = "https://gks.gs/m/peers/snatched";
	
	@Override
	protected List<SnatchedEntry> doInBackground(String... params) {

		Log.v(this.getClass().getSimpleName(), String.format("begin (%s)", snatchedUrl));
		HttpGet searchRequest = new HttpGet(snatchedUrl);
    	HttpResponse searchResponse = browser.execute(searchRequest);
    	Document searchWebPage = Jsoup.parse(getStringContent(searchResponse));
    	
    	//HeaderFooterScrapper.readPage(searchWebPage, profile);
    	
    	Elements resultEntries = searchWebPage.select("tbody>tr");
    	List<SnatchedEntry> result = new ArrayList<SnatchedEntry>(resultEntries.size());
    	for(int i = 1; i < resultEntries.size(); i += 1){
    		
    		result.add(parse(resultEntries.get(i)));
    	}
    	
    	Log.v(this.getClass().getSimpleName(), String.format("finish (%d results)", result.size()));
		return result;
	}
	protected SnatchedEntry parse(Element tr) {
		SnatchedEntry result = new SnatchedEntry();
		
		Iterator<Element> colIt = tr.children().iterator();
		
		Element column = colIt.next();//Column name
		if(!column.text().equalsIgnoreCase("Torrent Supprimé"))
		{
			result.prezLocaion = column.child(0).attr("href");
			result.title = column.child(0).text();
		}
		else
		{
			result.title = "Torrent Supprimé";
		}
		
		column = colIt.next();//Column Seeding
		result.seeding = column.child(0).attr("alt");
		
		column = colIt.next();//Column UL
		result.qtUl = DataSize.parseSize(column.text());
		
		column = colIt.next();//Column DL
		result.qtDl = DataSize.parseSize(column.text());
		
		column = colIt.next();//Column Real DL
		result.qtRealDl = DataSize.parseSize(column.text());
		
		column = colIt.next();//Column Seed Time
		result.seedTime = column.text();
		
		column = colIt.next();//Column Ratio
		result.ratio = column.child(0).text();
		
		column = colIt.next();//Column Dl torrent
		if(!result.title.equalsIgnoreCase("Torrent Supprimé"))
		result.dlLocation = column.child(0).text();
		
		return result;
	}
}
