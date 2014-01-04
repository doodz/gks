package com.gks2.api.scrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.protocol.HTTP;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

public abstract class HttpAsyncTask<Params, Result> extends AsyncTask<Params, Void, Result> {
	protected Browser browser;
	public void setBrowser(Browser browser) { this.browser = browser; }
	
	protected UserProfile profile;
	public void setUserProfile(UserProfile profile) { this.profile = profile; }
	
	@Override
	protected void onPostExecute(Result result) {
		this.ringListeners(result);
	};
	
	private CompoObservable listener = new CompoObservable();
	public void addObserver(Observer observer) {
		this.listener.addObserver(observer);
	}
	
	protected void ringListeners(Object result) {
		this.listener.notifyObservers(result);
	}
	
	public static InputStream getUngzipedInputStream(HttpResponse response) {
		try {
			return AndroidHttpClient.getUngzippedContent(response.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getCharset(HttpResponse response) {
		HttpEntity entity = response.getEntity();

		if (entity.getContentType() != null) {
			HeaderElement values[] = entity.getContentType().getElements();

			if (values.length > 0) {
				NameValuePair param = values[0].getParameterByName("charset");

				if (param != null) return param.getValue();
			}
		}
		
		return HTTP.DEFAULT_CONTENT_CHARSET;
	}

	public static String getStringContent(HttpResponse response) {
		try {
			HttpEntity entityResponse = response.getEntity();
			InputStream is = AndroidHttpClient.getUngzippedContent(entityResponse);
			
			InputStreamReader reader = new InputStreamReader(is, getCharset(response));
			BufferedReader bufferedReader = new BufferedReader(reader);
			StringBuffer result = new StringBuffer();
			String extract;
			while ((extract = bufferedReader.readLine()) != null) result.append(extract);
			bufferedReader.close();
			reader.close();
			is.close();
			entityResponse.consumeContent();
			return result.toString();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return "";
	}
	
	private class CompoObservable extends Observable {
		@Override
		public void notifyObservers(Object arg) {
			System.out.println(super.countObservers());
			if (super.countObservers() <= 0) return;
			super.setChanged();
			super.notifyObservers(arg);
		}
	}
}
