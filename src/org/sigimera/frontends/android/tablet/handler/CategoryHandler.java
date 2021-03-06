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
package org.sigimera.frontends.android.tablet.handler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Oberhauser
 */
public class CategoryHandler {
    private List<String> categories = new ArrayList<String>();

    public CategoryHandler() {
        this.categories.add("Latest Crisis");
        this.categories.add("Earthquake/Tsunami");
        this.categories.add("Flood");
        this.categories.add("Tropical Cyclone");
        this.categories.add("Volcanic Eruption");
    }

    public String getCategoryName(int _position) {
        if ( _position >= this.categories.size() )
            return null;
        return this.categories.get(_position);
    }

    /**
     * @return
     */
    public int getLength() { return this.categories.size(); }

}
