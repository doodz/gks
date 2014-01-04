package com.gks2.app;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gks2.api.scrapper.BookMarkEntry;
import com.gks2.api.scrapper.BookMarkRequest;
import com.gks2.api.scrapper.God;

public class BookMarkActivity extends FragmentActivity implements ActionBar.TabListener {

	/**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    
    private List<List<BookMarkEntry>> lst = null;
    
    final static Integer TAB_NB = 4;
    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;

    private String getFragmentTag(int pos){
        return "android:switcher:"+R.id.pager+":"+pos;
    }
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(TAB_NB);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });
        
        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        
        this.lst = ( List<List<BookMarkEntry>>) getLastCustomNonConfigurationInstance();
    }

    @Override
    protected void onResume() {
    super.onResume();
    
    final Observer onResult = new Observer() {
		@SuppressWarnings("unchecked")
		@Override 
		public void update(Observable observable, Object data) {
			
			
			BookMarkActivity.this.lst = (List<List<BookMarkEntry>>)data;
			setTabValues();

		}
	};  
		if(BookMarkActivity.this.lst == null)
			God.getInstance(this.getApplicationContext()).getBookMark(new BookMarkRequest(), onResult);
		else
			setTabValues();
    }
    
   
    
    private void setTabValues(){

    	if(this.lst != null){
    		
    		for(int i =0; i < TAB_NB; i++ ){
				BookMarkListFragment f = (BookMarkListFragment) BookMarkActivity.this.getSupportFragmentManager().findFragmentByTag(getFragmentTag(i));
				if(f != null){
					f.setEntries(lst.get(i));
					Log.v(this.getClass().getSimpleName(), String.format("set entries (%s)",  mAppSectionsPagerAdapter.getPageTitle(i)));
				}
			}
    		
    	}
    	
    }
    
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());  
       
    }
    
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
    
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
           	 return lst;
    }
    
    
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public class AppSectionsPagerAdapter extends FragmentPagerAdapter {
    	
    	SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    	
        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0: return new BookMarkListFragment();
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    //return new LaunchpadSectionFragment();
                	
                case 1: return new BookMarkListFragment();
                case 2: return new BookMarkListFragment();
                case 3: return new BookMarkListFragment();
                default:
                    // The other sections of the app are dummy placeholders.
                    Fragment fragment = new DummySectionFragment();
                    Bundle args = new Bundle();
                    args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
                    fragment.setArguments(args);
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            return TAB_NB;
        }

        @Override
        public CharSequence getPageTitle(int position) {
        	
        	switch (position) 
        	{ 
	        	case 0 : return getString( R.string.bookmark_torrents);
	        	case 1 : return getString( R.string.bookmark_forums);
	        	case 2 : return getString( R.string.bookmark_wiki);
	        	case 3 : return getString( R.string.bookmark_requests);
	        	default : return "Section " + (position + 1);
        	}

        }
    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class LaunchpadSectionFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);

            // Demonstration of a collection-browsing activity.
            rootView.findViewById(R.id.demo_collection_button)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), CollectionBookMarkActivity.class);
                            startActivity(intent);
                        }
                    });

            // Demonstration of navigating to external activities.
            rootView.findViewById(R.id.demo_external_activity)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Create an intent that asks the user to pick a photo, but using
                            // FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET, ensures that relaunching
                            // the application from the device home screen does not return
                            // to the external activity.
                            Intent externalActivityIntent = new Intent(Intent.ACTION_PICK);
                            externalActivityIntent.setType("image/*");
                            externalActivityIntent.addFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                            startActivity(externalActivityIntent);
                        }
                    });

            return rootView;
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);
            Bundle args = getArguments();
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                    getString(R.string.dummy_section_text, args.getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
}
