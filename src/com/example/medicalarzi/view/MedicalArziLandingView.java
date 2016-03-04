package com.example.medicalarzi.view;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.example.medicalarzi.component.AdminComponent;
import com.example.medicalarzi.component.ArziFooterComponent;
import com.example.medicalarzi.component.ArziFormComponent;
import com.example.medicalarzi.component.ArziHeaderComponent;
import com.example.medicalarzi.component.InboxComponent;
import com.example.medicalarzi.component.PendingTasksComponent;
import com.example.medicalarzi.component.SearchComponent;
import com.example.medicalarzi.model.BodyPart;
import com.example.medicalarzi.model.Condition;
import com.example.medicalarzi.model.Patient;
import com.example.medicalarzi.model.Procedure;
import com.example.medicalarzi.model.SecurityRole;
import com.example.medicalarzi.service.LookupService;
import com.example.medicalarzi.service.PatientService;
import com.example.medicalarzi.util.MedicalArziConstants;
import com.example.medicalarzi.util.MedicalArziUtils;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author Mustafa Kanchwala
 *
 */
@Theme("medicalarzi")
@UIScope
@SpringView(name = MedicalArziLandingView.NAME)
public class MedicalArziLandingView extends CustomComponent implements View,
		SelectedTabChangeListener {

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

	// ArziForm Component
	private ArziFormComponent newArziComponent;

	// Inbox Component
	private InboxComponent inboxComponent;
	
	// Pending Tasks Component
	private PendingTasksComponent pendingTasksComponent;

	// Search Component
	private SearchComponent searchComponent;

	// Admin Component
	private AdminComponent adminComponent;

	// Footer
	private ArziFooterComponent footer;

	private Patient patient;

	private Boolean refreshInbox = false;
	
	private Boolean refreshPendingTasks = false;

	/**
	 * Service stubs - Could use the @Autowire for the service stubs or get it
	 * from the ServiceLocator. @SpringView autowires the spring service beans,
	 * use the ServiceLocator when trying to use the beans from outside the
	 * 
	 * @SpringView.
	 **/
	@Autowired
	@Qualifier("service.PatientService")
	private PatientService patientService;

	@Autowired
	@Qualifier("service.LookupService")
	private LookupService lookupService;

	// get Tabsheet component
	public TabSheet getTabSheet() {
		return tabSheet;
	}

	// get Inbox component
	public InboxComponent getInboxComponent() {
		return inboxComponent;
	}
		
	// get PendingTasks component
	public PendingTasksComponent getPendingTasksComponent() {
		return pendingTasksComponent;
	}

	public Boolean isRefreshInbox() {
		return refreshInbox;
	}

	public void setRefreshInbox(Boolean refreshInbox) {
		this.refreshInbox = refreshInbox;
	}

	public Boolean isRefreshPendingTasks() {
		return refreshPendingTasks;
	}

	public void setRefreshPendingTasks(Boolean refreshPendingTasks) {
		this.refreshPendingTasks = refreshPendingTasks;
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

		newArziComponent.getItsNumber().setValue(
				String.valueOf(patient.getItsNumber()));

		// Select the default condition as Other
		setDefaultCondition();
		// Select the default procedure as Other
		setDefaultProcedure();
		// Select the default body part as Other
		setDefaultBodyPart();
		

		makeFieldsReadOnlyForNewArzi(true);

		String patientName = MedicalArziUtils.constructPtntFullName(patient);

		logger.debug("Patient \"" + patientName
				+ " has logged in successfully.");

		// Set the full name of the logged in patient/user
		Label loggedInName = (Label) MedicalArziUtils.findById(header,
				MedicalArziConstants.HEADER_LOGGED_IN_PTNT_NAME);

		loggedInName.setValue(patientName);

		Notification.show("Welcome to the Medical Arzi View!!!");
	}

	/**
	 * This method is responsible for iterating through the list of 'Conditions'
	 * and setting the 'Other' as the default condition.
	 */
	@SuppressWarnings("unchecked")
	private void setDefaultCondition() {
		List<Condition> listOfConditions = (List<Condition>) newArziComponent
				.getCondition().getItemIds();
		for (Iterator<Condition> iterator = listOfConditions.iterator(); iterator
				.hasNext();) {
			Condition condition = (Condition) iterator.next();
			if (condition.getConditionId().equals(MedicalArziConstants.CONDITION_OTHER_ID)) {
				newArziComponent.getCondition().select(condition);
				break;
			}
		}
	}
	
	/**
	 * This method is responsible for iterating through the list of 'Procedure'
	 * and setting the 'Other' as the default procedure.
	 */
	@SuppressWarnings("unchecked")
	private void setDefaultProcedure() {
		List<Procedure> listOfProcedures = (List<Procedure>) newArziComponent
				.getProcedure().getItemIds();
		for (Iterator<Procedure> iterator = listOfProcedures.iterator(); iterator
				.hasNext();) {
			Procedure procedure = (Procedure) iterator.next();
			if (procedure.getProcedureId().equals(MedicalArziConstants.PROCEDURE_OTHER_ID)) {
				newArziComponent.getProcedure().select(procedure);
				break;
			}
		}
	}

	/**
	 * This method is responsible for iterating through the list of 'BodyParts'
	 * and setting the 'Other' as the default body part.
	 */
	@SuppressWarnings("unchecked")
	private void setDefaultBodyPart() {
		List<BodyPart> listOfBodyParts = (List<BodyPart>) newArziComponent
				.getBodyPart().getItemIds();
		for (Iterator<BodyPart> iterator = listOfBodyParts.iterator(); iterator
				.hasNext();) {
			BodyPart bodyPart = (BodyPart) iterator.next();
			if (bodyPart.getBodyPartId().equals(MedicalArziConstants.BODY_PART_OTHER_ID)) {
				newArziComponent.getBodyPart().select(bodyPart);
				break;
			}
		}
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
		tabSheet.setId(MedicalArziConstants.ARZI_LANDING_VIEW_TABSHEET);
		tabSheet.addStyleName("borderless");
		tabSheet.addStyleName("mapTabsheet");
		tabSheet.addSelectedTabChangeListener(this);
		tabSheet.setSizeFull();

		// viewLayout
		newArziComponent = new ArziFormComponent();
		newArziComponent.setId(MedicalArziConstants.NEW_ARZI_TAB_COMPONENT_ID);
		tabSheet.addTab(newArziComponent,
				MedicalArziConstants.NEW_ARZI_TAB_CAPTION, new ThemeResource(
						"icons/newArzi.png"));

		// gridLayout
		inboxComponent = new InboxComponent();
		inboxComponent.setId(MedicalArziConstants.INBOX_TAB_COMPONENT_ID);
		tabSheet.addTab(inboxComponent, MedicalArziConstants.INBOX_TAB_CAPTION,
				new ThemeResource("icons/inbox.png"));

		// Search tab - only available to Admins & Doctors
		SecurityRole adminRole = lookupService
				.getSecurityRoleById(MedicalArziConstants.SEC_ROLE_ID_ADMIN);

		SecurityRole doctorRole = lookupService
				.getSecurityRoleById(MedicalArziConstants.SEC_ROLE_ID_DOCTOR);

		if (patient.getRoles().contains(adminRole)
				|| patient.getRoles().contains(doctorRole)) {
			
			pendingTasksComponent = new PendingTasksComponent();
			pendingTasksComponent.setId(MedicalArziConstants.PENDING_TASKS_TAB_COMPONENT_ID);
			tabSheet.addTab(pendingTasksComponent,
					MedicalArziConstants.PENDING_TASKS_TAB_CAPTION, new ThemeResource(
							"icons/pendingTasks.png"));
			
			
			searchComponent = new SearchComponent();
			tabSheet.addTab(searchComponent,
					MedicalArziConstants.SEARCH_TAB_CAPTION, new ThemeResource(
							"icons/search.png"));
		}

		// Admin Tab - only available for Admins
		if (patient.getRoles().contains(adminRole)) {
			adminComponent = new AdminComponent();
			adminComponent.setId(MedicalArziConstants.ADMIN_TAB_COMPONENT_ID);
			tabSheet.addTab(adminComponent,
					MedicalArziConstants.ADMIN_TAB_CAPTION, new ThemeResource(
							"icons/administration.png"));
		}
	}

	/**
	 * This method just sets the fields which cannot be allowed to be edited by
	 * the users as read only. This method can add or remove the readOnly fields
	 * based on the requirements decision.
	 * 
	 * @param boolean
	 */
	private void makeFieldsReadOnlyForNewArzi(boolean flag) {
		newArziComponent.getItsNumber().setReadOnly(true);

		newArziComponent.getGender().setReadOnly(true);
		
		newArziComponent.getDob().setReadOnly(true);
	}

	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		TabSheet tabSheetComponent = event.getTabSheet();

		// Get the selected tab
		Component selectedTab = tabSheetComponent.getSelectedTab();

		// Get the componentId which is set when the view is built
		String componentId = selectedTab.getId();

		if (StringUtils.isNotEmpty(componentId)) {
			// If the selected tab is Inbox, refetch all the arzis for the user
			// from the dbase.
			if (inboxComponent != null
					&& componentId.equals(inboxComponent.getId())
					&& isRefreshInbox()) {

				logger.debug("Refresh the Inbox tab with the latest content.");

				// We don't have to refresh the inbox every time we switch tab.
				// Inbox has to be refresh with the latest content only after
				// the patient submits a new arzi.
				setRefreshInbox(false);

				inboxComponent.refreshGridWithFreshData();

			} else if (pendingTasksComponent != null
					&& componentId.equals(pendingTasksComponent.getId())) {

				logger.debug("Refresh the Inbox tab with the latest content.");

				// We don't have to refresh the Pending Tasks tab every time we
				// switch tab. Pending Tasks tab has to be refresh with the
				// latest content only after the reviewer assigns certain tasks
				// to himself/herself.
				//setRefreshPendingTasks(false);

				pendingTasksComponent.refreshGridWithFreshData();
			}
		}
	}

}
