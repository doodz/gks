package com.gks2.api.scrapper;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


/***
 * this class is use to scrap user info in header and footer 
 * @author thibault
 *
 */
public class HeaderFooterScrapper {
//private final static Pattern loginExtractHeader = Pattern.compile("(.+)\\s\\(Déconnexion\\)$");
	
	/**
	 * static class ;
	 * */
	private HeaderFooterScrapper(){}
	
	/***
	 * Use to call header and footer reader 
	 * @param htmlPage
	 * @param UserProfile
	 */
	public static void readPage(Document htmlPage, UserProfile profile) {
		readHeader(htmlPage, profile);
		readFooter(htmlPage.select("div.stats").first());
	}
	
	
	/***
	 * 
	 * Use to set UserProfil info from Doucument as htmlPage, to UserProfile
	 * @param htmlPage
	 * @param UserProfile
	 */
	public static void readHeader(Document htmlPage, UserProfile profile) {
		readHeader(htmlPage.select("div#my-account").first(), profile);
	}
	
	/***
	 * Use to set UserProfil info from node.element(jsoup), to UserProfile
	 * @param loginBar
	 * @param profile
	 */
	public static void readHeader(Element loginBar, UserProfile profile) {
		if (profile == null) return;
		/*
		Matcher m = loginExtractHeader.matcher(loginBar.child(0).text());
		if ( ! m.find()) throw new Error("Global unmatch! : "+loginBar.child(0).text());
		profile.username = m.group(1);
		profile.unreadedMails = Integer.parseInt(loginBar.child(2).text());
		profile.needVotes = Integer.parseInt(loginBar.child(4).text());*/
		profile.username = loginBar.select("label.radiocheck").text().substring(2);;
		//profile.uploadedTotalData = DataSize.parseSize(loginBar.select("span.upload").text().substring(2));
		//profile.downloadedTotalData = DataSize.parseSize(loginBar.select("span.download").text().substring(2));
	}
	
	
	/***
	 * No implemented
	 * @param statsBlock
	 */
	private static void readFooter(Element statsBlock) {
		
	}
}
