package com.gks2.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import com.gks2.api.scrapper.God;
import com.gks2.api.scrapper.SearchEntry;
import com.gks2.api.scrapper.SnatchedEntry;
import com.google.analytics.tracking.android.EasyTracker;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SnatchedActivity extends Activity {
	
	
	private List<SnatchedEntry> result = null;
	private List<Map<String, String>> formatedData = null;
	private ListView list = null;
	private ProgressDialog ringProgressDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_snatched);
	}

	@Override
    public void onStart() {
		super.onStart();
		launchRingDialog();
		final Observer onResult = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				List<SnatchedEntry> result = (List<SnatchedEntry>) arg;
				list = (ListView) findViewById(R.id.torrentList);
				formatedData = new ArrayList<Map<String,String>>();
				
				for (SnatchedEntry e : result) {
					//if (e.isRead) continue;
					formatedData.add(e.toMap());
					//formatedData.add(toMap(SearchEntry.class, e));
				}
				displayResult();
				 StopRingDialog();
			}
		};
		
		God.getInstance(SnatchedActivity.this.getApplicationContext()).getSnatched(null, onResult);
	}
	
	private void displayResult(){
		
		final ListAdapter adpt = new SimpleAdapter(SnatchedActivity.this.getBaseContext(),
				formatedData,
				R.layout.snatched_lstitem,
				new String[]{"title","ratio","seedTime","qtDl","qtUl","qtRealDl","seeding"},
				new int[] {R.id.tTitle,R.id.tRatio,R.id.tSeedeTime, R.id.tDl ,R.id.tUl,R.id.tRealDl,R.id.tSeeding});
		list.setAdapter(adpt);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.snatched, menu);
		return true;
	}
	private void launchRingDialog() {
        this.ringProgressDialog = ProgressDialog.show(SnatchedActivity.this, SnatchedActivity.this.getString(R.string.progress_dialog_title_searching), "  ", true);
        this.ringProgressDialog.setCancelable(true);
	}
	
	private void StopRingDialog(){
		 if(SnatchedActivity.this.ringProgressDialog != null)
			 SnatchedActivity.this.ringProgressDialog.dismiss();
	}

	@Override
	public void onStop() {
	  super.onStop();
	  
	  if(SnatchedActivity.this.ringProgressDialog != null && SnatchedActivity.this.ringProgressDialog.isShowing())
	  {
		  SnatchedActivity.this.ringProgressDialog.dismiss();
		  SnatchedActivity.this.ringProgressDialog = null;
	  }
	  
	 
	}
}
