package com.gks2.api.scrapper;

import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.util.Log;

public class GetUserProfilTask extends HttpAsyncTask<String, Integer>{
	
	public final static String userProfilUrl = "https://gks.gs/m/account/";
	@Override
	protected Integer doInBackground(String... params) {

		Log.v(this.getClass().getSimpleName(), String.format("begin (%s)", userProfilUrl));
		final HttpGet httpRequest = new HttpGet(userProfilUrl);
		final HttpResponse httpResp = browser.execute(httpRequest);	 
		final String content = getStringContent(httpResp);
		final Document htmlResponse = Jsoup.parse(content);
		//HeaderFooterScrapper.readPage(htmlResponse, profile);
		Element resultEntrie = htmlResponse.select("#userlink").first();
		if(resultEntrie != null)
			parseForUserId(resultEntrie, profile);
		resultEntrie = htmlResponse.select("div#my-account").first();
		if(resultEntrie != null)
			parse(resultEntrie, profile);
		return null;
	}
	
	protected void parseForUserId(Element div, UserProfile userProfile){
		
		String str = div.select("li>a").attr("href");
		String[] tab = str.split("/");
		userProfile.userid = tab[2];
	}
	
	/***
	 * set user info from html to userProfile class
	 * @param p
	 * @param userProfile
	 */
	protected void parse(Element p, UserProfile userProfile) {
		
		Iterator<Element> colIt = p.children().iterator();
		Element columnNom = colIt.next();
		userProfile.username = columnNom.child(1).select("label.radiocheck").text();
		
		colIt.next(); //Titre Personnalisé		
		Element columnClass = colIt.next();
		userProfile.userclass =  columnClass.child(1).select("label.radiocheck").text();
		
		//colIt.next(); //Adresse Email
		
		userProfile.usermail = colIt.next().child(1).select("label.radiocheck").text();
		userProfile.userip = colIt.next().child(1).select("label.radiocheck").text();
		colIt.next(); //blanck
		colIt.next(); //history
		colIt.next(); //Inscription	
		colIt.next();
		colIt.next();
		colIt.next(); //Age / Sexe
		colIt.next(); //Contributions
		
		Element columnKarmaAura = colIt.next();
		String K_A = columnKarmaAura.child(1).select("label.radiocheck").text();
		String[] tabStr= K_A.split("/");
		userProfile.karma = tabStr[0];
		userProfile.aura = tabStr[1];
		
		Element upDownRatio = colIt.next();
		//userProfile.uploadedTotalData = DataSize.parseSize(upDownRatio.child(1).select("span.upload").text().substring(2));
		//userProfile.downloadedTotalData = DataSize.parseSize(upDownRatio.child(1).select("span.download").text().substring(2));
		userProfile.uploadedTotalData = upDownRatio.child(1).select("span.upload").text().substring(2);
		userProfile.downloadedTotalData = upDownRatio.child(1).select("span.download").text().substring(2);
		userProfile.ratio 	= upDownRatio.child(1).select("span.r20").text();
		userProfile.ratioreq = upDownRatio.child(1).select("em").text();
		
		colIt.next(); // Torrents up
		colIt.next(); // Requests
		colIt.next(); // Communauté
		colIt.next(); // Badges
		colIt.next(); // Passkey
		colIt.next();
		Element authKey = colIt.next();
		userProfile.authKey = authKey.child(1).select("input").attr("value");
	}

}
