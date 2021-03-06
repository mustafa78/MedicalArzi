package com.example.medicalarzi.component;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.medicalarzi.model.ArziSearchCriteria;
import com.example.medicalarzi.model.ArziSearchResult;
import com.example.medicalarzi.model.ArziType;
import com.example.medicalarzi.model.BodyPart;
import com.example.medicalarzi.model.Condition;
import com.example.medicalarzi.model.GregHijDate;
import com.example.medicalarzi.model.Jamaat;
import com.example.medicalarzi.model.Lookup;
import com.example.medicalarzi.model.Procedure;
import com.example.medicalarzi.service.ReviewerService;
import com.example.medicalarzi.service.ServiceLocator;
import com.example.medicalarzi.util.MedicalArziConstants;
import com.example.medicalarzi.util.MedicalArziUtils;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
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
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.themes.Reindeer;

public class DoctorSearch extends CustomComponent implements ClickListener {

	private static final long serialVersionUID = -2608386102633068228L;

	private static Logger logger = LogManager.getLogger(DoctorSearch.class);

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

	@PropertyId("searchPeriod")
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
	public DoctorSearch() {
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
		resultsContainer.addNestedContainerBean("arzi.currentStatus");
		resultsContainer.addNestedContainerBean("arzi.arziType");
		resultsContainer.addNestedContainerBean("arzi.bodyPart");
		resultsContainer.addNestedContainerBean("arzi.condition");
		resultsContainer.addNestedContainerBean("arzi.conditionStartDate");
		resultsContainer.addNestedContainerBean("arzi.procedure");

		// Setting the grid columns
		resultsGrid.setColumns("patient.itsNumber", "patient.firstName",
				"patient.lastName", "patient.jamaat.jamaatName",
				"patient.jamaat.jamiyatName", "arzi.currentStatus.statusDesc",
				"arzi.arziType.arziTypeName", "arzi.bodyPart.bodyPartName",
				"arzi.condition.conditionName", "arzi.otherCondition",
				"arzi.conditionStartDate.gregorianCalDate",
				"arzi.procedure.procedureName");

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
		resultsGrid.getColumn("patient.itsNumber").setConverter(
				MedicalArziUtils.itsNumberConverter());

		// Set Header captions
		resultsGrid.getColumn("arzi.currentStatus.statusDesc")
				.setHeaderCaption("Status");

		resultsGrid
				.getColumn("arzi.conditionStartDate.gregorianCalDate")
				.setHeaderCaption("Condition Start Date")
				.setRenderer(
						new DateRenderer("%1$tB %1$td, %1$tY", Locale.ENGLISH));

		resultsGrid.getColumn("patient.jamaat.jamaatName").setHeaderCaption(
				"Jamaat");

		resultsGrid.getColumn("patient.jamaat.jamiyatName").setHeaderCaption(
				"Jamiyat");

		resultsGrid.getColumn("arzi.arziType.arziTypeName").setHeaderCaption(
				"Type");

		resultsGrid.getColumn("arzi.bodyPart.bodyPartName").setHeaderCaption(
				"Body Part");

		resultsGrid.getColumn("arzi.condition.conditionName").setHeaderCaption(
				"Condition");

		resultsGrid.getColumn("arzi.procedure.procedureName").setHeaderCaption(
				"Procedure");

		// This call prepends the header row to the existing grid
		HeaderRow searchResultsHeader = resultsGrid.prependHeaderRow();

		// Get hold of the columnID, mind you in my case this is a nestedID
		// Join the arzi related cells
		HeaderCell arziStatusCell = searchResultsHeader
				.getCell("arzi.currentStatus.statusDesc");
		HeaderCell arziTypeCell = searchResultsHeader
				.getCell("arzi.arziType.arziTypeName");
		searchResultsHeader.join(arziStatusCell, arziTypeCell).setText("Arzi");

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
		searchResultsHeader.join(bodyPartCell, condCell, procCell, otherCondCell,
				condStartDtCell).setText("Medical Information");
		
		// Join the Patient info cells
		HeaderCell itsNumCell = searchResultsHeader.getCell("patient.itsNumber");
		HeaderCell firstNameCell = searchResultsHeader.getCell("patient.firstName");
		HeaderCell lastNameCell = searchResultsHeader.getCell("patient.lastName");
		searchResultsHeader.join(itsNumCell, firstNameCell, lastNameCell).setText(
				"Patient");	
		
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

					// this would default to now
					Calendar startDate = Calendar.getInstance();
					Calendar endDate = Calendar.getInstance();

					// Subtract the number of days to get the range between
					// current date and number of days selected as the criteria.
					if (searchPeriod.getLookupId().equals(MedicalArziConstants.SEARCH_PERIOD_LAST_3_DAYS_ID)) {

						startDate.add(Calendar.DAY_OF_MONTH, -3);

					} else if (searchPeriod.getLookupId().equals(MedicalArziConstants.SEARCH_PERIOD_3_TO_7_DAYS_ID)) {

						startDate.add(Calendar.DAY_OF_MONTH, -7);

						endDate.add(Calendar.DAY_OF_MONTH, -3);

					} else if (searchPeriod.getLookupId().equals(MedicalArziConstants.SEARCH_PERIOD_7_TO_15_DAYS_ID)) {

						startDate.add(Calendar.DAY_OF_MONTH, -15);

						endDate.add(Calendar.DAY_OF_MONTH, -7);

					} else if (searchPeriod.getLookupId().equals(MedicalArziConstants.SEARCH_PERIOD_15_TO_30_DAYS_ID)) {

						startDate.add(Calendar.DAY_OF_MONTH, -30);

						endDate.add(Calendar.DAY_OF_MONTH, -15);

					} else if (searchPeriod.getLookupId()
							.equals(MedicalArziConstants.SEARCH_PERIOD_30_DAYS_OR_OLDER_ID)) {

						startDate.add(Calendar.DAY_OF_MONTH, -30);
					}
					
					// Start Date
					GregHijDate searchStartDate = ServiceLocator.getInstance().getLookupService()
							.getRequestedGregorianHijriCalendar(startDate.getTime());

					userEnteredSearchCriteria.setStartDate(searchStartDate);					
					
					// End Date
					GregHijDate searchEndDate = ServiceLocator.getInstance().getLookupService()
							.getRequestedGregorianHijriCalendar(endDate.getTime());

					userEnteredSearchCriteria.setEndDate(searchEndDate);					
					
				} else {
					// Reset the criteria
					userEnteredSearchCriteria.setStartDate(null);
					userEnteredSearchCriteria.setEndDate(null);
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
				// Add data to the container
				resultsContainer.addAll(searchResultList);

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

}
