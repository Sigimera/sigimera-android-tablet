package org.sigimera.frontends.android.tablet;

import java.io.InputStream;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements ActionBar.TabListener {
	private static final int REQUEST_CODE = 1;
	private View actionBarView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ActionBar actionBar = getActionBar();

        actionBar.addTab(actionBar.newTab().setText("By Type").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("By Country").setTabListener(this));
        
        this.actionBarView = getLayoutInflater().inflate(R.layout.action_bar_custom, null);
        
        actionBar.setCustomView(this.actionBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowHomeEnabled(true);
        
        if(savedInstanceState != null) {
            int selectedTab = savedInstanceState.getInt("selectedTab");
            actionBar.selectTab(actionBar.getTabAt(selectedTab));
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case R.id.share:
    			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
    			/**
    			 * TODO: Share here the crisis URL...
    			 */
    			shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "http://www.sigimera.org");
    			shareIntent.setType("text/plain");
    			this.startActivity(shareIntent);
    			return true;
    		case R.id.camera:
    			Intent intent = new Intent();
    			intent.setType("image/*");
    			intent.setAction(Intent.ACTION_GET_CONTENT);
    			intent.addCategory(Intent.CATEGORY_OPENABLE);
    			startActivityForResult(intent, REQUEST_CODE);
    			return true;
    		case R.id.toggleTitles:
    			FragmentManager fragManager = getFragmentManager();
    			CrisisListFragment crisisListFrag = (CrisisListFragment) fragManager.findFragmentById(R.id.crisis_list);
    			if ( crisisListFrag.isHidden() ) {
    				fragManager.beginTransaction().show(crisisListFrag).commit();
    			} else {
    				fragManager.beginTransaction().hide(crisisListFrag).commit();
    			}
    				
    			return true;
    		case R.id.showSettings:
    			Intent settings = new Intent(MainActivity.this, Settings.class);
    			this.startActivity(settings);
    			return true;
    		case R.id.showAboutDialog:
    			this.showAboutDialog();
    			return true;
    	}
    	return false;
    }
    
    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
    	super.onActivityResult(_requestCode, _resultCode, _data);
    	
    	if ( _resultCode == RESULT_CANCELED ) {
    		Toast toast = Toast.makeText(this, "Process Cancelled!", 10000);
    		toast.show();
    	} else if ( _requestCode == REQUEST_CODE && _resultCode == RESULT_OK ) {
			try {
				InputStream stream = getContentResolver().openInputStream(_data.getData());
				Bitmap bitmap = BitmapFactory.decodeStream(stream);
	    		stream.close();
	    		/**
	    		 * TODO: Call here a upload dialog...
	    		 */
	    		Toast.makeText(this, "Image found... " + bitmap.getHeight() + " x " + bitmap.getWidth(), 30000).show();
			} catch (Exception e) {
				Toast.makeText(this, "This file is no image!", 10000).show();
			} 
    	}
    }
    
    private void showAboutDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        DialogFragment newFragment = AboutDialogFragment.newInstance();
        newFragment.show(ft, "dialog");
    }

	/**
	 * @see android.app.ActionBar.TabListener#onTabReselected(android.app.ActionBar.Tab, android.app.FragmentTransaction)
	 */
	@Override
	public void onTabReselected(Tab _tab, FragmentTransaction _ft) {
		FragmentManager fragManager = getFragmentManager();
		CrisisListFragment crisisListFrag = (CrisisListFragment)fragManager.findFragmentById(R.id.crisis_list);
		if ( crisisListFrag.isHidden() ) {
			fragManager.beginTransaction().show(crisisListFrag).commit();
		}
	}

	/**
	 * @see android.app.ActionBar.TabListener#onTabSelected(android.app.ActionBar.Tab, android.app.FragmentTransaction)
	 */
	@Override
	public void onTabSelected(Tab _tab, FragmentTransaction _ft) {
		FragmentManager fragManager = getFragmentManager();
		CrisisListFragment crisisListFrag = (CrisisListFragment)fragManager.findFragmentById(R.id.crisis_list);
		if ( crisisListFrag.isHidden() ) {
			fragManager.beginTransaction().show(crisisListFrag).commit();
		}
		
		crisisListFrag.populateCrisisList(_tab.getPosition());
	}

	/**
	 * @see android.app.ActionBar.TabListener#onTabUnselected(android.app.ActionBar.Tab, android.app.FragmentTransaction)
	 */
	@Override
	public void onTabUnselected(Tab _tab, FragmentTransaction _ft) {}
	
    @Override
    public void onSaveInstanceState (Bundle _outState) {
        super.onSaveInstanceState(_outState);
        ActionBar bar = getActionBar();
        int category = bar.getSelectedTab().getPosition();
        _outState.putInt("selectedTab", category);
    }
    
}