package com.gks2.api.scrapper;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.preference.PreferenceManager;
import android.util.Log;
/**
*This class is responsible for the simulation of a Web browser.
 * */
public class Browser {
	
	protected String UserAgent = "Android";//"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.142 Safari/535.19";
	protected long GraceTime = 1000; // Minimum period of inactivity between two queries
	protected Boolean CookieStore = false;
	
	protected AndroidHttpClient client;
	protected HttpContext clientContext;
	protected BlockingQueue<HttpUriRequest> taskQueue = new ArrayBlockingQueue<HttpUriRequest>(10);
	protected long timestampOfLastResponse;
	protected CookieStore cookieStore;
	private Context AppContext = null;
	
	/**
	 * Determine if app Have an Internet Connection
	 * @return
	 */
	public Boolean HaveInternet(){
		
		ConnectivityManager cm =
		        (ConnectivityManager)AppContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
		                      activeNetwork.isConnectedOrConnecting();
		
		return isConnected;
	}  
	
	public Browser(Context AppContext) {
		
		this.AppContext = AppContext;
		initPrefBrowser();
		this.client = AndroidHttpClient.newInstance(UserAgent); // The "navigator" who will scrapper
		this.clientContext = new BasicHttpContext(); // it is to have a more realistic "browser"
		this.clientContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore); // to keep cookies
		this.timestampOfLastResponse = 0;
	}
	
	private void initPrefBrowser(){
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AppContext);
		this.UserAgent = prefs.getString("pref_useragent", "Android");
		this.GraceTime = Integer.parseInt(prefs.getString("pref_gracetime", "1000"));
		this.CookieStore = prefs.getBoolean("pref_cookiestore", false);
		if(this.CookieStore)
			cookieStore = new com.gks2.helper.PersistentCookieStore(prefs);
		else
			cookieStore = new BasicCookieStore();
	}
	
	protected long getGraceDuration() {
		return GraceTime - (System.currentTimeMillis()-timestampOfLastResponse);
	}
	
	public HttpResponse execute(HttpUriRequest request) {
		try {
			this.taskQueue.put(request); // If an request is already running, it will be blocked
			AndroidHttpClient.modifyRequestToAcceptGzipResponse(request);
			
			long graceDuration = getGraceDuration();
			if (graceDuration > 0) {
				Log.v("Browser", String.format("sleep (%d ms)", graceDuration));
				Thread.sleep(graceDuration); // It enforces the timeout
				Log.v("Browser", "wake-up");
			}
			
			HttpResponse result = this.client.execute(request, clientContext);
			
			this.timestampOfLastResponse = System.currentTimeMillis(); //
			this.taskQueue.take(); // We open the cage
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
