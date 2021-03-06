/**
 *
 */
package com.example.medicalarzi.util;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.medicalarzi.model.Arzi;
import com.example.medicalarzi.model.ArziSearchResult;
import com.example.medicalarzi.model.ArziType;
import com.example.medicalarzi.model.BodyPart;
import com.example.medicalarzi.model.Condition;
import com.example.medicalarzi.model.Jamaat;
import com.example.medicalarzi.model.Lookup;
import com.example.medicalarzi.model.Patient;
import com.example.medicalarzi.model.Procedure;
import com.vaadin.data.Container;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToLongConverter;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;

/**
 * @author Mkanchwa
 *
 */
public class MedicalArziUtils {

	public static Logger logger = LogManager.getLogger(MedicalArziUtils.class);

	/**
	 *
	 */
	public MedicalArziUtils() {
	}

	/**
	 * 
	 * @param field
	 * @param attribute
	 */
	public static void installSingleValidator(Field<?> field, String attribute) {

		Collection<Validator> validators = field.getValidators();

		if (validators == null || validators.isEmpty()) {
			//Creates a Vaadin validator utilizing the JSR-303 validation
			field.addValidator(new BeanValidator(Patient.class, attribute));
		}
	}

	/**
	 * Checks the component recursively and returns the matching component based
	 * on the id.
	 *
	 * @param root
	 * @param id
	 * 
	 * @return com.vaadin.ui.Component
	 */
	public static Component findById(HasComponents root, String id) {

		Iterator<Component> iterate = root.iterator();

		while (iterate.hasNext()) {
			Component c = iterate.next();

			if (StringUtils.isNotEmpty(c.getId()) && id.equals(c.getId())) {
				return c;
			}
			if (c instanceof HasComponents) {
				Component cc = findById((HasComponents) c, id);
				if (cc != null)
					return cc;
			}
		}

		return null;
	}
	

	/**
	 * Constructs a com.vaadin.data.util.BeanItemContainer container based on a
	 * given class and returns that container
	 *
	 * @param obj
	 * 
	 * @return com.vaadin.data.Container
	 */
	@SuppressWarnings("unchecked")
	public static Container getContainer(Class<?> objClass) {

		Container container = null;

		if (objClass == Lookup.class)
			container = new BeanItemContainer<Lookup>(
					(Class<? super Lookup>) objClass);
		else if (objClass == BodyPart.class)
			container = new BeanItemContainer<BodyPart>(
					(Class<? super BodyPart>) objClass);
		else if (objClass == Condition.class)
			container = new BeanItemContainer<Condition>(
					(Class<? super Condition>) objClass);
		else if (objClass == Procedure.class)
			container = new BeanItemContainer<Procedure>(
					(Class<? super Procedure>) objClass);
		else if (objClass == ArziType.class)
			container = new BeanItemContainer<ArziType>(
					(Class<? super ArziType>) objClass);
		else if (objClass == Arzi.class)
			container = new BeanItemContainer<Arzi>(
					(Class<? super Arzi>) objClass);
		else if (objClass == Jamaat.class)
			container = new BeanItemContainer<Jamaat>(
					(Class<? super Jamaat>) objClass);
		else if (objClass == ArziSearchResult.class)
			container = new BeanItemContainer<ArziSearchResult>(
					(Class<? super ArziSearchResult>) objClass);
		else if(objClass == String.class)
			container = new BeanItemContainer<String>(
					(Class<? super String>) objClass);

		return container;
	}

	/**
	 * This method provides the thread-safe access to the session attributes.
	 * 
	 * When you want to access your session attributes through class
	 * VaadinSession you have to take into account that there might be more than
	 * one thread that accesses this data at the same time. Therefore, you have
	 * to work with session attributes in a thread-safe way. To do that, class
	 * VaadinSession provides a lock mechanism allowing you to lock the session
	 * while accessing the session data.
	 * 
	 * @param key
	 * @param value
	 */
	public static void setSessionAttribute(String key, Object value) {
		try {
			VaadinSession.getCurrent().getLockInstance().lock();
			VaadinSession.getCurrent().setAttribute(key, value);
		} finally {
			VaadinSession.getCurrent().getLockInstance().unlock();
		}
	}
	
	/**
	 * This method is responsible to access your session attributes through
	 * class VaadinSession. It returns the object stored in the session for the
	 * given key.
	 * 
	 * @param key
	 * 
	 * @return java.lang.Object
	 */
	public static Object getSessionAttribute(String key) {
		return VaadinSession.getCurrent().getAttribute(key);
	}
	
	/**
	 * This method is responsible for creating a notification based on the
	 * passed parameters. The notification might be a ERROR, WARNING or USER
	 * FRIENDLY message. The position on the page is specified by the passed
	 * position parameter and the notification delay is set to "-1" which means
	 * that the message would not disappear until it is clicked.
	 * 
	 * @param caption
	 * @param description
	 * @param type
	 * @param position
	 * @param cssStyleName
	 * @param delayMsec
	 */
	public static void createAndShowNotification(String caption,
			String description, Type type, Position position,
			String cssStyleName, int delayMsec) {
		Notification notif = new Notification(caption, description, type, true);
		notif.setDelayMsec(delayMsec);
		if(StringUtils.isNotBlank(cssStyleName)) {
			notif.setStyleName(cssStyleName);
		}
		notif.setPosition(position);
		notif.show(Page.getCurrent());
	}
	
