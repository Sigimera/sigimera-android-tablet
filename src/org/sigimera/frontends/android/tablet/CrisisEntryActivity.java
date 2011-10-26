/**
 * Copyright (C) 2011 by Sigimera
 * All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>
 */

package org.sigimera.frontends.android.tablet;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.jaxen.JaxenException;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.XPath;
import org.jaxen.dom4j.Dom4jXPath;
import org.sigimera.frontends.android.tablet.data.CrisisEntity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Visualization of a single crisis entity.
 * 
 * @author Alex Oberhauser
 */
public class CrisisEntryActivity extends Activity implements OnClickListener {
	private static final int REQUEST_CODE = 1;
	private String crisisID;
	
	private ActionBar actionBar;
	private OnClickListener mapClickListener = this;
	
	private CrisisEntity crisis;
	
	private ImageView imageView;
	private Drawable imageDrawable;
	
	private final Handler guiHandler = new Handler();
	private final Runnable updateCrisisEntry = new Runnable() {
		@Override
		public void run() {
			try {
				updateCrisisEntryInGUI();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	private final Runnable updateImageView = new Runnable() {
		@Override
		public void run() {
	    	imageView.setImageDrawable(imageDrawable);
	    	imageView.setAlpha(0f);
	    	imageView.animate().setDuration(1500).alpha(1f);
	    	imageView.setClickable(true);
	    	imageView.setOnClickListener(mapClickListener);
		}
	};
	
	private synchronized Node selectNode(String _xpath, Document _doc) throws JaxenException {
		 HashMap<String, String> nsMap = new HashMap<String, String>();
		 nsMap.put("dc", "http://purl.org/dc/elements/1.1/");
		 nsMap.put("dct", "http://purl.org/dc/terms/");
		 nsMap.put("crisis", "http://ns.sigimera.org/crisis.owl#");
		 nsMap.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		 nsMap.put("foaf", "http://xmlns.com/foaf/0.1/");
		 nsMap.put("geo", "http://www.w3.org/2003/01/geo/wgs84_pos#");
		 
		 XPath xpath = new Dom4jXPath(_xpath);
		 xpath.setNamespaceContext(new SimpleNamespaceContext(nsMap));
		 
		 return (Node) xpath.selectSingleNode(_doc);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crisis_entry);

        View v = findViewById(R.id.crisisEntryMain);
        v.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
        
    	this.crisisID = null;

    	Bundle bundle = getIntent().getExtras();
    	if ( savedInstanceState != null ) {
    		this.crisisID = savedInstanceState.getString("crisisid");
    		this.crisis = (CrisisEntity) savedInstanceState.getSerializable("crisisObj");
    	} else if ( bundle != null ) {
    		this.crisisID = bundle.getString("crisisid");
    	}

        this.actionBar = getActionBar();
        
        this.actionBar.setTitle(this.crisisID);
        this.actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        this.actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        this.actionBar.setDisplayShowHomeEnabled(true);
        this.actionBar.setDisplayHomeAsUpEnabled(true);
    	
    	if ( this.crisis == null ) {
    		try {
				this.crisis = new CrisisEntity(new URI(this.crisisID));
		        
		        Thread worker = new Thread() {
					@Override
					public void run() {
				    	SAXReader reader = new SAXReader();
				    	Document doc;
						try {
							doc = reader.read(new URL(crisisID));
	
							Node title = selectNode("//rdf:Description/dc:title", doc);
							if ( title != null )
								crisis.setTitle(title.getStringValue());
								
							Node description = selectNode("//rdf:Description/dc:description", doc);
							if ( description != null )
								crisis.setDescription(description.getStringValue());
							
							Node issued = selectNode("//rdf:Description/dct:issued", doc);
							if ( issued != null )
								crisis.setIssued(issued.getStringValue());
							
							Node latitude = selectNode("//rdf:Description/geo:lat", doc);
							if ( latitude != null )
								crisis.setLatitude(latitude.getStringValue());
							
							Node longitude = selectNode("//rdf:Description/geo:long", doc);
							if ( longitude != null )
								crisis.setLongitude(longitude.getStringValue());
							
							Node crisisType = selectNode("//rdf:Description/crisis:hasCrisisType/@rdf:resource", doc);
							if ( crisisType != null )
								crisis.setCrisisType(crisisType.getStringValue());
							
							Node crisisLevel = selectNode("//rdf:Description/crisis:alertLevelAsgard", doc);
							if ( crisisLevel == null )
								crisisLevel = selectNode("//rdf:Description/crisis:alertLevelGdas", doc);
							if ( crisisLevel != null )
								crisis.setRiskLevel(crisisLevel.getStringValue());
							
							if ( longitude != null && latitude != null ) {
								URL depictionURL = new URL("http://staticmap.openstreetmap.de/staticmap.php?center=" + 
										latitude.getStringValue() + "," + 
										longitude.getStringValue() + "&zoom=4&markers=" + 
										latitude.getStringValue() + "," + 
										longitude.getStringValue() + 
										",ol-marker&size=470x230&maptype=osmarenderer");
				    			if ( depictionURL != null ) {
									crisis.setDepictionURL(depictionURL.toString());
				    			}
							}
							
							/**
							 * TODO: Set more values, e.g. location, risk level, ...
							 */
							
			        		guiHandler.post(updateCrisisEntry);
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (DocumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JaxenException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
		        };
		        worker.start();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	} else {
    		try {
				this.updateCrisisEntryInGUI();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}

    }
    
    private void setTableValue(int _textviewID, String _text) {
    	TextView textView = (TextView)findViewById(_textviewID);
    	if ( _text != null && !_text.equals("") ) {
    		if ( textView != null )
    			textView.setText(_text);
    	}
    }
    
    public void updateCrisisEntryInGUI() throws MalformedURLException, DocumentException {
    	this.actionBar.setTitle(this.crisis.getTitle());
    
    	TextView crisisTitle = (TextView)findViewById(R.id.crisisEntryTitle);
    	crisisTitle.setText(this.crisis.getTitle());
    	
    	TextView crisisID = (TextView)findViewById(R.id.crisisEntryType);
    	crisisID.setText(this.crisis.getCrisisType());
    	
    	String riskLevel = this.crisis.getRiskLevel();
    	if ( "Green".equalsIgnoreCase(riskLevel.trim()) )
    		crisisTitle.setTextColor(Color.GREEN);
    	else if ( "Orange".equalsIgnoreCase(riskLevel.trim()) )
    		crisisTitle.setTextColor(Color.argb(255, 255, 125, 0));
    	else if ( "Red".equalsIgnoreCase(riskLevel.trim()) )
    		crisisTitle.setTextColor(Color.RED);

    	final ImageView depiction = (ImageView) findViewById(R.id.crisisEntryDepiction);
    	if ( depiction != null ) {
    		Thread worker = new Thread() {
    			public void run() {
    				try {
    					InputStream is = (InputStream) new URL(crisis.getDepictionURL()).getContent();
    					imageDrawable = Drawable.createFromStream(is, "crisis-depiction");
    					imageView = depiction;
    		    		guiHandler.post(updateImageView);
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			}
    		};
    		worker.start();
    	}
    	
    	TextView crisisDescription = (TextView)findViewById(R.id.crisisEntryDescription);
    	crisisDescription.setText(this.crisis.getDescription());
    	
    	TextView crisisIssued = (TextView)findViewById(R.id.crisisEntryIssued);
    	Date issued = this.crisis.getIssued();
    	if ( issued != null )
    		crisisIssued.setText(issued.toLocaleString());
    	
    	TableLayout crisisTable = (TableLayout)findViewById(R.id.crisisEntryTable);
    	if ( crisisTable != null )
    		crisisTable.setVisibility(TableLayout.VISIBLE);
    	
    	setTableValue(R.id.crisisEntryTabletLatValue, this.crisis.getLatitude());
    	setTableValue(R.id.crisisEntryTabletLongValue, this.crisis.getLongitude());
    	setTableValue(R.id.crisisEntryTabletRiskLevelValue, this.crisis.getRiskLevel());
    	setTableValue(R.id.crisisEntryTabletCrisisTypeValue, this.crisis.getCrisisType());
    	setTableValue(R.id.crisisEntryTabletIssuedLocalValue, this.crisis.getIssued().toLocaleString());
    	setTableValue(R.id.crisisEntryTabletIssuedGMTValue, this.crisis.getIssued().toGMTString());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.entry_menu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case android.R.id.home:
    			finish();
    			return true;
    		case R.id.refresh:
    			Toast.makeText(this, "TODO: Refresh the entity (not implemented)...", Toast.LENGTH_SHORT).show();
    			return true;
    		case R.id.camera:
    			Intent intent = new Intent();
    			intent.setType("image/*");
    			intent.setAction(Intent.ACTION_GET_CONTENT);
    			intent.addCategory(Intent.CATEGORY_OPENABLE);
    			startActivityForResult(intent, REQUEST_CODE);
    			return true;
    		case R.id.share:
    			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
    			String title = this.crisis.getTitle();
    			shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Found this crisis at http://www.sigimera.org");
    			if ( title != null )
    				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, title + " - " + this.crisisID );
    			shareIntent.setType("text/plain");
    			this.startActivity(shareIntent);
    			return true;
    		case R.id.showSettings:
    			Intent settings = new Intent(CrisisEntryActivity.this, Settings.class);
    			this.startActivity(settings);
    			return true;
    		case R.id.showAboutDialog:
    			this.showAboutDialog();
    			return true;
    	}
    	return false;
    }
    
    private void showAboutDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        DialogFragment newFragment = AboutDialogFragment.newInstance();
        newFragment.show(ft, "dialog");
    }
    
    @Override
    public void onSaveInstanceState(Bundle _outState) {
        super.onSaveInstanceState(_outState);
        _outState.putString("crisisid", this.crisisID);
        _outState.putSerializable("crisisObj", this.crisis);
    }

	/**
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View _view) {
		if ( _view.getId() == R.id.crisisEntryDepiction ) {
			String uri = "geo:" + this.crisis.getLatitude() + "," + this.crisis.getLongitude() 
				+ "?q=" + this.crisis.getLatitude()  + "," + this.crisis.getLongitude() 
				+ "(" + this.crisis.getTitle().replace(" ", "+").replace("(", "[").replace(")", "]") + ")";
			startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
		}
		
	}
    
}
