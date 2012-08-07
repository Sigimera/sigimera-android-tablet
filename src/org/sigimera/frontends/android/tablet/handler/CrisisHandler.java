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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.jaxen.JaxenException;
import org.sigimera.frontends.android.tablet.data.CrisisEntity;

/**
 * @author Alex Oberhauser
 */
public class CrisisHandler {
	private static final String ACCESS_TOKEN = "a50913f3b0cc9d4fc71afce6bc46679718064d3050e18274926821b33531efdd";
	private static final String apiSearch = "http://api.sigimera.org/v1/crisis?access_token=" + ACCESS_TOKEN;
	private static final String queryPrefix = "/rdf:RDF/rdf:Description";

	private static List<CrisisEntity> getCrisis(String _category) throws MalformedURLException, DocumentException, URISyntaxException {
		List<CrisisEntity> crisisList = new ArrayList<CrisisEntity>();

		SAXReader reader = new SAXReader();
		Document crisisDoc;
		if ( _category != null)
			crisisDoc = reader.read(new URL(apiSearch + "&type=" + _category));
		else
			crisisDoc = reader.read(new URL(apiSearch));

		@SuppressWarnings("unchecked")
		List<Element> crisisEntries = (List<Element>)crisisDoc.selectNodes(queryPrefix);
		for ( Element crisisElement : crisisEntries ) {
			String crisisURI = crisisElement.valueOf("@rdf:about");

			if ( !crisisURI.startsWith("http://data.sigimera.org/crisis") )
				continue;
			CrisisEntity crisisEntity = new CrisisEntity(new URI(crisisURI));

			try {
				Node titleNode = XPathHandler.selectNode("//rdf:Description[@rdf:about='" + crisisURI + "']/dc:title", crisisDoc);
				if ( titleNode != null )
					crisisEntity.setTitle(titleNode.getText());

				Node descriptionNode = XPathHandler.selectNode("//rdf:Description[@rdf:about='" + crisisURI + "']/dc:description", crisisDoc);
				if ( descriptionNode != null )
					crisisEntity.setDescription(descriptionNode.getText());

				Node issuedNode = XPathHandler.selectNode("//rdf:Description[@rdf:about='" + crisisURI + "']/dct:issued", crisisDoc);
				if ( issuedNode != null )
					crisisEntity.setIssued(issuedNode.getText());

				Node latNode = XPathHandler.selectNode("//rdf:Description[@rdf:about='" + crisisURI + "']/geo:lat", crisisDoc);
				if ( latNode != null )
					crisisEntity.setLatitude(latNode.getText());

				Node longNode = XPathHandler.selectNode("//rdf:Description[@rdf:about='" + crisisURI + "']/geo:long", crisisDoc);
				if ( longNode != null )
					crisisEntity.setLongitude(longNode.getText());

			} catch (JaxenException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			crisisList.add(crisisEntity);
		}
		Collections.sort(crisisList);
		return crisisList;
	}

	public static List<CrisisEntity> getAllCrisis() throws MalformedURLException, DocumentException, URISyntaxException {
		return CrisisHandler.getCrisis(null);
	}

	public static List<CrisisEntity> getEarthquakeTsunami() throws MalformedURLException, DocumentException, URISyntaxException {
		return CrisisHandler.getCrisis("EarthquakeTsunami");
	}

	public static List<CrisisEntity> getFlood() throws MalformedURLException, DocumentException, URISyntaxException {
		return CrisisHandler.getCrisis("Flood");
	}

	public static List<CrisisEntity> getTropicalCyclone() throws MalformedURLException, DocumentException, URISyntaxException {
		return CrisisHandler.getCrisis("TropicalCyclone");
	}

	public static List<CrisisEntity> getVolcanicEruptions() throws MalformedURLException, DocumentException, URISyntaxException {
		return CrisisHandler.getCrisis("VolcanicEruption");
	}

}
