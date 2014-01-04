package com.gks2.api.scrapper;

public class AjaxRequest {
	//"https://gks.gs/ajax.php?action=add&type=booktorrent&tid=%s";
	//"https://gks.gs/ajax.php?action=add&type=autoget&tid=%s";
	//ajax.php?action=del&type=delbookmark&tid=373653
	//https://gks.gs/autoget/delete&id=132163
	
	public SortActionAjax action = null;
	public SortTypeAjax type = null;
	public int tid = -42;
	
	public AjaxRequest(int tid){ 
		this(tid,null,null);
		
	}
	
	public AjaxRequest(int tid, SortActionAjax action, SortTypeAjax type){
		this.tid = tid;
		this.action = (action == null)? SortActionAjax.Add : action;
		this.type = (type == null)? SortTypeAjax.booktorrent : type;
		
	}
}