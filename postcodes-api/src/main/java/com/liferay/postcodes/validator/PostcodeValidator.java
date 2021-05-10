package com.liferay.postcodes.validator;

import com.liferay.postcodes.exception.PostcodeValidationException;

/**
 * @author Peter Richards
 */
public interface PostcodeValidator {
	/**
	 * Validates a string against the criteria of a valid UK postcode
	 * @param postcode the postcode
	 * @throws PostcodeValidationException 
	 */
	public void validate(String postcode); 
}
