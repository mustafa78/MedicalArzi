/**
 *
 */
package com.example.medicalarzi.validator;

import com.vaadin.data.validator.AbstractValidator;

/**
 * @author mkanchwa
 *
 */
//Validator for validating the passwords
public class PasswordValidator extends
        AbstractValidator<String> {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public PasswordValidator() {
        super("The password provided is not valid");
    }

    @Override
    protected boolean isValidValue(String value) {
        //
        // Password must be at least 8 characters long and contain at least
        // one number
        //
    	if(value == null) {
    		return false;
    	}
    	else if (value != null
                && (value.length() < 8 || !value.matches(".*\\d.*"))) {
            return false;
        }
        return true;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }
}
