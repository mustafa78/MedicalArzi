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
import com.example.medicalarzi.model.BodyPart;
import com.example.medicalarzi.model.Condition;
import com.example.medicalarzi.model.Procedure;
import com.example.medicalarzi.service.LookupService;
import com.example.medicalarzi.service.PatientService;
import com.example.medicalarzi.util.MedicalArziConstants;
import com.example.medicalarzi.util.MedicalArziUtils;
import com.vaadin.annotations.Theme;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.converter.StringToLongConverter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.VaadinUIScope;
import com.vaadin.spring.navigator.annotation.SpringView;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
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
public class MedicalArziLandingView extends CustomComponent implements View,
		ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8787924147975780066L;

	public static final String NAME = "landing";

	public static Logger logger = LogManager
			.getLogger(MedicalArziLandingView.class);

	private VerticalLayout mainLayout;

	// Header
	private ArziHeaderComponent header;

	private VerticalLayout viewLayout;

	private TabSheet tabSheet;

	// Patient Personal Info
	private Panel ptntInfoSection;

	private CustomFormComponent ptntInfoForm;

	// Patient Medical Info
	private Panel ptntMedicalInfoSection;

	private CustomFormComponent ptntMedicalInfoForm;
	
	// Buttons layout
	private HorizontalLayout buttonsLayout;
	
	private Button submitBtn;
	
	private Button saveBtn;

	private Button cancelBtn;

	// Footer
	private ArziFooterComponent footer;

	// Personal information fields
	@PropertyId("itsNumber")
	private TextField itsNumber;

	private TextField firstName;

	private TextField middleName;

	private TextField lastName;

	// Medical information fields
	@PropertyId("arziType")
	private ComboBox arziType;

	@PropertyId("bodyPart")
	private ComboBox bodyPart;

	@PropertyId("procedure")
	private ComboBox procedure;

	@PropertyId("condition")
	private ComboBox condition;
	
	private TextArea description;

	// Service stubs
	@Autowired
	@Qualifier("service.PatientService")
	private PatientService patientService;

	@Autowired
	@Qualifier("service.LookupService")
	private LookupService lookupService;

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

		logger.debug("Patient \"" + patientName
				+ " has logged in successfully.");

		Label loggedInName = (Label) MedicalArziUtils.findById(header,
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
		tabSheet.addStyleName("borderless");
		tabSheet.addStyleName("mapTabsheet");
		tabSheet.setSizeFull();

		// viewLayout
		buildViewlayout();
		tabSheet.addTab(viewLayout, "New Arzi", null);
		tabSheet.addTab(new Label("Test tab"), "Test Tab", null);
	}

	/**
	 * This method will build the CustomForm and add all the fields for a new
	 * arzi into the CustomFormComponent.
	 * 
	 */
	private void buildViewlayout() {
		// viewLayout
		viewLayout = new VerticalLayout();
		viewLayout.setSizeFull();
		viewLayout.setImmediate(false);
		viewLayout.setMargin(true);
		viewLayout.setSpacing(true);

		// ptntInfoSection
		buildPatientInfoSection();

		// ptntMedicalSection
		buildPatientMedicalSection();
		
		// buttonsLayout
		buildButtonsLayout();
	}

	/**
	 * This method will build the patient information section of the Patient
	 * Arzi.
	 * 
	 */
	private void buildPatientInfoSection() {
		ptntInfoSection = new Panel("Patient Information :");
		ptntInfoSection.setSizeFull();
		ptntInfoSection.addStyleName("arziContent");
		viewLayout.addComponent(ptntInfoSection);
		viewLayout.setExpandRatio(ptntInfoSection, 1.0f);

		// Patient info customForm
		ptntInfoForm = new CustomFormComponent();
		ptntInfoForm.setImmediate(false);
		ptntInfoForm.setSizeFull();
		ptntInfoForm.setStyleName("customForm");
		ptntInfoSection.setContent(ptntInfoForm);

		/**
		 * Add the fields to the left FormLayout.
		 * 
		 */
		FormLayout leftFormLayout = (FormLayout) MedicalArziUtils.findById(
				ptntInfoForm,
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
				ptntInfoForm,
				MedicalArziConstants.CUSTOM_FORM_RIGHTFORM_LAYOUT_ID);

		// firstName
		firstName = new TextField("First Name:");
		firstName.setWidth("300px");
		rightFormLayout.addComponent(firstName);

		lastName = new TextField("Last Name:");
		lastName.setWidth("300px");
		rightFormLayout.addComponent(lastName);
	}

	/**
	 * 
	 */
	private void buildPatientMedicalSection() {
		ptntMedicalInfoSection = new Panel("Medical Conditions :");
		ptntMedicalInfoSection.setSizeFull();
		ptntMedicalInfoSection.addStyleName("arziContent");
		viewLayout.addComponent(ptntMedicalInfoSection);
		viewLayout.setExpandRatio(ptntMedicalInfoSection, 1.0f);

		// Patient info customForm
		ptntMedicalInfoForm = new CustomFormComponent();
		ptntMedicalInfoForm.setImmediate(false);
		ptntMedicalInfoForm.setSizeFull();
		ptntMedicalInfoForm.setStyleName("customForm");
		ptntMedicalInfoSection.setContent(ptntMedicalInfoForm);

		/**
		 * Add the fields to the left FormLayout.
		 * 
		 */
		FormLayout leftFormLayout = (FormLayout) MedicalArziUtils.findById(
				ptntMedicalInfoForm,
				MedicalArziConstants.CUSTOM_FORM_LEFTFORM_LAYOUT_ID);

		// bodyPart
		bodyPart = new ComboBox("Body Part:");
		bodyPart.setContainerDataSource(MedicalArziUtils
				.getContainer(BodyPart.class));
		bodyPart.addItems(lookupService
				.getListOfAllBodyParts());
		bodyPart.setInputPrompt("Please select the affected body part.");
		bodyPart.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		bodyPart.setItemCaptionPropertyId("bodyPartName");
		leftFormLayout.addComponent(bodyPart);
		
		// procedure
		procedure = new ComboBox("Medical Procedure:");
		procedure.setContainerDataSource(MedicalArziUtils
				.getContainer(Procedure.class));
		procedure.addItems(lookupService
				.getListOfAllMedicalProcedures());
		procedure.setInputPrompt("Please select the medical procedure.");
		procedure.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		procedure.setItemCaptionPropertyId("procedureName");
		leftFormLayout.addComponent(procedure);		

		/**
		 * Add the fields to the right FormLayout.
		 * 
		 */
		FormLayout rightFormLayout = (FormLayout) MedicalArziUtils.findById(
				ptntMedicalInfoForm,
				MedicalArziConstants.CUSTOM_FORM_RIGHTFORM_LAYOUT_ID);

		condition = new ComboBox("Medical Condition:");
		condition.setContainerDataSource(MedicalArziUtils
				.getContainer(Condition.class));
		condition.addItems(lookupService
				.getListOfAllMedicalConditions());
		condition.setInputPrompt("Please select your medical condition.");
		condition.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		condition.setItemCaptionPropertyId("conditionName");
		rightFormLayout.addComponent(condition);
	}
	
	private void buildButtonsLayout() {

		buttonsLayout = new HorizontalLayout();

		// cancelBtn
		cancelBtn = new Button(new ThemeResource("img/cancel.png"));
		cancelBtn.setStyleName(Reindeer.BUTTON_LINK);
		cancelBtn.addClickListener(this);
		cancelBtn.setImmediate(true);
		buttonsLayout.addComponent(cancelBtn);
		buttonsLayout.setExpandRatio(cancelBtn, 1.0f);
		buttonsLayout.setComponentAlignment(cancelBtn, Alignment.MIDDLE_RIGHT);
		
		// saveBtn
		saveBtn = new Button("", this);
		saveBtn.setIcon(new ThemeResource("img/save.png"));
		saveBtn.setStyleName(Reindeer.BUTTON_LINK);
		buttonsLayout.addComponent(saveBtn);
		buttonsLayout.setExpandRatio(saveBtn, 0.2f);
		buttonsLayout.setComponentAlignment(saveBtn, Alignment.MIDDLE_CENTER);

		// submitBtn
		submitBtn = new Button("", this);
		submitBtn.setIcon(new ThemeResource("img/submit.png"));
		submitBtn.setStyleName(Reindeer.BUTTON_LINK);
		buttonsLayout.addComponent(submitBtn);
		buttonsLayout.setExpandRatio(submitBtn, 1.0f);
		buttonsLayout.setComponentAlignment(submitBtn, Alignment.MIDDLE_LEFT);

		buttonsLayout.setSpacing(true);
		buttonsLayout.setStyleName("ptntRegistrationBtn");
		
		// adds buttonLayout to the viewLayout
		viewLayout.addComponent(buttonsLayout);
		viewLayout.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
	}	

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(cancelBtn)) {
			getUI().getNavigator().navigateTo(SimpleLoginView.NAME);
			
			MedicalArziUtils.setSessionAttribute(
					"isRegistrationSuccess", false);			
		}
		else if(event.getButton().equals(saveBtn)) {
			//Insert a new arzi into the database and change the status to save
		}
		else if(event.getButton().equals(submitBtn)) {
			//Insert a new arzi into the database and change the status to submitted
		}
	}

}
