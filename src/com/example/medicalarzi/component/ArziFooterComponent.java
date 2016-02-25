package com.example.medicalarzi.component;


import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ArziFooterComponent extends CustomComponent {

	/**
	 *
	 */
	private static final long serialVersionUID = -4957349961029159181L;

	public static Logger logger = LogManager.getLogger(ArziFooterComponent.class);

	private VerticalLayout mainLayout;

	private Label message;

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public ArziFooterComponent() {
		
		buildMainLayout();
		
		setCompositionRoot(mainLayout);
	}

	private void buildMainLayout() {
		// top-level component properties
		setSizeFull();

		// the main layout and components will be created here
		mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);

		String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

		message = new Label("� Copyright " + currentYear + " - Anjuman-e-Ezzi, Washington DC developers. All rights reserved.");
		message.setStyleName("footer");
		mainLayout.addComponent(message);
		mainLayout.setComponentAlignment(message, Alignment.MIDDLE_CENTER);

	}

}
