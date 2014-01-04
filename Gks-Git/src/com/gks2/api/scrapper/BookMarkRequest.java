package com.gks2.api.scrapper;

public class BookMarkRequest {
	
	public SortTabBookMark tab;
	
	public BookMarkRequest(){ 
		this(null);		
	}
	
	public BookMarkRequest(SortTabBookMark tab){		
		this.tab = (tab == null)? SortTabBookMark.Torrents : tab;		
	}
	
}
