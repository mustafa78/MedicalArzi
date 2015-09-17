package com.example.medicalarzi.view;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.example.medicalarzi.component.ArziDateField;
import com.example.medicalarzi.component.ArziFooterComponent;
import com.example.medicalarzi.component.ArziHeaderComponent;
import com.example.medicalarzi.component.CustomFormComponent;
import com.example.medicalarzi.converter.StringToLookupConverter;
import com.example.medicalarzi.handler.MedicalArziCommitHandler;
import com.example.medicalarzi.model.Arzi;
import com.example.medicalarzi.model.ArziType;
import com.example.medicalarzi.model.BodyPart;
import com.example.medicalarzi.model.Condition;
import com.example.medicalarzi.model.GregHijDate;
import com.example.medicalarzi.model.Patient;
import com.example.medicalarzi.model.Procedure;
import com.example.medicalarzi.model.Status;
import com.example.medicalarzi.service.LookupService;
import com.example.medicalarzi.service.PatientService;
import com.example.medicalarzi.util.MedicalArziConstants;
import com.example.medicalarzi.util.MedicalArziUtils;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Container;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.VaadinUIScope;
import com.vaadin.spring.navigator.annotation.SpringView;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author Mustafa Kanchwala
 *
 */
@Theme("medicalarzi")
@VaadinUIScope
@SpringView(name = MedicalArziLandingView.NAME)
public class MedicalArziLandingView extends CustomComponent implements View,
		ClickListener, SelectedTabChangeListener, SelectionListener {

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

	private TabSheet tabSheet;

	private VerticalLayout viewLayout;

	// Patient Personal Info
	private Panel ptntInfoSection;

	private CustomFormComponent ptntInfoForm;

	// Patient Medical Info
	private Panel ptntMedicalInfoSection;

	private CustomFormComponent ptntMedicalInfoForm;

	// Inbox
	private VerticalLayout inboxViewLayout;

	private Grid arziGrid;

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

	// Medical information fields
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

	// private TextArea description;

	// Binding Fields
	private BeanFieldGroup<Patient> ptntFieldsBinder;

	private BeanFieldGroup<Arzi> arziFieldsBinder;

	private Patient patient;

	// Service stubs
	@Autowired
	@Qualifier("service.PatientService")
	private PatientService patientService;

	@Autowired
	@Qualifier("service.LookupService")
	private LookupService lookupService;

	// Getters
	public TextField getItsNumber() {
		return itsNumber;
	}

	public TextField getFirstName() {
		return firstName;
	}

	public TextField getMiddleName() {
		return middleName;
	}

	public TextField getLastName() {
		return lastName;
	}

	public ArziDateField getDob() {
		return dob;
	}

	public TextField getGender() {
		return gender;
	}

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
		// Get the patient's full name from the session
		patient = (Patient) getSession().getAttribute(
				MedicalArziConstants.SESS_ATTR_PTNT_INFO);

		// Initialize the components in the view
		init();

		getItsNumber().setValue(String.valueOf(patient.getItsNumber()));
		getGender().setValue(patient.getGender().getDescription());

		setFieldsReadOnly(true);

		String patientName = MedicalArziUtils.constructPtntFullName(patient);

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

		bindFieldsToModel();
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
		Arzi arzi = new Arzi();
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
	 * This method will build a new tabsheet on this landing page.
	 * 
	 * @return
	 */
	private void buildTabSheet() {
		tabSheet = new TabSheet();
		tabSheet.addStyleName("borderless");
		tabSheet.addStyleName("mapTabsheet");
		tabSheet.addSelectedTabChangeListener(this);
		tabSheet.setSizeFull();

		// viewLayout
		buildViewlayout();
		tabSheet.addTab(viewLayout, MedicalArziConstants.NEW_ARZI_TAB_CAPTION,
				new ThemeResource("icons/newArzi.png"));
		// gridLayout
		buildInboxViewLayout();
		tabSheet.addTab(inboxViewLayout,
				MedicalArziConstants.INBOX_TAB_CAPTION, new ThemeResource(
						"icons/inbox.png"));
	}

	/**
	 * This method will build the grid layout which will list all the arzis
	 * which are saved/submitted.
	 * 
	 */
	private void buildInboxViewLayout() {
		// inboxViewLayout
		inboxViewLayout = new VerticalLayout();
		inboxViewLayout.setId(MedicalArziConstants.INBOX_TAB_COMPONENT_ID);
		inboxViewLayout.setMargin(true);
		inboxViewLayout.setSpacing(true);	

		Label inboxDescription = new Label();
		inboxDescription.setValue("My Arzis:");
		inboxDescription.setStyleName("v-captiontext");
		inboxViewLayout.addComponent(inboxDescription);
		inboxViewLayout.setExpandRatio(inboxDescription, 0.1f);

		buildArziGrid();
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void buildArziGrid() {
		arziGrid = new Grid();
		arziGrid.setSizeFull();
		arziGrid.setImmediate(false);
		arziGrid.setSelectionMode(SelectionMode.SINGLE);

		// Sets the grid editor to be enabled
		arziGrid.setEditorEnabled(true);
		// Setting the field group
		arziGrid.setEditorFieldGroup(getFieldGroup());

		// Initialize the container
		BeanItemContainer<Arzi> arziContainer = (BeanItemContainer<Arzi>) MedicalArziUtils
				.getContainer(Arzi.class);

		// Retrieve all the arzis for the patient based on the ITS number
		List<Arzi> arziList = patientService.retrieveAllArzisForPatient(patient
				.getItsNumber());

		/**
		 * Update the Grid with fresh data. Two step process of replacing bean
		 * items. (1) First remove all BeanItem objects with
		 * Container::removeAllItems method. (2) Then add replacement BeanItem
		 * objects with the BeanItemContainer::addAll method.
		 */
		arziContainer.removeAllItems();
		arziContainer.addAll(arziList);

		// Add nested properties to the header
		arziContainer.addNestedContainerBean("currentStatus");
		arziContainer.addNestedContainerBean("arziType");
		arziContainer.addNestedContainerBean("procedure");
		arziContainer.addNestedContainerBean("condition");
		arziContainer.addNestedContainerBean("conditionStartDate");
		arziContainer.addNestedContainerBean("bodyPart");

		// Add the container to the datasource
		arziGrid.setContainerDataSource(arziContainer);

		arziGrid.setColumns("itsNumber", "currentStatus.statusDesc",
				"arziType.arziTypeName", "condition.conditionName",
				"conditionStartDate.gregorianCalDate",
				"procedure.procedureName", "bodyPart.bodyPartName");

		// Sets the converter on the ITS number to remove the grouping used.
		arziGrid.getColumn("itsNumber").setConverter(
				MedicalArziUtils.itsNumberConverter());
		/*
		 * TextField itsNumEditor = new TextField();
		 * itsNumEditor.setConverter(MedicalArziUtils.itsNumberConverter());
		 * arziGrid.getColumn("itsNumber").setEditorField(itsNumEditor);
		 */
		arziGrid.getColumn("itsNumber").setEditable(false);

		// Set the header captions for each column
		arziGrid.getColumn("currentStatus.statusDesc").setHeaderCaption(
				"Arzi Status");
		arziGrid.getColumn("currentStatus.statusDesc").setEditable(false);

		arziGrid.getColumn("arziType.arziTypeName").setHeaderCaption("Type");
		arziGrid.getColumn("arziType.arziTypeName").setEditorField(
				getComboBox("", "Please select a type...", "Type is required!",
						lookupService.getListOfAllArziTypes(), "arziTypeName",
						MedicalArziUtils.getContainer(ArziType.class)));

		arziGrid.getColumn("condition.conditionName").setHeaderCaption(
				"Condition");
		arziGrid.getColumn("condition.conditionName").setEditorField(
				getComboBox("", "Please select a condition...",
						"Condition is required!",
						lookupService.getListOfAllMedicalConditions(),
						"conditionName",
						MedicalArziUtils.getContainer(Condition.class)));

		arziGrid.getColumn("procedure.procedureName").setHeaderCaption(
				"Procedure");
		arziGrid.getColumn("procedure.procedureName").setEditorField(
				getComboBox("", "Please select a procedure...",
						"Procedure is required!",
						lookupService.getListOfAllMedicalProcedures(),
						"procedureName",
						MedicalArziUtils.getContainer(Procedure.class)));

		arziGrid.getColumn("bodyPart.bodyPartName").setHeaderCaption(
				"Body Part");
		arziGrid.getColumn("bodyPart.bodyPartName").setEditorField(
				getComboBox("", "Please select a body part...",
						"Body Part is required!",
						lookupService.getListOfAllBodyParts(), "bodyPartName",
						MedicalArziUtils.getContainer(BodyPart.class)));

		arziGrid.getColumn("conditionStartDate.gregorianCalDate")
				.setHeaderCaption("Condition Start Date");

		// Set the renderers
		arziGrid.getColumn("conditionStartDate.gregorianCalDate").setRenderer(
				new DateRenderer("%1$tB %1$te, %1$tY", Locale.ENGLISH));

		// This call prepends the header row to the existing grid
		HeaderRow ptntMedicalInfoHeader = arziGrid.prependHeaderRow();
		// Get hold of the columnID, mind you in my case this is a nestedID
		HeaderCell arziTypeName = ptntMedicalInfoHeader
				.getCell("arziType.arziTypeName");
		HeaderCell conditionName = ptntMedicalInfoHeader
				.getCell("condition.conditionName");
		HeaderCell conditionStartDt = ptntMedicalInfoHeader
				.getCell("conditionStartDate.gregorianCalDate");
		HeaderCell procedureName = ptntMedicalInfoHeader
				.getCell("procedure.procedureName");
		HeaderCell bodyPartName = ptntMedicalInfoHeader
				.getCell("bodyPart.bodyPartName");

		// Now join all of these cells to form a logical block
		HeaderCell ptntMedicalInfoHeaderCell = ptntMedicalInfoHeader.join(
				arziTypeName, conditionName, conditionStartDt, procedureName,
				bodyPartName);

		ptntMedicalInfoHeaderCell.setText("Medical Information");

		arziGrid.addSelectionListener(this);

		// Add the Vertical Split Panel to the Vertical Layout
		inboxViewLayout.addComponent(arziGrid);
	}

	/**
	 * 
	 * @return com.vaadin.data.fieldgroup.FieldGroup
	 */
	private FieldGroup getFieldGroup() {
		BeanFieldGroup<Arzi> arziFieldGroup = new BeanFieldGroup<Arzi>(
				Arzi.class);

		arziFieldGroup.addCommitHandler(new MedicalArziCommitHandler());

		return arziFieldGroup;
	}

	/**
	 * This method returns the combox with the possible values extracted from
	 * the passed container and displaying the passed property from an item
	 * 
	 * @param caption
	 * @param inputPrompt
	 * @param requiredErrorMsg
	 * @param items
	 * @param propertyId
	 * @param container
	 * @return
	 */
	private Field<?> getComboBox(String caption, String inputPrompt,
			String requiredErrorMsg, Collection<?> items, Object propertyId,
			Container container) {
		ComboBox comboBox = new ComboBox(caption);
		// comboBox.setNullSelectionAllowed(true);
		comboBox.setInputPrompt(inputPrompt);
		comboBox.setContainerDataSource(container);
		comboBox.addItems(items);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId(propertyId);
		comboBox.setRequired(true);
		comboBox.setRequiredError(requiredErrorMsg);
		return comboBox;
	}

	/**
	 * This method will build the CustomForm and add all the fields for a new
	 * arzi into the CustomFormComponent.
	 * 
	 */
	private void buildViewlayout() {
		// viewLayout
		viewLayout = new VerticalLayout();
		viewLayout.setId(MedicalArziConstants.NEW_ARI_TAB_COMPONENT_ID);
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

		// This is needed so that if the panel has a fixed or percentual size
		// and its content becomes too big to fit in the content area, the panel
		// will scroll in the direction so that the content is visible or else
		// the content will be cut to the width/height of the panel and cannot
		// be scrolled and viewed entirely.
		ptntMedicalInfoSection.getContent().setHeightUndefined();

		/**
		 * Add the fields to the left FormLayout.
		 * 
		 */
		FormLayout leftFormLayout = (FormLayout) MedicalArziUtils.findById(
				ptntMedicalInfoForm,
				MedicalArziConstants.CUSTOM_FORM_LEFTFORM_LAYOUT_ID);
		// arziType
		arziType = new ComboBox("Arzi Type:");
		arziType.setContainerDataSource(MedicalArziUtils
				.getContainer(ArziType.class));
		arziType.addItems(lookupService.getListOfAllArziTypes());
		arziType.setInputPrompt("Please select the arzi type.");
		arziType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		arziType.setItemCaptionPropertyId("arziTypeName");
		arziType.setRequired(true);
		leftFormLayout.addComponent(arziType);

		// bodyPart
		bodyPart = new ComboBox("Body Part:");
		bodyPart.setContainerDataSource(MedicalArziUtils
				.getContainer(BodyPart.class));
		bodyPart.addItems(lookupService.getListOfAllBodyParts());
		bodyPart.setInputPrompt("Please select the affected body part.");
		bodyPart.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		bodyPart.setItemCaptionPropertyId("bodyPartName");
		bodyPart.setRequired(true);
		leftFormLayout.addComponent(bodyPart);

		conditionStartDate = new ArziDateField("Condition Start Date:");
		conditionStartDate.setImmediate(true);
		conditionStartDate
				.setDescription("Please enter the date in the dd/MM/yyy format.");
		conditionStartDate.setRequired(true);
		leftFormLayout.addComponent(conditionStartDate);

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
		condition.addItems(lookupService.getListOfAllMedicalConditions());
		condition.setInputPrompt("Please select your medical condition.");
		condition.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		condition.setItemCaptionPropertyId("conditionName");
		condition.setRequired(true);
		rightFormLayout.addComponent(condition);

		// procedure
		procedure = new ComboBox("Medical Procedure:");
		procedure.setContainerDataSource(MedicalArziUtils
				.getContainer(Procedure.class));
		procedure.addItems(lookupService.getListOfAllMedicalProcedures());
		procedure.setInputPrompt("Please select the medical procedure.");
		procedure.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		procedure.setItemCaptionPropertyId("procedureName");
		procedure.setRequired(true);
		rightFormLayout.addComponent(procedure);
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
		saveBtn = new Button(new ThemeResource("img/save.png"));
		saveBtn.addClickListener(this);
		saveBtn.setStyleName(Reindeer.BUTTON_LINK);
		buttonsLayout.addComponent(saveBtn);
		buttonsLayout.setExpandRatio(saveBtn, 0.2f);
		buttonsLayout.setComponentAlignment(saveBtn, Alignment.MIDDLE_CENTER);

		// submitBtn
		submitBtn = new Button(new ThemeResource("img/submit.png"));
		submitBtn.addClickListener(this);
		submitBtn.setStyleName(Reindeer.BUTTON_LINK);
		buttonsLayout.addComponent(submitBtn);
		buttonsLayout.setExpandRatio(submitBtn, 1.0f);
		buttonsLayout.setComponentAlignment(submitBtn, Alignment.MIDDLE_LEFT);

		buttonsLayout.setSpacing(true);
		buttonsLayout.setStyleName("ptntRegistrationBtn");

		// adds buttonLayout to the viewLayout
		viewLayout.addComponent(buttonsLayout);
		viewLayout
				.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(cancelBtn)) {
			logger.debug("Cancel Button Clicked.");

		} else if (event.getButton().equals(saveBtn)
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
				patientService.updatePatientInfo(ptntInfo);

				// FieldGroup buffered mode is on, so commit() is required.
				// Throws CommitException
				arziFieldsBinder.commit();

				Arzi arzi = arziFieldsBinder.getItemDataSource().getBean();

				GregHijDate ghReqSubmitDt = lookupService
						.getRequestedGregorianHijriCalendar(new Date());

				arzi.setRequestSubmitDate(ghReqSubmitDt);
				arzi.setCurrentStatusDate(ghReqSubmitDt);

				// Get the entire data for the GregHijDate based on the
				// gregorian date.
				arzi.setConditionStartDate(lookupService
						.getRequestedGregorianHijriCalendar(arzi
								.getConditionStartDate().getGregorianCalDate()));

				Status arziStatus = new Status();
				// Set the status to "Draft' when Save Btn is clicked and
				// 'Submitted' when Submit Btn is clicked.
				if (event.getButton().equals(saveBtn)) {
					arziStatus
							.setStatusId(MedicalArziConstants.ARZI_DRAFT_STATUS);
				} else {
					arziStatus
							.setStatusId(MedicalArziConstants.ARZI_SUBMITTED_STATUS);
				}

				arzi.setCurrentStatus(arziStatus);

				logger.debug("Inserting a new arzi for patient with ITS number-> \""
						+ ptntInfo.getItsNumber() + "\"");

				// Insert the new arzi for the patient
				patientService.createNewArzi(arzi);

				// Create a user friendly notification on success.
				String successMsg = "The arzi for \""
						+ MedicalArziUtils.constructPtntFullName(ptntInfo)
						+ "\" is successfully created";

				// Once the new arzi is submitted which the selection to the
				// Inbox tab with the user friendly success message.
				tabSheet.setSelectedTab(inboxViewLayout);

				MedicalArziUtils.createAndShowNotification(null, successMsg,
						Type.HUMANIZED_MESSAGE, Position.TOP_LEFT,
						"userFriendlyMsg", -1);

			} catch (CommitException ce) {
				logger.error(ce);

				String errorDescription = "Fields marked with asterisk (*) are required. "
						+ "Please enter the required values and fix the errors before proceeding further.";

				// Create an error notification if the required fields are not
				// entered correctly.
				MedicalArziUtils.createAndShowNotification(null,
						errorDescription, Type.ERROR_MESSAGE,
						Position.TOP_LEFT, "errorMsg",
						MedicalArziConstants.USER_FRIENDLY_MSG_DELAY_MSEC);
			}
		}
	}

	/**
	 * This method just sets the fields which cannot be allowed to be edited by
	 * the users as read only. This method can add or remove the readOnly fields
	 * based on the requirements decision.
	 * 
	 * @param boolean
	 */
	private void setFieldsReadOnly(boolean flag) {
		getItsNumber().setReadOnly(true);

		getFirstName().setReadOnly(true);

		getMiddleName().setReadOnly(true);

		getLastName().setReadOnly(true);

		getGender().setReadOnly(true);
	}

	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		TabSheet tabSheetComponent = event.getTabSheet();

		// Get the selected tab (here we know it's a layout)
		Layout selectedTab = (Layout) tabSheetComponent.getSelectedTab();

		// Get the componentId (which is the layout) which is set when the view
		// is built
		String componentId = selectedTab.getId();

		if (StringUtils.isNotEmpty(componentId)) {
			// If the selected tab is Inbox, refetch all the arzis for the user
			// from the dbase.
			if (inboxViewLayout != null
					&& componentId.equals(inboxViewLayout.getId())) {

				logger.debug("The user has selected the Inbox tab. Reload the tab with the latest content.");

				// Fill the tab content when the tab is selected. Remove all the
				// components from the selected tab and then replace the
				// selected tab with the new tab with the latest contents
				selectedTab.removeAllComponents();
				buildInboxViewLayout();
				tabSheet.replaceComponent(selectedTab, inboxViewLayout);

			} else if (viewLayout != null
					&& componentId.equals(viewLayout.getId())) {
				logger.debug("The user has selected the New Arzi tab.");
			}
		}
	}

	@Override
	public void select(SelectionEvent event) {

		Arzi selectedArzi = (Arzi) arziGrid.getSelectedRow();

		if (selectedArzi != null) {
			if (selectedArzi.getCurrentStatus().getStatusId().intValue() == MedicalArziConstants.ARZI_SUBMITTED_STATUS
					.intValue()) {
				arziGrid.setEditorEnabled(false);
			} else {
				arziGrid.setEditorEnabled(true);
			}
		}

		logger.debug("The patient with ITS number:-> " + patient.getItsNumber()
				+ " selected arzi : " + selectedArzi + " for editing");
	}

}
