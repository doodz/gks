package com.gks2.api.scrapper;

public class SearchOption {
	public final String groupId;
	public final String name;
	public final String id;
	
	public SearchOption(String id, String name, String groupId) {
		this.id = id; this.name=name; this.groupId=groupId;
	}
	
	@Override
	public boolean equals(Object obj) {
		try {
			SearchOption so = (SearchOption) obj;
			return groupId.equals(so.groupId) && name.equals(so.name) && id.equals(so.id);
		} catch (Exception e) { return false; }
		
	}
}
