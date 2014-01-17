package com.gks2.api.scrapper;

import java.util.Observable;
import java.util.Observer;

import com.gks2.app.R;
import com.gks2.app.SearchActivity;

import android.content.Context;
import android.widget.Toast;

public class God {
	
	protected Browser browser = null;
	protected UserProfile ownerProfile = null;
	protected static God mInstance = null;
	private Context AppContext = null;
	public Boolean isLoged = false;
	
	public God(Context AppContext) {
		this.AppContext = AppContext;
		 this.browser = new Browser(AppContext);
	}

	public static God getInstance(Context AppContext) {
        if( mInstance == null ) {
            mInstance = new God(AppContext);
        }
        return mInstance;
    }
	
	private void makeToastNoNetWork(){
		if(AppContext != null)
		Toast.makeText(this.AppContext, this.AppContext.getString(R.string.progress_dialog_is_not_connected), 
				Toast.LENGTH_SHORT).show();	
	}
	
	/// gsk userInfo
	public UserProfile getUserProfile() { return this.ownerProfile; }
	
	public void LoginFromCookies(){
		ownerProfile = new UserProfile();
		God.this.isLoged = true;		
	}
	
	/// login on gsk
	public void executeLogin(final String login, final String password, final Observer onResult) {
		
		if(this.browser.HaveInternet()){
			LoginTask task = new LoginTask();
			
			// God needs the result of this task
			final Observer proxyObserver = new Observer() {
				@Override
				public void update(Observable o, Object arg) {
					Integer code = (Integer) arg;
					if (code == LoginTask.SUCCES_CODE){ 
						ownerProfile = new UserProfile();
						God.this.isLoged = true;
					}
					else ownerProfile = null;
					
					// come on, we do pass :)
					onResult.update(o, arg);
				}
			};
			
			prepareTask(task, proxyObserver);
				task.execute(login, password);
		}else{
			this.makeToastNoNetWork();
		}
	}
	
	public void retrieveUserProfil(String request, Observer onResult) {
		
		if(this.browser.HaveInternet()){
			if (this.ownerProfile == null) throw new IllegalStateException("Must be logged in to retrieve his profile");
			GetUserProfilTask task = new GetUserProfilTask();
			prepareTask(task, onResult);
			if (request == null) task.execute(); 
			else task.execute(request);
		}else{
			this.makeToastNoNetWork();
		}
	}
	
	public void retriveRatioHistory(String request, Observer onResult){
		
		if(this.browser.HaveInternet()){
			if (this.ownerProfile == null) throw new IllegalStateException("Must be logged in to retrieve his profile");
			GetRatiohistoryTask task = new GetRatiohistoryTask();
			prepareTask(task, onResult);
			if (request == null) task.execute(); 
			else task.execute(request);
		}else{
			this.makeToastNoNetWork();
		}
		
	}
	public void search(SearchTorrentRequest request, Observer onResult) {
		
		if(this.browser.HaveInternet()){
			if (this.ownerProfile == null) throw new IllegalStateException("Must be logged in to retrieve his profile");
			SearchTorrentTask task = new SearchTorrentTask();
			
			/*final Observer proxyObserver = new Observer() {
				@Override
				public void update(Observable o, Object arg) {
					List<SearchEntry> result  = (List<SearchEntry>) arg;
					int i = 0;
					i++;
				}
			};*/
			
			prepareTask(task, onResult);
			task.execute(request);
		}else{
			this.makeToastNoNetWork();
		}
	}
	
	private void prepareTask(HttpAsyncTask<?, ?> task, Observer onResult) {
		task.setBrowser(browser);
		task.setUserProfile(ownerProfile);
		task.addObserver(onResult);
		
	}
	
	public void downloadTorrent(String torrentPash, Observer onResult) {
		
		if(this.browser.HaveInternet()){
			if (this.ownerProfile == null) throw new IllegalStateException("Il faut �tre logg� pour r�cup�rer un torrent");
			DownloadTorrentTask task = new DownloadTorrentTask();
			prepareTask(task, onResult);
			task.execute(torrentPash);
		}else{
			this.makeToastNoNetWork();
		}
	}
	
	public void downloadTorrent(SearchEntry torrentEntry, Observer onResult) {
		
		downloadTorrent(torrentEntry.dlLocation, onResult);
	}
	
	public void getTorrentPres(String torrent_URL, Observer onResult) {
		
		if(this.browser.HaveInternet()){
			if (this.ownerProfile == null) throw new IllegalStateException("Must be logged in to retrieve his profile");
			GetTorrentDetailTask task = new GetTorrentDetailTask();
			prepareTask(task, onResult);
			task.execute(torrent_URL);
		}else{
			this.makeToastNoNetWork();
		}
	}
	
	public void getChartRatio(String request, Observer onResult){
		
		if(this.browser.HaveInternet()){
			if (this.ownerProfile == null) throw new IllegalStateException("Must be logged in to retrieve his profile");
			GetChartRatioTask task = new GetChartRatioTask();
			prepareTask(task, onResult);
			task.execute();
		}else{
			this.makeToastNoNetWork();
		}
	}
	
	public void getFreeLeechState(String request, Observer onResult){
		if(this.browser.HaveInternet()){
			if (this.ownerProfile == null) throw new IllegalStateException("Must be logged in to retrieve his profile");
			GetFreeLeechStateTask task = new GetFreeLeechStateTask();
			prepareTask(task, onResult);
			task.execute();
		}else{
			this.makeToastNoNetWork();
		}
	}
	
	public void addBookMark(String request, Observer onResult){
		
		if(this.browser.HaveInternet()){
			if (this.ownerProfile == null) throw new IllegalStateException("Must be logged in to retrieve his profile");
			BookMarkTorrentTask task = new BookMarkTorrentTask();
			prepareTask(task, onResult);
			task.execute(request);
		}else{
			this.makeToastNoNetWork();
		}
	}
	
	
	public void setAjaxRequest(AjaxRequest request, Observer onResult)
	{
		if(this.browser.HaveInternet()){
			if (this.ownerProfile == null) throw new IllegalStateException("Must be logged in to retrieve his profile");
			AjaxTask task = new AjaxTask();
			prepareTask(task, onResult);
			task.execute(request);		
		}else{
			this.makeToastNoNetWork();
		}
	}
	
	public void getBookMark(BookMarkRequest request, Observer onResult)
	{
		if(this.browser.HaveInternet()){
			if (this.ownerProfile == null) throw new IllegalStateException("Must be logged in to retrieve his profile");
			BookMarkTask task = new BookMarkTask();
			prepareTask(task, onResult);
			task.execute(request);
		}else{
			this.makeToastNoNetWork();
		}
	}
	
	public void getSnatched(String request, Observer onResult)
	{
		if(this.browser.HaveInternet()){
			if (this.ownerProfile == null) throw new IllegalStateException("Must be logged in to retrieve his profile");
			GetSnatchedTask task = new GetSnatchedTask();
			prepareTask(task, onResult);
			task.execute(request);
		}else{
			this.makeToastNoNetWork();
		}
	}
	
}
