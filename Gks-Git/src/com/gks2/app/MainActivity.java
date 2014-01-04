package com.gks2.app;


import com.gks2.app.R;
import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	SharedPreferences prefs;
	
	@Override
	  public void onStart() {
	    super.onStart();
	    EasyTracker.getInstance(this).activityStart(this);
	  }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		final EditText unField = (EditText) findViewById(R.id.loginField);
		Editable e = unField.getText();
		e.clear();
		e.append(prefs.getString("pref_username", "Login"));
		final EditText pField = (EditText) findViewById(R.id.passField);
		e = pField.getText();
		e.clear();
		e.append(prefs.getString("pref_password", "p4ssw0rd"));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	public void doTrashLogin(View v) {
		
		final Editor e = prefs.edit();
		e.remove("pref_username");
		e.remove("pref_password");
		e.putBoolean("firstLogin",true );
		e.commit();
		this.finish();
		
	}
	
	public void doSave(View v) {
		
		final Editor e = prefs.edit();
		
		final EditText unField = (EditText) findViewById(R.id.loginField);
		final EditText pField = (EditText) findViewById(R.id.passField);
		final String username = unField.getText().toString();
		final String password = pField.getText().toString();
		e.putString("pref_username", username);
		e.putString("pref_password",	password);
		e.putBoolean("firstLogin", false);
		e.commit();
		
		Intent myIntent = new Intent(this, HomeActivity.class);
		this.startActivity(myIntent);
		//this.finish();
	}
	
	@Override
	  public void onStop() {
	    super.onStop();
	    EasyTracker.getInstance(this).activityStop(this);
	  }

}
