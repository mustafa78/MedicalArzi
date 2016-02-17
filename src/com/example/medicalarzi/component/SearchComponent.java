package com.example.medicalarzi.component;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
import com.example.medicalarzi.model.Status;
import com.example.medicalarzi.service.ReviewerService;
import com.example.medicalarzi.service.ServiceLocator;
import com.example.medicalarzi.util.MedicalArziConstants;
import com.example.medicalarzi.util.MedicalArziUtils;
import com.example.medicalarzi.view.MedicalArziLandingView;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
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
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
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

	/**Top Layout components & buttons**/
	private VerticalLayout topLayout;

	private CustomFormComponent searchCriteria;

	private Button searchBtn;
	
	/**Bottom Layout components & buttons**/
	private VerticalLayout bottomLayout;
	
	private Grid resultsGrid;
	
	private Button assignBtn;

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
	
	private final Set<BeanItem<ArziSearchResult>> editedItems = new HashSet<BeanItem<ArziSearchResult>>();

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

		// bottomLayout
		buildBottomLayout();
		splitPanel.addComponent(bottomLayout);
	}

	/**
	 * This method builds the top section of the split panel containing the
	 * search criteria and the search button.
	 */
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
	
	/**
	 * This method builds the bottom section of the split panel containing the
	 * results grid and assign button..
	 */
	private void buildBottomLayout() {
		bottomLayout = new VerticalLayout();
		bottomLayout.setSizeFull();
		bottomLayout.setImmediate(false);
		bottomLayout.setMargin(true);
		bottomLayout.setSpacing(true);
		
		buildResultsGrid();
		bottomLayout.addComponent(resultsGrid);
		bottomLayout.setExpandRatio(resultsGrid, 2.0f);
	
		// assignBtn
		assignBtn = new Button(new ThemeResource("img/assign_me.png"));
		assignBtn.addClickListener(this);
		assignBtn.setStyleName(Reindeer.BUTTON_LINK);
		bottomLayout.addComponent(assignBtn);
		bottomLayout.setComponentAlignment(assignBtn, Alignment.MIDDLE_CENTER);
		
		// The bottom layout should not be visible initially. It should only be
		// visible only when the user clicks on the "Search" button.
		bottomLayout.setVisible(false);
	}

	@SuppressWarnings("unchecked")
	private void buildResultsGrid() {
		resultsGrid = new Grid();
		resultsGrid.setSizeFull();
		resultsGrid.setImmediate(false);
		
		// Sets the Multi selection mode for this grid.
		resultsGrid.setSelectionMode(SelectionMode.MULTI);

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
				"patient.jamaat.jamiyatName", "arzi.arziId",
				"arzi.arziType.arziTypeName", "arzi.currentStatus.statusDesc",
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
		HeaderCell arziIdCell = searchResultsHeader
				.getCell("arzi.arziId");
		HeaderCell arziTypeCell = searchResultsHeader
				.getCell("arzi.arziType.arziTypeName");
		HeaderCell arziStatusCell = searchResultsHeader
				.getCell("arzi.currentStatus.statusDesc");
		HeaderCell arziSubmitDtCell = searchResultsHeader
				.getCell("arzi.requestSubmitDate.gregorianCalDate");
		searchResultsHeader.join(arziIdCell, arziTypeCell, arziStatusCell,
				arziSubmitDtCell).setText("Arzi");

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

		ArziSearchCriteria userEnteredSearchCriteria = null;
		
		if (event.getButton().equals(searchBtn)) {
			try {
				// FieldGroup buffered mode is on, so commit() is required.
				// Throws CommitException
				searchCriteriaFieldsBinder.commit();

				userEnteredSearchCriteria = searchCriteriaFieldsBinder
						.getItemDataSource().getBean();

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

					// Subtract the number of days to get the range between
					// current date and number of days selected as the criteria.
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
				
				// Populate and refresh the grid with the data based on the user
				// entered search criteria.
				refreshGridWithFreshData(userEnteredSearchCriteria);

			} catch (CommitException ce) {
				logger.error(ce);

				String errorDescription = "Please fix the errors before proceeding further.";

				// Create an error notification if the required fields are not
				// entered correctly.
				MedicalArziUtils.createAndShowNotification(null,
						errorDescription, Type.ERROR_MESSAGE,
						Position.TOP_LEFT, "errorMsg", -1);
			}
		} else if (event.getButton().equals(assignBtn)) {

			if (!editedItems.isEmpty()) {

				ReviewerService reviewSer = ServiceLocator.getInstance()
						.getReviewerService();

				// Get the patient information from the session. In this tab,
				// the patient also has the reviewer role so that he/she is able
				// to review all the submitted arzis and take appropriate
				// action.
				Patient patient = (Patient) MedicalArziUtils
						.getSessionAttribute(MedicalArziConstants.SESS_ATTR_PTNT_INFO);

				for (BeanItem<ArziSearchResult> arziSearchResult : editedItems) {
					Arzi arzi = arziSearchResult.getBean().getArzi();

					// For ArziHeader to update the arzi
					Status arziStatus = new Status();
					arziStatus
							.setStatusId(MedicalArziConstants.ARZI_IN_PROCESS_STATUS);
					arzi.setCurrentStatus(arziStatus);

					GregHijDate currentDate = ServiceLocator.getInstance()
							.getLookupService()
							.getRequestedGregorianHijriCalendar(new Date());

					arzi.setCurrentStatusDate(currentDate);

					// For Arzi to create a new record in the detail table.
					arzi.setStatusChangeDate(currentDate);

					arzi.setStatus(arziStatus);

					arzi.setReviewDate(currentDate);

					// The patient is also the reviewer.
					Long reviewerItsNumber = patient.getItsNumber();

					arzi.setReviewerItsNumber(reviewerItsNumber);

					reviewSer.updateAnExistingArzi(arzi);
				}
				
				// Refresh the grid so that the latest status displayed in the
				// grid after the reviewer has assigned arzi for review.
				refreshGridWithFreshData(userEnteredSearchCriteria);	

				// Get the current view and set the selected tab to Inbox to
				// display the newly saved/submitted arzi in the Inbox.
				View currentView = UI.getCurrent().getNavigator()
						.getCurrentView();

				if (currentView instanceof MedicalArziLandingView) {
					
					MedicalArziLandingView landingView = (MedicalArziLandingView) currentView;

					// Once the arzis are assigned, reconstruct the Pending
					// Task tab to get the latest data and then switch the
					// selection to the Pending Tasks tab with the user
					// friendly success message.
					landingView.setRefreshPendingTasks(true);

					landingView.getTabSheet().setSelectedTab(
							landingView.getPendingTasksComponent());

					String successMsg = "\""
							+ MedicalArziUtils.constructPtntFullName(patient)
							+ "\" has been successfully assigned arzis to review.";

					MedicalArziUtils.createAndShowNotification(null,
							successMsg, Type.HUMANIZED_MESSAGE,
							Position.TOP_LEFT, "userFriendlyMsg",
							MedicalArziConstants.USER_FRIENDLY_MSG_DELAY_MSEC);
				}
			}

		}

	}

	/**
	 * This method is responsible for searching the database based on the user
	 * selected search criteria and populating the results in the grid. If the
	 * selected criteria does not fetch any result, the bottom layout is not
	 * shown which contains the grid and the "Assign" button.
	 * 
	 * @param userEnteredSearchCriteria
	 */
	private void refreshGridWithFreshData(
			ArziSearchCriteria userEnteredSearchCriteria) {
		ReviewerService reviewerService = ServiceLocator.getInstance()
				.getReviewerService();

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
			// Hide the bottom layout containing the grid and the
			// "Assign" Button when the search yields 0 results.
			bottomLayout.setVisible(false);
			
			String description = "No results found for the search criteria.";
			
			MedicalArziUtils.createAndShowNotification(null,
					description, Type.ERROR_MESSAGE, Position.TOP_LEFT,
					"errorMsg", -1);
		} else {
			// Add data to the container
			resultsContainer.addAll(searchResultList);
			
			// Display the bottom layout with the results in the grid
			// and the "Assign" Button.
			bottomLayout.setVisible(true);
			
			// "Assign Me" button should be disabled by default and
			// should only be enabled when the user starts selecting
			// arzis for assignment.
			assignBtn.setEnabled(false);
		}
	}

	@Override
	public void select(SelectionEvent event) {
		// Logic for handling selection changes from Grid
        boolean empty = event.getSelected().isEmpty();
        
        if (!empty) {
            // Keep track of items currently being edited.
            editedItems.addAll(getItemIds(event.getAdded()));
            
            editedItems.removeAll(getItemIds(event.getRemoved()));
            
        } else {
            editedItems.clear();
        }
        
        assignBtn.setEnabled(
                resultsGrid.getSelectedRows().size() > 0);
		
	}
	
	/**
     * Gets a list of BeanItems for given item IDs from the container of
     * Grid.
     *
     * @param itemIds
     *            set of item ids
     * @return a collection of BeanItems for given itemIds
     */
	@SuppressWarnings({ "unchecked" })
	private Collection<BeanItem<ArziSearchResult>> getItemIds(
			Set<Object> itemIds) {
		
		Set<BeanItem<ArziSearchResult>> items = new HashSet<BeanItem<ArziSearchResult>>();
		
		for (Object id : itemIds) {
			items.add((BeanItem<ArziSearchResult>) resultsGrid
					.getContainerDataSource().getItem(id));
		}
		
		return items;
	}

}
