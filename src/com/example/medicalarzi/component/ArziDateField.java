/**
 * 
 */
package com.example.medicalarzi.component;

import java.util.Date;
import java.util.GregorianCalendar;

import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.PopupDateField;

/**
 * @author mkanchwa
 *
 */
public class ArziDateField extends PopupDateField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5265038644126153716L;

	/**
	 * 
	 */
	public ArziDateField() {
		super();
		setDateFormat("dd/MM/yyyy");
	}

	/**
	 * @param dataSource
	 * @throws IllegalArgumentException
	 */
	public ArziDateField(Property<?> dataSource)
			throws IllegalArgumentException {
		super(dataSource);
		// Don't be too tight about the user-input
		setLenient(true);
		// Display only year, month, and day in slash-delimited format
		setDateFormat("dd/MM/yyyy");
		setInputPrompt("dd/MM/yyyy");
	}

	/**
	 * @param caption
	 */
	public ArziDateField(String caption) {
		super(caption);
		// Don't be too tight about the user-input
		setLenient(true);
		// Display only year, month, and day in slash-delimited format
		setDateFormat("dd/MM/yyyy");
		setInputPrompt("dd/MM/yyyy");
	}

	/**
	 * @param caption
	 * @param value
	 */
	public ArziDateField(String caption, Date value) {
		super(caption, value);
		// Don't be too tight about the user-input
		setLenient(true);
		// Display only year, month, and day in slash-delimited format
		setDateFormat("dd/MM/yyyy");
		setInputPrompt("dd/MM/yyyy");
	}

	/**
	 * @param caption
	 * @param dataSource
	 */
	public ArziDateField(String caption, Property<?> dataSource) {
		super(caption, dataSource);
		// Don't be too tight about the user-input
		setLenient(true);
		// Display only year, month, and day in slash-delimited format
		setDateFormat("dd/MM/yyyy");
		setInputPrompt("dd/MM/yyyy");
	}

	@Override
	protected Date handleUnparsableDateString(String dateString)
			throws Converter.ConversionException {
		// Try custom parsing
		String fields[] = dateString.split("/");
		if (fields.length >= 3) {
			try {
				int day = Integer.parseInt(fields[0]);
				int month = Integer.parseInt(fields[1]) - 1;
				int year = Integer.parseInt(fields[2]);
				GregorianCalendar c = new GregorianCalendar(year, month, day);
				return c.getTime();
			} catch (NumberFormatException e) {
				throw new Converter.ConversionException("Not a number");
			}
		}

		// Bad date
		throw new Converter.ConversionException("Your date needs two slashes");
	}

}