	/**
	 * This method is responsible for setting the attributes in the current
	 * request.
	 * 
	 * @param key
	 * @param value
	 */
	public static void setRequestAttribute(String key, Object value) {
		VaadinService.getCurrentRequest().setAttribute(key, value);
	}

	/**
	 * This method is responsible to access the request attributes through
	 * class VaadinService. It returns the object stored in the request for the
	 * given key.
	 * 
	 * @param key
	 * 
	 * @return java.lang.Object
	 */
	public static Object getRequestAttribute(String key) {
		return VaadinService.getCurrentRequest().getAttribute(key);
	}
	
	/**
	 * This method is responsible for constructing the patients full name based
	 * on his first name, last name, title and his middle name and middle name
	 * title
	 * 
	 * @param ptnt
	 * 
	 * @return java.lang.String
	 */
	public static String constructPtntFullName(Patient ptnt) {
		StringBuffer fullName = new StringBuffer();
		
		if (ptnt != null) {
			// Patient Title
			if (ptnt.getPtntTitle() != null
					&& !(StringUtils.equalsIgnoreCase(ptnt.getPtntTitle()
							.getLookupValue(),
							MedicalArziConstants.MAP_DAWAT_TITLE_BHAI) || StringUtils
							.equalsIgnoreCase(ptnt.getPtntTitle()
									.getLookupValue(),
									MedicalArziConstants.MAP_DAWAT_TITLE_BEHEN))) {
				fullName.append(ptnt.getPtntTitle().getLookupValue());
				fullName.append(" ");
			}

			// Their first name
			fullName.append(ptnt.getFirstName());

			// Their middle name title
			if (ptnt.getPtntMiddleNmTitle() != null
					&& !(StringUtils.equalsIgnoreCase(ptnt
							.getPtntMiddleNmTitle().getLookupValue(),
							MedicalArziConstants.MAP_DAWAT_TITLE_BHAI) || StringUtils
							.equalsIgnoreCase(ptnt.getPtntMiddleNmTitle()
									.getLookupValue(),
									MedicalArziConstants.MAP_DAWAT_TITLE_BEHEN))) {
				fullName.append(" ");
				fullName.append(ptnt.getPtntMiddleNmTitle().getLookupValue());

			}

			// Their middle name
			if (StringUtils.isNotEmpty(ptnt.getMiddleName())) {
				fullName.append(" ");
				fullName.append(ptnt.getMiddleName());
				fullName.append(" ");
			}
			else {
				fullName.append(" ");
			}

			// Their last name
			fullName.append(ptnt.getLastName());
		}
		
		logger.debug("Patient's full name: " + fullName.toString());
		
		return fullName.toString();
	}	
	
	/**
	 * This method is responsible for converting from java.lang.String to
	 * java.lang.Long and back and it removes the grouping of the values with a
	 * comma for example 12345678 is represented by default as 123,456,78 but we
	 * do not want this grouping for the ITS number for the patient, so we have
	 * to override the format and set the grouping to false.
	 * 
	 * @return com.vaadin.data.util.Converter<String, Long>
	 */
	public static Converter<String, Long> itsNumberConverter() {
		return new StringToLongConverter() {
			private static final long serialVersionUID = 1L;

			@Override
			protected NumberFormat getFormat(Locale locale) {
				NumberFormat format = super.getFormat(locale);
				// This eliminates the "," after grouping of 3 digits.
				format.setGroupingUsed(false);
				return format;
			};
		};
		
	}
	
	/**
	 * This method returns the combobox with the possible values extracted from
	 * the passed container and displaying the passed property from an item
	 * 
	 * @param caption
	 * @param inputPrompt
	 * @param items
	 * @param propertyId
	 * @param container
	 * @param isRequired
	 * @param requiredErrorMsg  
	 * 
	 * @return com.vaadin.ui.Field<?>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Field<?> getComboBox(String caption, String inputPrompt,
			Collection<?> items, Object propertyId, Container container,
			Converter converter, Boolean isRequired, String requiredErrorMsg) {
		ComboBox comboBox = new ComboBox(caption);
		comboBox.setImmediate(true);
		comboBox.setInputPrompt(inputPrompt);
		comboBox.setContainerDataSource(container);
		comboBox.addItems(items);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId(propertyId);
		if (converter != null) {
			comboBox.setConverter(converter);
		}
		comboBox.setRequired(isRequired);
		if (StringUtils.isNotBlank(requiredErrorMsg)) {
			comboBox.setRequiredError(requiredErrorMsg);
		}
		return comboBox;
	}
	

	/**
	 * 
	 * @param caption
	 * @param isEnabled
	 * @param isRequired
	 * @param requiredErrorMsg
	 * 
	 * @return com.vaadin.ui.Field<?>
	 */
	public static Field<?> getTextFieldEditor(String caption,
			Boolean isEnabled, Boolean isRequired, String requiredErrorMsg) {
		TextField textField = new TextField(caption);
		textField.setNullRepresentation("");
		textField.setEnabled(isEnabled);
		textField.setRequired(isRequired);
		if (StringUtils.isNotBlank(requiredErrorMsg)) {
			textField.setRequiredError(requiredErrorMsg);
		}

		return textField;
	}
}
