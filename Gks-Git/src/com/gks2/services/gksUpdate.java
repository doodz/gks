package com.gks2.services;

import java.util.Observable;
import java.util.Observer;

import com.gks2.api.scrapper.God;
import com.gks2.api.scrapper.UserProfile;
import com.gks2.app.HomeActivity;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class gksUpdate extends IntentService {

	public gksUpdate(String name) {		
		super(name);
		this.api = God.getInstance(this.getApplicationContext());
	}

	public gksUpdate() {		
		super(TAG);
		this.api = God.getInstance(this.getApplicationContext());
	}
	
	private God api;
	SharedPreferences prefs;
	private final static String TAG = "IntentServiceGksUpdate";
	
	
	
	@Override
	public IBinder onBind(Intent intent) {
		 Log.v("Gks Binder", intent.toString());
	        return null;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		final Observer onUserProfil = new Observer() {
			@Override
			public void update(Observable observable, Object data) {
				updateValues();
			};
		};
		Log.v("Gks retrieveUserProfil", intent.toString());
		api.retrieveUserProfil(null,onUserProfil);
	}

	public void updateValues(){
		Log.v("Gks updateValues"," IntentService gksUpdate");
		UserProfile profile = api.getUserProfile();
		prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		Editor editor = prefs.edit();
		editor.putString("username", profile.username);
		editor.putString("userid", profile.userid);
		editor.putString("registeringDate", profile.registeringDate);
		editor.putString("lastVisiteDate", profile.lastVisiteDate);
		editor.putString("uploadedTotalData", profile.uploadedTotalData);
		editor.putString("downloadedTotalData", profile.downloadedTotalData);
		editor.putString("uploadedDailyData", profile.uploadedDailyData);
		editor.putString("downloadedDailyData", profile.downloadedDailyData);
		editor.putString("userclass", profile.userclass);
		editor.putString("usermail", profile.usermail);
		editor.putString("userip", profile.userip);
		editor.putString("karma", profile.karma);
		editor.putString("aura", profile.aura);
		editor.putString("ratio", profile.ratio);
		editor.putString("ratioreq", profile.ratioreq);
		editor.putString("authKey", profile.authKey);
		editor.commit();
	}
	
}
