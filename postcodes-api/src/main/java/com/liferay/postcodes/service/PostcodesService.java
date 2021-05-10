package com.liferay.postcodes.service;

import com.liferay.portal.kernel.search.geolocation.GeoLocationPoint;

/**
 * @author Peter Richards
 */
public interface PostcodesService {
	/**
	 * Lookup a UK postcode and find its geolocation.
	 * @param postcode the UK postcode
	 * @return the geolocation
	 */
	public GeoLocationPoint FindGeoLocation(String postcode);
	
	/**
	 * Finds the closest UK postcode to a geolocation.
	 * @param geolocation the geolocation
	 * @return the postcode, if one found, otherwise null
	 */
	public String FindNearestPostcode(GeoLocationPoint geolocation);
}