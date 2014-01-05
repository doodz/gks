package com.gks2.api.scrapper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;



public class UserProfile {
	
	public String username = "";
	public String userid = "";
	public String registeringDate = "";
	public String lastVisiteDate = "";
	
	public Bitmap avatarBMP = null;
	//public DataSize uploadedTotalData = null;
	//public DataSize downloadedTotalData = null;
	public String uploadedTotalData = "";
	public String downloadedTotalData = "";
	public String uploadedDailyData = "";
	public String downloadedDailyData = "";
	public String userclass = "";
	public String usermail = "";
	public String userip = "";
	public String karma = "";
	public String aura = "";
	public String ratio = "";
	public String ratioreq = "";
	public String authKey = "";
	public String chartRatio = "";
	public String avatar = "";
	
	public void fill(Context context){
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		this.username = prefs.getString("pref_username"," ");
		this.userid = prefs.getString("pref_password"," "); 
		this.registeringDate = prefs.getString("registeringDate"," ");
		this.lastVisiteDate = prefs.getString("lastVisiteDate"," ");
		this.uploadedTotalData = prefs.getString("uploadedTotalData"," ");
		this.downloadedTotalData = prefs.getString("downloadedTotalData"," ");
		this.uploadedDailyData = prefs.getString("uploadedDailyData"," ");
		this.downloadedDailyData = prefs.getString("downloadedDailyData"," ");
		this.userclass = prefs.getString("userclass"," ");
		this.usermail = prefs.getString("usermail"," ");
		this.userip = prefs.getString("userip"," ");
		this.karma = prefs.getString("karma"," ");
		this.aura = prefs.getString("aura"," ");
		this.ratio= prefs.getString("ratio"," ");
		this.ratioreq = prefs.getString("ratioreq"," ");
		this.authKey = prefs.getString("authKey"," ");
		this.avatar = prefs.getString("avatar"," ");
	}
	
	
}
