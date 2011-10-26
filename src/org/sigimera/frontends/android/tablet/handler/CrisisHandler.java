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
import org.dom4j.io.SAXReader;
import org.sigimera.frontends.android.tablet.data.CrisisEntity;

/**
 * @author Alex Oberhauser
 */
public class CrisisHandler {
	public static final String apiSearch = "http://api.sigimera.org/v1/crisis?access_token=CHANGEIT";
	public static final String queryPrefix = "/rdf:RDF/rdf:Description";
	
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
			
			CrisisEntity crisisEntity = new CrisisEntity(new URI(crisisURI));
			
			crisisEntity.setTitle(crisisElement.selectSingleNode("./dc:title").getText());
			crisisEntity.setDescription(crisisElement.selectSingleNode("./dc:description").getText());
			crisisEntity.setIssued(crisisElement.selectSingleNode("./dct:issued").getText());
			
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
