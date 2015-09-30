/**
 * 
 */
package com.example.medicalarzi.converter;

import java.util.Locale;

import com.example.medicalarzi.model.Jamaat;
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
public class StringToJamaatConverter implements Converter<String, Jamaat> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3823216659414646867L;

	private Container container;

	/**
	 * Constructor
	 */
	public StringToJamaatConverter() {
		// TODO Auto-generated constructor stub
	}

	public StringToJamaatConverter(Container container) {
		this.container = container;
	}

	@Override
	public Jamaat convertToModel(String value,
			Class<? extends Jamaat> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null) {
			return null;
		}

		Item item = container.getItem(value);
		if (item == null) {
			return null;
		}

		Jamaat jamaat = (Jamaat) container.getItem(value); 
		return jamaat;
	}

	@Override
	public String convertToPresentation(Jamaat jamaat,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {

		if(jamaat != null) {
			return jamaat.getJamaatName();
		}
		return null;
	}

	@Override
	public Class<Jamaat> getModelType() {
		return Jamaat.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
