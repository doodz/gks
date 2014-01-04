package com.gks2.api.scrapper;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class GetTorrentDetailTask extends HttpAsyncTask<String, TorrentDetailEntry> {
	
	public final static String searchUrl = "https://gks.gs";
	private static Pattern categoryExtract = Pattern.compile("cat=(\\d+)");
	private TorrentDetailEntry Entry;
	@Override
	protected TorrentDetailEntry doInBackground(String... params) {
		
		String forgedUrl = searchUrl+params[0];
		Log.v(this.getClass().getSimpleName(), String.format("get Detail (%s)", forgedUrl));
		HttpGet searchRequest = new HttpGet(forgedUrl);
    	HttpResponse searchResponse = browser.execute(searchRequest);
    	Document searchWebPage = Jsoup.parse(getStringContent(searchResponse));
    	Element resultEntries = searchWebPage.select("div#contenu").first();
    	Entry = new TorrentDetailEntry();
    	try{resultEntries = parse(resultEntries);}catch(Exception e){ e.printStackTrace();}
    	return Entry;
    	/*if(resultEntries != null)
    		return resultEntries.toString();
    	else 
    		return "<meta name=\"viewport\" content=\"width=320; user-scalable=no\" />Erreur lors de la récupération des données...";
    	 */
	}
	
	protected Element parse(Element el) {

		
		Elements divs = el.children().select("div");
		
		//Element torrent = el.select("div#torrent").first();
		//Elements divs = torrent.select("div");
		
		
		
		Element head_bloc = divs.get(2);
		Element notice_head = divs.get(3);
		Element dl_info = divs.get(4);
		Element dl = divs.get(5);
		Element share = divs.get(6);
		Element prez = divs.get(7);
		Element infos = divs.get(8);
		
		Element nfo = el.children().select("textarea").first(); 
		if(nfo != null)
			this.Entry.Nfo = nfo.text();
		this.Entry.Prez = prez.toString();
		
		return prez;
	}

}
