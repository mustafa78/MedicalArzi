/**
 * 
 */
package com.example.medicalarzi.converter;

import java.util.Locale;

import com.example.medicalarzi.model.Procedure;
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
public class StringToProcedureConverter implements Converter<String, Procedure> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3823216659414646867L;

	private Container container;

	/**
	 * Constructor
	 */
	public StringToProcedureConverter() {
	}

	public StringToProcedureConverter(Container container) {
		this.container = container;
	}

	@Override
	public Procedure convertToModel(String value,
			Class<? extends Procedure> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null) {
			return null;
		}

		Item item = container.getItem(value);
		if (item == null) {
			return null;
		}

		Procedure procedure = (Procedure) container.getItem(value);
		return procedure;
	}

	@Override
	public String convertToPresentation(Procedure procedure,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {

		if (procedure != null) {
			return procedure.getProcedureName();
		}
		return null;
	}

	@Override
	public Class<Procedure> getModelType() {
		return Procedure.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
