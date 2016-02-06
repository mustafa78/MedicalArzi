package com.example.medicalarzi.component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.medicalarzi.model.Arzi;
import com.example.medicalarzi.model.ArziSearchCriteria;
import com.example.medicalarzi.model.ArziSearchResult;
import com.example.medicalarzi.model.ArziType;
import com.example.medicalarzi.model.BodyPart;
import com.example.medicalarzi.model.Condition;
import com.example.medicalarzi.model.GregHijDate;
import com.example.medicalarzi.model.Jamaat;
import com.example.medicalarzi.model.Lookup;
import com.example.medicalarzi.model.Patient;
import com.example.medicalarzi.model.Procedure;
import com.example.medicalarzi.service.ReviewerService;
import com.example.medicalarzi.service.ServiceLocator;
import com.example.medicalarzi.util.MedicalArziConstants;
import com.example.medicalarzi.util.MedicalArziUtils;
import com.example.medicalarzi.view.MedicalArziLandingView;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.themes.Reindeer;

public class SearchComponent extends CustomComponent implements ClickListener, SelectionListener
		 {

	private static final long serialVersionUID = -5883684094223685184L;

	private static Logger logger = LogManager.getLogger(SearchComponent.class);

	// Layout and components
	private VerticalSplitPanel splitPanel;

	private Grid resultsGrid;

	private VerticalLayout topLayout;

	private CustomFormComponent searchCriteria;

	private Button searchBtn;

	// Search criteria fields bound to ArziSearchCriteria object.
	@PropertyId("itsNumber")
	private TextField itsNumber;

	@PropertyId("firstName")
	private TextField firstName;

	@PropertyId("lastName")
	private TextField lastName;

	@PropertyId("arziType")
	private ComboBox arziType;

	@PropertyId("bodyPart")
	private ComboBox bodyPart;

	@PropertyId("procedure")
	private ComboBox procedure;

	@PropertyId("condition")
	private ComboBox condition;

	@PropertyId("jamaat")
	private ComboBox jamaat;

	@PropertyId("numberOfDays")
	private ComboBox numOfDays;

	// Binding Fields
	private BeanFieldGroup<ArziSearchCriteria> searchCriteriaFieldsBinder;

	// Grid container
	private BeanItemContainer<ArziSearchResult> resultsContainer;

	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 *
	 */
	public SearchComponent() {
		// Build the split panel. This is the mainLayout for this component
		buildSplitPanel();

		// Bind the member fields to the model
		bindFieldsToModel();

		// The root component. Must be set
		setCompositionRoot(splitPanel);
	}

	/**
	 * This method is responsible for binding the member fields to the
	 * ArziSearchCriteria model.
	 */
	private void bindFieldsToModel() {
		logger.debug("Binding the patient and the arzi fields to their respective models.");

		/* Bind the arzi fields. */
		ArziSearchCriteria searchCriteriaObj = new ArziSearchCriteria();
		/* Bind the search criteria fields */
		searchCriteriaFieldsBinder = new BeanFieldGroup<ArziSearchCriteria>(
				ArziSearchCriteria.class);
		// Bind all the member fields whose type extends Field to the property
		// id of the item.
		// The mapping is done based on the @PropertyId annotation
		searchCriteriaFieldsBinder.bindMemberFields(this);
		// Add bean item that is bound.
		searchCriteriaFieldsBinder.setItemDataSource(searchCriteriaObj);
		// Set the buffered mode on, if using the bean validation JSR-3.0 with
		// vaadin. Does not work when fields are immediately updated with the
		// bean.
		searchCriteriaFieldsBinder.setBuffered(true);
	}

	private void buildSplitPanel() {
		// top-level component properties
		setSizeFull();

		// common part: create layout
		splitPanel = new VerticalSplitPanel();
		splitPanel.setImmediate(false);
		splitPanel.setLocked(true);
		splitPanel.setSizeFull();
		splitPanel.setSplitPosition(62, Unit.PERCENTAGE, true);
		// Use a custom style
		splitPanel.addStyleName("invisiblesplitter");

		// topLayout
		buildTopLayout();
		splitPanel.addComponent(topLayout);

		// arziTable
		buildResultsGrid();
		splitPanel.addComponent(resultsGrid);
	}

	private void buildTopLayout() {
		// common part: create layout
		topLayout = new VerticalLayout();
		topLayout.setImmediate(false);
		topLayout.setMargin(true);
		topLayout.setSpacing(true);

		// searchCriteria
		buildSearchCriteria();
		topLayout.addComponent(searchCriteria);

		// search
		searchBtn = new Button(new ThemeResource("img/search.png"));
		searchBtn.addClickListener(this);
		searchBtn.setStyleName(Reindeer.BUTTON_LINK);
		// Set the search button as the default button. Causes a click event for
		// the button to be fired.
		searchBtn.setClickShortcut(KeyCode.ENTER);
		topLayout.addComponent(searchBtn);
		topLayout.setComponentAlignment(searchBtn, Alignment.MIDDLE_CENTER);
	}

	private void buildSearchCriteria() {
		// Search criteria customForm
		searchCriteria = new CustomFormComponent();
		searchCriteria.setImmediate(false);
		searchCriteria.setSizeFull();
		searchCriteria.setStyleName("customForm");

		/**
		 * Add the fields to the left FormLayout.
		 * 
		 */
		FormLayout leftFormLayout = (FormLayout) MedicalArziUtils.findById(
				searchCriteria,
				MedicalArziConstants.CUSTOM_FORM_LEFTFORM_LAYOUT_ID);
		// itsNumber
		itsNumber = new TextField("ITS Number:");
		itsNumber.setMaxLength(8);
		itsNumber.setNullRepresentation("");
		itsNumber.setConverter(MedicalArziUtils.itsNumberConverter());
		leftFormLayout.addComponent(itsNumber);

		// lastName
		lastName = new TextField("Surname/Last Name:");
		lastName.setNullRepresentation("");
		lastName.setWidth("300px");
		leftFormLayout.addComponent(lastName);

		// region
		loadJamaats();
		leftFormLayout.addComponent(jamaat);

		// bodyPart
		loadBodyParts();
		leftFormLayout.addComponent(bodyPart);

		loadNumberOfDays();
		leftFormLayout.addComponent(numOfDays);

		/**
		 * Add the fields to the right FormLayout.
		 * 
		 */
		FormLayout rightFormLayout = (FormLayout) MedicalArziUtils.findById(
				searchCriteria,
				MedicalArziConstants.CUSTOM_FORM_RIGHTFORM_LAYOUT_ID);

		// firstName
		firstName = new TextField("First Name:");
		firstName.setNullRepresentation("");
		firstName.setWidth("300px");
		rightFormLayout.addComponent(firstName);

		// bodyPart
		loadArziTypes();
		rightFormLayout.addComponent(arziType);

		// condition
		loadConditions();
		rightFormLayout.addComponent(condition);

		// procedure
		loadProcedures();
		rightFormLayout.addComponent(procedure);

	}

	private void loadJamaats() {
		jamaat = new ComboBox("Jamaat:");
		jamaat.setImmediate(true);
		jamaat.setContainerDataSource(MedicalArziUtils
				.getContainer(Jamaat.class));
		jamaat.addItems(ServiceLocator.getInstance().getLookupService()
				.getListOfAllJamaats());
		jamaat.setInputPrompt("Please select your jamaat.");
		jamaat.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		jamaat.setItemCaptionPropertyId("jamaatName");
		jamaat.setRequired(false);
	}

	private void loadArziTypes() {
		arziType = new ComboBox("Arzi Type:");
		arziType.setContainerDataSource(MedicalArziUtils
				.getContainer(ArziType.class));
		arziType.addItems(ServiceLocator.getInstance().getLookupService()
				.getListOfAllArziTypes());
		arziType.setInputPrompt("Please select the arzi type.");
		arziType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		arziType.setItemCaptionPropertyId("arziTypeName");
		arziType.setRequired(false);
	}

	private void loadConditions() {
		condition = new ComboBox("Medical Condition:");
		condition.setImmediate(true);
		condition.setContainerDataSource(MedicalArziUtils
				.getContainer(Condition.class));
		condition.addItems(ServiceLocator.getInstance().getLookupService()
				.getListOfAllMedicalConditions());
		condition.setInputPrompt("Please select the medical condition.");
		condition.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		condition.setItemCaptionPropertyId("conditionName");
		condition.setRequired(false);
	}

	private void loadBodyParts() {
		bodyPart = new ComboBox("Body Part:");
		bodyPart.setContainerDataSource(MedicalArziUtils
				.getContainer(BodyPart.class));
		bodyPart.addItems(ServiceLocator.getInstance().getLookupService()
				.getListOfAllBodyParts());
		bodyPart.setInputPrompt("Please select the body part.");
		bodyPart.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		bodyPart.setItemCaptionPropertyId("bodyPartName");
		bodyPart.setRequired(false);
	}

	private void loadProcedures() {
		procedure = new ComboBox("Medical Procedure:");
		procedure.setContainerDataSource(MedicalArziUtils
				.getContainer(Procedure.class));
		procedure.addItems(ServiceLocator.getInstance().getLookupService()
				.getListOfAllMedicalProcedures());
		procedure.setInputPrompt("Please select the medical procedure.");
		procedure.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		procedure.setItemCaptionPropertyId("procedureName");
		procedure.setRequired(false);
	}

	private void loadNumberOfDays() {
		numOfDays = new ComboBox("Search Period:");
		numOfDays.setContainerDataSource(MedicalArziUtils
				.getContainer(Lookup.class));
		numOfDays.addItems(ServiceLocator.getInstance().getLookupService()
				.getByLookupType(MedicalArziConstants.SEARCH_NUM_OF_DAYS));
		numOfDays.setInputPrompt("Please select the number of days to search.");
		numOfDays.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		numOfDays.setItemCaptionPropertyId("lookupValue");
		numOfDays.setRequired(false);
	}

	@SuppressWarnings("unchecked")
	private void buildResultsGrid() {
		resultsGrid = new Grid();
		resultsGrid.setSizeFull();
		resultsGrid.setImmediate(false);

		resultsContainer = (BeanItemContainer<ArziSearchResult>) MedicalArziUtils
				.getContainer(ArziSearchResult.class);

		// Set the container as the grid datasource
		resultsGrid.setContainerDataSource(resultsContainer);

		// Add nested properties to the header
		resultsContainer.addNestedContainerBean("patient");
		resultsContainer.addNestedContainerBean("arzi");

		// Add all the intermediate nested properties (which are also
		// beans)
		resultsContainer.addNestedContainerBean("patient.jamaat");
		resultsContainer.addNestedContainerBean("arzi.requestSubmitDate");
		resultsContainer.addNestedContainerBean("arzi.currentStatus");
		resultsContainer.addNestedContainerBean("arzi.arziType");
		resultsContainer.addNestedContainerBean("arzi.bodyPart");
		resultsContainer.addNestedContainerBean("arzi.condition");
		resultsContainer.addNestedContainerBean("arzi.conditionStartDate");
		resultsContainer.addNestedContainerBean("arzi.procedure");

		// Setting the grid columns
		resultsGrid.setColumns("patient.itsNumber", "patient.firstName",
				"patient.lastName", "patient.jamaat.jamaatName",
				"patient.jamaat.jamiyatName", "arzi.arziType.arziTypeName",
				"arzi.currentStatus.statusDesc",
				"arzi.requestSubmitDate.gregorianCalDate",
				"arzi.bodyPart.bodyPartName", "arzi.condition.conditionName",
				"arzi.otherCondition",
				"arzi.conditionStartDate.gregorianCalDate",
				"arzi.procedure.procedureName");

		resultsGrid.addSelectionListener(this);

		// Customize the grid columns
		customizeResultsGridColumns();

	}

	/**
	 * This method is responsible for customizing all the grid columns like
	 * setting the caption, setting the editor fields and grouping the similar
	 * columns together etc.
	 * 
	 */
	private void customizeResultsGridColumns() {
		// Sets the converter on the ITS number to remove the grouping used.
		resultsGrid.getColumn("patient.itsNumber").setMaximumWidth(100)
				.setConverter(MedicalArziUtils.itsNumberConverter());
		
		resultsGrid.getColumn("patient.firstName").setMaximumWidth(110);
		
		resultsGrid.getColumn("patient.lastName").setMaximumWidth(110);

		// Set Header captions
		resultsGrid.getColumn("arzi.currentStatus.statusDesc")
				.setHeaderCaption("Status");

		resultsGrid
				.getColumn("arzi.conditionStartDate.gregorianCalDate")
				.setMaximumWidth(140)
				.setHeaderCaption("Condition Start Date")
				.setRenderer(
						new DateRenderer("%1$tb %1$td, %1$tY", Locale.ENGLISH));

		resultsGrid.getColumn("patient.jamaat.jamaatName").setHeaderCaption(
				"Jamaat");

		resultsGrid.getColumn("patient.jamaat.jamiyatName").setHeaderCaption(
				"Jamiyat");

		resultsGrid
				.getColumn("arzi.requestSubmitDate.gregorianCalDate")
				.setMaximumWidth(130)
				.setHeaderCaption("Submit Date")
				.setRenderer(
						new DateRenderer("%1$tb %1$td, %1$tY", Locale.ENGLISH));

		resultsGrid.getColumn("arzi.arziType.arziTypeName").setHeaderCaption(
				"Type");

		resultsGrid.getColumn("arzi.bodyPart.bodyPartName").setHeaderCaption(
				"Body Part");

		resultsGrid.getColumn("arzi.condition.conditionName").setHeaderCaption(
				"Condition");

		resultsGrid.getColumn("arzi.procedure.procedureName").setHeaderCaption(
				"Procedure");
		
		resultsGrid.getColumn("arzi.otherCondition").setMaximumWidth(150);

		// This call prepends the header row to the existing grid
		HeaderRow searchResultsHeader = resultsGrid.prependHeaderRow();

		// Get hold of the columnID, mind you in my case this is a nestedID
		// Join the arzi related cells
		HeaderCell arziTypeCell = searchResultsHeader
				.getCell("arzi.arziType.arziTypeName");
		HeaderCell arziStatusCell = searchResultsHeader
				.getCell("arzi.currentStatus.statusDesc");
		HeaderCell arziSubmitDtCell = searchResultsHeader
				.getCell("arzi.requestSubmitDate.gregorianCalDate");
		searchResultsHeader
				.join(arziTypeCell, arziStatusCell, arziSubmitDtCell).setText(
						"Arzi");

		// Join the Medical info cells
		HeaderCell bodyPartCell = searchResultsHeader
				.getCell("arzi.bodyPart.bodyPartName");
		HeaderCell condCell = searchResultsHeader
				.getCell("arzi.condition.conditionName");
		HeaderCell procCell = searchResultsHeader
				.getCell("arzi.procedure.procedureName");
		HeaderCell otherCondCell = searchResultsHeader
				.getCell("arzi.otherCondition");
		HeaderCell condStartDtCell = searchResultsHeader
				.getCell("arzi.conditionStartDate.gregorianCalDate");
		searchResultsHeader.join(bodyPartCell, condCell, procCell,
				otherCondCell, condStartDtCell).setText("Medical Information");

		// Join the Patient info cells
		HeaderCell itsNumCell = searchResultsHeader
				.getCell("patient.itsNumber");
		HeaderCell firstNameCell = searchResultsHeader
				.getCell("patient.firstName");
		HeaderCell lastNameCell = searchResultsHeader
				.getCell("patient.lastName");
		searchResultsHeader.join(itsNumCell, firstNameCell, lastNameCell)
				.setText("Patient");

	}

	/**
	 * Implement the button click listener. Called when the 'Search' button is
	 * clicked.
	 * 
	 * @param event
	 */
	@Override
	public void buttonClick(ClickEvent event) {

		if (event.getButton().equals(searchBtn)) {
			try {
				// FieldGroup buffered mode is on, so commit() is required.
				// Throws CommitException
				searchCriteriaFieldsBinder.commit();

				ArziSearchCriteria userEnteredSearchCriteria = searchCriteriaFieldsBinder
						.getItemDataSource().getBean();

				ReviewerService reviewerService = ServiceLocator.getInstance()
						.getReviewerService();

				Lookup searchPeriod = (Lookup) numOfDays.getValue();

				// If the user entered the number of days to search as one of
				// the search criteria
				if (searchPeriod != null) {

					GregHijDate currentDate = ServiceLocator.getInstance()
							.getLookupService()
							.getRequestedGregorianHijriCalendar(new Date());

					userEnteredSearchCriteria.setCurrentDate(currentDate);

					// this would default to now
					Calendar calendar = Calendar.getInstance();

					// Subtract the number of days to get the
					if (searchPeriod.getLookupId().intValue() == MedicalArziConstants.SEARCH_PERIOD_LAST_7_DAYS_ID) {
						calendar.add(Calendar.DAY_OF_MONTH, -7);
					} else if (searchPeriod.getLookupId().intValue() == MedicalArziConstants.SEARCH_PERIOD_LAST_10_DAYS_ID) {
						calendar.add(Calendar.DAY_OF_MONTH, -10);
					} else if (searchPeriod.getLookupId().intValue() == MedicalArziConstants.SEARCH_PERIOD_LAST_14_DAYS_ID) {
						calendar.add(Calendar.DAY_OF_MONTH, -14);
					} else if (searchPeriod.getLookupId().intValue() == MedicalArziConstants.SEARCH_PERIOD_LAST_21_DAYS_ID) {
						calendar.add(Calendar.DAY_OF_MONTH, -21);
					} else if (searchPeriod.getLookupId().intValue() == MedicalArziConstants.SEARCH_PERIOD_LAST_30_DAYS_ID) {
						calendar.add(Calendar.DAY_OF_MONTH, -30);
					}

					GregHijDate dateForSearchPeriod = ServiceLocator
							.getInstance()
							.getLookupService()
							.getRequestedGregorianHijriCalendar(
									calendar.getTime());

					userEnteredSearchCriteria
							.setSearchPeriodDate(dateForSearchPeriod);
				} else {
					// Reset the criteria
					userEnteredSearchCriteria.setCurrentDate(null);
					userEnteredSearchCriteria.setSearchPeriodDate(null);
				}

				List<ArziSearchResult> searchResultList = reviewerService
						.searchArzisByCriteria(userEnteredSearchCriteria);

				/**
				 * Update the Grid with fresh data. Two step process of
				 * replacing bean items. (1) First remove all BeanItem objects
				 * with Container::removeAllItems method. (2) Then add
				 * replacement BeanItem objects with the
				 * BeanItemContainer::addAll method.
				 */
				resultsContainer.removeAllItems();
				if (searchResultList != null && searchResultList.isEmpty()) {
					String description = "No results found for the search criteria.";
					MedicalArziUtils.createAndShowNotification(null,
							description, Type.ERROR_MESSAGE, Position.TOP_LEFT,
							"errorMsg", -1);
				} else {
					// Add data to the container
					resultsContainer.addAll(searchResultList);
				}

			} catch (CommitException ce) {
				logger.error(ce);

				String errorDescription = "Please fix the errors before proceeding further.";

				// Create an error notification if the required fields are not
				// entered correctly.
				MedicalArziUtils.createAndShowNotification(null,
						errorDescription, Type.ERROR_MESSAGE,
						Position.TOP_LEFT, "errorMsg", -1);
			}
		}

	}

	@Override
	public void select(SelectionEvent event) {
		ArziSearchResult selectedResult = (ArziSearchResult) resultsGrid
				.getSelectedRow();

		if (selectedResult != null) {
			// Get the current view and set the selected tab to Inbox to
			// display the newly saved/submitted arzi in the Inbox.
			View currentView = UI.getCurrent().getNavigator().getCurrentView();

			if (currentView instanceof MedicalArziLandingView) {
				MedicalArziLandingView landingView = (MedicalArziLandingView) currentView;

				ArziFormComponent reviewArziComponent = populateTheFormWithSelectedResult(selectedResult);

				String reviewArziCaption = MedicalArziConstants.REVIEW_ARZI_TAB_CAPTION
						+ " - " + selectedResult.getArzi().getArziId();

				String reviewArziTabId = MedicalArziConstants.REVIEW_ARZI_TAB_ID_PREFIX
						+ " _ " + selectedResult.getArzi().getArziId();

				// Add the tab for the arzi which is being reviewed.
				Tab reviewArziTab = landingView.getTabSheet().addTab(
						reviewArziComponent, reviewArziCaption);
				reviewArziTab.setId(reviewArziTabId);
				reviewArziTab.setClosable(true);
			}

		}
		
	}
	
	/**
	 * This method is responsible for populating the fields on the
	 * ArziFormComponent with the arzi and patient information fetched from the
	 * selected result row.
	 * 
	 * @param reviewArziComponent
	 * @param selectedResult
	 * 
	 * @return com.example.medicalarzi.component.ArziFormComponent
	 */
	private ArziFormComponent populateTheFormWithSelectedResult(
			ArziSearchResult selectedResult) {
		
		// Extract the arzi and the patient information
		Arzi arziInfo = selectedResult.getArzi();
		
		Patient ptntInfo = selectedResult.getPatient();
		
		ArziFormComponent reviewArziComponent = new ArziFormComponent(ptntInfo, arziInfo);
		
		reviewArziComponent.getItsNumber().setValue(
				String.valueOf(ptntInfo.getItsNumber()));
		
		makeFieldsReadOnlyWhenReviewing(reviewArziComponent);
		
		Component buttonLayout = MedicalArziUtils.findById(reviewArziComponent,
				MedicalArziConstants.ARZI_FORM_COMPONENT_BUTTON_LAYOUT_ID);
		
		buttonLayout.setVisible(false);
		
		return reviewArziComponent;

	}
	
	/**
	 * 
	 * @param reviewArziComponent
	 */
	private void makeFieldsReadOnlyWhenReviewing(
			ArziFormComponent reviewArziComponent) {
		// Patient's profile information set to read only.
		reviewArziComponent.getItsNumber().setReadOnly(true);

		reviewArziComponent.getFirstName().setReadOnly(true);

		reviewArziComponent.getMiddleName().setReadOnly(true);

		reviewArziComponent.getLastName().setReadOnly(true);

		reviewArziComponent.getGender().setReadOnly(true);

		reviewArziComponent.getDob().setReadOnly(true);

		reviewArziComponent.getAddressLn1().setReadOnly(true);

		reviewArziComponent.getAddressLn2().setReadOnly(true);

		reviewArziComponent.getCity().setReadOnly(true);

		reviewArziComponent.getCountryState().setReadOnly(true);

		reviewArziComponent.getZip().setReadOnly(true);
		
		reviewArziComponent.getJamaat().setReadOnly(true);
		
		// Patient's arzi information set to read only.
		reviewArziComponent.getArziType().setReadOnly(true);
		
		reviewArziComponent.getBodyPart().setReadOnly(true);
		
		reviewArziComponent.getProcedure().setReadOnly(true);
		
		reviewArziComponent.getCondition().setReadOnly(true);
		
		reviewArziComponent.getConditionStartDate().setReadOnly(true);
		
		if(reviewArziComponent.getOtherCondition() != null)
			reviewArziComponent.getOtherCondition().setReadOnly(true);
		
	}

}
