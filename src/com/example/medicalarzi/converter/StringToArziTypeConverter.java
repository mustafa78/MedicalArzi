/**
 * 
 */
package com.example.medicalarzi.converter;

import java.util.Locale;

import com.example.medicalarzi.model.ArziType;
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
public class StringToArziTypeConverter implements Converter<String, ArziType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3823216659414646867L;

	private Container container;

	/**
	 * Constructor
	 */
	public StringToArziTypeConverter() {
		// TODO Auto-generated constructor stub
	}

	public StringToArziTypeConverter(Container container) {
		this.container = container;
	}

	@Override
	public ArziType convertToModel(String value,
			Class<? extends ArziType> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null) {
			return null;
		}

		Item item = container.getItem(value);
		if (item == null) {
			return null;
		}

		ArziType arziType = (ArziType) container.getItem(value); //(Condition) item.getItemProperty("condition");
		return arziType;
	}

	@Override
	public String convertToPresentation(ArziType arziType,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {

		if(arziType != null) {
			return arziType.getArziTypeName();
		}
		return null;
	}

	@Override
	public Class<ArziType> getModelType() {
		return ArziType.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
