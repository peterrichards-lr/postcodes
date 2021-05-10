package com.liferay.postcodes.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.portal.kernel.search.geolocation.GeoLocationPoint;
import com.liferay.portal.kernel.util.Validator;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Component(immediate = true, service = PostcodesIoApiHelper.class)
public class PostcodesIoApiHelper {
	public String callPostcodeLookup(double longitude, double latitude) {
		if (Math.abs(longitude) > 180)
			throw new IllegalArgumentException("Longitude must a number between -180 and 180");
		
		if (Math.abs(latitude) > 90)
			throw new IllegalArgumentException("Latitude must be a number between -90 and 90");
		
		String url = getBaseUrl(PostcodesIoConstants.POSTCODE_LOOKUP_URL);
		url = url.replace("{{longitude}}", String.valueOf(longitude));
		url = url.replace("{{latitude}}", String.valueOf(latitude));
		
		return extractPostcode(get(url));
	}
	
	private String extractPostcode(JsonNode jsonNode) {
		final JsonNode status = jsonNode.get("status");
		if (status.asInt() != HttpStatus.OK.value())
			return null;
		
		final JsonNode result = jsonNode.get("result");
		String postcode = result.get("postcode").asText();
		return postcode;
	}

	public GeoLocationPoint callGeolocationLookup(final String postcode) {
		if (Validator.isBlank(postcode))
			throw new IllegalArgumentException("Postcode cannot be blank");
		
		String url = getBaseUrl(PostcodesIoConstants.GETLOCATION_LOOKUP_URL);
		url = url.replace("{{postcode}}", postcode.replaceAll("\\s+",""));
		
		return extractGeoLocation(get(url));
	}
	
	private GeoLocationPoint extractGeoLocation(JsonNode jsonNode) {
		final JsonNode status = jsonNode.get("status");
		if (status.asInt() != HttpStatus.OK.value())
			return null;
		
		final JsonNode result = jsonNode.get("result");
		
		final double latitude = result.get("latitude").asDouble();
		final double longitude = result.get("longitude").asDouble();
		
		return new GeoLocationPoint(latitude, longitude);
	}

	private String getBaseUrl(final String path) {
		return PostcodesIoConstants.SCHEME + "://" + PostcodesIoConstants.POSTCODE_IO_ENDPOINT + path;
	}
	
	private JsonNode get(final String url) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		ObjectMapper mapper = new ObjectMapper();

		if (response.getStatusCode() != HttpStatus.OK) {
			_log.error("The call to Postcodes.io was unsuccessful. Status code - " + response.getStatusCode());
			return null;
		}

		try {
			return mapper.readTree(response.getBody());
		} catch (JsonProcessingException e) {
			_log.error("Unable to read response as JSON", e);
			return null;
		}
	}

	private static final Logger _log = LoggerFactory.getLogger(PostcodesIoApiHelper.class);
}
