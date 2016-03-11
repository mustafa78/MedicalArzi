/**
 * 
 */
package com.example.medicalarzi.component;

import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import com.example.medicalarzi.view.MedicalArziLandingView;
import com.vaadin.data.Container;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author mkanchwa
 *
 */
public class InboxComponent extends CustomComponent implements ClickListener,
		ItemClickListener {

	private static final long serialVersionUID = 2521689363206700694L;
	
	public static Logger logger = LogManager.getLogger(InboxComponent.class);
	
	private Patient patient;
	
	// Inbox
	private VerticalLayout inboxViewLayout;

	private Grid arziGrid;
	
	// Buttons layout
	private HorizontalLayout buttonsLayout;

	private Button submitBtn;

	private Button saveBtn;
	
	private Button deleteBtn;
	
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
		inboxDescription.setCaption("My Arzis: <i>(Double click to view)</i>");
		inboxDescription.setCaptionAsHtml(true);
		inboxDescription.setStyleName("v-captiontext");
		inboxViewLayout.addComponent(inboxDescription);
		inboxViewLayout.setExpandRatio(inboxDescription, 0.1f);

		buildArziGrid();
		inboxViewLayout.addComponent(arziGrid);
		inboxViewLayout.setExpandRatio(arziGrid, 2.0f);
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
		arziContainer.addNestedContainerBean("requestSubmitDate");
		arziContainer.addNestedContainerBean("currentStatus");

		// Customize the columns
		customizeArziGridColumns();
		
		//Add the event listener on the grid on selecting a row.
		//arziGrid.addSelectionListener(this);
		arziGrid.addItemClickListener(this);
	}
	
	
	/**
	 * This method is responsible for customizing all the grid columns like
	 * setting the caption and grouping the similar columns together etc.
	 */
	private void customizeArziGridColumns() {
		
		arziGrid.setColumns("arziId", "requestSubmitDate.gregorianCalDate",
				"arziSummary", "currentStatus.statusDesc");
		
		// Column reordering allowed
		arziGrid.setColumnReorderingAllowed(true);
		
		// Sets the converter on the ITS number to remove the grouping used.
		arziGrid.getColumn("arziId").setHeaderCaption("ID").setMaximumWidth(70)
				.setConverter(MedicalArziUtils.itsNumberConverter());
		
		arziGrid.getColumn("requestSubmitDate.gregorianCalDate")
				.setHeaderCaption("Saved/Submit Date")
				.setMaximumWidth(150)
				.setRenderer(
						new DateRenderer("%1$tb %1$td, %1$tY", Locale.ENGLISH));	

		arziGrid.getColumn("currentStatus.statusDesc")
				.setHeaderCaption("Status").setMaximumWidth(110);


		//groupArziGridColumns();
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
		
		
		// Adding the commit handler to the field group
		MedicalArziCommitHandler maCommitHdlr = new MedicalArziCommitHandler();
		
		arziGrid.getEditorFieldGroup().addCommitHandler(maCommitHdlr);
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
	 * This method is responsible for populating the fields on the
	 * ArziFormComponent with the arzi and patient information fetched from the
	 * selected result row.
	 * 
	 * @param selectedArzi
	 * 
	 * @return com.example.medicalarzi.component.ArziFormComponent
	 */
	private ArziFormComponent populateTheFormWithSelectedArziData(
			Arzi selectedArzi) {
		ArziFormComponent ediArziComponent = new ArziFormComponent(patient,
				selectedArzi);

		ediArziComponent.getItsNumber().setValue(
				String.valueOf(patient.getItsNumber()));
		
		return ediArziComponent;
	}
	
	/**
	 * 
	 * @param arziComponent
	 */
	private void makeFieldsReadOnly(ArziFormComponent arziComponent) {
		
		// Patient's profile information set to read only.
		arziComponent.getItsNumber().setReadOnly(true);
		arziComponent.getGender().setReadOnly(true);
		arziComponent.getDob().setReadOnly(true);
		arziComponent.getJamaat().setReadOnly(true);
		
		// Patient's address
		arziComponent.getAddressLn1().setReadOnly(true);
		arziComponent.getAddressLn2().setReadOnly(true);
		arziComponent.getCountry().setReadOnly(true);
		arziComponent.getCity().setReadOnly(true);
		arziComponent.getCountryState().setReadOnly(true);
		arziComponent.getZip().setReadOnly(true);
		arziComponent.getPhoneNum().setReadOnly(true);
		arziComponent.getPrimaryLocationOption().setReadOnly(true);

		// Patient's arzi information set to read only.
		arziComponent.getArziType().setReadOnly(true);
		arziComponent.getBodyPart().setReadOnly(true);
		if (arziComponent.getOtherBodyPart() != null) {
			arziComponent.getOtherBodyPart().setReadOnly(true);
		}
		arziComponent.getProcedure().setReadOnly(true);
		if (arziComponent.getOtherProcedure() != null) {
			arziComponent.getOtherProcedure().setReadOnly(true);
		}
		arziComponent.getCondition().setReadOnly(true);
		arziComponent.getConditionStartDate().setReadOnly(true);
		if (arziComponent.getOtherCondition() != null) {
			arziComponent.getOtherCondition().setReadOnly(true);
		}
		arziComponent.getArziSummary().setReadOnly(true);

		// Patient's Medical History
		arziComponent.getAsthma().setReadOnly(true);
		arziComponent.getAtrialFibrillation().setReadOnly(true);
		arziComponent.getCholesterol().setReadOnly(true);
		arziComponent.getHyperTension().setReadOnly(true);
		arziComponent.getThyroidDisorder().setReadOnly(true);
		arziComponent.getCancer().setReadOnly(true);
		if (arziComponent.getCancerType() != null) {
			arziComponent.getCancerType().setReadOnly(true);
		}
		arziComponent.getHeartDisease().setReadOnly(true);
		if (arziComponent.getHeartDiseaseType() != null) {
			arziComponent.getHeartDiseaseType().setReadOnly(true);
		}
		arziComponent.getDiabetes().setReadOnly(true);
		if (arziComponent.getDiabetesOption() != null) {
			arziComponent.getDiabetesOption().setReadOnly(true);
		}
		arziComponent.getOtherProblems().setReadOnly(true);
	}	
	

	/**
	 * This is the itemClick listener which listens to the click event on any of
	 * the grid items (row). Override this when the SelectionListener is added
	 * to the grid
	 * 
	 * @param event
	 */	
	
	@Override
	public void itemClick(ItemClickEvent event) {
		// Open the arzi for editing on double click event.
		if (event.isDoubleClick()) {
			Arzi selectedArzi = (Arzi) event.getItemId();
			
			if (selectedArzi != null) {

				View currentView = UI.getCurrent().getNavigator()
						.getCurrentView();

				if (currentView instanceof MedicalArziLandingView) {
					MedicalArziLandingView landingView = (MedicalArziLandingView) currentView;

					ArziFormComponent arziComponent = populateTheFormWithSelectedArziData(selectedArzi);

					String arziCaption = null;
					String arziTabComponentId = null;
					
					if (selectedArzi.getCurrentStatus().getStatusId()
							.equals(MedicalArziConstants.ARZI_DRAFT_STATUS)) {

						arziCaption = MedicalArziConstants.EDIT_ARZI_TAB_CAPTION
								+ " # " + selectedArzi.getArziId();
						arziTabComponentId = MedicalArziConstants.EDIT_ARZI_TAB_COMPONENT_ID
								+ "_" + selectedArzi.getArziId();
					} else if (selectedArzi.getCurrentStatus().getStatusId()
							.equals(MedicalArziConstants.ARZI_SUBMITTED_STATUS)) {

						arziCaption = MedicalArziConstants.VIEW_ARZI_TAB_CAPTION
								+ " # " + selectedArzi.getArziId();

						arziTabComponentId = MedicalArziConstants.VIEW_ARZI_TAB_COMPONENT_ID
								+ "_" + selectedArzi.getArziId();

						makeFieldsReadOnly(arziComponent);
					}

					// Find the component based on the id inside the tab for
					// the arzi which is being reviewed.
					Component arziTabComponent = MedicalArziUtils.findById(
							landingView.getTabSheet(), arziTabComponentId);

					// If the tab for the arzi id to be edited is not
					// added, add the tab for that arzi. Else just take the
					// user to the requested edit arzi tab.
					Tab arziTab = null;
					if (arziTabComponent == null) {

						// Set the unique id to track if the arzi to be
						// reviewed is already open.
						arziComponent.setId(arziTabComponentId);

						// Add the new tab for the arzi to be edited and
						// make it closable.
						arziTab = landingView.getTabSheet().addTab(
								arziComponent, arziCaption);

						arziTab.setClosable(true);

						landingView.getTabSheet().setSelectedTab(arziTab);

					} else {
						// Get the tab for the component and select it.
						arziTab = landingView.getTabSheet().getTab(
								arziTabComponent);

						landingView.getTabSheet().setSelectedTab(arziTab);
					}

					logger.debug("The patient with ITS number:-> "
							+ patient.getItsNumber() + " selected arzi : "
							+ selectedArzi + " for editing");
				}			

			}
			
		}
		
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


}
