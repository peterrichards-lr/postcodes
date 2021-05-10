package com.liferay.postcodes.validator.impl;

import com.liferay.postcodes.exception.PostcodeValidationException;
import com.liferay.postcodes.validator.PostcodeValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.osgi.service.component.annotations.Component;

@Component(immediate = true, service = PostcodeValidator.class)
public class PostcodeValidatorImpl implements PostcodeValidator {

	private final static Pattern postcodePattern = Pattern.compile(
			"([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9][A-Za-z]?))))\\s?[0-9][A-Za-z]{2})");

	@Override
	public void validate(String postcode) {
		List<String> errors = new ArrayList<>();

		if (!isPostcodeValid(postcode, errors)) {
			throw new PostcodeValidationException(errors);
		}
	}

	private boolean isPostcodeValid(String postcode, List<String> errors) {
		return postcodePattern.matcher(postcode).matches();
	}
}
