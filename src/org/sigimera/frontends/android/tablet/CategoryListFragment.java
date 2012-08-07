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
package org.sigimera.frontends.android.tablet;

import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

import org.sigimera.frontends.android.tablet.handler.CategoryHandler;

import android.app.ListFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author Alex Oberhauser
 */
public class CategoryListFragment extends ListFragment {
    private int curPosition = 0;
    private int selectedTab = 0;

	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            this.curPosition = savedInstanceState.getInt("listPosition");
            this.selectedTab = savedInstanceState.getInt("selectedTab");
        }

        this.populateCrisisList(this.selectedTab);

        ListView lv = getListView();
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setCacheColorHint(Color.TRANSPARENT);

        this.selectPosition(this.curPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        this.updateContent(position);
    }

    public void populateCrisisList(int _selectedTab) {
    	if ( _selectedTab == 0 ) {
    		CategoryHandler catHandler = new CategoryHandler();

    		int catLength = catHandler.getLength();
    		String[] items = new String[catLength];
    		for (int count = 0; count < catLength; count++)
    			items[count] = catHandler.getCategoryName(count);

    		setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.category_list_entry, items));
    	} else if ( _selectedTab == 1 ) {
    		Locale[] locales = Locale.getAvailableLocales();
    		SortedSet<String> countryNames = new TreeSet<String>();

    		for (int count = 0; count < locales.length; count++) {
    			String countryName = locales[count].getDisplayCountry();
    			if ( !"".equals(countryName) )
    				countryNames.add(countryName);
    		}

    		String[] items = (String[])countryNames.toArray(new String[countryNames.size()]);
    		setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.category_list_entry, items));
    	}
    	this.selectedTab = _selectedTab;
    }

    private void updateContent(int _listPosition) {
        CrisisListFragment frag = (CrisisListFragment) getFragmentManager().findFragmentById(R.id.crisis_list);
        frag.updateContent(this.selectedTab, _listPosition);
        this.curPosition = _listPosition;
    }

    public void selectPosition(int position) {
        ListView lv = getListView();
        lv.setItemChecked(position, true);
        updateContent(position);
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("listPosition", this.curPosition);
        outState.putInt("selectedTab", this.selectedTab);
    }

}
