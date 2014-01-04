package com.gks2.adapters;

import java.util.List;




import com.gks2.api.scrapper.SearchEntry;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SearchEntryAdaptater extends BaseAdapter {
	protected List<SearchEntry> searchResult;

	@Override
	public int getCount() { return searchResult.size(); }

	@Override
	public Object getItem(int position) { return searchResult.get(position); }

	@Override
	public long getItemId(int position) { return position; }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) { return null; }

}