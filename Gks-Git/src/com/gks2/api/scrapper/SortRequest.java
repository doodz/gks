package com.gks2.api.scrapper;


public class SortRequest {
	SortOrderType type = null;
	String field = null;
	
	public SortRequest(String field) { this(field, null); }
	
	public SortRequest(String field, SortOrderType type) {
		this.type = (type==null)?SortOrderType.DESC:type;
		this.field = (field==null)?"added":field;
	}
}
