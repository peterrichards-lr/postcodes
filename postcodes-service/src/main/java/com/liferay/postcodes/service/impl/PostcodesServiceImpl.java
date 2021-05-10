package com.liferay.postcodes.service.impl;

import com.liferay.portal.kernel.search.geolocation.GeoLocationPoint;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.postcodes.core.PostcodesIoApiHelper;
import com.liferay.postcodes.service.PostcodesService;
import com.liferay.postcodes.validator.PostcodeValidator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Peter Richards
 */
@Component(immediate = true, service = PostcodesService.class)
public class PostcodesServiceImpl implements PostcodesService {

	@Override
	public GeoLocationPoint FindGeoLocation(final String postcode) {
		validator.validate(postcode);
		
		_log.debug("Looking up geolocation - " + postcode);
		final GeoLocationPoint geolocation =  apiHelper.callGeolocationLookup(postcode);		
		_log.debug(postcode + " is located at " + geolocation.getLatitude() +", " + geolocation.getLongitude());
		return geolocation;
	}

	@Override
	public String FindNearestPostcode(GeoLocationPoint geolocation) {
		if (Validator.isNull(geolocation)) {
			throw new IllegalArgumentException("The geolocation cannot be null.");
		}
		
		_log.debug("Looking up postcode - " +  geolocation.getLatitude() +", " + geolocation.getLongitude());
		final String postcode = apiHelper.callPostcodeLookup(geolocation.getLongitude(), geolocation.getLongitude());
		_log.debug(postcode + " is located at " + geolocation.getLatitude() +", " + geolocation.getLongitude());
		return postcode;
	}

	@Reference
	private PostcodesIoApiHelper apiHelper;

	@Reference
	private PostcodeValidator validator;

	private static final Logger _log = LoggerFactory.getLogger(PostcodesServiceImpl.class);
}