package com.gks2.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.gks2.api.scrapper.AjaxRequest;
import com.gks2.api.scrapper.AjaxTask;
import com.gks2.api.scrapper.BookMarkEntry;
import com.gks2.api.scrapper.God;
import com.gks2.api.scrapper.SortActionAjax;
import com.gks2.api.scrapper.SortTypeAjax;

public class BookMarkListFragment extends ListFragment {
	
	public static final int BOOKMARK_TORRENT = 11;
	public static final int BOOKMARK_FORUM = 12;
	public static final int BOOKMARK_WIKI = 13;
	public static final int BOOKMARK_REQUEST = 14;
	public static final String ARG_SECTION_BOOKMARK = "section_bookmark_number";
	private int BookmarkType = -42;
	public List<Map<String, String>> formatedData;
	private int selectedItem = -42;
	ListAdapter adpt = null;
	// This is the Adapter being used to display the list's data
    SimpleCursorAdapter mAdapter;

	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      Bundle args = getArguments();
      this.BookmarkType =  args.getInt(ARG_SECTION_BOOKMARK);
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
		
	    	adpt = new SimpleAdapter(fa,
	    			formatedData,
					R.layout.bookmark_torrent_listitem,
					new String[]{"Name", "Size", "","qtSeeders", "qtLeechers","DateAdded"},
					new int[] {R.id.tTitle, R.id.tSize, R.id.tIcon, R.id.tSeeders, R.id.tLeechers,R.id.tAdded});
	    	BookMarkListFragment.this.setListAdapter(adpt);
	    	getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
			     @Override
					public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
							long id) {
			    	 ((BookMarkActivity) getActivity()).selectedItem = position;
			    	 //BookMarkListFragment.this.selectedItem = position;
			    	 BookMarkListFragment.this.getActivity().openContextMenu(parentAdapter);
			     }
			});		
	    	registerForContextMenu(getListView());
		}
		else{
			this.setEmptyText("No data Found");
			setListShownNoAnimation(true);
		}
		
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        
        switch (BookMarkListFragment.this.BookmarkType) {
	     case BOOKMARK_TORRENT :
	    	 inflater.inflate(R.menu.context_menu_bookmark_torrent, menu);
	    	 break;
	     case BOOKMARK_FORUM :
	    	 inflater.inflate(R.menu.context_menu_bookmark_forum, menu);
	    	 break;
	     case BOOKMARK_WIKI :
	    	 inflater.inflate(R.menu.context_menu_bookmark_wiki, menu);
	    	 break;
	     case BOOKMARK_REQUEST :
	    	 inflater.inflate(R.menu.context_menu_bookmark_request, menu);
	    	 break;
	    default:
	    	 inflater.inflate(R.menu.context_menu_bookmark_torrent, menu);
	    	 break;
	     }
        
    }
    
    // This method is called when user selects an Item in the Context menu
 	 @Override
 	 public boolean onContextItemSelected(MenuItem item) {

 	     switch (item.getItemId()) {
 	     case R.id.context_menu_torrent_details :
 	    	 ShowPrez();
 	    	 return true;
 	     case R.id.context_menu_torrent_download :
 	    	 DownloadTorrent();
 	    	 return true;
 	     case R.id.context_menu_torrent_delete :
 	    	((BookMarkActivity) BookMarkListFragment.this.getActivity()).DeleteBookmark(SortTypeAjax.delbookmark);
 	    	 return true;
 	    case R.id.context_menu_forum_delete :
 	    	((BookMarkActivity) BookMarkListFragment.this.getActivity()).DeleteBookmark(SortTypeAjax.delforumbookmark);
	    	 return true;
 	   case R.id.context_menu_request_delete :
 		  ((BookMarkActivity) BookMarkListFragment.this.getActivity()).DeleteBookmark(SortTypeAjax.delrequestbookmark);
	    	 return true;
 	  case R.id.context_menu_wiki_delete :
 		 ((BookMarkActivity) BookMarkListFragment.this.getActivity()).DeleteBookmark(SortTypeAjax.delwikiobookmark);
	    	 return true;
 	    default:
 	    	Toast.makeText(BookMarkListFragment.this.getActivity(), "No implemented", Toast.LENGTH_SHORT).show();	
 	    	return true;
 	     }
 	 }
    
 	private void DeleteBookmark(SortTypeAjax typeAjax){
 		int iiii =  this.selectedItem;
 		if(BookMarkListFragment.this.selectedItem >= 0){
 			
 			@SuppressWarnings("unchecked")
			Map<String,String> itemSelected = (Map<String, String>) getListView().getItemAtPosition(selectedItem);			
			AjaxRequest request = new AjaxRequest(Integer.parseInt(itemSelected.get("IdBookMark")),SortActionAjax.Dell,typeAjax);
			this.ajaxTask(request);
		 }
 	}
 	 
 	private void ajaxTask(AjaxRequest request){
		
		if(request != null)
		 {

			 final Observer onResult = new Observer() {
					@Override
					public void update(Observable o, Object arg) {
						
						Integer input = (Integer) arg;
						if(input.equals(AjaxTask.SUCCES_BOOKMARK_CODE) || input.equals(AjaxTask.SUCCES_AUTOGET_CODE)){
							BookMarkListFragment.this.formatedData.remove(BookMarkListFragment.this.selectedItem);
							((BaseAdapter) BookMarkListFragment.this.getListView().getAdapter()).notifyDataSetChanged();
						}			
						else
							Toast.makeText(BookMarkListFragment.this.getActivity(), BookMarkListFragment.this.getString(R.string.progress_dialog_bookmark_error), 
									Toast.LENGTH_SHORT).show();		
					}};
			God.getInstance(BookMarkListFragment.this.getActivity().getApplicationContext()).setAjaxRequest(request, onResult);
		 }		
		
	}

 	private void ShowPrez(){
 		
 		if(BookMarkListFragment.this.selectedItem >= 0){
 			
 			@SuppressWarnings("unchecked")
			Map<String,String> itemSelected = (Map<String, String>) getListView().getItemAtPosition(selectedItem);
 			
			 Intent i = new Intent();
	         i.setClass(BookMarkListFragment.this.getActivity(), TorrentDetailActivity.class);
	         i.putExtra("url", itemSelected.get("Location"));
	         i.putExtra("nom", itemSelected.get("Name"));
	         i.putExtra("ID", itemSelected.get("Id"));
	         startActivity(i);
		 }
	 }
	 
	 private void DownloadTorrent(){
		 if(BookMarkListFragment.this.selectedItem >= 0){	
			@SuppressWarnings("unchecked")
	 		final Map<String,String> itemSelected = (Map<String, String>) getListView().getItemAtPosition(selectedItem);
	 			
			final NotifyManager nm = new NotifyManager();
			 
			 final Observer onResult = new Observer() {
					@Override
					public void update(Observable o, Object arg) {
						
						byte[] input = (byte[]) arg;
						SaveFileManager sfm = new SaveFileManager(input);
						sfm.context = BookMarkListFragment.this.getActivity().getApplicationContext();
						sfm.id = Integer.parseInt(itemSelected.get("Id"));
						sfm.Save(itemSelected.get("Location"));
						
						
					}};
			Toast.makeText(BookMarkListFragment.this.getActivity(), BookMarkListFragment.this.getString(R.string.progress_dialog_message_prefix_downloading), Toast.LENGTH_SHORT).show();
			God.getInstance(BookMarkListFragment.this.getActivity()).downloadTorrent(itemSelected.get("Location"), onResult);
		 }
	 }
    
    @Override 
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
    }
    
   
    
}
