/**
 *
 */
package com.example.medicalarzi.handler;

import java.sql.SQLException;

import org.apache.ibatis.exceptions.PersistenceException;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.AbstractErrorMessage.ContentMode;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.ErrorEvent;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Page;
import com.vaadin.server.UserError;
import com.vaadin.shared.Position;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

/**
 * @author mkanchwa
 *
 */
public class CustomErrorHandler implements ErrorHandler {

	/**
	 *
	 */
	private static final long serialVersionUID = 7229687994546616265L;

	/**
	 *
	 */
	public CustomErrorHandler() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.server.ErrorHandler#error(com.vaadin.server.ErrorEvent)
	 */
	@Override
	public void error(ErrorEvent event) {
		// Finds the original source of the error/exception
		AbstractComponent component = DefaultErrorHandler
				.findAbstractComponent(event);
		if (component != null) {
			ErrorMessage errorMessage = getErrorMessageForException(event
					.getThrowable());
			if (errorMessage != null) {
				component.setComponentError(errorMessage);
				Notification notif = new Notification(null, errorMessage.getFormattedHtmlMessage(),
						Type.ERROR_MESSAGE, true);
				notif.setPosition(Position.TOP_LEFT);
				notif.setStyleName("errorMsg");
				notif.show(Page.getCurrent());
				return;
			}
		}
		DefaultErrorHandler.doDefault(event);
	}

	private static ErrorMessage getErrorMessageForException(Throwable t) {

		PersistenceException persistenceException = getCauseOfType(t,
				PersistenceException.class);
		if (persistenceException != null) {
			return new UserError(persistenceException.getLocalizedMessage(),
					ContentMode.TEXT, ErrorMessage.ErrorLevel.ERROR);
		}

		SQLException sqlException = getCauseOfType(t, SQLException.class);
		if (sqlException != null) {
			return new UserError(sqlException.getLocalizedMessage(),
					ContentMode.TEXT, ErrorMessage.ErrorLevel.ERROR);
		}

		FieldGroup.CommitException commitException = getCauseOfType(t,
				FieldGroup.CommitException.class);
		if (commitException != null) {
			return new UserError(commitException.getLocalizedMessage(),
					ContentMode.TEXT, ErrorMessage.ErrorLevel.ERROR);
		}

		Converter.ConversionException conversionException = getCauseOfType(t,
				Converter.ConversionException.class);
		if (conversionException != null) {
			return new UserError(conversionException.getLocalizedMessage(),
					ContentMode.TEXT, ErrorMessage.ErrorLevel.ERROR);
		}

		return new UserError(
				"Unexpected exception occured from: "
						+ t.getClass().getName()
						+ ". Please email mustafa.kanchwala@gmail.com for further assistance.");

	}

	@SuppressWarnings("unchecked")
	private static <T extends Throwable> T getCauseOfType(Throwable th,
			Class<T> type) {
		while (th != null) {
			if (type.isAssignableFrom(th.getClass())) {
				return (T) th;
			} else {
				th = th.getCause();
			}
		}
		return null;
	}

}
