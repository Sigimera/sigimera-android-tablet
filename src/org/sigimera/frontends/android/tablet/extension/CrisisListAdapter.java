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

package org.sigimera.frontends.android.tablet.extension;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.sigimera.frontends.android.tablet.R;
import org.sigimera.frontends.android.tablet.data.CrisisEntity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Alex Oberhauser
 */
public class CrisisListAdapter extends BaseAdapter {
	private final List<CrisisEntity> items;
	
	private ConcurrentHashMap<Integer, ImageView> crisisMap = new ConcurrentHashMap<Integer, ImageView>();
	private ConcurrentHashMap<Integer, Drawable> crisisMapImage = new ConcurrentHashMap<Integer, Drawable>();
	
	private final Handler guiHandler = new Handler();
	private final Runnable updateCrisisMap = new Runnable() {
		@Override
		public void run() {
			updateCrisisMap();
		}
	};
	
	public CrisisListAdapter(final Context _context, final int _itemsId, final List<CrisisEntity> _items) {
		this.items = _items;
	}
	
	/**
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return this.items.size();
	}

	/**
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int _position) {
		 return this.items.get(_position);
	}

	/**
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int _position) {
		return _position;
	}

	private synchronized void updateCrisisMap() {
		for ( int count = 0; count < this.crisisMapImage.size(); count++ ) {
			ImageView view = this.crisisMap.remove(count);
			Drawable drawable = this.crisisMapImage.remove(count);
			System.out.println("[DEBUG*********] drawable = " + count + " -> " + drawable);
			System.out.println("[DEBUG*********] view = " + count + " -> " + view);
			if ( view != null && drawable != null )
				view.setImageDrawable(drawable);
		}
	}
	
	/**
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int _position, View _view, ViewGroup _parent) {
		final CrisisEntity crisisEntity = this.items.get(_position);
		View itemView = null;
		
		if ( _view == null) {
			LayoutInflater inflater = (LayoutInflater) _parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			itemView = inflater.inflate(R.layout.crisis_entry_list_entry, null);
		} else {
			itemView = _view;
		}
		
		TextView crisisTitle = (TextView) itemView.findViewById(R.id.crisisTitel);
		crisisTitle.setText(crisisEntity.getTitle());
		
		TextView crisisSummary = (TextView) itemView.findViewById(R.id.crisisSummary);
		String crisisSummaryText = crisisEntity.getDescription();
		if ( crisisSummaryText.length() > 370 )
			crisisSummaryText = crisisSummaryText.substring(0, 370) + "...";
		crisisSummary.setText(crisisSummaryText);
		
		TextView crisisDate = (TextView) itemView.findViewById(R.id.crisisDate);
		crisisDate.setText(crisisEntity.getIssued().toLocaleString());
		
//		this.crisisMap.put(_position, (ImageView)itemView.findViewById(R.id.crisisMap));
//		Thread worker = new Thread() {
//			public void run() {
//				String latitude = crisisEntity.getLatitude();
//				String longitude = crisisEntity.getLongitude();
//				if ( longitude != null && latitude != null ) {
//					try {
//						URL depictionURL = new URL("http://staticmap.openstreetmap.de/staticmap.php?center=" + 
//								latitude + "," + 
//								longitude + "&zoom=4&markers=" + 
//								latitude + "," + 
//								longitude + 
//								",ol-marker&size=230x120&maptype=osmarenderer");
//						InputStream is = (InputStream) depictionURL.getContent();
//						crisisMapImage.put(_position, Drawable.createFromStream(is, "crisis-depiction-" + _position));
//						guiHandler.post(updateCrisisMap);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		};
//		worker.start();
		
		return itemView;
	}

}
