/**
 *
 */
package com.example.medicalarzi.util;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.medicalarzi.model.Lookup;
import com.example.medicalarzi.model.Patient;
import com.vaadin.data.Container;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.HasComponents;

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

			field.addValidator(new BeanValidator(Patient.class, attribute));
		}
	}

	/**
	 * Checks the component recursively and returns the matching component based
	 * on the id.
	 *
	 * @param root
	 * @param id
	 * @return
	 */
	public static Component findById(HasComponents root, String id) {
		logger.debug("findById called on " + root);

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
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Container getContainer(Class<?> objClass) {

		Container container = null;

		if (objClass == Lookup.class)
			container = new BeanItemContainer<Lookup>(
					(Class<? super Lookup>) objClass);

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

}
