/**
 * 
 */
package com.example.medicalarzi.view;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

/**
 * @author mkanchwa
 *
 */
@Theme("medicalarzi")
@UIScope
@SpringView(name = SimpleLoginMainView.NAME)
public class SimpleLoginMainView extends CustomComponent implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7561444653517929974L;

	public static final String NAME = "startingView";

	Label label = new Label();

	Button logout = new Button("Logout", new Button.ClickListener() {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {

			// "Logout" the user
			getSession().setAttribute("user", null);

			// Refresh this view, should redirect to login view
			getUI().getNavigator().navigateTo(NAME);
		}
	});

	/**
	 * 
	 */
	public SimpleLoginMainView() {
		setCompositionRoot(new CssLayout(label));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener
	 * .ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		getUI().getNavigator().navigateTo(SimpleLoginView.NAME);

		// Get the user name from the session
		// String username = String.valueOf(getSession().getAttribute("user"));

		// And show the username
		// label.setValue("Hello " + username);
	}

}
