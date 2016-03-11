/**
 * 
 */
package com.example.medicalarzi.component;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.medicalarzi.converter.StringToLookupConverter;
import com.example.medicalarzi.model.Arzi;
import com.example.medicalarzi.model.ArziType;
import com.example.medicalarzi.model.BodyPart;
import com.example.medicalarzi.model.Condition;
import com.example.medicalarzi.model.GregHijDate;
import com.example.medicalarzi.model.Jamaat;
import com.example.medicalarzi.model.MedicalHistory;
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
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
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

	private static Logger logger = LogManager.getLogger(ArziFormComponent.class);
	
	private static final Integer PRIMARY_HOME_LOCATION_YES = 111;
	
	private static final Integer PRIMARY_HOME_LOCATION_NO = 222;
	
	private static final Integer DIABETES_TYPE_1 = 100;
	
	private static final Integer DIABETES_TYPE_2 = 200;

	// Main View Layout
	private VerticalLayout viewLayout;
	
	/****************************
	 * Arzi Details
	 ***************************/
	private Panel arziDetailsSection;
	
	private VerticalLayout arziDetailsLayout;

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
	private Label otherCondPlaceHolder;
	
	@PropertyId("otherProcedure")
	private TextField otherProcedure;
	private Label otherProcPlaceHolder;
	
	@PropertyId("otherBodyPart")
	private TextField otherBodyPart;
	private Label otherBdyPrtPlaceHolder;

	// arzi summary part of the arzi details section
	@PropertyId("arziSummary")
	private TextArea arziSummary;
	
	/****************************
	 * Patient Medical History
	 ****************************/
	private Panel ptntMedHistSection;
	
	private VerticalLayout ptntMedHistLayout;
	
	private CustomFormComponent ptntMedHistForm;
	
	@PropertyId("asthmaInd")
	private CheckBox asthma;
	
	@PropertyId("cholesterolInd")
	private CheckBox cholesterol;
	
	@PropertyId("atrialFibrillationInd")
	private CheckBox atrialFibrillation;
	
	@PropertyId("diabetesInd")
	private CheckBox diabetes;
	private OptionGroup diabetesOption;
	
	@PropertyId("hyperTensionInd")
	private CheckBox hyperTension;
	
	@PropertyId("thyroidDisorderInd")
	private CheckBox thyroidDisorder;
	
	@PropertyId("cancerInd")
	private CheckBox cancer;
	
	@PropertyId("heartDiseaseInd")
	private CheckBox heartDisease;
	
	@PropertyId("cancerType")
	private TextField cancerType;
	
	@PropertyId("heartDiseaseType")
	private TextField heartDiseaseType;
	
	@PropertyId("otherInfo")
	private TextArea otherProblems;

	/**************************
	 * Patient Personal Info *
	 ***************************/
	private Panel ptntInfoSection;
	
	private VerticalLayout ptntInfoAddrLayout;

	private CustomFormComponent ptntInfoForm;

	// Personal information fields bound to the "Patient" model
	@PropertyId("itsNumber")
	private TextField itsNumber;

	private TextField fullName;

	@PropertyId("gender")
	private TextField gender;

	@PropertyId("dob")
	private ArziDateField dob;
	
	@PropertyId("jamaat")
	private ComboBox jamaat;	
	
	/**Patient Address Info**/
	private CustomFormComponent ptntAddrForm;
	
	@PropertyId("homeAddress1")
	private TextField addressLn1;

	@PropertyId("homeAddress2")
	private TextField addressLn2;

	private ComboBox city;

	private ComboBox state;
	
	private ComboBox country;

	@PropertyId("zip")
	private TextField zip;
	
	private TextField phoneNum;
	
	// permanent address y/n
	private OptionGroup primaryLocationOption;
	
	/*******************
	 * Buttons layout
	 ********************/
	private HorizontalLayout buttonsLayout;

	private Button submitBtn;

	private Button saveBtn;	

	// Binding Fields
	private BeanFieldGroup<Patient> ptntFieldsBinder;

	private BeanFieldGroup<Arzi> arziFieldsBinder;
	
	private BeanFieldGroup<MedicalHistory> medicalHistoryFieldsBinder;
	
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

	public TextField getGender() {
		return gender;
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

	public ComboBox getCity() {
		return city;
	}

	/*
	 * getState() is a Vaadin framework level method and that is the reason we
	 * need to change this getter to getCountryState()
	 */
	public ComboBox getCountryState() {
		return state;
	}

	public ComboBox getCountry() {
		return country;
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

	public TextField getOtherBodyPart() {
		return otherBodyPart;
	}

	public TextArea getOtherProblems() {
		return otherProblems;
	}

	public TextField getOtherProcedure() {
		return otherProcedure;
	}

	public ComboBox getJamaat() {
		return jamaat;
	}

	public TextArea getArziSummary() {
		return arziSummary;
	}

	public CheckBox getAsthma() {
		return asthma;
	}

	public CheckBox getAtrialFibrillation() {
		return atrialFibrillation;
	}
	
	public CheckBox getCholesterol() {
		return cholesterol;
	}

	public CheckBox getHyperTension() {
		return hyperTension;
	}

	public CheckBox getThyroidDisorder() {
		return thyroidDisorder;
	}
	
	public CheckBox getDiabetes() {
		return diabetes;
	}

	public OptionGroup getDiabetesOption() {
		return diabetesOption;
	}

	public CheckBox getCancer() {
		return cancer;
	}

	public CheckBox getHeartDisease() {
		return heartDisease;
	}

	public TextField getCancerType() {
		return cancerType;
	}

	public TextField getHeartDiseaseType() {
		return heartDiseaseType;
	}

	public TextField getFullName() {
		return fullName;
	}

	public TextField getPhoneNum() {
		return phoneNum;
	}

	public OptionGroup getPrimaryLocationOption() {
		return primaryLocationOption;
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
		
		/*Bind the medical history fields)*/
		MedicalHistory medHist = new MedicalHistory();
		medicalHistoryFieldsBinder= new BeanFieldGroup<MedicalHistory>(MedicalHistory.class);
		medicalHistoryFieldsBinder.setItemDataSource(medHist);
		medicalHistoryFieldsBinder.bindMemberFields(this);
		medicalHistoryFieldsBinder.setBuffered(true);
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
		
		// ptntMedHistSection
		buildMedicalHistorySection();
		viewLayout.addComponent(ptntMedHistSection);
		viewLayout.setExpandRatio(ptntMedHistSection, 1.0f);
		
		// ptntInfoSection
		buildPatientInfoAddrSection();
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
	private void buildPatientInfoAddrSection() {
		ptntInfoSection = new Panel("Patient Information :");
		ptntInfoSection.setSizeFull();
		ptntInfoSection.addStyleName("arziContent");
		
		// ptntInfoAddrLayout
		ptntInfoAddrLayout = new VerticalLayout();
		ptntInfoAddrLayout.setImmediate(false);
		ptntInfoAddrLayout.setSizeFull();		

		// ptntInfoForm
		buildPatientInfoForm();
		ptntInfoAddrLayout.addComponent(ptntInfoForm);
		
		Label addressInstruction = new Label();
		addressInstruction
				.setCaption("Please enter the contact information where the patient is currently located:");
		addressInstruction.setStyleName("instructionTxt", true);
		ptntInfoAddrLayout.addComponent(addressInstruction);
		
		// ptntAddrForm
		buildPatienAddressForm();
		ptntInfoAddrLayout.addComponent(ptntAddrForm);
		
		// primaryLocationOption
		buildPrimaryLocationOptions();
		ptntInfoAddrLayout.addComponent(primaryLocationOption);
		
		ptntInfoSection.setContent(ptntInfoAddrLayout);

		// This is needed so that if the panel has a fixed or percentual size
		// and its content becomes too big to fit in the content area, the panel
		// will scroll in the direction so that the content is visible or else
		// the content will be cut to the height of the panel and cannot
		// be scrolled and viewed entirely.
		ptntInfoSection.getContent().setHeightUndefined();
	}
	
	/**
	 * 
	 */
	private void buildPatientInfoForm() {
		// Patient info customForm
		ptntInfoForm = new CustomFormComponent();
		ptntInfoForm.setImmediate(false);
		ptntInfoForm.setSizeFull();
		ptntInfoForm.setStyleName("customForm");		
		
		/**
		 * Add the fields to the left FormLayout.
		 * 
		 */
		FormLayout leftFormLayout = (FormLayout) MedicalArziUtils.findById(
				ptntInfoForm,
				MedicalArziConstants.CUSTOM_FORM_LEFTFORM_LAYOUT_ID);

		// itsNumber
		itsNumber = new TextField("Patient's ITS Number:");
		itsNumber.setReadOnly(true);
		itsNumber.setRequired(true);
		itsNumber.setMaxLength(8);
		itsNumber.setConverter(MedicalArziUtils.itsNumberConverter());
		leftFormLayout.addComponent(itsNumber);

		// gender
		gender = new TextField("Gender:");
		gender.setReadOnly(true);
		gender.setWidth("300px");
		gender.setConverter(new StringToLookupConverter());
		leftFormLayout.addComponent(gender);
		
		jamaat = new ComboBox("Jamaat Affiliation:");
		jamaat.setImmediate(true);
		jamaat.setContainerDataSource(MedicalArziUtils
				.getContainer(Jamaat.class));
		jamaat.addItems(getLookupService().getListOfAllJamaats());
		jamaat.setInputPrompt("Please select your jamaat.");
		jamaat.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		jamaat.setItemCaptionPropertyId("jamaatName");
		jamaat.setRequired(true);
		leftFormLayout.addComponent(jamaat);		

		/**
		 * Add the fields to the right FormLayout.
		 * 
		 */
		FormLayout rightFormLayout = (FormLayout) MedicalArziUtils.findById(
				ptntInfoForm,
				MedicalArziConstants.CUSTOM_FORM_RIGHTFORM_LAYOUT_ID);

		// fullName
		fullName = new TextField("Patient's Name:");
		fullName.setValue(MedicalArziUtils.constructPtntFullName(this.patient));
		fullName.setReadOnly(true);
		fullName.setWidth("300px");
		rightFormLayout.addComponent(fullName);

		dob = new ArziDateField("Date of Birth:");
		dob.setImmediate(true);
		dob.setReadOnly(true);
		dob.setDescription("Please enter the date in the dd/MM/yyy format.");
		rightFormLayout.addComponent(dob);		
	}
	
	/**
	 * 
	 */
	private void buildPatienAddressForm() {
		// Patient info customForm
		ptntAddrForm = new CustomFormComponent();
		ptntAddrForm.setImmediate(false);
		ptntAddrForm.setSizeFull();
		ptntAddrForm.setStyleName("customForm");
		
		/**
		 * Add the fields to the left FormLayout.
		 * 
		 */
		FormLayout leftFormLayout = (FormLayout) MedicalArziUtils.findById(
				ptntAddrForm,
				MedicalArziConstants.CUSTOM_FORM_LEFTFORM_LAYOUT_ID);
		
		// addressLn1
		addressLn1 = new TextField("Address Ln1:");
		addressLn1.setWidth("300px");
		addressLn1.setMaxLength(200);
		addressLn1.setNullRepresentation("");
		leftFormLayout.addComponent(addressLn1);

		// addressLn2
		addressLn2 = new TextField("Address Ln2:");
		addressLn2.setWidth("300px");
		addressLn2.setMaxLength(200);
		addressLn2.setNullRepresentation("");
		leftFormLayout.addComponent(addressLn2);

		// zip
		zip = new TextField("Pincode/Zip:");
		zip.setWidth("100px");
		zip.setMaxLength(10);
		zip.setNullRepresentation("");
		leftFormLayout.addComponent(zip);
		
		// phoneNum
		phoneNum = new TextField("Phone:");
		phoneNum.setWidth("100px");
		phoneNum.setMaxLength(15);
		phoneNum.setNullRepresentation("");
		leftFormLayout.addComponent(phoneNum);		
		
		
		/**
		 * Add the fields to the right FormLayout.
		 * 
		 */
		FormLayout rightFormLayout = (FormLayout) MedicalArziUtils.findById(
				ptntAddrForm,
				MedicalArziConstants.CUSTOM_FORM_RIGHTFORM_LAYOUT_ID);		
		
		// country
		country = new ComboBox("Country:");
		country.setImmediate(true);
		country.setWidth("300px");
		country.setId(MedicalArziConstants.ARZI_FORM_COMPONENT_COUNTRY_COMBOBOX_ID);
		country.addItems(getLookupService().getListOfAllCountries());
		country.setInputPrompt("Please select your country.");
		country.setRequired(true);
		country.addValueChangeListener(this);
		rightFormLayout.addComponent(country);

		// state
		state = new ComboBox("State:");
		state.setWidth("200px");
		state.setId(MedicalArziConstants.ARZI_FORM_COMPONENT_STATE_COMBOBOX_ID);
		state.addValueChangeListener(this);
		rightFormLayout.addComponent(state);
		
		// city
		city = new ComboBox("City:");
		city.setWidth("200px");
		city.setId(MedicalArziConstants.ARZI_FORM_COMPONENT_CITY_COMBOBOX_ID);
		rightFormLayout.addComponent(city);		
	}
	
	/**
	 * This method is responsible for building the radio button option where the
	 * patient can select if the entered address is the primary location for the
	 * patient or the patient is located somewhere else when the arzi is being
	 * submitted.
	 */
	private void buildPrimaryLocationOptions() {
		// gender
		primaryLocationOption = new OptionGroup("Is this the patient's primary home location:");
		
		primaryLocationOption.addItem(PRIMARY_HOME_LOCATION_YES);
		primaryLocationOption.setItemCaption(PRIMARY_HOME_LOCATION_YES, "Yes");
		
		primaryLocationOption.addItem(PRIMARY_HOME_LOCATION_NO);
		primaryLocationOption.setItemCaption(PRIMARY_HOME_LOCATION_NO, "No");
    
		primaryLocationOption.setRequired(true);
		primaryLocationOption.setStyleName("horizontal");
		primaryLocationOption.setStyleName("primLoc", true);
		primaryLocationOption.addValueChangeListener(this);
	}	

	/**
	 * This method is responsible for building the section where the patient
	 * provide details about the condition, procedure and the affected body part
	 * for this arzi
	 */
	private void buildArziDetailsSection() {
		arziDetailsSection = new Panel("Arzi Details:");
		arziDetailsSection.setSizeFull();
		arziDetailsSection.addStyleName("arziContent");
		
		arziDetailsLayout = new VerticalLayout();
		arziDetailsLayout.setImmediate(false);
		arziDetailsLayout.setSizeFull();		

		// Arzi Details CustomForm
		arziDetailsForm = new CustomFormComponent();
		arziDetailsForm.setImmediate(false);
		arziDetailsForm.setSizeFull();
		arziDetailsForm.setStyleName("customForm");
		
		arziDetailsLayout.addComponent(arziDetailsForm);
		arziDetailsSection.setContent(arziDetailsLayout);

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
		condition.setFilteringMode(FilteringMode.CONTAINS);
		condition.addValueChangeListener(this);
		leftFormLayout.addComponent(condition);
		
		// procedure
		procedure = new ComboBox("Medical Procedure:");
		procedure.setContainerDataSource(MedicalArziUtils
				.getContainer(Procedure.class));
		procedure.addItems(getLookupService().getListOfAllMedicalProcedures());
		procedure.setInputPrompt("Please select the medical procedure.");
		procedure.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		procedure.setItemCaptionPropertyId("procedureName");
		procedure.setRequired(true);
		procedure.setFilteringMode(FilteringMode.CONTAINS);
		procedure.addValueChangeListener(this);
		leftFormLayout.addComponent(procedure);

		// bodyPart
		bodyPart = new ComboBox("Body Part:");
		bodyPart.setContainerDataSource(MedicalArziUtils
				.getContainer(BodyPart.class));
		bodyPart.addItems(getLookupService().getListOfAllBodyParts());
		bodyPart.setInputPrompt("Please select the affected body part.");
		bodyPart.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		bodyPart.setItemCaptionPropertyId("bodyPartName");
		bodyPart.setRequired(true);
		bodyPart.setFilteringMode(FilteringMode.CONTAINS);
		bodyPart.addValueChangeListener(this);
		leftFormLayout.addComponent(bodyPart);
		
		/**
		 * Add the fields to the right FormLayout.
		 * 
		 */
		FormLayout rightFormLayout = (FormLayout) MedicalArziUtils.findById(
				arziDetailsForm,
				MedicalArziConstants.CUSTOM_FORM_RIGHTFORM_LAYOUT_ID);

		// condition start date
		conditionStartDate = new ArziDateField("Condition Start Date:");
		conditionStartDate.setImmediate(true);
		conditionStartDate
				.setDescription("Please enter the date in the dd/MM/yyy format.");
		conditionStartDate.setRequired(true);
		rightFormLayout.addComponent(conditionStartDate);

		// other condition
		otherCondition = new TextField("Other Condition:");
		otherCondition.setDescription("Please enter your condition..");
		otherCondition.setWidth("300px");
		otherCondition.setNullRepresentation("");
		otherCondition.setVisible(false);
		rightFormLayout.addComponent(otherCondition);
		
		// other condition placeholder
		otherCondPlaceHolder = new Label("");
		otherCondPlaceHolder.setVisible(true);
		rightFormLayout.addComponent(otherCondPlaceHolder);
		
		// other procedure
		otherProcedure = new TextField("Other Procedure:");
		otherProcedure.setDescription("Please enter your procedure..");
		otherProcedure.setWidth("300px");
		otherProcedure.setNullRepresentation("");
		otherProcedure.setVisible(false);
		rightFormLayout.addComponent(otherProcedure);
		
		// other procedure placeholder
		otherProcPlaceHolder = new Label("");
		otherProcPlaceHolder.setVisible(true);
		rightFormLayout.addComponent(otherProcPlaceHolder);		
		
		// other body part
		otherBodyPart = new TextField("Other Body Part:");
		otherBodyPart.setDescription("Please enter your body part..");
		otherBodyPart.setWidth("300px");
		otherBodyPart.setNullRepresentation("");
		otherBodyPart.setVisible(false);
		rightFormLayout.addComponent(otherBodyPart);
		
		// other body part placeholder
		otherBdyPrtPlaceHolder = new Label("");
		otherBdyPrtPlaceHolder.setVisible(true);
		rightFormLayout.addComponent(otherBdyPrtPlaceHolder);		
		
		// arziSummaryForm
		arziSummary = new TextArea("Summary of Arzi:");
		arziSummary.setImmediate(false);
		arziSummary.setNullRepresentation("");
		arziSummary.setWidth(67, Unit.PERCENTAGE);
		arziSummary.setWordwrap(true);
		arziSummary.setMaxLength(4000);
		arziSummary.setStyleName("arziDetails-arziSmry");
		arziDetailsLayout.addComponent(arziSummary);
	}

	
	/**
	 * 
	 */
	private void buildMedicalHistorySection() {
		ptntMedHistSection = new Panel("Medical History:");
		ptntMedHistSection.setSizeFull();
		ptntMedHistSection.addStyleName("arziContent");
		
		ptntMedHistLayout = new VerticalLayout();
		ptntMedHistLayout.setImmediate(false);
		ptntMedHistLayout.setSizeFull();
		
		ptntMedHistForm = new CustomFormComponent();
		ptntMedHistForm.setImmediate(false);
		ptntMedHistForm.setSizeFull();
		ptntMedHistForm.setStyleName("customForm");
		
		ptntMedHistLayout.addComponent(ptntMedHistForm);
		ptntMedHistSection.setContent(ptntMedHistLayout);
		
		// This is needed so that if the panel has a fixed or percentual size
		// and its content becomes too big to fit in the content area, the panel
		// will scroll in the direction so that the content is visible or else
		// the content will be cut to the width/height of the panel and cannot
		// be scrolled and viewed entirely.
		ptntMedHistSection.getContent().setHeightUndefined();		
		
		/**
		 * Add the fields to the left FormLayout.
		 * 
		 */
		FormLayout leftFormLayout = (FormLayout) MedicalArziUtils.findById(
				ptntMedHistForm,
				MedicalArziConstants.CUSTOM_FORM_LEFTFORM_LAYOUT_ID);
		
		// asthma
		asthma = new CheckBox("Asthma", false);
		asthma.setImmediate(false);
		leftFormLayout.addComponent(asthma);
		
		// cholesterol
		cholesterol = new CheckBox("High Cholesterol", false);
		cholesterol.setImmediate(false);
		leftFormLayout.addComponent(cholesterol);
		
		// atrialFibrillation
		atrialFibrillation = new CheckBox("Atrial Fibrillation (Irregular Heartbeat)", false);
		atrialFibrillation.setImmediate(false);
		leftFormLayout.addComponent(atrialFibrillation);
		
		// cancerLayout
		HorizontalLayout cancerLayout = new HorizontalLayout();
		// cancer
		cancer = new CheckBox("Cancer", false);
		cancer.setId(MedicalArziConstants.ARZI_FORM_COMPONENT_MED_HIST_CANCER_ID);
		cancer.setImmediate(true);
		cancer.addValueChangeListener(this);
		cancerLayout.addComponent(cancer);
		
		// cancerType
		cancerType = new TextField("<i>(Specify Type)</i>:");
		cancerType.setCaptionAsHtml(true);
		cancerType.setWidth("300px");
		cancerType.setVisible(false);
		cancerType.setNullRepresentation("");
		cancerType.setStyleName("cancerType");
		cancerLayout.addComponent(cancerType);
		leftFormLayout.addComponent(cancerLayout);
		
		FormLayout rightFormLayout = (FormLayout) MedicalArziUtils.findById(
				ptntMedHistForm,
				MedicalArziConstants.CUSTOM_FORM_RIGHTFORM_LAYOUT_ID);
		
		// diabetes
		HorizontalLayout diabetesLayout = new HorizontalLayout();
		diabetes = new CheckBox("Diabetes Mellitus", false);
		diabetes.setId(MedicalArziConstants.ARZI_FORM_COMPONENT_MED_HIST_DIABETES_ID);
		diabetes.setImmediate(true);
		diabetes.addValueChangeListener(this);
		// Add diabetes checkbox to the layout
		diabetesLayout.addComponent(diabetes);
		// diabetesOption
		buildDiabetesOptions();
		diabetesLayout.addComponent(diabetesOption);
		// Add the layout to the right form
		rightFormLayout.addComponent(diabetesLayout);
		
		// thyroidDisorder
		thyroidDisorder = new CheckBox("Thyroid Disorder", false);
		thyroidDisorder.setImmediate(false);
		rightFormLayout.addComponent(thyroidDisorder);
		
		// hyperTension
		hyperTension = new CheckBox("Hypertension (High Blood Pressure)", false);
		hyperTension.setImmediate(false);
		rightFormLayout.addComponent(hyperTension);
		
		// heartDiseaseLayout
		HorizontalLayout heartDiseaseLayout = new HorizontalLayout();
		// heartDisease
		heartDisease = new CheckBox("Heart Disease", false);
		heartDisease.setId(MedicalArziConstants.ARZI_FORM_COMPONENT_MED_HIST_HEART_DISEASE_ID);
		heartDisease.setImmediate(true);
		heartDisease.addValueChangeListener(this);
		heartDiseaseLayout.addComponent(heartDisease);	
		
		// heartDiseaseType
		heartDiseaseType = new TextField("<i>(Specify Type)</i>");
		heartDiseaseType.setCaptionAsHtml(true);
		heartDiseaseType.setWidth("300px");
		heartDiseaseType.setVisible(false);
		heartDiseaseType.setNullRepresentation("");
		heartDiseaseType.setStyleName("heartDiseaseType");
		heartDiseaseLayout.addComponent(heartDiseaseType);	
		rightFormLayout.addComponent(heartDiseaseLayout);		
		
		// other problems
		otherProblems = new TextArea("Other Problems/Pre-existing conditions (List below):");
		otherProblems.setImmediate(false);
		otherProblems.setNullRepresentation("");
		otherProblems.setWidth(70, Unit.PERCENTAGE);
		otherProblems.setWordwrap(true);
		otherProblems.setMaxLength(4000);
		otherProblems.setStyleName("ptntMedHist");
		ptntMedHistLayout.addComponent(otherProblems);		
	}
	
	/**
	 * This method is responsible for building the 2 types of diabetes options
	 * for the patient to select if the patient has diabetes as part of their
	 * medical history.
	 */
	private void buildDiabetesOptions() {
		// diabetesOptions
		diabetesOption = new OptionGroup("<i>(Specify Type)</i>");
		diabetesOption.setCaptionAsHtml(true);
		
		diabetesOption.addItem(DIABETES_TYPE_1);
		diabetesOption.setItemCaption(DIABETES_TYPE_1, "Type 1");
		
		diabetesOption.addItem(DIABETES_TYPE_2);
		diabetesOption.setItemCaption(DIABETES_TYPE_2, "Type 2");
    
		diabetesOption.setStyleName("horizontal");
		diabetesOption.setStyleName("diabetesType", true);
		diabetesOption.setVisible(false);
	}	

	/**
	 * 
	 */
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

				// FieldGroup buffered mode is on, so commit() is required.
				// Throws CommitException
				arziFieldsBinder.commit();

				Arzi arziInfo = arziFieldsBinder.getItemDataSource().getBean();
				arziInfo.setItsNumber(ptntInfo.getItsNumber());
				
				medicalHistoryFieldsBinder.commit();

				// FieldGroup buffered mode is on, so commit() is required.
				// Throws CommitException				
				MedicalHistory medHistInfo = medicalHistoryFieldsBinder
						.getItemDataSource().getBean();

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
				
				// If the entered patient is not the primary home location, then
				// do not update the address information in the D_PTNT table but
				// save the address in the F_ARZI_HDR table which is valid for
				// that arzi only.
				Integer selectedPrimLocOpt = (Integer)primaryLocationOption.getValue();
				if (selectedPrimLocOpt.equals(PRIMARY_HOME_LOCATION_NO)) {
					
					logger.debug("The address entered for this arzi is not the primary address for the patient with  ITS number-> \""
							+ ptntInfo.getItsNumber() + "\"");
					
					// Copy the address fields in the the Arzi bean
					arziInfo.setTempHomeAddress1(ptntInfo.getHomeAddress1());
					arziInfo.setTempHomeAddress2(ptntInfo.getHomeAddress2());
					arziInfo.setTempLocation(ptntInfo.getLocation());
					arziInfo.setTempZip(ptntInfo.getZip());

					// Blank out the address info from the Patient bean as we do
					// not want to update the patient's permenant address if
					// this is not the primary location for the patient.
					ptntInfo.setHomeAddress1(null);
					ptntInfo.setHomeAddress2(null);
					ptntInfo.setLocation(null);
					ptntInfo.setZip(null);
				}
				
				logger.debug("Updating the patient information for patient with  ITS number-> \""
						+ ptntInfo.getItsNumber() + "\"");
				
				// Update the D_PTNT table
				getPatientService().updatePatientInfo(ptntInfo);
				
				// Check if the medical history already exists for the patient.
				// If the medical history does not exist create a new record in
				// the F_MED_HIST table or else update the medical history for
				// the patient.
				MedicalHistory savedMedHistory = getPatientService()
						.retreivePatientsMedicalHistory(ptntInfo.getItsNumber());

				if (savedMedHistory == null) {
					medHistInfo.setItsNumber(ptntInfo.getItsNumber());
					getPatientService().savePatientsMedicalHistory(medHistInfo);
					
				} else {
					getPatientService().updatePatientsMedicalHistory(
							savedMedHistory);
				}

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
		
		// If the medical condition, procedure & body part is selected.
		if (event.getProperty().getValue() instanceof Condition) {

			Condition selectedCondition = (Condition) event.getProperty()
					.getValue();

			if (selectedCondition.getConditionId().intValue() == MedicalArziConstants.CONDITION_OTHER_ID
					.intValue()) {
				// Hide the other condition placeholder
				otherCondPlaceHolder.setVisible(false);
				// Display the other condition textfield
				otherCondition.setVisible(true);
				otherCondition.setRequired(true);
			} else {
				// Hide the other condition textfield 
				otherCondition.setVisible(false);
				otherCondition.setRequired(false);
				// Display the other condition placeholder
				otherCondPlaceHolder.setVisible(true);				
			}
			
		} else if (event.getProperty().getValue() instanceof Procedure) {

			Procedure selectedProcedure = (Procedure) event.getProperty()
					.getValue();

			if (selectedProcedure.getProcedureId().intValue() == MedicalArziConstants.PROCEDURE_OTHER_ID
					.intValue()) {
				// Hide the other procedure placeholder
				otherProcPlaceHolder.setVisible(false);
				// Display the other procedure textfield
				otherProcedure.setVisible(true);
				otherProcedure.setRequired(true);

			} else {
				// Hide the other procedure textfield
				otherProcedure.setVisible(false);
				otherProcedure.setRequired(false);
				// Display the other condition placeholder
				otherProcPlaceHolder.setVisible(true);				
			}
			
		} else if (event.getProperty().getValue() instanceof BodyPart) {

			BodyPart selectedBodyPart = (BodyPart) event.getProperty()
					.getValue();

			if (selectedBodyPart.getBodyPartId().intValue() == MedicalArziConstants.BODY_PART_OTHER_ID
					.intValue()) {
				// Hide the other body part placeholder
				otherBdyPrtPlaceHolder.setVisible(false);
				// Display the other body part textfield
				otherBodyPart.setVisible(true);
				otherBodyPart.setRequired(true);

			} else {
				// Hide the other body part textfield
				otherBodyPart.setVisible(false);
				otherBodyPart.setRequired(false);
				// Display the other body part placeholder
				otherBdyPrtPlaceHolder.setVisible(true);
			}
		}
		
		// If the medical history conditions are selected
		if (event.getProperty() instanceof CheckBox) {
			CheckBox checkBox = (CheckBox) event.getProperty();
			if(StringUtils.isNotBlank(checkBox.getId())) {
				// Make the cancer type textfield visible and required for the patient to
				// specify the specific type, if the cancer indicator is selected.			
				if (checkBox.getId().equals(MedicalArziConstants.ARZI_FORM_COMPONENT_MED_HIST_CANCER_ID)) {
					if (checkBox.getValue() != null && checkBox.getValue()) {
						cancerType.setVisible(true);
						cancerType.setRequired(true);
					} else {
						cancerType.setVisible(false);
						cancerType.setRequired(false);
					}
				}
				
				// Make the heart disease type textfield visible and required for the
				// patient to specify the specific type, if the heart disease indicator is
				// selected.
				if (checkBox.getId().equals(MedicalArziConstants.ARZI_FORM_COMPONENT_MED_HIST_HEART_DISEASE_ID)) {
					if (checkBox.getValue() != null && checkBox.getValue()) {
						heartDiseaseType.setVisible(true);
						heartDiseaseType.setRequired(true);

					} else {
						heartDiseaseType.setVisible(false);
						heartDiseaseType.setRequired(false);
					}
				}
				
				// Make sure the diabetesOptions are visible and required for
				// the patient to select, if the diabetes indicator is selected.
				if (checkBox.getId().equals(MedicalArziConstants.ARZI_FORM_COMPONENT_MED_HIST_DIABETES_ID)) {
					if (checkBox.getValue() != null && checkBox.getValue()) {
						diabetesOption.setVisible(true);
						diabetesOption.setRequired(true);
					} else {
						diabetesOption.setVisible(false);
						diabetesOption.setRequired(false);
					}
				}
			}
		}

		// Selected country
		String selectedCountry = null;
		// If the country and the state dropdowns are selected
		if (event.getProperty() instanceof ComboBox) {
			ComboBox comboBox = (ComboBox) event.getProperty();

			if(StringUtils.isNotBlank(comboBox.getId())) {
				// Country is selected
				if (comboBox.getId().equals(MedicalArziConstants.ARZI_FORM_COMPONENT_COUNTRY_COMBOBOX_ID)) {
	
					selectedCountry = (String) comboBox.getValue();
	
					// If the country is selected and state is not selected, we need
					// to load all the states for that country
					if (StringUtils.isNotBlank(selectedCountry)) {
						// Remove the old states and cities if the country is changed.
						state.removeAllItems();
						city.removeAllItems();
						
						state.addItems(getLookupService().getListOfAllStatesForCountry(selectedCountry));
						state.setInputPrompt("Please select your state");
						state.setRequired(true);
					}
					
				} else if (comboBox.getId().equals(MedicalArziConstants.ARZI_FORM_COMPONENT_STATE_COMBOBOX_ID)) {
					// Selected state
					String selectedState = (String) comboBox.getValue();
					
					selectedCountry = (String) country.getValue();
	
					// If the country and state are both selected, we need to load
					// all the cities for that country and state.
					if (StringUtils.isNotBlank(selectedState)) {
						city.removeAllItems();
						city.addItems(
								getLookupService().getListOfAllCitiesForStateAndCountry(selectedState, selectedCountry));
						city.setInputPrompt("Please select your city");
						city.setRequired(true);
					}
				}
			}
		}
		
	}
}
