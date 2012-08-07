/**
 * Sigimera Crises Information Platform Android Client
 * Copyright (C) 2011-2012 by Sigimera
 * All Rights Reserved
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package org.sigimera.frontends.android.tablet.widget;

import org.sigimera.frontends.android.tablet.CrisisEntryActivity;
import org.sigimera.frontends.android.tablet.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

public class StackWidgetProvider extends AppWidgetProvider {
    public static final String TOAST_ACTION = "org.sigimera.frontends.android.tablet.widget.TOAST_ACTION";
    public static final String EXTRA_ITEM = "org.sigimera.frontends.android.tablet.widget.EXTRA_ITEM";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onReceive(Context context, Intent _intent) {
//        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if ( _intent.getAction().equals(TOAST_ACTION) ) {
//            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        	String crisisID = _intent.getStringExtra(EXTRA_ITEM);
        	Intent intent = new Intent(context, CrisisEntryActivity.class);
        	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        	Bundle bundle = new Bundle();
        	bundle.putString("crisisid", crisisID);
        	intent.putExtras(bundle);
        	context.startActivity(intent);
        }
        super.onReceive(context, _intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] _widgetIDs) {
        // update each of the widgets with the remote adapter
        for (int i = 0; i < _widgetIDs.length; ++i) {

            // Here we setup the intent which points to the StackViewService which will
            // provide the views for this collection.
            Intent intent = new Intent(context, StackWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, _widgetIDs[i]);
            // When intents are compared, the extras are ignored, so we need to embed the extras
            // into the data so that the extras will not be ignored.
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            rv.setRemoteAdapter(_widgetIDs[i], R.id.stack_view, intent);

            // The empty view is displayed when the collection has no items. It should be a sibling
            // of the collection view.
            rv.setEmptyView(R.id.stack_view, R.id.empty_view);

            // Here we setup the a pending intent template. Individuals items of a collection
            // cannot setup their own pending intents, instead, the collection as a whole can
            // setup a pending intent template, and the individual items can set a fillInIntent
            // to create unique before on an item to item basis.
            Intent toastIntent = new Intent(context, StackWidgetProvider.class);
            toastIntent.setAction(StackWidgetProvider.TOAST_ACTION);
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, _widgetIDs[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);

            appWidgetManager.updateAppWidget(_widgetIDs[i], rv);
        }
        super.onUpdate(context, appWidgetManager, _widgetIDs);
    }
}
