/**
 * 
 */
package com.example.medicalarzi.converter;

import java.util.Locale;

import com.example.medicalarzi.model.BodyPart;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.converter.Converter;

/**
 * If you have custom types that you want to represent using the built in field
 * components, you can easily create your own converter to take care of
 * converting between your own type and the native data type of the field.
 * 
 * @author mkanchwa
 *
 */
public class StringToBodyPartConverter implements Converter<String, BodyPart> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3823216659414646867L;

	private Container container;

	/**
	 * Constructor
	 */
	public StringToBodyPartConverter() {
	}

	public StringToBodyPartConverter(Container container) {
		this.container = container;
	}

	@Override
	public BodyPart convertToModel(String value,
			Class<? extends BodyPart> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null) {
			return null;
		}

		Item item = container.getItem(value);
		if (item == null) {
			return null;
		}

		BodyPart bodyPart = (BodyPart) container.getItem(value);
		return bodyPart;
	}

	@Override
	public String convertToPresentation(BodyPart bodyPart,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {

		if (bodyPart != null) {
			return bodyPart.getBodyPartName();
		}
		return null;
	}

	@Override
	public Class<BodyPart> getModelType() {
		return BodyPart.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
