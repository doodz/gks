package com.gks2.api.scrapper;

public class BasicPaginedResultRequest implements PaginedResultRequest {
	public BasicPaginedResultRequest(int page, SortRequest sorting) {
		this.targetedPageResult = page;
		this.sortRequest = sorting;
	}
	
	protected SortRequest sortRequest;
	public SortRequest getSortRequest() { return sortRequest; }
	public void setSortRequest(SortRequest sortRequest) { this.sortRequest = sortRequest; }
	
	@Override
	public int getTargetedPageResult() { return targetedPageResult; }
	public void setTargetedPageResult (int i){this.targetedPageResult = i;}
	public void targetedPageResultNext(){ this.targetedPageResult++;}
	public void targetedPageResultPrev(){
		if(this.targetedPageResult > 0)
			this.targetedPageResult--;
		}
	protected int targetedPageResult = 0;
}