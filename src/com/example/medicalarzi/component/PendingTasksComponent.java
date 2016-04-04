/**
 * 
 */
package com.example.medicalarzi.component;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.example.medicalarzi.model.Arzi;
import com.example.medicalarzi.model.ArziSearchResult;
import com.example.medicalarzi.model.GregHijDate;
import com.example.medicalarzi.model.Patient;
import com.example.medicalarzi.model.Status;
import com.example.medicalarzi.service.ReviewerService;
import com.example.medicalarzi.service.ServiceLocator;
import com.example.medicalarzi.util.MedicalArziConstants;
import com.example.medicalarzi.util.MedicalArziUtils;
import com.example.medicalarzi.view.MedicalArziLandingView;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
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
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Mkanchwa
 *
 */
public class PendingTasksComponent extends CustomComponent implements
		SelectionListener, ClickListener, ItemClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3728488161400839820L;

	// Pending Tasks
	private VerticalLayout viewLayout;

	private Grid pendingTasksGrid;
	
	private Button withdrawBtn;

	// Grid container
	private BeanItemContainer<ArziSearchResult> pendingTasksContainer;
	
	private final Set<BeanItem<ArziSearchResult>> editedItems = new HashSet<BeanItem<ArziSearchResult>>();
	
	private Patient patient;

	/**
	 * 
	 */
	public PendingTasksComponent() {
		// Build the view layout and set it as the composition root.
		buildViewLayout();

		setCompositionRoot(viewLayout);
	}

	/**
	 * @param compositionRoot
	 */
	public PendingTasksComponent(Component compositionRoot) {
		super(compositionRoot);
	}

	private void buildViewLayout() {
		// top-level component properties
		setSizeFull();

		// viewLayout
		viewLayout = new VerticalLayout();
		viewLayout
				.setId(MedicalArziConstants.PENDING_TASKS_COMPONENT_VIEW_LAYOUT_ID);
		viewLayout.setMargin(true);
		viewLayout.setSpacing(true);

		Label pendingTaskDesc = new Label();
		pendingTaskDesc.setValue("My Pending Tasks: (Double Click to Edit)");
		pendingTaskDesc.setStyleName("v-captiontext");
		viewLayout.addComponent(pendingTaskDesc);
		viewLayout.setExpandRatio(pendingTaskDesc, 0.1f);

		buildPendingTasksGrid();
		viewLayout.addComponent(pendingTasksGrid);
		
		withdrawBtn = new Button(new ThemeResource("img/withdraw.png"));
		withdrawBtn.addClickListener(this);
		withdrawBtn.setStyleName(Reindeer.BUTTON_LINK);
		viewLayout.addComponent(withdrawBtn);
		viewLayout.setComponentAlignment(withdrawBtn, Alignment.MIDDLE_CENTER);
	}

	@SuppressWarnings("unchecked")
	private void buildPendingTasksGrid() {

		pendingTasksGrid = new Grid();
		pendingTasksGrid.setSizeFull();
		pendingTasksGrid.setStyleName("wrapLine");
		pendingTasksGrid.setImmediate(false);

		// Set the selection mode.
		pendingTasksGrid.setSelectionMode(SelectionMode.MULTI);

		// Initialize the container
		pendingTasksContainer = (BeanItemContainer<ArziSearchResult>) MedicalArziUtils
				.getContainer(ArziSearchResult.class);

		// Set the container as the grid datasource
		pendingTasksGrid.setContainerDataSource(pendingTasksContainer);

		// Add nested properties to the header
		pendingTasksContainer.addNestedContainerBean("patient");
		pendingTasksContainer.addNestedContainerBean("arzi");

		// Add all the intermediate nested properties (which are also
		// beans)
		pendingTasksContainer.addNestedContainerBean("arzi.requestSubmitDate");
		pendingTasksContainer.addNestedContainerBean("arzi.currentStatus");
		pendingTasksContainer.addNestedContainerBean("arzi.statusChangeDate");
		pendingTasksContainer.addNestedContainerBean("arzi.condition");
		pendingTasksContainer.addNestedContainerBean("arzi.procedure");
		pendingTasksContainer.addNestedContainerBean("arzi.bodyPart");

		// Setting the grid columns
		pendingTasksGrid.setColumns("arzi.requestSubmitDate.gregorianCalDate", "arzi.arziId",
				"arzi.currentStatus.statusDesc", "arzi.statusChangeDate.gregorianCalDate",
				"arzi.condition.conditionName", "arzi.procedure.procedureName", "arzi.bodyPart.bodyPartName",
				"arzi.arziSummary");

		pendingTasksGrid.addSelectionListener(this);
		pendingTasksGrid.addItemClickListener(this);

		// Customize the grid columns
		customizeResultsGridColumns();

		// Get the patient information from the session. In this tab, the
		// patient also has the reviewer role so that he/she is able to review
		// all the submitted arzis and take appropriate action.
		patient = (Patient) MedicalArziUtils.getSessionAttribute(MedicalArziConstants.SESS_ATTR_PTNT_INFO);

		// Populate the grid with data
		refreshGridWithFreshData();
	}

	/**
	 * This method is responsible for customizing all the grid columns like
	 * setting the caption, setting the editor fields and grouping the similar
	 * columns together etc.
	 * 
	 */
	private void customizeResultsGridColumns() {
		// Set Header captions
		pendingTasksGrid.getColumn("arzi.currentStatus.statusDesc").setHeaderCaption("Status");

		pendingTasksGrid.getColumn("arzi.arziId").setHeaderCaption("Arzi ID").setMaximumWidth(70);

		pendingTasksGrid.getColumn("arzi.requestSubmitDate.gregorianCalDate").setMaximumWidth(130)
				.setHeaderCaption("Arzi Submit Date").setRenderer(new DateRenderer("%1$tb %1$td, %1$tY", Locale.ENGLISH));

		pendingTasksGrid.getColumn("arzi.statusChangeDate.gregorianCalDate").setMaximumWidth(130)
				.setHeaderCaption("Status Date").setRenderer(new DateRenderer("%1$tb %1$td, %1$tY", Locale.ENGLISH));

		pendingTasksGrid.getColumn("arzi.condition.conditionName").setHeaderCaption("Medical Condition")
				.setMaximumWidth(270);

		pendingTasksGrid.getColumn("arzi.procedure.procedureName").setHeaderCaption("Medical Procedure")
				.setMaximumWidth(290);

		pendingTasksGrid.getColumn("arzi.bodyPart.bodyPartName").setHeaderCaption("Body Part").setMaximumWidth(270);
		
		pendingTasksGrid.getColumn("arzi.arziSummary").setHeaderCaption("Arzi Summary");

		//groupResultsGridColumns();
	}

	/**
	 * This method is responsible for grouping the related results grid columns.
	 */
	private void groupResultsGridColumns() {
		// This call prepends the header row to the existing grid
		HeaderRow searchResultsHeader = pendingTasksGrid.prependHeaderRow();

		// Get hold of the columnID, mind you in my case this is a nestedID
		// Join the arzi related cells
		HeaderCell arziIdCell = searchResultsHeader.getCell("arzi.arziId");
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
	 * This method is responsible for fetching the arzis assigned to the
	 * reviewer from the database and updating the datasource container for the
	 * arzi grid with the latest set of data.
	 * 
	 */
	public void refreshGridWithFreshData() {
		ReviewerService reviewerService = ServiceLocator.getInstance()
				.getReviewerService();

		// The patient is also the reviewer.
		Long reviewerItsNumber = patient.getItsNumber();

		List<ArziSearchResult> pendingTaskList = reviewerService
				.retrieveArzisAssignedToReviewer(reviewerItsNumber);

		pendingTasksContainer.removeAllItems();

		if (pendingTaskList != null && pendingTaskList.isEmpty()) {
			
			Notification.show("You do not have any pending tasks!!!");
		} else {
			
			pendingTasksContainer.addAll(pendingTaskList);
			
			// "Withdraw" button should be disabled by default and
			// should only be enabled when the user starts selecting
			// arzis for withdraw.
			if(withdrawBtn != null) {
				withdrawBtn.setEnabled(false);
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

		ArziFormComponent reviewArziComponent = new ArziFormComponent(ptntInfo,
				arziInfo);

		reviewArziComponent.getItsNumber().setValue(
				String.valueOf(ptntInfo.getItsNumber()));

		makeFieldsReadOnlyWhenReviewing(reviewArziComponent);

		// Remove the default buttons layout (which contains "Save Draft" and
		// "Submit Arzi" buttons from the ArziFormComponent.
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

		reviewArziComponent.getGender().setReadOnly(true);

		reviewArziComponent.getDob().setReadOnly(true);

		reviewArziComponent.getAddressLn1().setReadOnly(true);

		reviewArziComponent.getAddressLn2().setReadOnly(true);

		reviewArziComponent.getZip().setReadOnly(true);

		reviewArziComponent.getJamaat().setReadOnly(true);

		// Patient's arzi information set to read only.
		reviewArziComponent.getArziType().setReadOnly(true);

		reviewArziComponent.getBodyPart().setReadOnly(true);

		reviewArziComponent.getProcedure().setReadOnly(true);

		reviewArziComponent.getCondition().setReadOnly(true);

		reviewArziComponent.getConditionStartDate().setReadOnly(true);

		if (reviewArziComponent.getOtherCondition() != null)
			reviewArziComponent.getOtherCondition().setReadOnly(true);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		
		if (event.getButton().equals(withdrawBtn)) {

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
						.setStatusId(MedicalArziConstants.ARZI_SUBMITTED_STATUS);
				arzi.setCurrentStatus(arziStatus);
				arzi.setStatus(arziStatus);

				GregHijDate currentDate = ServiceLocator.getInstance()
						.getLookupService()
						.getRequestedGregorianHijriCalendar(new Date());

				arzi.setCurrentStatusDate(currentDate);
				// For Arzi to create a new record in the detail table.
				arzi.setStatusChangeDate(currentDate);

				arzi.setReviewDate(currentDate);

				// The patient is also the reviewer.
				Long reviewerItsNumber = patient.getItsNumber();

				arzi.setReviewerItsNumber(reviewerItsNumber);
				arzi.setUpdatedBy(String.valueOf(reviewerItsNumber));

				reviewSer.assignArziForReview(arzi);
			}
			
			refreshGridWithFreshData();
			
			String successMsg = "\""
					+ MedicalArziUtils.constructPtntFullName(patient)
					+ "\" has withdrawn arzis assigned for his/her review.";

			MedicalArziUtils.createAndShowNotification(null,
					successMsg, Type.HUMANIZED_MESSAGE,
					Position.TOP_LEFT, "userFriendlyMsg",
					MedicalArziConstants.USER_FRIENDLY_MSG_DELAY_MSEC);			
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
        
        withdrawBtn.setEnabled(
                pendingTasksGrid.getSelectedRows().size() > 0);
	}	

	@Override
	public void itemClick(ItemClickEvent event) {
		// Open the arzi for review on double click event.
		if (event.isDoubleClick()) {

			ArziSearchResult selectedResult = (ArziSearchResult) event.getItemId();

			if (selectedResult != null) {
				// Get the current view and set the selected tab to Inbox to
				// display the newly saved/submitted arzi in the Inbox.
				View currentView = UI.getCurrent().getNavigator()
						.getCurrentView();

				if (currentView instanceof MedicalArziLandingView) {
					MedicalArziLandingView landingView = (MedicalArziLandingView) currentView;

					ArziFormComponent reviewArziComponent = populateTheFormWithSelectedResult(selectedResult);

					String reviewArziCaption = MedicalArziConstants.REVIEW_ARZI_TAB_CAPTION
							+ " # " + selectedResult.getArzi().getArziId();

					String reviewArziTabComponentId = MedicalArziConstants.REVIEW_ARZI_TAB_COMPONENT_ID
							+ "_" + selectedResult.getArzi().getArziId();

					// Find the component based on the id inside the tab for the
					// arzi which is being reviewed.
					Component reviewArziTabComponent = MedicalArziUtils
							.findById(landingView.getTabSheet(),
									reviewArziTabComponentId);

					// If the tab for the arzi id to be reviewed is not added,
					// add the tab for that arzi. Else just take
					// the user to the requested review arzi tab.
					Tab reviewArziTab = null;
					if (reviewArziTabComponent == null) {

						// Set the unique id to track if the arzi to be reviewed is already open.
						reviewArziComponent.setId(reviewArziTabComponentId);
						
						// Add the reviewer section in the main(viewLayout)
						// layout where the reviewer enters their comments for
						// the arzi
						Layout mainLayoutForReviewArzi = (VerticalLayout) MedicalArziUtils
								.findById(
										reviewArziComponent,
										MedicalArziConstants.ARZI_FORM_COMPONENT_VIEW_LAYOUT_ID);
						
						ReviewArziComponent reviewerSection = new ReviewArziComponent(
								mainLayoutForReviewArzi,
								selectedResult.getArzi());
						
						reviewerSection
								.setId(MedicalArziConstants.REVIEW_ARZI_COMPONENT_ID);

						// Add the new tab for the arzi to be reviewed.
						reviewArziTab = landingView.getTabSheet().addTab(
								reviewArziComponent, reviewArziCaption);

						reviewArziTab.setClosable(true);

						landingView.getTabSheet().setSelectedTab(reviewArziTab);

					} else {
						// Get the tab for the component and select it.
						reviewArziTab = landingView.getTabSheet().getTab(
								reviewArziTabComponent);

						landingView.getTabSheet().setSelectedTab(reviewArziTab);
					}
				}
			}
		}

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
			items.add((BeanItem<ArziSearchResult>) pendingTasksGrid
					.getContainerDataSource().getItem(id));
		}

		return items;
	}	

}
