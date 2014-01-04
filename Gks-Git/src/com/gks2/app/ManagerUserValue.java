package com.gks2.app;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

import com.gks2.api.scrapper.God;
import com.gks2.api.scrapper.UserProfile;

public class ManagerUserValue {
	
	private God api;
	Context context;
	SharedPreferences prefs;
	
	public ManagerUserValue(Context context){
		this.api = God.getInstance(context.getApplicationContext());
		this.context = context;
	}
	
	public void UpdateUserProfil(final Observer onResult){
		
		final Observer onUserProfil = new Observer() {
			@Override
			public void update(Observable observable, Object data) {
				updateValues();
				onResult.update(observable, data);
			};
		};
		api.retrieveUserProfil(null,onUserProfil);	
	}
	
	public void UpdateChartRatio(final Observer onResult){
		
		final Observer onChartRatio = new Observer() {
			@Override
			public void update(Observable observable, Object data) {
				String graph = (String) data;
				updateRatio(graph);
				onResult.update(observable, graph);
			};
		};
		
		api.getChartRatio(null,onChartRatio);
		
	}
	
	private void updateRatio(String graph){
		Log.v("Gks updateRatio"," IntentService gksUpdate");
		UserProfile profile = api.getUserProfile();
		prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
		Editor editor = prefs.edit();
		profile.chartRatio = graph;
		editor.putString("chartRatio", graph);
		editor.commit();
	}
	
	private void updateValues(){
		Log.v("Gks updateValues"," IntentService gksUpdate");
		UserProfile profile = api.getUserProfile();
		prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
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
