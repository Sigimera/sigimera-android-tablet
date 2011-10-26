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

package org.sigimera.frontends.android.tablet.widget;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;
import org.sigimera.frontends.android.tablet.R;
import org.sigimera.frontends.android.tablet.data.CrisisEntity;
import org.sigimera.frontends.android.tablet.handler.CrisisHandler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
 * @author Alex Oberhauser
 */
public class StackWidgetService extends RemoteViewsService {
	/**
	 * @see android.widget.RemoteViewsService#onGetViewFactory(android.content.Intent)
	 */
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent _intent) {
		return new StackRemoteViewsFactory(this.getApplicationContext(), _intent);
	}
}

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<CrisisEntity> widgetCrisisItems = new ArrayList<CrisisEntity>();
    private Context context;
//    private int mAppWidgetId;

    public StackRemoteViewsFactory(Context _context, Intent intent) {
        this.context = _context;
//        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    public void onCreate() {
        // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
        // for example downloading or creating content etc, should be deferred to onDataSetChanged()
        // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.
//        for (int i = 0; i < mCount; i++) {
//            widgetItems.add(new WidgetItem(i + "!"));
//        }
    	Thread worker = new Thread() {
    		public void run() {
    	    	try {
    				widgetCrisisItems = CrisisHandler.getAllCrisis();
    				if ( widgetCrisisItems.size() > 10 )
    					widgetCrisisItems = widgetCrisisItems.subList(0, 10);
    			} catch (MalformedURLException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			} catch (DocumentException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			} catch (URISyntaxException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
    		}
    	};
    	worker.start();


        // We sleep for 3 seconds here to show how the empty view appears in the interim.
        // The empty view is set in the StackWidgetProvider and should be a sibling of the
        // collection view.
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        this.widgetCrisisItems.clear();
    }

    @Override
    public int getCount() {
        return this.widgetCrisisItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        // position will always range from 0 to getCount() - 1.

        // We construct a remote views item based on our widget item xml file, and set the
        // text based on the position.
        RemoteViews rv = new RemoteViews(this.context.getPackageName(), R.layout.widget_item);
        rv.setTextViewText(R.id.widget_item, this.widgetCrisisItems.get(position).getTitle());

        // Next, we set a fill-intent which will be used to fill-in the pending intent template
        // which is set on the collection view in StackWidgetProvider.
        Bundle extras = new Bundle();
        extras.putString(StackWidgetProvider.EXTRA_ITEM, this.widgetCrisisItems.get(position).getCrisisID().toString());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent);

        // You can do heaving lifting in here, synchronously. For example, if you need to
        // process an image, fetch something from the network, etc., it is ok to do it here,
        // synchronously. A loading view will show up in lieu of the actual contents in the
        // interim.

        return rv;
    }

    /**
     * null returns the default loading view.
     * 
     * @see android.widget.RemoteViewsService.RemoteViewsFactory#getLoadingView()
     */
    public RemoteViews getLoadingView() {
        return null;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean hasStableIds() {
        return true;
    }

    public void onDataSetChanged() {
        // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
        // on the collection view corresponding to this factory. You can do heaving lifting in
        // here, synchronously. For example, if you need to process an image, fetch something
        // from the network, etc., it is ok to do it here, synchronously. The widget will remain
        // in its current state while work is being done here, so you don't need to worry about
        // locking up the widget.
    }
}
