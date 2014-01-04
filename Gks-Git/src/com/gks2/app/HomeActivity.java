package com.gks2.app;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gks2.api.scrapper.God;
import com.gks2.api.scrapper.LoginTask;
import com.gks2.api.scrapper.UserProfile;
import com.google.analytics.tracking.android.EasyTracker;

public class HomeActivity extends Activity {
	
	private ProgressDialog ringProgressDialog = null;	
	private God api;
	WebView details_www;
	SharedPreferences prefs;
	private Thread rebour;
    private Calendar maintenant;
    private TextView horloge;
    private long diff;
    private long milli;
    private long sec;
    private long min;
    private long heure;
    private long jour;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] mMenuTitles;
    private Handler handler = new Handler() {
        
        public void handleMessage(android.os.Message msg) {
        if(msg.what == 0) {
        horloge.setText(String.valueOf(jour) + " : " + String.valueOf(heure) + " : " + String.valueOf(min) + " : " + String.valueOf(sec) + " : " + String.valueOf(milli));
        ;
        }};};
    
	public HomeActivity(){
		//this.api = God.getInstance();
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  // Save UI state changes to the savedInstanceState.
	  // This bundle will be passed to onCreate if the process is
	  // killed and restarted.
	  final TextView freeLeechField = ((TextView) findViewById(R.id.freeLeechField));
	  String fl = freeLeechField.getText().toString();
	  savedInstanceState.putString("MyFl", fl);
	 
	  // etc.
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onRestoreInstanceState(savedInstanceState);
	  // Restore UI state from the savedInstanceState.
	  // This bundle has also been passed to onCreate.
	  final TextView freeLeechField = ((TextView) findViewById(R.id.freeLeechField));
	  String fl = savedInstanceState.getString("MyFl");
	  freeLeechField.setText(fl);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_home);

		initDrawer();

		prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
		
		if (prefs.getBoolean("firstLogin", true)) {
            Intent myIntent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(myIntent);
            finish();
        } else if (prefs.getBoolean("firstSettings", true)) {
            Intent myIntent = new Intent(HomeActivity.this, SettingsActivity.class);
            final Editor e = prefs.edit();
    		e.putBoolean("firstSettings",false );
    		e.commit();
            startActivity(myIntent);
        }
        else
        	initFreeLeech();
        
	}
	
	@Override
    public void onStart() {
		super.onStart();
		 EasyTracker.getInstance(this).activityStart(this);
		if(api == null || api.getUserProfile() == null)
			 HomeActivity.this.doLogin(
						prefs.getString("pref_username", "unamed"),
						prefs.getString("pref_password", "p4ssw0rd"));
		
	}

	/**
	 * Handle Navigation Click Events
	 */
	private void initDrawer(){
		
		mTitle = mDrawerTitle = getTitle();
		this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
                ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		this.mMenuTitles = getResources().getStringArray(R.array.menu);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mMenuTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        		@Override
    			public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
    					long id) {
        			selectedItem(position);
        		}	
        });
        
        // Open and Close with the App Icon
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
	
	/**
	 *  Highlight the selected item, update the title, and close the drawer
	 * @param position
	 */
	private void selectedItem(int position) {
		
		String[] menu = getResources().getStringArray(R.array.menu);
	    mDrawerList.setItemChecked(position, true);
	    setTitle(menu[position]);
	    mDrawerLayout.closeDrawer(mDrawerList);
	   
	    // Cannot switch on a value of type String for source level below 1.7
	    switch (position) {
	    case 0:
	    	//Account
	    	break;
	    case 1 :
	    	displayBookMark();
	    	//bookmark
	    	break;
	    case 2 :
	    	//snatched
	    	break;
	    case 3 :
	    	//serieswatch
	    	break;
	    case 4 :
	    	//Pictures
	    	break;
	    case 5 :
	    	//autoget
	    	break;
	    case 6 :
	    	//Mailbox
	    	break;
	    case 7 :
	    	//Top torrent
	    	break;
	    case 8 :
	    	//My Statistics
	    	break;
	    case 9 :
	    	displaySettings();
	    	//Settings
	    	break;
	    case 10 :
	    	showAboutMenu();
	    	//About
	    	break;
	    default :
	    	
	    } 
	}
	
	private void displayBookMark(){
		Intent myIntent = new Intent(this, BookMarkActivity.class);
		HomeActivity.this.startActivity(myIntent);
	}
	
	private void displaySettings(){
		Intent myIntent = new Intent(this, SettingsActivity.class);
		HomeActivity.this.startActivity(myIntent);
	}
	
	/**
	 * Set the action bar's title
	 * @param title
	 */
	@Override
	public void setTitle(CharSequence title) {
	    mTitle = title;
	    getActionBar().setTitle(mTitle);
	}
	
	/**
	 * Called when create the activity
	 * @param u
	 * @param p
	 */
	protected void doLogin(String u, String p) {
		
		this.api = God.getInstance(getApplicationContext());
		final Observer onLogin = new Observer() {
			@Override
			//if executeLogin return LoginTask.SUCCES_CODE task call update 
			public void update(Observable observable, Object data) {
				
				Integer code = (Integer) data;
				if (code != LoginTask.SUCCES_CODE){
					Intent myIntent = new Intent(HomeActivity.this, MainActivity.class);
			        startActivity(myIntent);
					HomeActivity.this.finish();
				}
				else{
					UserProfile profile = api.getUserProfile();
					profile.fill(getBaseContext());
					HomeActivity.this.StopRingDialog();				
					String str = prefs.getString("ratio",null);
					if(str == null)
			     		updateUserValues();
			     	else
			     	{
			     		setChartRatio(null);
						setUserValues();
			     	}
				}
			}
		};
		
		if(prefs.getBoolean("pref_cookiestore", false)
				&& !prefs.getString("cookies", "").equalsIgnoreCase("")){
			api.LoginFromCookies();
			UserProfile profile = api.getUserProfile();
			profile.fill(getBaseContext());
			HomeActivity.this.StopRingDialog();				
			String str = prefs.getString("ratio",null);
			if(str == null)
	     		updateUserValues();
	     	else
	     	{
	     		setChartRatio(null);
				setUserValues();
	     	}
			
		}else{
			this.launchRingDialog();	
	    	api.executeLogin(u, p, onLogin);
		}
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.search, menu);
		//return true;
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.home, menu);
	    return true;
	}
	
	/**
	 * 
	 * @param item
	 * @return
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
		
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			//NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.searchmenu :
			displaySearch();
			return true;
		case R.id.refreshmenu :
			updateUserValues();
			return true;
		case R.id.helpmenu :
			showAboutMenu();
			return true;
		/*case R.id.refreshmenu:
			refresh();
				return true;*/
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void showAboutMenu(){
		Intent myIntent = new Intent(this, AboutActivity.class);
		HomeActivity.this.startActivity(myIntent);
	}
	
	private void displaySearch(){
		Intent myIntent = new Intent(this, SearchActivity.class);
		HomeActivity.this.startActivity(myIntent);
	}
	
	private void launchRingDialog() {
        this.ringProgressDialog = ProgressDialog.show(HomeActivity.this,  HomeActivity.this.getString(R.string.progress_dialog_title_connecting), "  ", true);
        this.ringProgressDialog.setCancelable(true);
	 }
 
	private void StopRingDialog(){		
		if(HomeActivity.this.ringProgressDialog != null 
				&& HomeActivity.this.ringProgressDialog.isShowing())
		 HomeActivity.this.ringProgressDialog.dismiss();
	 }
	 
	 /**
	  * Called when the activity is resumed or created
	  */
	public void setUserValues(){
 
		final TextView ratioCtrl = ((TextView) findViewById(R.id.ratioField));
		final TextView upField = ((TextView) findViewById(R.id.upField));
		final TextView downField = ((TextView) findViewById(R.id.downField));
		final TextView nameField = ((TextView) findViewById(R.id.nameField));
		final TextView karmaField = ((TextView) findViewById(R.id.karmaField));
		final TextView auraField = ((TextView) findViewById(R.id.auraField));
		final TextView userclassField = ((TextView) findViewById(R.id.userclassField));
		final TextView userName = ((TextView) findViewById(R.id.userNameField));
		//final UserProfile profile = api.getUserProfile();
		
		getString(R.string.gks_username);
		ratioCtrl.setText(prefs.getString("ratio",getString(R.string.ratio)));
		upField.setText(prefs.getString("uploadedTotalData",getString(R.string.gks_GoLeft)));
		downField.setText(prefs.getString("downloadedTotalData",getString(R.string.gks_GoLeft)));
		nameField.setText(prefs.getString("username",getString(R.string.gks_username)));
		karmaField.setText(prefs.getString("karma",getString(R.string.gks_GoLeft)));
		auraField.setText(prefs.getString("aura",getString(R.string.gks_GoLeft)));
		userclassField.setText(prefs.getString("userclass",getString(R.string.gks_class)));
		userName.setText(prefs.getString("pref_username",getString(R.string.gks_username)));

	 }
	 
	 /**
	  * Display the chartRatio on the view
	  * @param str
	  */
	private void setChartRatio(String str){
		 final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); 
		 	if(str == null)
		 		str = prefs.getString("chartRatio", "");

		 	final WebView details_www = (WebView) findViewById(R.id.chartratio);
		 	
		 	details_www.getSettings().setJavaScriptEnabled(true);
		 	details_www.setWebViewClient(new WebViewClient());
	        details_www.setWebChromeClient(new WebChromeClient());
			details_www.loadDataWithBaseURL(null, str, "text/html", "utf-8", null);
	 }
	 
	/**
	 * Update value for ChartRatio, UserProfil && FreeLeech 
	 */
	public void updateUserValues() {
		 
		 if(api == null || api.getUserProfile() == null)
			 HomeActivity.this.doLogin(
						prefs.getString("pref_username", "unamed"),
						prefs.getString("pref_password", "p4ssw0rd"));
		 else{
		 ManagerUserValue m = new ManagerUserValue( getBaseContext() );
		 final Observer onResult = new Observer() {
				@Override 
				public void update(Observable observable, Object data) {
					setUserValues();
				}
			};
		
		m.UpdateUserProfil(onResult);
		
		final Observer onChartRatio = new Observer() {
			@Override 
			public void update(Observable observable, Object data) {
				setChartRatio((String)data);
			}
		};
		
		m.UpdateChartRatio(onChartRatio);
		
		final Observer onFreeleech = new Observer() {
			@Override 
			public void update(Observable observable, Object data) {
				setFreeLeech((String[])data);
			}
		};
		
		this.api.getFreeLeechState(null, onFreeleech);
		 }
	 }
	
	/**
	 * Push freeleech values on sharedpreferences && call initFreeLeech() if date is not null
	 * @param data 
	 */
	private void setFreeLeech(String[] data){
		 
		 final TextView freeLeechField = ((TextView) findViewById(R.id.freeLeechField));
		 if(data != null){
			 if(HomeActivity.this.prefs != null){
				 final Editor e = prefs.edit();
				 e.putString("fl_type", data[0]);
				 e.putLong("fl_val", Long.parseLong(data[1]));
				 e.commit();
				 initFreeLeech();
			 }
			 freeLeechField.setText(data[0]+data[1]);
		 }else{
			 Toast.makeText(HomeActivity.this,getString(R.string.gks_no_freeLeeh),Toast.LENGTH_SHORT).show();
			 /*HomeActivity.this.doLogin(
						prefs.getString("pref_username", "unamed"),
						prefs.getString("pref_password", "p4ssw0rd"));*/
		 }
		 
	 }
	
	/**
	 * set the timer on home activity if freeleech is set on sharedpreferences
	 */
	private void initFreeLeech(){
		
		if(prefs != null){
			final Long flVal = prefs.getLong("fl_val",0);
			maintenant = Calendar.getInstance();
	    	if((flVal - maintenant.getTimeInMillis()) > 0){
	    		
				horloge = ((TextView) findViewById(R.id.freeLeechField));
				TextView freeL = ((TextView) findViewById(R.id.freeLeechNameField));
				freeL.setText(prefs.getString("fl_type",getString(R.string.gks_no_freeLeeh)));
	    		rebour = new Thread(new Runnable() {
	                
	                public void run() {
	                    while (sec > 0) {
	                    maintenant = Calendar.getInstance();
	                    diff = flVal - maintenant.getTimeInMillis();
	                    milli = diff % 1000;
	                    sec = ((diff - milli)/ 1000) % 60;
	                    min = ((diff - milli - sec)/ (1000 * 60) % 60);
	                    heure = ((diff - milli - sec - min)/ (1000 * 60 * 60) % 24);
	                    jour = ((diff - milli - sec - min - heure)/ (1000 * 60 * 60 * 24));
	                    handler.sendEmptyMessage(0);
	                    try {
	                        Thread.sleep(4000);
	                    } catch (InterruptedException e) {
	                        // TODO Auto-generated catch block
	                        e.printStackTrace();
	                    }
	                    }
	                }
	        }
	        );
	            rebour.start();
			}else{
				final Editor e = prefs.edit();
				 e.putString("fl_type", "");
				 e.putLong("fl_val", 0);
				 e.commit();
			}
	    	
		}
	}
	
	/**
	 *
	 */
	@Override
	protected void onStop() {
	    super.onStop();
	    
	    if(HomeActivity.this.ringProgressDialog != null && HomeActivity.this.ringProgressDialog.isShowing())
	    {
	    	HomeActivity.this.ringProgressDialog.dismiss();
	    	HomeActivity.this.ringProgressDialog = null;
	    }
	    EasyTracker.getInstance(this).activityStop(this);
	}
}
