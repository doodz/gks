package com.gks2.test;

import java.util.ArrayList;
import java.util.List;

import com.gks2.api.scrapper.*;

public class test {
	
	
	public static List<SearchEntry>  getlstTorrent(){
		List<SearchEntry> arrList = new ArrayList<SearchEntry>(); 
		
		for (int i = 0; i < 20; i++) {
			SearchEntry item = new SearchEntry();
			item.title = "torrent"+i;
			item.prezLocaion = "locationTorrent"+i;
			arrList.add(item);
		}
		return arrList;
		
	}
}
