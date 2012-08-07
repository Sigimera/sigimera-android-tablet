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
package org.sigimera.frontends.android.tablet.data;

import java.io.Serializable;
import java.net.URI;
import java.text.ParseException;
import java.util.Date;

import org.sigimera.frontends.android.tablet.helper.DateHelper;

/**
 * @author Alex Oberhauser
 */
public class CrisisEntity implements Comparable<CrisisEntity>, Serializable {
    private static final long serialVersionUID = -4278100321931970579L;

    private final URI crisisID;

    private String title = "unknown";
    private String description = "";
    private Date issued = null;
    private String riskLevel = "";
    private String depictionURL = null;
    private String latitude = "";
    private String longitude = "";
    private String crisisType = "unknown";

    public CrisisEntity(URI _crisisID) {
        this.crisisID = _crisisID;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description.replaceAll("\\<[^>]*>","");
    }

    public Date getIssued() {
        return this.issued;
    }

    public void setIssued(String issued) {
        try {
            this.issued = DateHelper.getDateFromXSDDateTime(issued);
        } catch (ParseException e) {
            this.issued = new Date();
        }
    }

    public String getCrisisType() {
        return this.crisisType;
    }

    public void setCrisisType(String _crisisType) {
        this.crisisType = _crisisType.replace("http://ns.sigimera.org/taxonomy/crisisTypes.owl#", "");
    }

    public String getRiskLevel() {
        return this.riskLevel;
    }

    public void setRiskLevel(String _riskLevel) {
        this.riskLevel = _riskLevel;
    }

    public String getDepictionURL() {
        return this.depictionURL;
    }

    public void setDepictionURL(String _depictionURL) {
        this.depictionURL = _depictionURL;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String _lat) {
        this.latitude = _lat;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String _long) {
        this.longitude = _long;
    }

    public URI getCrisisID() {
        return this.crisisID;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(CrisisEntity _crisis) {
        if ( this.issued.after(_crisis.getIssued()) )
            return -1;
        else if ( this.issued.before(_crisis.getIssued()) )
            return 1;
        else
            return 0;
    }

}
