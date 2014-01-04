package com.gks2.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;

public class AboutActivity extends Activity {

	@Override
	  public void onStart() {
	    super.onStart();
	    EasyTracker.getInstance(this).activityStart(this);
	  }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		String version = "????";
	    PackageInfo pInfo;
	        
		// Show the Up button in the action bar.
		setupActionBar();
		

        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        
        TextView appVersion = (TextView)findViewById(R.id.appVersion);
        appVersion.setText("Version " + version);
        
        Button changelog = (Button) findViewById(R.id.btnChgLog);
        changelog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chlog = "";
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("Changelog.txt"), "UTF-8"));
                    String mLine = reader.readLine();
                    while (mLine != null) {
                        mLine = reader.readLine();
                        chlog += mLine + "\n";
                    }
                    reader.close();
                } catch (IOException e) {
                }


                AlertDialog alertDialog = new AlertDialog.Builder(AboutActivity.this).setTitle("Changelog").setMessage(chlog).setIcon(R.drawable.file).show();
                TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                textView.setTextSize(10);
                textView.setTypeface(Typeface.MONOSPACE);
            }
        });
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
		getMenuInflater().inflate(R.menu.about, menu);
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
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	  public void onStop() {
	    super.onStop();
	    EasyTracker.getInstance(this).activityStop(this);
	  }
	
}
