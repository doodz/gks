package com.gks2.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.gks2.api.scrapper.BookMarkEntry;

public class BookMarkListFragment extends ListFragment {
	
	
	private List<Map<String, String>> formatedData;
	
	// This is the Adapter being used to display the list's data
    SimpleCursorAdapter mAdapter;
    
    // These are the Contacts rows that we will retrieve
    static final String[] PROJECTION = new String[] {ContactsContract.Data._ID,
            ContactsContract.Data.DISPLAY_NAME};
    
    // This is the select criteria
    static final String SELECTION = "((" + 
            ContactsContract.Data.DISPLAY_NAME + " NOTNULL) AND (" +
            ContactsContract.Data.DISPLAY_NAME + " != '' ))";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);

      	// Create a progress bar to display while the list loads
        ProgressBar progressBar = new ProgressBar(this.getActivity());
        
        //(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER)
        progressBar.setLayoutParams(new LayoutParams());
        progressBar.setIndeterminate(true);
        //getListView().setEmptyView(progressBar);
       
        // Must add the progress bar to the root of the layout
        ViewGroup root = (ViewGroup) this.getActivity().findViewById(android.R.id.content);
        //root.addView(progressBar);
       this.setRetainInstance(true);
    }

    public void setEntries(List<BookMarkEntry> entries){
    	
    	formatedData = new ArrayList<Map<String,String>>();
		
		for (BookMarkEntry e : entries) {
			formatedData.add(e.toMap());
		}

		FragmentActivity fa = BookMarkListFragment.this.getActivity();
		if(fa != null && entries.size() > 0){
		
	    	final ListAdapter adpt = new SimpleAdapter(fa,
	    			formatedData,
					R.layout.torrent_listitem,
					new String[]{"Name","Size","qtSeeders","qtLeechers"},
					new int[] {R.id.tTitle,R.id.tSize ,R.id.tSeeders,R.id.tLeechers});
	    	BookMarkListFragment.this.setListAdapter(adpt);
	    	
	    	
		}
		else{
			this.setEmptyText("No data Found");
			setListShownNoAnimation(true);
		}
		
    }
    
    

    @Override 
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
    }
    
   
    
}
