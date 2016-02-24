/**
 * 
 */
package com.example.medicalarzi.component;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.medicalarzi.converter.StringToArziTypeConverter;
import com.example.medicalarzi.converter.StringToBodyPartConverter;
import com.example.medicalarzi.converter.StringToConditionConverter;
import com.example.medicalarzi.converter.StringToProcedureConverter;
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
import com.example.medicalarzi.service.ServiceLocator;
import com.example.medicalarzi.util.MedicalArziConstants;
import com.example.medicalarzi.util.MedicalArziUtils;
import com.vaadin.data.Container;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
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
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author mkanchwa
 *
 */
public class InboxComponent extends CustomComponent implements
		SelectionListener, ClickListener, ValueChangeListener {

	private static final long serialVersionUID = 2521689363206700694L;
	
	public static Logger logger = LogManager.getLogger(InboxComponent.class);
	
	private Patient patient;
	
	// Inbox
	private VerticalLayout inboxViewLayout;
	
	private VerticalSplitPanel splitPanel;

	private Grid arziGrid;
	
	private VerticalLayout editArziFormLayout;
	
	private CustomFormComponent arziForm;
	
	// Buttons layout
	private HorizontalLayout buttonsLayout;

	private Button submitBtn;

	private Button saveBtn;
	
	private Button deleteBtn;

	@PropertyId("arziType")
	private ComboBox arziType;

	@PropertyId("bodyPart")
	private ComboBox bodyPart;

	@PropertyId("procedure")
	private ComboBox procedure;

	@PropertyId("condition")
	private ComboBox condition;
	
	@PropertyId("otherCondition")
	private TextField otherCondition;
	
	@PropertyId("conditionStartDate.gregorianCalDate")
	private ArziDateField conditionStartDate;
	
	// Binding Fields
	private BeanFieldGroup<Arzi> arziFieldsBinder;
	
	private BeanItemContainer<Arzi> arziContainer;
	
	/**
	 * 
	 */
	public InboxComponent() {
		// Get the patient's full name from the session
		patient = (Patient) MedicalArziUtils
				.getSessionAttribute(
				MedicalArziConstants.SESS_ATTR_PTNT_INFO);
		
		// Build the inbox view. This is the mainLayout for this component
		buildInboxViewLayout();
		
		// Bind the member fields to the model
		bindFieldsToModel();

		setCompositionRoot(inboxViewLayout);
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
	
	/**
	 * This method is responsible for binding the member fields to the
	 * ArziSearchCriteria model.
	 */
	private void bindFieldsToModel() {
		logger.debug("Binding the patient and the arzi fields to their respective models.");

		/* Bind the arzi fields. */
		Arzi arziBinderObj = new Arzi();
		/* Bind the search criteria fields */
		arziFieldsBinder = new BeanFieldGroup<Arzi>(
				Arzi.class);
		// Bind all the member fields whose type extends Field to the property
		// id of the item.
		// The mapping is done based on the @PropertyId annotation
		arziFieldsBinder.bindMemberFields(this);
		// Add bean item that is bound.
		arziFieldsBinder.setItemDataSource(arziBinderObj);
		// Set the buffered mode on, if using the bean validation JSR-3.0 with
		// vaadin. Does not work when fields are immediately updated with the
		// bean.
		arziFieldsBinder.setBuffered(true);
	}	
	
	
	/**
	 * This method will build the grid layout which will list all the arzis
	 * which are saved/submitted.
	 * 
	 */
	private void buildInboxViewLayout() {
		// top-level component properties
		setSizeFull();
		
		// inboxViewLayout
		inboxViewLayout = new VerticalLayout();
		inboxViewLayout.setId(MedicalArziConstants.INBOX_COMPONENT_VIEW_LAYOUT_ID);
		inboxViewLayout.setSizeFull();
		inboxViewLayout.setMargin(true);
		inboxViewLayout.setSpacing(true);	

		Label inboxDescription = new Label();
		inboxDescription.setValue("My Arzis: (Select to edit)");
		inboxDescription.setStyleName("v-captiontext");
		inboxViewLayout.addComponent(inboxDescription);
		inboxViewLayout.setExpandRatio(inboxDescription, 0.1f);

		buildSplitPanel();
		inboxViewLayout.addComponent(splitPanel);
		inboxViewLayout.setExpandRatio(splitPanel, 4.0f);
	}	
	
	/**
	 * This method is responsible for building the veritical split panel and
	 * adding the grid in the top half and the vertical layout in the bottom
	 * half (which contains the form elements and the buttons)
	 */
	private void buildSplitPanel() {
		splitPanel = new VerticalSplitPanel();
		splitPanel.setLocked(true);
		splitPanel.setSizeFull();
		splitPanel.setSplitPosition(50, Unit.PERCENTAGE);
		// Use a custom style
		splitPanel.addStyleName("invisiblesplitter");
		
		buildArziGrid();
		splitPanel.addComponent(arziGrid);
		
		buildEditArziFormLayout();
		splitPanel.addComponent(editArziFormLayout);
	}
	
	/**
	 * This method is responsible for building the vertical layout which
	 * contains the form elements for editing and buttons layout which contains
	 * the Save, Submit and Delete arzis.
	 */
	private void buildEditArziFormLayout() {
		// editArziFormLayout
		editArziFormLayout = new VerticalLayout();
		editArziFormLayout.setSizeFull();
		editArziFormLayout.setEnabled(false);
		editArziFormLayout.setMargin(true);
		editArziFormLayout.setSpacing(true);	

		buildArziFormSection();
		editArziFormLayout.addComponent(arziForm);
		
		buildButtonsLayout();
		editArziFormLayout.addComponent(buttonsLayout);
		editArziFormLayout.setComponentAlignment(buttonsLayout,
				Alignment.MIDDLE_CENTER);
	}
	
	/**
	 * This method is responsible for building the form elements section which
	 * contains the arzi elements that could be edited.
	 * 
	 */
	private void buildArziFormSection() {
		arziForm = new CustomFormComponent();
		arziForm.setSizeFull();
		arziForm.setStyleName("customForm");
		
		
		/**
		 * Add the fields to the left FormLayout.
		 * 
		 */
		FormLayout leftFormLayout = (FormLayout) MedicalArziUtils.findById(
				arziForm,
				MedicalArziConstants.CUSTOM_FORM_LEFTFORM_LAYOUT_ID);
		
		arziType = (ComboBox) getField(ArziType.class);
		leftFormLayout.addComponent(arziType);
		
		condition = (ComboBox) getField(Condition.class);
		condition.addValueChangeListener(this);
		leftFormLayout.addComponent(condition);
		
		otherCondition = (TextField) getField(String.class);
		otherCondition.setWidth("300px");
		leftFormLayout.addComponent(otherCondition);
		
		/**
		 * Add the fields to the right FormLayout.
		 * 
		 */
		FormLayout rightFormLayout = (FormLayout) MedicalArziUtils.findById(
				arziForm,
				MedicalArziConstants.CUSTOM_FORM_RIGHTFORM_LAYOUT_ID);
		
		bodyPart = (ComboBox) getField(BodyPart.class);
		rightFormLayout.addComponent(bodyPart);
		
		conditionStartDate = new ArziDateField("Condition Start Date:");
		conditionStartDate.setImmediate(true);
		conditionStartDate
				.setDescription("Please enter the date in the dd/MM/yyy format.");
		conditionStartDate.setRequired(true);
		rightFormLayout.addComponent(conditionStartDate);
		
		procedure = (ComboBox) getField(Procedure.class);
		rightFormLayout.addComponent(procedure);
	}
	
	/**
	 * This method is responsible for building the horizontal buttons layout
	 * which conatins the Save, Submit and Delete arzi buttons.
	 * 
	 */
	private void buildButtonsLayout() {

		buttonsLayout = new HorizontalLayout();
		
		// cancelBtn
		deleteBtn = new Button(new ThemeResource("img/delete-arzi.png"));
		deleteBtn.addClickListener(this);
		deleteBtn.setStyleName(Reindeer.BUTTON_LINK);
		buttonsLayout.addComponent(deleteBtn);
		buttonsLayout.setExpandRatio(deleteBtn, 1.0f);
		buttonsLayout.setComponentAlignment(deleteBtn, Alignment.MIDDLE_RIGHT);		

		// saveBtn
		saveBtn = new Button(new ThemeResource("img/save-arzi.png"));
		saveBtn.addClickListener(this);
		saveBtn.setStyleName(Reindeer.BUTTON_LINK);
		buttonsLayout.addComponent(saveBtn);
		buttonsLayout.setExpandRatio(saveBtn, 0.2f);
		buttonsLayout.setComponentAlignment(saveBtn, Alignment.MIDDLE_CENTER);

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
	
	/**
	 * This method builds the arzi grid on the Inbox tab with the list of arzis
	 * for the logged in user. The user is also able to edit his/her arzi if it
	 * is not yet submitted and sitting in the Draft mode. Once an arzi is
	 * submitted, the user will not be able to edit that arzi.
	 */
	@SuppressWarnings("unchecked")
	private void buildArziGrid() {
		arziGrid = new Grid();
		arziGrid.setSizeFull();

		// Set the selection mode.
		arziGrid.setSelectionMode(SelectionMode.SINGLE);
		
		// Initialize the container
		arziContainer = (BeanItemContainer<Arzi>) MedicalArziUtils
				.getContainer(Arzi.class);
		
		// Set the container as the grid datasource
		arziGrid.setContainerDataSource(arziContainer);

		
		// Retrieve all the arzis for the patient based on the ITS number
		// Get the patient's full name from the session
		patient = (Patient) MedicalArziUtils.getSessionAttribute(
				MedicalArziConstants.SESS_ATTR_PTNT_INFO);
		
		refreshGridWithFreshData();

		// Add nested properties to the header
		arziContainer.addNestedContainerBean("currentStatus");
		arziContainer.addNestedContainerBean("arziType");
		arziContainer.addNestedContainerBean("condition");
		arziContainer.addNestedContainerBean("procedure");
		arziContainer.addNestedContainerBean("bodyPart");
		arziContainer.addNestedContainerBean("conditionStartDate");

		// Customize the columns
		customizeArziGridColumns();
		
		//Add the event listener on the grid on selecting a row.
		arziGrid.addSelectionListener(this);
		//arziGrid.addItemClickListener(this);
	}
	
	
	/**
	 * This method is responsible for customizing all the grid columns like
	 * setting the caption and grouping the similar columns together etc.
	 */
	private void customizeArziGridColumns() {
		
		arziGrid.setColumns("arziId", "currentStatus.statusDesc",
				"arziType.arziTypeName", "bodyPart.bodyPartName",
				"condition.conditionName", "otherCondition",
				"conditionStartDate.gregorianCalDate",
				"procedure.procedureName");
		
		// Column reordering allowed
		arziGrid.setColumnReorderingAllowed(true);
		
		// Sets the converter on the ITS number to remove the grouping used.
		arziGrid.getColumn("arziId").setHeaderCaption("ID").setMaximumWidth(70)
				.setConverter(MedicalArziUtils.itsNumberConverter());

		arziGrid.getColumn("arziType.arziTypeName").setMaximumWidth(160)
				.setHeaderCaption("Type");

		arziGrid.getColumn("currentStatus.statusDesc")
				.setHeaderCaption("Status").setMaximumWidth(110);

		arziGrid.getColumn("bodyPart.bodyPartName").setHeaderCaption(
				"Body Part");

		arziGrid.getColumn("conditionStartDate.gregorianCalDate")
				.setHeaderCaption("Condition Start Date")
				.setMaximumWidth(150)
				.setRenderer(
						new DateRenderer("%1$tb %1$td, %1$tY", Locale.ENGLISH));

		groupArziGridColumns();
	}
	

	/**
	 * This method is responsible for customizing all the grid columns for the
	 * editable grid like setting the caption, setting the editor fields and
	 * grouping the similar columns together etc.
	 * 
	 */
	public void customizeEditableArziGridColumns() {
		
		// Sets the grid editor to be enabled
		arziGrid.setEditorEnabled(true);
		
		arziGrid.setEditorSaveCaption("Save Draft");
		
		// Setting the field group
		arziGrid.setEditorFieldGroup(getFieldGroup());
		
		arziGrid.setColumns("arziId", "currentStatus.statusDesc",
				"arziType", "condition", "otherCondition",
				"conditionStartDate.gregorianCalDate", "procedure", "bodyPart");
		
		// Column reordering allowed
		arziGrid.setColumnReorderingAllowed(true);
		
		// Sets the converter on the ITS number to remove the grouping used.
		arziGrid.getColumn("arziId").setHeaderCaption("ID").setMaximumWidth(70)
				.setConverter(MedicalArziUtils.itsNumberConverter())
				.setEditable(false);

		arziGrid.getColumn("arziType").setMaximumWidth(160)
				.setHeaderCaption("Type");

		arziGrid.getColumn("currentStatus.statusDesc")
				.setHeaderCaption("Status").setMaximumWidth(110)
				.setEditable(false);
		
		arziGrid.getColumn("conditionStartDate.gregorianCalDate")
				.setHeaderCaption("Condition Start Date")
				.setEditorField(new ArziDateField())
				.setRenderer(
						new DateRenderer("%1$tB %1$td, %1$tY", Locale.ENGLISH));
		
		//Setting the other condition values in the textfield editor on the grid for editing.
		arziGrid.getColumn("otherCondition").setEditorField(
				MedicalArziUtils.getTextFieldEditor(null, true, false, null));
		
		Container condContainer = MedicalArziUtils
				.getContainer(Condition.class);
		StringToConditionConverter condConv = new StringToConditionConverter(
				condContainer);
		arziGrid.getColumn("condition")
				.setConverter(condConv)
				.setEditorField(
						MedicalArziUtils.getComboBox("",
								"Please select a condition...",
								getLookupService().getListOfAllMedicalConditions(),
								"conditionName", condContainer, null, true, "Condition is required!"));
		
		//Setting the arziType values in the combobox editor on the grid for editing.
		Container arziTypeContainer = MedicalArziUtils
				.getContainer(ArziType.class);
		StringToArziTypeConverter arziTypeConv = new StringToArziTypeConverter(
				arziTypeContainer);
		arziGrid.getColumn("arziType")
				.setConverter(arziTypeConv)
				.setEditorField(
						MedicalArziUtils.getComboBox("",
								"Please select a type...", 
								getLookupService().getListOfAllArziTypes(),
								"arziTypeName", arziTypeContainer, null, true, "Type is required!"));
		
		Container procContainer = MedicalArziUtils
				.getContainer(Procedure.class);
		StringToProcedureConverter procConv = new StringToProcedureConverter(
				procContainer);
		arziGrid.getColumn("procedure")
				.setConverter(procConv)
				.setEditorField(
						MedicalArziUtils.getComboBox("",
								"Please select a procedure...",
								getLookupService().getListOfAllMedicalProcedures(),
								"procedureName", procContainer, null, true, "Procedure is required!"));

		Container bdyPrtContainer = MedicalArziUtils
				.getContainer(BodyPart.class);
		StringToBodyPartConverter bdyPrtConv = new StringToBodyPartConverter(
				bdyPrtContainer);
		arziGrid.getColumn("bodyPart")
				.setConverter(bdyPrtConv)
				.setEditorField(
						MedicalArziUtils.getComboBox("", "Please select a body part...",
								getLookupService().getListOfAllBodyParts(),
								"bodyPartName", bdyPrtContainer, null, true, "Body Part is required!"));
		
		groupArziGridColumns();
		
		// Adding the commit handler to the field group
		MedicalArziCommitHandler maCommitHdlr = new MedicalArziCommitHandler();
		
		arziGrid.getEditorFieldGroup().addCommitHandler(maCommitHdlr);
	}

	/**
	 * This method is responsible for the logical grouping of the arzi grid
	 * columns. Basically, the Patient information section and Medical
	 * Information section on the Inbox grid
	 * 
	 */
	private void groupArziGridColumns() {
		// This call prepends the header row to the existing grid
		HeaderRow ptntMedicalInfoHeader = arziGrid.prependHeaderRow();
		
		// 
		HeaderCell arziId = ptntMedicalInfoHeader.getCell("arziId");
		HeaderCell arziType = ptntMedicalInfoHeader.getCell("arziType.arziTypeName");
		HeaderCell currentStatus = ptntMedicalInfoHeader.getCell("currentStatus.statusDesc");
		
		// Now join all of these cells to form a logical block
		HeaderCell ptntArziInfoHeaderCell = ptntMedicalInfoHeader.join(arziId,
				currentStatus, arziType);
		
		ptntArziInfoHeaderCell.setHtml("<b>Arzi</b>");
		
		// Get hold of the columnID, mind you in my case this is a nestedID
		HeaderCell condition = ptntMedicalInfoHeader.getCell("condition.conditionName");
		HeaderCell otherCondition = ptntMedicalInfoHeader.getCell("otherCondition");
		HeaderCell conditionStartDt = ptntMedicalInfoHeader
				.getCell("conditionStartDate.gregorianCalDate");
		HeaderCell procedure = ptntMedicalInfoHeader.getCell("procedure.procedureName");
		HeaderCell bodyPart = ptntMedicalInfoHeader.getCell("bodyPart.bodyPartName");

		// Now join all of these cells to form a logical block
		HeaderCell ptntMedicalInfoHeaderCell = ptntMedicalInfoHeader.join(
				condition, otherCondition, conditionStartDt, procedure,
				bodyPart);

		ptntMedicalInfoHeaderCell.setHtml("<b>Medical Information</b>");
	}
	
	/**
	 * This method creates the field group for the arzi grid items and binds it to the
	 * the datasource model.
	 * 
	 * @return com.vaadin.data.fieldgroup.FieldGroup
	 */
	private BeanFieldGroup<Arzi> getFieldGroup() {
		Arzi arzi = new Arzi();

		BeanFieldGroup<Arzi> arziGridFieldsBinder = new BeanFieldGroup<Arzi>(Arzi.class);
		
		arziGridFieldsBinder.setItemDataSource(arzi);
		
		return arziGridFieldsBinder;
	}	


	/**
	 * This is the itemClick listener which listens to the click event on any of
	 * the grid items (row). Override this when the SelectionListener is added
	 * to the grid
	 * 
	 * @param event
	 */	
	@Override
	public void select(SelectionEvent event) {

		Arzi selectedArzi = (Arzi) arziGrid.getSelectedRow();

		if (selectedArzi != null) {
			
			arziType.select(selectedArzi.getArziType());

			condition.select(selectedArzi.getCondition());
			
			conditionStartDate.setValue(selectedArzi
					.getConditionStartDate().getGregorianCalDate());

			procedure.select(selectedArzi.getProcedure());

			bodyPart.select(selectedArzi.getBodyPart());

			if (StringUtils.isNotBlank(selectedArzi.getOtherCondition())) {
				otherCondition.setValue(selectedArzi.getOtherCondition());
			}			
			// The form should not be editable if the arzi is in the 'Submitted'
			// status
			if (selectedArzi.getCurrentStatus().getStatusId().intValue() == MedicalArziConstants.ARZI_DRAFT_STATUS
					.intValue()) {
				// Set the form elements with the data of the selected arzi.
				editArziFormLayout.setEnabled(true);

			} else {
				editArziFormLayout.setEnabled(false);
			}

		}

		logger.debug("The patient with ITS number:-> " + patient.getItsNumber()
				+ " selected arzi : " + selectedArzi + " for editing");
	}

	@Override
	public void buttonClick(ClickEvent event) {
		
		Arzi selectedArzi = (Arzi) arziGrid.getSelectedRow();
		
		String successMsg = "";
		
		if (event.getButton().equals(saveBtn)
				|| event.getButton().equals(submitBtn)) {
			try {
				// FieldGroup buffered mode is on, so commit() is required.
				// Throws CommitException
				arziFieldsBinder.commit();

				Arzi arzi = arziFieldsBinder.getItemDataSource().getBean();
				
				// Sets the arziId because the arziId is need for the update.
				arzi.setArziId(selectedArzi.getArziId());

				GregHijDate ghReqSubmitDt = getLookupService()
						.getRequestedGregorianHijriCalendar(new Date());

				arzi.setRequestSubmitDate(ghReqSubmitDt);
				arzi.setCurrentStatusDate(ghReqSubmitDt);

				// Get the entire data for the GregHijDate based on the
				// gregorian date.
				arzi.setConditionStartDate(getLookupService()
						.getRequestedGregorianHijriCalendar(
								arzi.getConditionStartDate()
										.getGregorianCalDate()));

				Status arziStatus = new Status();
				
				// Set the status to "Draft' when Save Btn is clicked and
				// 'Submitted' when Submit Btn is clicked.
				if (event.getButton().equals(saveBtn)) {
					arziStatus
							.setStatusId(MedicalArziConstants.ARZI_DRAFT_STATUS);
					
					successMsg = "The arzi \"" + arzi.getArziId() + "\" for \""
							+ MedicalArziUtils.constructPtntFullName(patient)
							+ "\" is successfully saved.";
				} else {
					arziStatus
							.setStatusId(MedicalArziConstants.ARZI_SUBMITTED_STATUS);
					
					successMsg = "The arzi \"" + arzi.getArziId() + "\" for \""
							+ MedicalArziUtils.constructPtntFullName(patient)
							+ "\" is successfully submitted.";
				}

				arzi.setCurrentStatus(arziStatus);

				logger.debug("Updating the arzi with arzi id \""
						+ arzi.getArziId()
						+ "\" for patient with ITS number-> \""
						+ patient.getItsNumber() + "\"");

				// Update the arzi based on the arzi id.
				getPatientService().updateAnExistingArzi(arzi);
				
				refreshGridWithFreshData();
				
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

		} else if (event.getButton().equals(deleteBtn)) {
			// Deactivate the arzi
			getPatientService().deactivateAnArziById(selectedArzi.getArziId());

			successMsg = "The arzi \"" + selectedArzi.getArziId() + "\" for \""
					+ MedicalArziUtils.constructPtntFullName(patient)
					+ "\" is successfully deleted.";

			refreshGridWithFreshData();
			
			MedicalArziUtils.createAndShowNotification(null, successMsg,
					Type.HUMANIZED_MESSAGE, Position.TOP_LEFT,
					"userFriendlyMsg",
					MedicalArziConstants.USER_FRIENDLY_MSG_DELAY_MSEC);
		}
	}

	/**
	 * This method is responsible for fetching the arzis for the user from the
	 * database and updating the datasource container for the arzi grid with the
	 * latest set of data.
	 * 
	 */
	public void refreshGridWithFreshData() {
		// Retrieve the list of arzis again and refresh the container
		// with the new set of data.
		List<Arzi> arziList = getPatientService().retrieveAllArzisForPatient(patient
				.getItsNumber());
		/**
		 * Update the Grid with fresh data. Two step process of replacing bean
		 * items. (1) First remove all BeanItem objects with
		 * Container::removeAllItems method. (2) Then add replacement BeanItem
		 * objects with the BeanItemContainer::addAll method.
		 */
		arziContainer.removeAllItems();
		
		arziContainer.addAll(arziList);
	}
	
	
	/**
	 * This method is responsible for returning the right field based on the
	 * dynamic class.
	 * 
	 * @param objClass
	 * 
	 * @return com.vaadin.ui.Field<?>
	 */
	private Field<?> getField(Class<?> objClass) {
		Field<?> field = null;

		if (objClass == BodyPart.class) {
			Container bdyPrtContainer = MedicalArziUtils
					.getContainer(BodyPart.class);
			
			field = MedicalArziUtils
					.getComboBox("Body Part:", "Please select a body part...",
							getLookupService().getListOfAllBodyParts(),
							"bodyPartName", bdyPrtContainer, null, true,
							"Body Part is required!");

		} else if (objClass == Condition.class) {
			Container condContainer = MedicalArziUtils
					.getContainer(Condition.class);

			field = MedicalArziUtils.getComboBox("Medical Condition:",
					"Please select a condition...", getLookupService()
							.getListOfAllMedicalConditions(), "conditionName",
					condContainer, null, true, "Condition is required!");

		} else if (objClass == Procedure.class) {
			Container procContainer = MedicalArziUtils
					.getContainer(Procedure.class);

			field = MedicalArziUtils.getComboBox("Medical Procedure",
					"Please select a procedure...", getLookupService()
							.getListOfAllMedicalProcedures(), "procedureName",
					procContainer, null, true, "Procedure is required!");

		} else if (objClass == ArziType.class) {
			Container arziTypeContainer = MedicalArziUtils
					.getContainer(ArziType.class);

			field = MedicalArziUtils.getComboBox("Arzi Type:", "Please select a type...",
					getLookupService().getListOfAllArziTypes(), "arziTypeName",
					arziTypeContainer, null, true, "Type is required!");
		} else if (objClass == String.class) {
			field = MedicalArziUtils.getTextFieldEditor("Other Condition:", true, false, null);
		}

		return field;
	}

	@Override
	public void valueChange(ValueChangeEvent event) {

		// Select/Deselect the other condition box based on the the value
		// selection in the condition combobox.
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
