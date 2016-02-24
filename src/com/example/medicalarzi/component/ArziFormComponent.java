/**
 * 
 */
package com.example.medicalarzi.component;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.medicalarzi.converter.StringToLookupConverter;
import com.example.medicalarzi.model.Arzi;
import com.example.medicalarzi.model.ArziType;
import com.example.medicalarzi.model.BodyPart;
import com.example.medicalarzi.model.Condition;
import com.example.medicalarzi.model.GregHijDate;
import com.example.medicalarzi.model.Jamaat;
import com.example.medicalarzi.model.Patient;
import com.example.medicalarzi.model.Procedure;
import com.example.medicalarzi.model.Status;
import com.example.medicalarzi.service.LookupService;
import com.example.medicalarzi.service.PatientService;
import com.example.medicalarzi.service.ServiceLocator;
import com.example.medicalarzi.util.MedicalArziConstants;
import com.example.medicalarzi.util.MedicalArziUtils;
import com.example.medicalarzi.view.MedicalArziLandingView;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.navigator.View;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author mkanchwa
 *
 */
public class ArziFormComponent extends CustomComponent implements
		ValueChangeListener, ClickListener {

	private static final long serialVersionUID = -7784387402793445565L;

	public static Logger logger = LogManager.getLogger(ArziFormComponent.class);

	// Main View Layout
	private VerticalLayout viewLayout;
	
	/****************************
	 * Arzi Details
	 ***************************/
	private Panel arziDetailsSection;

	private CustomFormComponent arziDetailsForm;
	
	// Arzi Details fields bound to the "Arzi" model
	@PropertyId("arziType")
	private ComboBox arziType;

	@PropertyId("bodyPart")
	private ComboBox bodyPart;

	@PropertyId("procedure")
	private ComboBox procedure;

	@PropertyId("condition")
	private ComboBox condition;

	@PropertyId("conditionStartDate.gregorianCalDate")
	private ArziDateField conditionStartDate;

	@PropertyId("otherCondition")
	private TextField otherCondition;	

	/******************
	 * Arzi Summary
	 *******************/
	private Panel arziSummarySection;
	
	private FormLayout arziSummaryForm;
	
	@PropertyId("arziSummary")
	private TextArea arziSummary;
	
	/****************************
	 * Patient Medical History
	 ****************************/
	private Panel ptntMedHistSection;
	
	private CustomFormComponent ptntMedHistForm;

	/**Patient Personal Info**/
	private Panel ptntInfoSection;

	private CustomFormComponent ptntInfoForm;

	// Personal information fields bound to the "Patient" model
	@PropertyId("itsNumber")
	private TextField itsNumber;

	@PropertyId("firstName")
	private TextField firstName;

	@PropertyId("middleName")
	private TextField middleName;

	@PropertyId("gender")
	private TextField gender;

	@PropertyId("lastName")
	private TextField lastName;

	@PropertyId("dob")
	private ArziDateField dob;

	@PropertyId("homeAddress1")
	private TextField addressLn1;

	@PropertyId("homeAddress2")
	private TextField addressLn2;

	@PropertyId("city")
	private TextField city;

	@PropertyId("state")
	private TextField state;

	@PropertyId("zip")
	private TextField zip;

	@PropertyId("jamaat")
	private ComboBox jamaat;
	
	/*******************
	 * Buttons layout
	 ********************/
	private HorizontalLayout buttonsLayout;

	private Button submitBtn;

	private Button saveBtn;	

	// Binding Fields
	private BeanFieldGroup<Patient> ptntFieldsBinder;

	private BeanFieldGroup<Arzi> arziFieldsBinder;

	private Patient patient;
	
	private Arzi arzi;

	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 * 
	 */
	public ArziFormComponent() {
		// Get the patient information from the session. Will be used to bind
		// fields to model.
		patient = (Patient) MedicalArziUtils
				.getSessionAttribute(MedicalArziConstants.SESS_ATTR_PTNT_INFO);

		// Initialize the arzi. Will be used to bind fields to model.
		arzi = new Arzi();

		initForm();
	}

	/**
	 * 
	 * @param patient
	 */
	public ArziFormComponent(Patient patient) {
		// Set the patient information from the passed argument. Will be used to
		// bind fields to model.
		this.patient = patient;

		initForm();
	}

	/**
	 * 
	 * @param arzi
	 */
	public ArziFormComponent(Arzi arzi) {
		// Set the arzi information from the passed argument. Will be used to
		// bind fields to model.
		this.arzi = arzi;

		initForm();
	}

	/**
	 * 
	 * @param patient
	 * @param arzi
	 */
	public ArziFormComponent(Patient patient, Arzi arzi) {
		// Set the patient information from the passed argument. Will be used to
		// bind fields to model.
		this.patient = patient;

		// Set the arzi information from the passed argument. Will be used to
		// bind fields to model.
		this.arzi = arzi;

		initForm();
	}

	/**
	 * Locates and returns the lookupService
	 * 
	 * @return com.example.medicalservice.service.LookupService
	 */
	private LookupService getLookupService() {
		return ServiceLocator.getInstance().getLookupService();
	}

	/**
	 * Locates and returns the patientService
	 * 
	 * @return com.example.medicalservice.service.PatientService
	 */
	private PatientService getPatientService() {
		return ServiceLocator.getInstance().getPatientService();
	}
	
	public TextField getItsNumber() {
		return itsNumber;
	}

	public TextField getFirstName() {
		return firstName;
	}

	public TextField getGender() {
		return gender;
	}

	public TextField getLastName() {
		return lastName;
	}

	public TextField getMiddleName() {
		return middleName;
	}

	public ArziDateField getDob() {
		return dob;
	}

	public TextField getAddressLn1() {
		return addressLn1;
	}

	public TextField getAddressLn2() {
		return addressLn2;
	}

	public TextField getCountryState() {
		return state;
	}

	public TextField getCity() {
		return city;
	}

	public ComboBox getArziType() {
		return arziType;
	}

	public ComboBox getBodyPart() {
		return bodyPart;
	}

	public ComboBox getProcedure() {
		return procedure;
	}

	public ArziDateField getConditionStartDate() {
		return conditionStartDate;
	}

	public TextField getOtherCondition() {
		return otherCondition;
	}

	public ComboBox getJamaat() {
		return jamaat;
	}

	public ComboBox getCondition() {
		return condition;
	}
	
	public TextField getZip() {
		return zip;
	}

	/**
	 * Initialize this component with the layout and form components and bind
	 * the form fields to the model.
	 * 
	 */
	private void initForm() {
		// Build the layout and elements
		buildViewlayout();

		// Bind the member fields to the model
		bindFieldsToModel();

		// The root component. Must be set
		setCompositionRoot(viewLayout);		
	}


	/**
	 * This method is responsible for binding the member fields to the Patient
	 * and Arzi Model because we update the the patient information in the
	 * D_PTNT table and the arzi information in the F_ARZI_HDR table.
	 */
	private void bindFieldsToModel() {
		logger.debug("Binding the patient and the arzi fields to their respective models.");

		/* Bind the patient fields */
		ptntFieldsBinder = new BeanFieldGroup<Patient>(Patient.class);
		// Add bean item that is bound.
		ptntFieldsBinder.setItemDataSource(patient);
		// Bind all the member fields whose type extends Field to the property
		// id of the item.
		// The mapping is done based on the @PropertyId annotation
		ptntFieldsBinder.bindMemberFields(this);
		// Set the buffered mode on, if using the bean validation JSR-3.0 with
		// vaadin. Does not work when fields are immediately updated with the
		// bean.
		ptntFieldsBinder.setBuffered(true);

		/* Bind the arzi fields. */
		arziFieldsBinder = new BeanFieldGroup<Arzi>(Arzi.class);
		/**
		 * =======>IMPORTANT: Binding for nested bean will work by switching
		 * "setItemDataSource" and "bindMemberFields".
		 * 
		 * =======>First bindMembers then add dataSource.
		 * **/
		arziFieldsBinder.bindMemberFields(this);
		arziFieldsBinder.setItemDataSource(arzi);
		arziFieldsBinder.setBuffered(true);

	}

	/**
	 * This method will build the CustomForm and add all the fields for a new
	 * arzi into the CustomFormComponent.
	 * 
	 */
	private void buildViewlayout() {
		// viewLayout
		viewLayout = new VerticalLayout();
		viewLayout.setId(MedicalArziConstants.ARZI_FORM_COMPONENT_VIEW_LAYOUT_ID);
		viewLayout.setSizeFull();
		viewLayout.setImmediate(false);
		viewLayout.setMargin(true);
		viewLayout.setSpacing(true);

		// arziDetailsSection
		buildArziDetailsSection();
		viewLayout.addComponent(arziDetailsSection);
		viewLayout.setExpandRatio(arziDetailsSection, 1.0f);
		
		// arziSummarySection
		buildArziSummarySection();
		viewLayout.addComponent(arziSummarySection);
		viewLayout.setExpandRatio(arziSummarySection, 1.0f);
		
		// ptntInfoSection
		buildPatientInfoSection();
		viewLayout.addComponent(ptntInfoSection);
		viewLayout.setExpandRatio(ptntInfoSection, 1.0f);

		// buttons
		buildButtonsLayout();
		// adds buttonLayout to the viewLayout
		viewLayout.addComponent(buttonsLayout);
		viewLayout
				.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
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

		// Patient info customForm
		ptntInfoForm = new CustomFormComponent();
		ptntInfoForm.setImmediate(false);
		ptntInfoForm.setSizeFull();
		ptntInfoForm.setStyleName("customForm");
		ptntInfoSection.setContent(ptntInfoForm);

		// This is needed so that if the panel has a fixed or percentual size
		// and its content becomes too big to fit in the content area, the panel
		// will scroll in the direction so that the content is visible or else
		// the content will be cut to the height of the panel and cannot
		// be scrolled and viewed entirely.
		ptntInfoSection.getContent().setHeightUndefined();

		/**
		 * Add the fields to the left FormLayout.
		 * 
		 */
		FormLayout leftFormLayout = (FormLayout) MedicalArziUtils.findById(
				ptntInfoForm,
				MedicalArziConstants.CUSTOM_FORM_LEFTFORM_LAYOUT_ID);

		// itsNumber
		itsNumber = new TextField("ITS Number:");
		itsNumber.setReadOnly(true);
		itsNumber.setRequired(true);
		itsNumber.setMaxLength(8);
		itsNumber.setConverter(MedicalArziUtils.itsNumberConverter());
		leftFormLayout.addComponent(itsNumber);

		// middleName
		middleName = new TextField("Middle Name:");
		middleName.setNullRepresentation("");
		middleName.setWidth("300px");
		leftFormLayout.addComponent(middleName);

		// gender
		gender = new TextField("Gender:");
		gender.setWidth("300px");
		gender.setConverter(new StringToLookupConverter());
		leftFormLayout.addComponent(gender);

		// addressLn1
		addressLn1 = new TextField("Address Ln1:");
		addressLn1.setWidth("300px");
		addressLn1.setNullRepresentation("");
		leftFormLayout.addComponent(addressLn1);

		// addressLn2
		addressLn2 = new TextField("Address Ln2:");
		addressLn2.setWidth("300px");
		addressLn2.setNullRepresentation("");
		leftFormLayout.addComponent(addressLn2);

		// zip
		zip = new TextField("Pincode/Zip:");
		zip.setWidth("300px");
		zip.setNullRepresentation("");
		leftFormLayout.addComponent(zip);

		/**
		 * Add the fields to the right FormLayout.
		 * 
		 */
		FormLayout rightFormLayout = (FormLayout) MedicalArziUtils.findById(
				ptntInfoForm,
				MedicalArziConstants.CUSTOM_FORM_RIGHTFORM_LAYOUT_ID);

		// firstName
		firstName = new TextField("First Name:");
		firstName.setReadOnly(true);
		firstName.setWidth("300px");
		rightFormLayout.addComponent(firstName);

		lastName = new TextField("Surname/Last Name:");
		lastName.setReadOnly(true);
		lastName.setWidth("300px");
		rightFormLayout.addComponent(lastName);

		dob = new ArziDateField("Date of Birth:");
		dob.setImmediate(true);
		dob.setDescription("Please enter the date in the dd/MM/yyy format.");
		rightFormLayout.addComponent(dob);

		// city
		city = new TextField("City:");
		city.setWidth("300px");
		city.setNullRepresentation("");
		rightFormLayout.addComponent(city);

		// state
		state = new TextField("State:");
		state.setWidth("300px");
		state.setNullRepresentation("");
		rightFormLayout.addComponent(state);

		jamaat = new ComboBox("Jamaat:");
		jamaat.setImmediate(true);
		jamaat.setContainerDataSource(MedicalArziUtils
				.getContainer(Jamaat.class));
		jamaat.addItems(getLookupService().getListOfAllJamaats());
		jamaat.setInputPrompt("Please select your jamaat.");
		jamaat.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		jamaat.setItemCaptionPropertyId("jamaatName");
		jamaat.setRequired(true);
		rightFormLayout.addComponent(jamaat);
	}

	/**
	 * 
	 */
	private void buildArziDetailsSection() {
		arziDetailsSection = new Panel("Arzi Details:");
		arziDetailsSection.setSizeFull();
		arziDetailsSection.addStyleName("arziContent");

		// Arzi Details CustomForm
		arziDetailsForm = new CustomFormComponent();
		arziDetailsForm.setImmediate(false);
		arziDetailsForm.setSizeFull();
		arziDetailsForm.setStyleName("customForm");
		arziDetailsSection.setContent(arziDetailsForm);

		// This is needed so that if the panel has a fixed or percentual size
		// and its content becomes too big to fit in the content area, the panel
		// will scroll in the direction so that the content is visible or else
		// the content will be cut to the width/height of the panel and cannot
		// be scrolled and viewed entirely.
		arziDetailsSection.getContent().setHeightUndefined();

		/**
		 * Add the fields to the left FormLayout.
		 * 
		 */
		FormLayout leftFormLayout = (FormLayout) MedicalArziUtils.findById(
				arziDetailsForm,
				MedicalArziConstants.CUSTOM_FORM_LEFTFORM_LAYOUT_ID);
		// arziType
		arziType = new ComboBox("Arzi Type:");
		arziType.setContainerDataSource(MedicalArziUtils
				.getContainer(ArziType.class));
		arziType.addItems(getLookupService().getListOfAllArziTypes());
		arziType.setInputPrompt("Please select the arzi type.");
		arziType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		arziType.setItemCaptionPropertyId("arziTypeName");
		arziType.setRequired(true);
		leftFormLayout.addComponent(arziType);
		
		// condition
		condition = new ComboBox("Medical Condition:");
		condition.setImmediate(true);
		condition.setContainerDataSource(MedicalArziUtils
				.getContainer(Condition.class));
		condition.addItems(getLookupService().getListOfAllMedicalConditions());
		condition.setInputPrompt("Please select your medical condition.");
		condition.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		condition.setItemCaptionPropertyId("conditionName");
		condition.setRequired(true);
		condition.addValueChangeListener(this);
		leftFormLayout.addComponent(condition);

		// other condition
		otherCondition = new TextField("Other Condition:");
		otherCondition.setDescription("Please enter your condition..");
		otherCondition.setWidth("300px");
		otherCondition.setNullRepresentation("");
		leftFormLayout.addComponent(otherCondition);		


		/**
		 * Add the fields to the right FormLayout.
		 * 
		 */
		FormLayout rightFormLayout = (FormLayout) MedicalArziUtils.findById(
				arziDetailsForm,
				MedicalArziConstants.CUSTOM_FORM_RIGHTFORM_LAYOUT_ID);

		// bodyPart
		bodyPart = new ComboBox("Body Part:");
		bodyPart.setContainerDataSource(MedicalArziUtils
				.getContainer(BodyPart.class));
		bodyPart.addItems(getLookupService().getListOfAllBodyParts());
		bodyPart.setInputPrompt("Please select the affected body part.");
		bodyPart.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		bodyPart.setItemCaptionPropertyId("bodyPartName");
		bodyPart.setRequired(true);
		rightFormLayout.addComponent(bodyPart);
		
		// condition start date
		conditionStartDate = new ArziDateField("Condition Start Date:");
		conditionStartDate.setImmediate(true);
		conditionStartDate
				.setDescription("Please enter the date in the dd/MM/yyy format.");
		conditionStartDate.setRequired(true);
		rightFormLayout.addComponent(conditionStartDate);

		// procedure
		procedure = new ComboBox("Medical Procedure:");
		procedure.setContainerDataSource(MedicalArziUtils
				.getContainer(Procedure.class));
		procedure.addItems(getLookupService().getListOfAllMedicalProcedures());
		procedure.setInputPrompt("Please select the medical procedure.");
		procedure.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		procedure.setItemCaptionPropertyId("procedureName");
		procedure.setRequired(true);
		rightFormLayout.addComponent(procedure);
	}
	
	private void buildArziSummarySection() {
		arziSummarySection = new Panel("Brief Summary of Arzi:");
		arziSummarySection.setSizeFull();
		arziSummarySection.addStyleName("arziContent");
		
		arziSummaryForm = new FormLayout();
		arziSummaryForm.setImmediate(false);
		arziSummaryForm.setSizeFull();
		arziSummaryForm.setStyleName("customForm");
		arziSummarySection.setContent(arziSummaryForm);
		
		arziSummary = new TextArea("Summary:");
		arziSummary.setImmediate(false);
		arziSummary.setNullRepresentation("");
		arziSummary.setWidth(70, Unit.PERCENTAGE);
		arziSummary.setWordwrap(true);
		arziSummary.setMaxLength(4000);
		arziSummary.setRequired(true);
		arziSummaryForm.addComponent(arziSummary);
	}	

	private void buildButtonsLayout() {

		buttonsLayout = new HorizontalLayout();
		buttonsLayout.setId(MedicalArziConstants.ARZI_FORM_COMPONENT_BUTTON_LAYOUT_ID);

		// saveBtn
		saveBtn = new Button(new ThemeResource("img/save-arzi.png"));
		saveBtn.addClickListener(this);
		saveBtn.setStyleName(Reindeer.BUTTON_LINK);
		buttonsLayout.addComponent(saveBtn);
		buttonsLayout.setExpandRatio(saveBtn, 1.0f);
		buttonsLayout.setComponentAlignment(saveBtn, Alignment.MIDDLE_RIGHT);

		// submitBtn
		submitBtn = new Button(new ThemeResource("img/submit-arzi.png"));
		submitBtn.addClickListener(this);
		submitBtn.setStyleName(Reindeer.BUTTON_LINK);
		buttonsLayout.addComponent(submitBtn);
		buttonsLayout.setExpandRatio(submitBtn, 1.0f);
		buttonsLayout.setComponentAlignment(submitBtn, Alignment.MIDDLE_LEFT);

		buttonsLayout.setSpacing(true);
		buttonsLayout.setStyleName("ptntRegistrationBtn");
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(saveBtn)
				|| event.getButton().equals(submitBtn)) {

			// Insert a new arzi into the database and change the status to
			// save/submitted
			try {
				// FieldGroup buffered mode is on, so commit() is required.
				// Throws CommitException
				ptntFieldsBinder.commit();

				Patient ptntInfo = ptntFieldsBinder.getItemDataSource()
						.getBean();

				// Update the D_PTNT table
				getPatientService().updatePatientInfo(ptntInfo);

				// FieldGroup buffered mode is on, so commit() is required.
				// Throws CommitException
				arziFieldsBinder.commit();

				Arzi arziInfo = arziFieldsBinder.getItemDataSource().getBean();

				GregHijDate ghReqSubmitDt = getLookupService()
						.getRequestedGregorianHijriCalendar(new Date());

				arziInfo.setCurrentStatusDate(ghReqSubmitDt);

				// Get the entire data for the GregHijDate based on the
				// gregorian date.
				arziInfo.setConditionStartDate(getLookupService()
						.getRequestedGregorianHijriCalendar(
								arziInfo.getConditionStartDate()
										.getGregorianCalDate()));

				Status arziStatus = new Status();
				// Set the status to "Draft' when Save Btn is clicked and
				// 'Submitted' when Submit Btn is clicked.
				if (event.getButton().equals(saveBtn)) {
					arziStatus
							.setStatusId(MedicalArziConstants.ARZI_DRAFT_STATUS);
				} else {
					arziInfo.setRequestSubmitDate(ghReqSubmitDt);
					
					arziStatus
							.setStatusId(MedicalArziConstants.ARZI_SUBMITTED_STATUS);
					
					// Needed for the entry in the detail table. Only after the
					// arzi is submitted, the detail rable is updated.
					arziInfo.setStatus(arziStatus);
					arziInfo.setStatusChangeDate(ghReqSubmitDt);
				}

				arziInfo.setCurrentStatus(arziStatus);

				logger.debug("Inserting a new arzi for patient with ITS number-> \""
						+ ptntInfo.getItsNumber() + "\"");

				// Insert the new arzi for the patient
				getPatientService().createNewArzi(arziInfo);

				// Create a user friendly notification on success.
				String successMsg = "The arzi for \""
						+ MedicalArziUtils.constructPtntFullName(ptntInfo)
						+ "\" is successfully created";

				// Get the current view and set the selected tab to Inbox to
				// display the newly saved/submitted arzi in the Inbox.
				View currentView = UI.getCurrent().getNavigator()
						.getCurrentView();

				if (currentView instanceof MedicalArziLandingView) {
					MedicalArziLandingView landingView = (MedicalArziLandingView) currentView;
					
					// Once the new arzi is submitted, reconstruct the inbox tab to
					// get the latest data and then switch the selection to the
					// Inbox tab with the user friendly success message.
					landingView.setRefreshInbox(true);

					landingView.getTabSheet().setSelectedTab(
							landingView.getInboxComponent());
				}

				MedicalArziUtils.createAndShowNotification(null, successMsg,
						Type.HUMANIZED_MESSAGE, Position.TOP_LEFT,
						"userFriendlyMsg",
						MedicalArziConstants.USER_FRIENDLY_MSG_DELAY_MSEC);

			} catch (CommitException ce) {
				logger.error(ce);

				String errorDescription = "Fields marked with asterisk (*) are required. "
						+ "Please enter the required values and fix the errors before proceeding further.";

				// Create an error notification if the required fields are not
				// entered correctly.
				MedicalArziUtils.createAndShowNotification(null,
						errorDescription, Type.ERROR_MESSAGE,
						Position.TOP_LEFT, "errorMsg", -1);
			}
		}
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		if (event.getProperty().getValue() instanceof Condition) {

			Condition selectedCondition = (Condition) event.getProperty()
					.getValue();

			if (selectedCondition.getConditionId().intValue() == MedicalArziConstants.MAP_OTHER_CONDITION_ID
					.intValue()) {
				otherCondition.setVisible(true);
				otherCondition.setRequired(true);

			} else {
				otherCondition.setVisible(false);
				otherCondition.setRequired(false);
			}
		}
	}

}
