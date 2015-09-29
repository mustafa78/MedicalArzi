/**
 * 
 */
package com.example.medicalarzi.converter;

import java.util.Locale;

import com.example.medicalarzi.model.Status;
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
public class StringToStatusConverter implements Converter<String, Status> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3823216659414646867L;

	private Container container;

	/**
	 * Constructor
	 */
	public StringToStatusConverter() {
	}

	public StringToStatusConverter(Container container) {
		this.container = container;
	}

	@Override
	public Status convertToModel(String value,
			Class<? extends Status> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null) {
			return null;
		}

		Item item = container.getItem(value);
		if (item == null) {
			return null;
		}

		Status status = (Status) container.getItem(value);
		return status;
	}

	@Override
	public String convertToPresentation(Status status,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {

		if (status != null) {
			return status.getStatusDesc();
		}
		return null;
	}

	@Override
	public Class<Status> getModelType() {
		return Status.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
