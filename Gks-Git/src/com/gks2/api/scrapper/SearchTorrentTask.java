package com.gks2.api.scrapper;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.DownloadManager.Request;
import android.net.Uri;
import android.util.Log;

public class SearchTorrentTask extends HttpAsyncTask<SearchTorrentRequest, List<SearchEntry>> {
	
	public final static String searchUrl = "https://gks.gs/sphinx/";
	public final static String  browseUrl =  "https://gks.gs/browse/";
	
	private static Pattern numberExtract = Pattern.compile("\\d+");

	@Override
	protected List<SearchEntry> doInBackground(SearchTorrentRequest... params) {
		String forgedUrl = buildUrl(params[0]);
		Log.v(this.getClass().getSimpleName(), String.format("begin (%s)", forgedUrl));
		HttpGet searchRequest = new HttpGet(forgedUrl);
    	HttpResponse searchResponse = browser.execute(searchRequest);
    	Document searchWebPage = Jsoup.parse(getStringContent(searchResponse));
    	
    	//HeaderFooterScrapper.readPage(searchWebPage, profile);
    	
    	Elements resultEntries = searchWebPage.select("table#torrent_list>tbody>tr");
    	List<SearchEntry> result = new ArrayList<SearchEntry>(resultEntries.size());
    	for(int i =1;i < resultEntries.size();i+=2){
    		result.add(parse(resultEntries.get(i)));
    	}
    	
    	Log.v(this.getClass().getSimpleName(), String.format("finish (%d results)", result.size()));
		return result;
	}
	
	protected SearchEntry parse(Element tr) {
		SearchEntry result = new SearchEntry();
		
		Iterator<Element> colIt = tr.children().iterator(); // Iterator over the columns of the input
		Matcher m = numberExtract.matcher(colIt.next().child(0).attr("href")); 
		result.type = m.find()?m.group():"";
		
		Element columnNom = colIt.next();
		Element LincName = columnNom.select("a").first();
		result.prezLocaion = LincName.attr("href");
		result.title =LincName.attr("title");
		Elements imgElements =  columnNom.select("img");
		for(int i = 1; i < imgElements.size();i++){
			if(imgElements.get(i).attr("alt").equals("FreeLeech"))
				result.FreeLeech = true;
		}

		Element myNext = colIt.next();
		result.dlLocation = myNext.child(0).attr("href");
		m = numberExtract.matcher(result.dlLocation);
		if (m.find()) result.id = Integer.parseInt(m.group());
		
		myNext = colIt.next();
		result.qtComments = Integer.parseInt(myNext.text());
		myNext = colIt.next();
		result.postSize = DataSize.parseSize(myNext.text());
		myNext = colIt.next();
		result.qtCompleteDownload = Integer.parseInt(myNext.text());
		myNext = colIt.next();
		result.qtSeeders = Integer.parseInt(myNext.text());
		myNext = colIt.next();
		result.qtLeechers = Integer.parseInt(myNext.text());
		
		return result;
	}
	
	public static String buildUrl(SearchTorrentRequest request) {
		String uri;
		if(request.request == null || request.request.isEmpty())
			uri = Uri.parse(browseUrl)
            .buildUpon()
            .appendQueryParameter("order", request.sortRequest.field)
            .appendQueryParameter("page", ""+request.targetedPageResult)
            .appendQueryParameter("category", request.categorie)
            .build().toString();
		else
			uri = Uri.parse(searchUrl)
                .buildUpon()
                .appendQueryParameter("q", request.request)
                .appendQueryParameter("order", request.sortRequest.field)
                .appendQueryParameter("page", ""+request.targetedPageResult)
                .appendQueryParameter("category", request.categorie)
                .build().toString();
		return uri;
	}

}
