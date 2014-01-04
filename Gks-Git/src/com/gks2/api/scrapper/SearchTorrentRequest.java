package com.gks2.api.scrapper;

import java.util.ArrayList;
import java.util.Collection;

public class SearchTorrentRequest extends BasicPaginedResultRequest {
	public String request = "";
	public String categorie = "";
	public String subCategorie;
	public Collection<SearchOption> options = new ArrayList<SearchOption>();
	public SortRequest sortRequest = new SortRequest(null, null);
	
	public SearchTorrentRequest() { this(0); }
	public SearchTorrentRequest(int i) { super(i, new SortRequest(SortableField.AGE, SortOrderType.DESC)); }
	public SearchTorrentRequest(SortRequest sorting) { super(0,sorting); }
	public SearchTorrentRequest(SortRequest sorting, int i) { super(i,sorting); }
	
	public class SortableField {
		public final static String TYPE = "category";
		//public final static String NAME = "name";
		public final static String QT_COMMENTS = "coms";
		public final static String AGE = "data";
		public final static String SIZE = "size";
		public final static String QT_COMPLETED_DOWNLOAD = "complets";
		public final static String QT_SEEDERS = "seeders";
		public final static String QT_LEECHERS = "leechers";
	}
	
	
	
}