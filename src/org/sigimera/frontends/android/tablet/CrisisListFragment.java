/**
 * Copyright (C) 2011 by Sigimera * All Rights Reserved
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

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.sigimera.frontends.android.tablet.data.CrisisEntity;
import org.sigimera.frontends.android.tablet.extension.CrisisListAdapter;
import org.sigimera.frontends.android.tablet.handler.CategoryHandler;
import org.sigimera.frontends.android.tablet.handler.CrisisHandler;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * @author Alex Oberhauser
 */
public class CrisisListFragment extends ListFragment {
	
	private List<Map<String, Object>> dataList;
	private List<CrisisEntity> crisisList;
	
	private final Handler guiHandler = new Handler();
	private final Runnable updateCrisisEntries = new Runnable() {
		@Override
		public void run() {
			updateCrisisEntriesInGUI();
		}
	};
	
	private final Runnable showNetworkConnectionError = new Runnable() {
		@Override
		public void run() {
			showNetworkConnectionError();
		}
	};
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
		 super.onActivityCreated(savedInstanceState);
    }

	/**
	 * @param category
	 * @param position
	 */
	public void updateContent(final int _tab, int _position) {
		final String categoryName = new CategoryHandler().getCategoryName(_position);
		
		this.dataList = new ArrayList<Map<String, Object>>();
		
		Thread worker = new Thread() {
			@Override
			public void run() {
				try {
					if ( categoryName.startsWith("Latest Crisis") && _tab == 0 ) {
						crisisList = CrisisHandler.getAllCrisis();
					} else if ( categoryName.startsWith("Earthquake/Tsunami") && _tab == 0 ) {
						crisisList = CrisisHandler.getEarthquakeTsunami();
					} else if ( categoryName.startsWith("Flood") && _tab == 0 ) {
						crisisList = CrisisHandler.getFlood();
					} else if ( categoryName.startsWith("Tropical Cyclone") && _tab == 0 ) {
						crisisList = CrisisHandler.getTropicalCyclone();
					} else if ( categoryName.startsWith("Volcanic Eruption") && _tab == 0 ) {
						crisisList = CrisisHandler.getVolcanicEruptions();
					} else {
						crisisList.clear();
					}
        		} catch (MalformedURLException e) {
        			guiHandler.post(showNetworkConnectionError);
        		} catch (DocumentException e) {
        			guiHandler.post(showNetworkConnectionError);
        		} catch (URISyntaxException e ) {
        			guiHandler.post(showNetworkConnectionError);
				}
        		guiHandler.post(updateCrisisEntries);
        	}
        };
        worker.start();
	}
	
	private void updateCrisisEntriesInGUI() {
		setListAdapter(new CrisisListAdapter(getActivity(), R.layout.crisis_entry_list_entry, crisisList));
	}
	
	private void showNetworkConnectionError() {
		Toast.makeText(getActivity(), "[ERROR] Not able to get crisis information...", Toast.LENGTH_LONG).show();
	}
	
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	String crisisID = this.crisisList.get(position).getCrisisID().toASCIIString();
    	Intent intent = new Intent(getActivity(), CrisisEntryActivity.class);
    	
    	Bundle bundle = new Bundle();
    	bundle.putString("crisisid", crisisID);
    	intent.putExtras(bundle);
    	startActivity(intent);
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
        this.crisisList.clear();
        this.dataList.clear();
    }
}
