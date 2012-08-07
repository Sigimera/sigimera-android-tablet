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

import java.util.HashMap;

import org.dom4j.Document;
import org.dom4j.Node;
import org.jaxen.JaxenException;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.XPath;
import org.jaxen.dom4j.Dom4jXPath;

/**
 * @author Alex Oberhauser
 */
public abstract class XPathHandler {

	public static Node selectNode(String _xpath, Document _doc) throws JaxenException {
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

}
