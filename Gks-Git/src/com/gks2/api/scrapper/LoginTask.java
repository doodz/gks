package com.gks2.api.scrapper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class LoginTask extends HttpAsyncTask<String, Integer> {
	protected final static String authUrl = "https://gks.gs/login";
	public final static int SUCCES_CODE = 2;
	public final static int INCORRECT_USERNAME_CODE = 4;
	public final static int INCORRECT_PASSWORD_CODE = 8;
	
	@Override
	protected Integer doInBackground(String... params) {
		Log.v("LoginTask", "start");
		HttpPost logRequest = new HttpPost(authUrl);
		logRequest.addHeader("X-Requested-With", "XMLHttpRequest");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
        
        nameValuePairs.add(new BasicNameValuePair("username", params[0]));
        nameValuePairs.add(new BasicNameValuePair("password", params[1]));
        nameValuePairs.add(new BasicNameValuePair("login", " Connexion "));
        
        HttpEntity e = null; 
        try {
			e = new UrlEncodedFormEntity(nameValuePairs);
		} catch (UnsupportedEncodingException e1) { e1.printStackTrace(); }
        
		logRequest.setEntity(e);
		HttpResponse response = browser.execute(logRequest);
		
		String s = super.getStringContent(response);
		Integer result = (s.contains("Votre nom d'utilisateur est incorrect"))?INCORRECT_USERNAME_CODE:
			(s.contains("Votre mot de passe est incorrect"))?INCORRECT_USERNAME_CODE: 
				SUCCES_CODE;
		
		Log.v("LoginTask", "finished "+result);
		return result;
	}
}
