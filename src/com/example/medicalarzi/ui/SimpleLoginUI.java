/**
 *
 */
package com.example.medicalarzi.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.medicalarzi.handler.CustomErrorHandler;
import com.example.medicalarzi.util.MedicalArziConstants;
import com.example.medicalarzi.view.ForgotPasswordView;
import com.example.medicalarzi.view.MedicalArziLandingView;
import com.example.medicalarzi.view.PatientRegistrationView;
import com.example.medicalarzi.view.SimpleLoginMainView;
import com.example.medicalarzi.view.SimpleLoginView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.EnableVaadin;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.VaadinUIScope;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;

/**
 * 
 * @author mkanchwa
 *
 */
@Title("Medical Arzi Project")
@Theme("medicalarzi")
@VaadinUIScope
@EnableVaadin
@SpringUI
public class SimpleLoginUI extends UI {

	/**
	 *
	 */
	private static final long serialVersionUID = -7046737238115985152L;
	
	public static Logger logger = LogManager.getLogger(SimpleLoginUI.class);
	
	@Autowired
	private SpringViewProvider viewProvider;
	
	private Navigator navigator;

	/**
	 *
	 */
	public SimpleLoginUI() {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.ui.UI#init(com.vaadin.server.VaadinRequest)
	 */
	@Override
	protected void init(VaadinRequest request) {
		logger.debug("Spring UI initialized.");

		// Create a new instance of the navigator. The navigator will attach
		// itself automatically to this view.
		navigator = new Navigator(this, this);
		// Initially the navigator state is empty and we need to set the error
		// view and this will be the initial view redirected to. The spring
		// services will not be autowired in the view set as an error view so we
		// use this view as a temporary view and then forward it to the Login
		// view.
		navigator.setErrorView(new SimpleLoginMainView());
		// Registers the spring view provider as the default view provider. All
		// the view needs to be annotated with @SpringView
		navigator.addProvider(viewProvider);
		
		setNavigator(navigator);

		/*
		 * the initial log view where the user can login to the application
		 * navigator.addview(simpleloginview.name, simpleloginview.class);
		 *
		 * // add the list view of the application
		 * navigator.addview(myformview.name, myformview.class);
		 *
		 * navigator.addview(arziview.name, arziview.class);
		 *
		 * navigator.addview(patientregistrationview.name,
		 * patientregistrationview.class);
		 */

		// We use a view change handler to ensure the user is always redirected
		// to the login view if the user is not logged in.
		navigator.addViewChangeListener(new ViewChangeListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {

				// Check if a user has logged in
				boolean isLoggedIn = getSession().getAttribute(
						MedicalArziConstants.SESS_ATTR_PTNT_ITS_NUM) != null;
				boolean isLoginView = event.getNewView() instanceof SimpleLoginView;
				boolean isPtntRegistrationView = event.getNewView() instanceof PatientRegistrationView;
				boolean isForgotPasswordView = event.getNewView() instanceof ForgotPasswordView;

				if (isPtntRegistrationView || isForgotPasswordView) {
					return true;
				} else if (!isLoggedIn && !isLoginView) {
					// Redirect to login view always if a user has not yet
					// logged in
					navigator.navigateTo(SimpleLoginView.NAME);
					return false;

				} else if (isLoggedIn && isLoginView) {
					// If someone tries to access to login view while logged in,
					// then cancel
					navigator.navigateTo(MedicalArziLandingView.NAME);
					return false;
				}

				return true;
			}

			@Override
			public void afterViewChange(ViewChangeEvent event) {

			}
		});

		// at main UI ...
		UI.getCurrent().setErrorHandler(new CustomErrorHandler());

		// ... or on session level
		// When a new client connects, it creates a new user session,
		// represented by an instance of VaadinSession.
		// VaadinSession also provides access to the lower-level session
		// objects, HttpSession and PortletSession, through a WrappedSession.
		// Deployment configurations could also be accessed through
		// VaadinSession.
		VaadinSession.getCurrent().setErrorHandler(new CustomErrorHandler());
	}

}
