/**
 * 
 */
package com.example.medicalarzi.converter;

import java.util.Locale;

import com.example.medicalarzi.model.Condition;
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
public class StringToConditionConverter implements Converter<String, Condition> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3823216659414646867L;

	private Container container;

	/**
	 * Constructor
	 */
	public StringToConditionConverter() {
		// TODO Auto-generated constructor stub
	}

	public StringToConditionConverter(Container container) {
		this.container = container;
	}

	@Override
	public Condition convertToModel(String value,
			Class<? extends Condition> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null) {
			return null;
		}

		Item item = container.getItem(value);
		if (item == null) {
			return null;
		}

		Condition condition = (Condition) container.getItem(value); //(Condition) item.getItemProperty("condition");
		return condition;
	}

	@Override
	public String convertToPresentation(Condition condition,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {

		if(condition != null) {
			return condition.getConditionName();
		}
		return null;
	}

	@Override
	public Class<Condition> getModelType() {
		return Condition.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
