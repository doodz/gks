package com.gks2.app;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.gks2.api.scrapper.God;
import com.gks2.api.scrapper.TorrentDetailEntry;
import com.google.analytics.tracking.android.EasyTracker;

public class TorrentDetailActivity extends Activity {

	WebView details_www;
	String torrent_URL, torrent_NFO, torrent_ID, torrent_Name;
	final String mimeType = "text/html";
    final String encoding = "utf-8";
	private God api;
	private ProgressDialog ringProgressDialog = null;
	private TorrentDetailEntry Entry;
	
	public TorrentDetailActivity(){
		this.api = God.getInstance(null);		
	}
	
	@Override
	  public void onStart() {
	    super.onStart();
	    EasyTracker.getInstance(this).activityStart(this);
	  }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_torrent_detail);
		
		torrent_URL = getIntent().getStringExtra("url");
        torrent_Name = getIntent().getStringExtra("nom");
        torrent_ID = getIntent().getStringExtra("ID");
        
		details_www = (WebView) findViewById(R.id.prez);
        details_www.getSettings().setUseWideViewPort(true);
        details_www.getSettings().setLoadWithOverviewMode(true);
        details_www.getSettings().setJavaScriptEnabled(true);
        this.GetPres();
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	private void GetPres()
	{
		TorrentDetailActivity.this.launchRingDialog();
		final Observer onResult = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				TorrentDetailEntry result = (TorrentDetailEntry)arg;
				details_www.loadDataWithBaseURL("fake://seeJavaDocForExplanation/", result.Prez, mimeType, encoding, "");
				TorrentDetailActivity.this.Entry = result;
				TorrentDetailActivity.this.StopRingDialog();
			}};
			this.api.getTorrentPres(torrent_URL, onResult);
		
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.torrent_detail, menu);
		return true;
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
		case R.id.menu_nfo :
			 showNfo();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void showNfo(){
		AlertDialog alertDialog = new AlertDialog.Builder(TorrentDetailActivity.this).setTitle("NFO")
				.setMessage(TorrentDetailActivity.this.Entry.Nfo).setIcon(R.drawable.file).show();
        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        textView.setTextSize(10);
        textView.setTypeface(Typeface.MONOSPACE);
	}
	
	private void launchRingDialog() {
        this.ringProgressDialog = ProgressDialog.show(TorrentDetailActivity.this,  
        		TorrentDetailActivity.this.getString(R.string.progress_dialog_title_getprez), "  ", true);
        this.ringProgressDialog.setCancelable(true);
        
	 }
 
	private void StopRingDialog(){
		
		if(TorrentDetailActivity.this.ringProgressDialog != null 
				&& TorrentDetailActivity.this.ringProgressDialog.isShowing())
			TorrentDetailActivity.this.ringProgressDialog.dismiss();
	 }
	
	
	@Override
	  public void onStop() {
	    super.onStop();
	    EasyTracker.getInstance(this).activityStop(this);
	  }
	
}
