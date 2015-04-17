package com.example.medicalarzi.view;

import java.text.NumberFormat;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.example.medicalarzi.component.ArziFooterComponent;
import com.example.medicalarzi.component.ArziHeaderComponent;
import com.example.medicalarzi.component.CustomFormComponent;
import com.example.medicalarzi.service.PatientService;
import com.example.medicalarzi.util.MedicalArziConstants;
import com.example.medicalarzi.util.MedicalArziUtils;
import com.vaadin.annotations.Theme;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.converter.StringToLongConverter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.VaadinUIScope;
import com.vaadin.spring.navigator.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author Mustafa Kanchwala
 *
 */
@Theme("medicalarzi")
@VaadinUIScope
@SpringView(name = MedicalArziLandingView.NAME)
public class MedicalArziLandingView extends CustomComponent implements View, ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8787924147975780066L;

	public static final String NAME = "landing";

	public static Logger logger = LogManager
			.getLogger(MedicalArziLandingView.class);

	private VerticalLayout mainLayout;

	private ArziHeaderComponent header;

	private TabSheet tabSheet;

	private CustomFormComponent customForm;

	private ArziFooterComponent footer;
	
	// Fields
	@PropertyId("itsNumber")
	private TextField itsNumber;

	private TextField firstName;
	
	private TextField middleName;

	private TextField lastName;

	@PropertyId("arziType")
	private ComboBox arziType;

	@PropertyId("procedure")
	private ComboBox procedure;

	@PropertyId("condition")
	private ComboBox condition;

	@Autowired
	@Qualifier("service.PatientService")
	private PatientService patientService;

	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 *
	 */
	public MedicalArziLandingView() {
		logger.debug("Inside the Medical Arzi Landing View.");
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// Initialize the components in the view
		init();
		
		// Get the patient's full name from the session
		String patientName = String.valueOf(getSession().getAttribute(
				MedicalArziConstants.SESS_ATTR_PTNT_FULL_NAME));
		
		logger.debug("Patient \"" + patientName + " has logged in successfully.");
		
		Label loggedInName = (Label)MedicalArziUtils.findById(header,
				MedicalArziConstants.HEADER_LOGGED_IN_PTNT_NAME);
		
		loggedInName.setValue(patientName);
		
		Notification.show("Welcome to the Medical Arzi View!!!");
	}

	/**
	 * This method should first build the main layout, set the composition root
	 * and then do any custom initialization.
	 *
	 */
	public void init() {
		buildMainLayout();
		mainLayout.setStyleName(Reindeer.LAYOUT_BLUE);
		setCompositionRoot(mainLayout);
	}

	private void buildMainLayout() {
		// top-level component properties
		setSizeFull();

		// the main layout and components will be created here
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setSizeFull();

		// header
		header = new ArziHeaderComponent();
		header.setImmediate(false);
		header.setHeight("108px");
		mainLayout.addComponent(header);

		// tabSheet
		buildTabSheet();
		mainLayout.addComponent(tabSheet);
		mainLayout.setExpandRatio(tabSheet, 4.0f);
		mainLayout.setComponentAlignment(tabSheet, Alignment.MIDDLE_CENTER);

		// footer
		footer = new ArziFooterComponent();
		footer.setImmediate(false);
		footer.setWidthUndefined();
		footer.setHeight("50px");
		mainLayout.addComponent(footer);
		mainLayout.setComponentAlignment(footer, Alignment.MIDDLE_CENTER);

	}

	/**
	 * This method will build a new tabsheet on this landing page.
	 * 
	 * @return
	 */
	private void buildTabSheet() {
		tabSheet = new TabSheet();
		tabSheet.setStyleName("borderless");
		tabSheet.setSizeFull();

		// customForm
		buildCustomForm();
		tabSheet.addTab(customForm, "New Arzi", null);
	}

	/**
	 * This method will build the CustomForm and add all the fields for a new
	 * arzi into the CustomFormComponent.
	 * 
	 */
	private void buildCustomForm() {

		customForm = new CustomFormComponent();
		customForm.setImmediate(false);
		customForm.setSizeFull();
		customForm.setStyleName("customForm");

		/**
		 * Add the fields to the left FormLayout.
		 * 
		 */
		FormLayout leftFormLayout = (FormLayout) MedicalArziUtils
				.findById(customForm,
						MedicalArziConstants.CUSTOM_FORM_LEFTFORM_LAYOUT_ID);

		// itsNumber
		itsNumber = new TextField("ITS Number:");
		itsNumber.setNullRepresentation("");
		itsNumber.setImmediate(true);
		itsNumber.setRequired(true);
		itsNumber.setMaxLength(8);
		itsNumber.setConverter(new StringToLongConverter() {
			private static final long serialVersionUID = 1L;

			@Override
			protected NumberFormat getFormat(Locale locale) {
				NumberFormat format = super.getFormat(locale);
				format.setGroupingUsed(false);
				return format;
			};
		});
		leftFormLayout.addComponent(itsNumber);
		
		// middleName
		middleName = new TextField("Middle Name:");
		middleName.setWidth("300px");
		leftFormLayout.addComponent(middleName);

		/**
		 * Add the fields to the right FormLayout.
		 * 
		 */
		FormLayout rightFormLayout = (FormLayout) MedicalArziUtils.findById(
				customForm,
				MedicalArziConstants.CUSTOM_FORM_RIGHTFORM_LAYOUT_ID);

		// firstName
		firstName = new TextField("First Name:");
		firstName.setWidth("300px");
		rightFormLayout.addComponent(firstName);
		
		lastName = new TextField("Last Name:");
		lastName.setWidth("300px");
		rightFormLayout.addComponent(lastName);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		
		
	}

}
