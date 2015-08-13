/**
 * 
 */
package com.example.medicalarzi.converter;

import java.util.Locale;

import com.example.medicalarzi.model.Lookup;
import com.example.medicalarzi.util.MedicalArziConstants;
import com.vaadin.data.util.converter.Converter;

/**
 * If you have custom types that you want to represent using the built in field
 * components, you can easily create your own converter to take care of
 * converting between your own type and the native data type of the field.
 * 
 * @author mkanchwa
 *
 */
public class StringToLookupConverter implements Converter<String, Lookup> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3823216659414646867L;

	/**
	 * Constructor
	 */
	public StringToLookupConverter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Lookup convertToModel(String value,
			Class<? extends Lookup> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null) {
            return null;
        }
		
		Lookup lookup = null;
		if(value.equalsIgnoreCase("M")) {
			lookup = new Lookup (MedicalArziConstants.MAP_GENDER_MALE_ID, value);
			lookup.setDescription("Male");
		}
		else {
			lookup = new Lookup (MedicalArziConstants.MAP_GENDER_FEMALE_ID, value);
			lookup.setDescription("Female");
		}
		lookup.setDescription(value);
		return lookup;
	}

	@Override
	public String convertToPresentation(Lookup lookup,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (lookup == null) {
            return null;
        } else {
            return lookup.getDescription();
        }
	}

	@Override
	public Class<Lookup> getModelType() {
		// TODO Auto-generated method stub
		return Lookup.class;
	}

	@Override
	public Class<String> getPresentationType() {
		// TODO Auto-generated method stub
		return String.class;
	}

}
