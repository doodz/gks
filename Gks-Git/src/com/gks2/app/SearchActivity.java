package com.gks2.app;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gks2.app.R;
import com.gks2.api.scrapper.AjaxRequest;
import com.gks2.api.scrapper.AjaxTask;
import com.gks2.api.scrapper.BookMarkTorrentTask;
import com.gks2.api.scrapper.God;
import com.gks2.api.scrapper.LoginTask;
import com.gks2.api.scrapper.SearchEntry;
import com.gks2.api.scrapper.SearchTorrentRequest;
import com.gks2.api.scrapper.SortActionAjax;
import com.gks2.api.scrapper.SortOrderType;
import com.gks2.api.scrapper.SortRequest;
import com.gks2.api.scrapper.SortTypeAjax;
import com.google.analytics.tracking.android.EasyTracker;


public class SearchActivity extends Activity {
	
	private God api;
	public int selectedItem = -1;
	protected Object mActionMode;
	private List<Map<String, String>> formatedData;
	private ProgressDialog ringProgressDialog = null;
	private SearchEntry itemSelected = null; 
	private ListView list;
	private List<SearchEntry> result = null;
	 ImageButton prev; 
	 ImageButton next;
	 SearchTorrentRequest request;
	 private boolean search = false;
	 
	public SearchActivity(){
		
		this.api = God.getInstance(null);
		this.request= new SearchTorrentRequest();
	}
	
