package com.gks2.api.scrapper;

public enum SortOrderType {
	DESC("desc"), ASC("asc");
	
	public String id;
	private SortOrderType(String id) { this.id = id; }
}
