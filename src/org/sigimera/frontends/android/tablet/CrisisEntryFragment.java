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

import android.app.ListFragment;
import android.os.Bundle;

/**
 * @author Alex Oberhauser
 */
public class CrisisEntryFragment extends ListFragment {
    
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
		 super.onActivityCreated(savedInstanceState);
    }

	/**
	 * @param category
	 * @param position
	 */
	public void updateContent(int _position) {
		// TODO Auto-generated method stub
		if ( _position == 0) {
			
		} else if ( _position == 1 ) {
			
		}
		System.out.println("[TODO] Category = " + _position);
	}
    
}
