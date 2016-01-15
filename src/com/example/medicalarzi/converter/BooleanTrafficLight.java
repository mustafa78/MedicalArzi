/**
 * 
 */
package com.example.medicalarzi.converter;

import java.util.Locale;

import com.vaadin.data.util.converter.StringToBooleanConverter;
import com.vaadin.server.FontAwesome;

/**
 * Converts the Boolean value into an HTML circle icon with different color
 * based on a true/false value.
 * 
 * @author mkanchwa
 *
 */
public class BooleanTrafficLight extends StringToBooleanConverter {

	private static final long serialVersionUID = 7867732341191389985L;

	/**
	 * 
	 */
	public BooleanTrafficLight() {
	}

	@Override
	public String convertToPresentation(Boolean value,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		String color;

		if (value == null || !value) {
			color = "#2DD085";
		} else {
			color = "#F54993";
		}
		return FontAwesome.CIRCLE.getHtml().replace("style=\"",
				"style=\"color: " + color + ";");
	}

}