	@Override
	public void onStart() {
	    super.onStart();
	    EasyTracker.getInstance(this).activityStart(this);
	  }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		// Show the Up button in the action bar.
		setupActionBar();
		EditText editText = (EditText) findViewById(R.id.searchfield);
		editText.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO : bug two catch on press enter key
				if (keyCode == KeyEvent.KEYCODE_ENTER)
				{
					SearchActivity.this.request.setTargetedPageResult(0);
					doSearch();
					return true;
				}
				return false;
			}
		});

		initButtons();
		list = (ListView) findViewById(R.id.torrentList);
		formatedData = (List<Map<String, String>>) getLastNonConfigurationInstance();
		if(formatedData != null)
			this.displayResult();
		
	}

	private void initButtons(){
		 prev = (ImageButton) findViewById(R.id.navbtn_prev);
	        prev.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	            	request.targetedPageResultPrev();
	                doSearch();
	            }
	        });

	        next = (ImageButton) findViewById(R.id.navbtn_next);
	        next.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	            	request.targetedPageResultNext();
	                doSearch();
	            }
	        });
	        this.prev.setVisibility(View.INVISIBLE);
	        //this.next.setVisibility(View.INVISIBLE);
	} 
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.search, menu);
		//return true;
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.search, menu);
	    return true;
	}
	
	@Override
	  public void onCreateContextMenu(ContextMenu menu, View v,
	          ContextMenuInfo menuInfo) {
	       
	      super.onCreateContextMenu(menu, v, menuInfo);
	      MenuInflater inflater = getMenuInflater();
	      inflater.inflate(R.menu.context_menu_torrent, menu);
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
	     case R.id.context_menu_torrent_autoget :
	    	 AddAutoget();
	    	 return true;
	     case R.id.context_menu_torrent_bookmark :
	    	 AddBookMark();
	    	 return true;
	     case R.id.context_menu_torrent_summary :
	    	 
	    	 return true;

	    default:
	    	return true;
	     }
	 }
	
		 
	private void AddAutoget(){
		if(this.itemSelected != null)
		 {
			AjaxRequest request = new AjaxRequest(itemSelected.id,SortActionAjax.Add,SortTypeAjax.autoget);
			this.ajaxTask(request);
		 }
	}
	private void AddBookMark(){
		
		if(this.itemSelected != null)
		 {
			AjaxRequest request = new AjaxRequest(itemSelected.id,SortActionAjax.Add,SortTypeAjax.booktorrent);
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
						if(input.equals(AjaxTask.SUCCES_BOOKMARK_CODE) || input.equals(AjaxTask.SUCCES_AUTOGET_CODE))
							Toast.makeText(SearchActivity.this, SearchActivity.this.getString(R.string.progress_dialog_bookmark_succes), 
									Toast.LENGTH_SHORT).show();						
						else
							Toast.makeText(SearchActivity.this, SearchActivity.this.getString(R.string.progress_dialog_bookmark_error), 
									Toast.LENGTH_SHORT).show();		
					}};
			this.api.setAjaxRequest(request, onResult);
		 }		
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.menu_search_filter:
			displayFilter();
				return true;
		case R.id.menu_search_help:
			showAboutMenu();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void showAboutMenu(){
		
		Intent myIntent = new Intent(this, AboutActivity.class);
		SearchActivity.this.startActivity(myIntent);
		
	}

	private void displayFilter(){
		final View dialogView = getLayoutInflater().inflate(R.layout.custom_properties, null);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this)
        .setIcon(R.drawable.ic_menu_filter)
        .setTitle(R.string.filter)
        .setCustomTitle(dialogView);
		 builder.setPositiveButton("Create", new OnClickListener() {

	            public void onClick(DialogInterface di, int i) {
	            
	            	Spinner order = (Spinner) dialogView.findViewById(R.id.filter_order);
	            	Spinner sort = (Spinner) dialogView.findViewById(R.id.filter_sort);
	            	Spinner cat = (Spinner) dialogView.findViewById(R.id.filter_category);
	            	String[] sortVal = getResources().getStringArray(R.array.sortby_value);
	            	int[] catVal = getResources().getIntArray(R.array.category_value);
	            	
	            	String sortStr = sortVal[sort.getSelectedItemPosition()]; 
	            	String catStr = ""+catVal[cat.getSelectedItemPosition()];
	            	SearchActivity.this.request.categorie = catStr;
	            	SearchActivity.this.request.sortRequest = new SortRequest( sortStr, 
	            			order.getSelectedItemPosition() == 0? SortOrderType.DESC : SortOrderType.ASC );
	            	
	            }
	        });
	        builder.setNegativeButton("Cancel", new OnClickListener() {

	            public void onClick(DialogInterface di, int i) {
	            }
	        });
	        builder.create().show();
	}
	
	@Override
    public void onResume() {
		if(!api.isLoged){
			
			final Observer onLogin = new Observer() {
				@Override
				//if executeLogin return LoginTask.SUCCES_CODE task call update 
				public void update(Observable observable, Object data) {
					
					Integer code = (Integer) data;
					if (code != LoginTask.SUCCES_CODE){
						Intent myIntent = new Intent(SearchActivity.this, MainActivity.class);
				        startActivity(myIntent);
				        SearchActivity.this.finish();
					}
					else{
						//StartSearch();
					}
				}
			};
			
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());		
			api.executeLogin(prefs.getString("username", "unamed"), prefs.getString("password", "p4ssw0rd"), onLogin);
			
		}
		if(formatedData != null)
			displayResult();
        super.onResume();
    }
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
       
        super.onSaveInstanceState(outState);
        
    }
	
	private void doSearch()
	{
		if(!api.isLoged){ 
			
			final Observer onLogin = new Observer() {
				@Override
				//if executeLogin return LoginTask.SUCCES_CODE task call update 
				public void update(Observable observable, Object data) {
					
					Integer code = (Integer) data;
					if (code != LoginTask.SUCCES_CODE){
						Intent myIntent = new Intent(SearchActivity.this, MainActivity.class);
				        startActivity(myIntent);
				        SearchActivity.this.finish();
					}
					else{
						StartSearch();
					}
				}
			};
			
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());		
			api.executeLogin(prefs.getString("pref_username", "unamed"), prefs.getString("pref_password", "p4ssw0rd"), onLogin);
		}
		else
			StartSearch();
	}
	
	private void updateNavBar(){
		if(prev == null)
			this.initButtons();
		
		//this.request
		TextView tv =  (TextView)findViewById(R.id.navbar_pagesText);
		int p = this.request.getTargetedPageResult();
		
		if(p < 1)
			this.prev.setVisibility(View.INVISIBLE);
		else
			this.prev.setVisibility(View.VISIBLE);
		
		
		tv.setText(""+(++p));
		
	}
	
	private void displayResult(){
		final ListAdapter adpt = new SimpleAdapter(SearchActivity.this.getBaseContext(),
				formatedData,
				R.layout.torrent_listitem,
				new String[]{"title","postSize","icon","typeIcon","qtSeeders","qtLeechers"},
				new int[] {R.id.tTitle,R.id.tSize,R.id.tIcon, R.id.tCat ,R.id.tSeeders,R.id.tLeechers});
		list.setAdapter(adpt);
	
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		     @Override
			public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
					long id) {
		    	 
		    	 @SuppressWarnings("unchecked")
		    	 HashMap<String, String> map = (HashMap<String, String>)list.getAdapter().getItem(position);
		    	 Intent i = new Intent();
                 i.setClass(SearchActivity.this, TorrentDetailActivity.class);
                 i.putExtra("url", map.get("prezLocaion"));
                 i.putExtra("nom", map.get("title"));
                 i.putExtra("ID", map.get("id"));
                 startActivity(i);
		     }
		});		
		
		list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                    int index, long arg3) {	            	
            	 itemSelected = result.get(index);
                //itemSelected = (SearchEntry) list.getItemAtPosition(index);
                SearchActivity.this.openContextMenu(arg0);
                 
                return true;
            }
		}); 
		
		// register for the contextmneu       
		registerForContextMenu(list);
		updateNavBar();
	}
	
	private void  StartSearch() {

		final TextView searchfield = ((TextView) findViewById(R.id.searchfield));
		request.request = searchfield.getText().toString();
		
		final Observer onResult = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				  result = (List<SearchEntry>) arg;
				list = (ListView) findViewById(R.id.torrentList);
				formatedData = new ArrayList<Map<String,String>>();
				
				for (SearchEntry e : result) {
					//if (e.isRead) continue;
					formatedData.add(e.toMap());
					//formatedData.add(toMap(SearchEntry.class, e));
				}
				displayResult();
				updateNavBar();
				if(formatedData.size()<1)
					Toast.makeText(SearchActivity.this,getString(R.string.noResult),Toast.LENGTH_SHORT).show();
				SearchActivity.this.StopRingDialog();
				SearchActivity.this.search = false;
			}
		};
		if(!search)
		{
			this.launchRingDialog();
			
			search = true;
			api.search(request, onResult);
		}
		//onResult.update(null, com.gks.test.test.getlstTorrent());
	}
	
	 public static Map<String, String> toMap(Class<?> c, Object instance) {
			Field[] searchEntryFields = c.getDeclaredFields();
			HashMap<String, String> result = new HashMap<String, String>(searchEntryFields.length);
			Object fieldValue = null;
			
			for (Field f : searchEntryFields) {
				try {
					fieldValue = f.get(instance);
					if (fieldValue != null) result.put(f.getName(), fieldValue.toString());
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
			
			return result;
		}
	 
	 public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		 mode.setTitle("Options");
		 mode.getMenuInflater().inflate(R.menu.rowselection, menu);
		 return true;
		}
		
	 public void launchRingDialog(View view) {launchRingDialog();}
	 
	 private void launchRingDialog() {
	         this.ringProgressDialog = ProgressDialog.show(SearchActivity.this, SearchActivity.this.getString(R.string.progress_dialog_title_searching), "  ", true);
	         this.ringProgressDialog.setCancelable(true);
     }
	 
	 private void StopRingDialog(){
		 if(SearchActivity.this.ringProgressDialog != null)
			 SearchActivity.this.ringProgressDialog.dismiss();
	 }
	 
	 
	 private void ShowPrez(){
		 if(SearchActivity.this.itemSelected != null)
		 {			 
			 Intent i = new Intent();
	         i.setClass(SearchActivity.this, TorrentDetailActivity.class);
	         i.putExtra("url", SearchActivity.this.itemSelected.prezLocaion);
	         i.putExtra("nom", SearchActivity.this.itemSelected.title);
	         i.putExtra("ID", SearchActivity.this.itemSelected.id);
	         startActivity(i);
		 }
	 }
	 
	 private void DownloadTorrent(){
		 if(this.itemSelected != null)
		 {
			final NotifyManager nm = new NotifyManager();
			 
			 final Observer onResult = new Observer() {
					@Override
					public void update(Observable o, Object arg) {
						
						byte[] input = (byte[]) arg;
						SaveFileManager sfm = new SaveFileManager(input);
						sfm.context = SearchActivity.this.getApplicationContext();
						sfm.id = itemSelected.id;
						sfm.Save(itemSelected.dlLocation);
						
						//Integer result = (Integer)arg;
						/*if(result != null && result.equals(DownloadTorrentTask.MESSAGE_DOWNLOAD_COMPLETE))
							Toast.makeText(SearchActivity.this, SearchActivity.this.getString(R.string.user_message_download_complete), Toast.LENGTH_SHORT).show();
						else
							 Toast.makeText(SearchActivity.this, SearchActivity.this.getString(R.string.error_message_general), Toast.LENGTH_SHORT).show();*/
						//nm.NotifyDownloadComplete();
					}};
			Toast.makeText(SearchActivity.this, SearchActivity.this.getString(R.string.progress_dialog_message_prefix_downloading), Toast.LENGTH_SHORT).show();
			//nm.NotifyDownload(SearchActivity.this);
			this.api.downloadTorrent(itemSelected, onResult);
		 }
	 }
	 
	 
	 @Override
     public Object onRetainNonConfigurationInstance() {
            	 return formatedData;
     }
	
	 @Override
	  public void onStop() {
	    super.onStop();
	    
	    if(SearchActivity.this.ringProgressDialog != null && SearchActivity.this.ringProgressDialog.isShowing())
	    {
	    	SearchActivity.this.ringProgressDialog.dismiss();
	    	SearchActivity.this.ringProgressDialog = null;
	    }
	    
	    EasyTracker.getInstance(this).activityStop(this);
	  }
	
}
