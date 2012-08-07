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
package org.sigimera.frontends.android.tablet.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Alex Oberhauser
 */
public abstract class DateHelper {

	/**
	 * @return The dateTime in xsd format with timezone.
	 */
	public static String getXSDDateTime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		StringBuilder dateBuilder = new StringBuilder(simpleDateFormat.format(new Date()));
		dateBuilder.insert(22, ":");
		return dateBuilder.toString();
	}

	public static Date getDateFromXSDDateTime(String _xsdDateTime) throws ParseException {
		if ( _xsdDateTime.length() != 25 )
			return null;
		StringBuilder dateBuilder = new StringBuilder(_xsdDateTime);
		dateBuilder.deleteCharAt(22);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		return simpleDateFormat.parse(dateBuilder.toString());
	}

	/**
	 * TimeZone is GMT (Greenwich Mean Time)
	 *
	 * @param _srcDate
	 * @param _srcDateFormat
	 * @return
	 * @throws ParseException
	 */
	public static String getXSDDateTime(String _srcDate, String _srcDateFormat) throws ParseException {
		DateFormat srcSimpleDateFormat = new SimpleDateFormat(_srcDateFormat, Locale.ENGLISH);
		Date srcDate = srcSimpleDateFormat.parse(_srcDate);

		SimpleDateFormat dstSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		dstSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		StringBuilder dstDate = new StringBuilder(dstSimpleDateFormat.format(srcDate));
		dstDate.insert(22, ":");
		return dstDate.toString();
	}

	public static String getDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(new Date());
	}

}
