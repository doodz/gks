package com.gks2.api.scrapper;

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

import android.net.Uri;
import android.util.Log;

public class BookMarkTask extends HttpAsyncTask<BookMarkRequest, List<List<BookMarkEntry>>> {
	
	public final static String BookMarkUrl = "https://gks.gs/bookmark/";
	private static Pattern numberExtract = Pattern.compile("\\d+");
	public final static int SUCCES_BOOKMARK_CODE = 2;
	public final static int ERROR_BOOKMARK_CODE = 4;
	
	@Override
	protected List<List<BookMarkEntry>> doInBackground(BookMarkRequest... params) {
		
		String forgedUrl = buildUrl(params[0]);
		Log.v(this.getClass().getSimpleName(), String.format("begin (%s)", forgedUrl));
		HttpGet searchRequest = new HttpGet(forgedUrl);
    	HttpResponse searchResponse = browser.execute(searchRequest);
    	Document BookMarkWebPage = Jsoup.parse(getStringContent(searchResponse));
    	
    	Elements resultEntries = BookMarkWebPage.select("div.list-wrap>ul");
    	List<List<BookMarkEntry>> returnLst = new ArrayList<List<BookMarkEntry>>(resultEntries.size());
    	Elements resultTabEntries;
    	String str;
    	
    	for(int i =0;i < resultEntries.size();i++){
    		
    		str = resultEntries.get(i).attr("id");
    		Log.v(this.getClass().getSimpleName(), String.format("fill entries  (%s)", str));
    		resultTabEntries = resultEntries.get(i).select("table#bookmark_list>tbody>tr");
        	List<BookMarkEntry> result = new ArrayList<BookMarkEntry>(resultTabEntries.size());
        	if(str.equals("torrent"))
	        	for(int j =0; j < resultTabEntries.size() - 1 ; j++ ){
	        		result.add(parseTorrent(resultTabEntries.get(j)));
	        	}
        	else if(str.equals("forum"))
        		for(int j =1; j < resultTabEntries.size() ; j++ ){
	        		result.add(parseForum(resultTabEntries.get(j)));
	        	}
        	else if(str.equals("wiki"))
    		for(int j =1; j < resultTabEntries.size() ; j++ ){
        		result.add(parseWiki(resultTabEntries.get(j)));
        	}
    		else
    			for(int j =1; j < resultTabEntries.size() ; j++ ){
            		result.add(parseRequest(resultTabEntries.get(j)));
            	}
        	
        	returnLst.add(result);
        	Log.v(this.getClass().getSimpleName(), String.format("finish bookmark %s (%d results)", str , result.size()));
    	}
    	
		return returnLst;
	}

	public static String buildUrl(BookMarkRequest request) {
		String uri = Uri.parse(BookMarkUrl)
                .buildUpon()
                .appendQueryParameter("tab", request.tab.toString())
                .build().toString();
		return uri;
	}
	
	protected BookMarkEntry parseTorrent(Element tr) {
		
		BookMarkEntry result = new BookMarkEntry();
		Matcher m = numberExtract.matcher(tr.attr("id"));
		result.IdBookMark = m.find() ? Integer.parseInt(m.group()) : -42;
		Iterator<Element> colIt = tr.children().iterator(); // Iterator over the columns of the input
		
		Element column = colIt.next(); //columnName
		result.Name = column.child(1).text();
		
		column = colIt.next(); //columnDateAdded
		result.DateAdded = column.text();
		
		column = colIt.next(); //columnSize
		result.Size = DataSize.parseSize(column.text());
		
		column = colIt.next(); //columnQtSeeder
		result.qtSeeders = Integer.parseInt(column.select("span.upload").text());
		
		column = colIt.next(); //columnQlLeecher
		result.qtLeechers =  Integer.parseInt(column.select("span.download").text());
		
		column = colIt.next(); //columnDL
		m = numberExtract.matcher(column.child(0).attr("href")); 
		result.Id = m.find() ? Integer.parseInt( m.group()) : -42;
		return result;
	}
	
	protected BookMarkEntry parseForum(Element tr) {
		
		BookMarkEntry result = new BookMarkEntry();
		Matcher m = numberExtract.matcher(tr.attr("id"));
		result.IdBookMark = m.find() ? Integer.parseInt(m.group()) : -42;
		Iterator<Element> colIt = tr.children().iterator(); // Iterator over the columns of the input
		
		Element column = colIt.next(); //columnSubject
		result.Name = column.child(1).text();
		
		column = colIt.next(); //columnDateAdded
		result.DateAdded = column.text();
		column = colIt.next(); //columnLastMsg
		column = colIt.next(); //columnLink
		
		return result;
	}
	
	protected BookMarkEntry parseWiki(Element tr) {
		
		BookMarkEntry result = new BookMarkEntry();
		Matcher m = numberExtract.matcher(tr.attr("id"));
		result.IdBookMark = m.find() ? Integer.parseInt(m.group()) : -42;
		Iterator<Element> colIt = tr.children().iterator(); // Iterator over the columns of the input
		
		Element column = colIt.next(); //columnTitle
		result.Name = column.child(1).text();
		
		column = colIt.next(); //columnDateAdded
		result.DateAdded = column.text();
		column = colIt.next(); //columnLastMsg
		return result;
	}
	
	protected BookMarkEntry parseRequest(Element tr) {
		
		BookMarkEntry result = new BookMarkEntry();
		Matcher m = numberExtract.matcher(tr.attr("id"));
		result.IdBookMark = m.find() ? Integer.parseInt(m.group()) : -42;
		Iterator<Element> colIt = tr.children().iterator(); // Iterator over the columns of the input
		
		Element column = colIt.next(); //columnTitle
		result.Name = column.child(1).text();
		
		column = colIt.next(); //columnDateAdded
		result.DateAdded = column.text();
		column = colIt.next(); //columnLFilled
		
		return result;
	}
}