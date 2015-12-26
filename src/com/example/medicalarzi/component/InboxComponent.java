/**
 * 
 */
package com.example.medicalarzi.component;

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
import com.example.medicalarzi.model.Patient;
import com.example.medicalarzi.model.Procedure;
import com.example.medicalarzi.service.LookupService;
import com.example.medicalarzi.service.PatientService;
import com.example.medicalarzi.service.ServiceLocator;
import com.example.medicalarzi.util.MedicalArziConstants;
import com.example.medicalarzi.util.MedicalArziUtils;
import com.vaadin.data.Container;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.DetailsGenerator;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.Grid.RowReference;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author mkanchwa
 *
 */
public class InboxComponent extends CustomComponent implements ItemClickListener, SelectionListener {

	private static final long serialVersionUID = 2521689363206700694L;
	
	public static Logger logger = LogManager.getLogger(InboxComponent.class);
	
	// Inbox
	private VerticalLayout inboxViewLayout;

	private Grid arziGrid;
	
	private Patient patient;

	/**
	 * 
	 */
	public InboxComponent() {
		buildInboxViewLayout();
		setCompositionRoot(inboxViewLayout);
	}

	/**
	 * @param compositionRoot
	 */
	public InboxComponent(Component compositionRoot) {
		
	}
	
	
	private LookupService getLookupService() {
		return ServiceLocator.getInstance().getLookupService();
	}
	
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
		inboxViewLayout.setId(MedicalArziConstants.INBOX_TAB_COMPONENT_ID);
		inboxViewLayout.setMargin(true);
		inboxViewLayout.setSpacing(true);	

		Label inboxDescription = new Label();
		inboxDescription.setValue("My Arzis: (Double click to edit)");
		inboxDescription.setStyleName("v-captiontext");
		inboxViewLayout.addComponent(inboxDescription);
		inboxViewLayout.setExpandRatio(inboxDescription, 0.1f);

		buildArziGrid();
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
		arziGrid.setImmediate(false);

		// Sets the grid editor to be enabled
		arziGrid.setEditorEnabled(true);
		// Setting the field group
		arziGrid.setEditorFieldGroup(getFieldGroup());
		
		// Initialize the container
		BeanItemContainer<Arzi> arziContainer = (BeanItemContainer<Arzi>) MedicalArziUtils
				.getContainer(Arzi.class);
		
		// Retrieve all the arzis for the patient based on the ITS number
		// Get the patient's full name from the session
		patient = (Patient) VaadinSession.getCurrent().getAttribute(
				MedicalArziConstants.SESS_ATTR_PTNT_INFO);
		List<Arzi> arziList = getPatientService().retrieveAllArzisForPatient(patient
				.getItsNumber());

		/**
		 * Update the Grid with fresh data. Two step process of replacing bean
		 * items. (1) First remove all BeanItem objects with
		 * Container::removeAllItems method. (2) Then add replacement BeanItem
		 * objects with the BeanItemContainer::addAll method.
		 */
		arziContainer.removeAllItems();
		// Add data to the container
		arziContainer.addAll(arziList);
		// Set the container as the grid datasource
		arziGrid.setContainerDataSource(arziContainer);

		// Add nested properties to the header
		arziContainer.addNestedContainerBean("currentStatus");
		arziContainer.addNestedContainerBean("conditionStartDate");

		arziGrid.setColumns("itsNumber", "currentStatus.statusDesc",
				"arziType", "condition", "otherCondition",
				"conditionStartDate.gregorianCalDate", "procedure", "bodyPart");
		
		/*arziGrid.getColumn("edit")
				.setWidth(35)
				.setHeaderCaption("Tools")
				.setRenderer(
						new ButtonRenderer(
								new ClickableRenderer.RendererClickListener() {
									private static final long serialVersionUID = 8902672690745778342L;

									@Override
									public void click(RendererClickEvent e) {
										Notification.show("Editing item "
												+ e.getItemId());
									}
								}));*/
		
		// Column reordering allowed
		arziGrid.setColumnReorderingAllowed(true);
		
		//Customize the columns
		customizeArziGridColumns();
		
		customizeEditDetails();
		
		//Add the event listner on the grid on selecting a row.
		//arziGrid.addSelectionListener(this);
		arziGrid.addItemClickListener(this);
		
		// Set this so that the default edit behavior of the grid (supports double click event) does not
		// interfere with the customized version.
		//arziGrid.setEditorEnabled(false);
		
		// Adding the commit handler to the field group
		MedicalArziCommitHandler maCommitHdlr = new MedicalArziCommitHandler();
		
		arziGrid.getEditorFieldGroup().addCommitHandler(maCommitHdlr);

		inboxViewLayout.addComponent(arziGrid);
	}
	

	/**
	 * This method is responsible for customizing all the grid columns like
	 * setting the caption, setting the editor fields and grouping the similar
	 * columns together etc.
	 * 
	 */
	private void customizeArziGridColumns() {
		// Sets the converter on the ITS number to remove the grouping used.
		arziGrid.getColumn("itsNumber")
				.setConverter(MedicalArziUtils.itsNumberConverter())
				.setEditable(false);
		
		arziGrid.getColumn("currentStatus.statusDesc")
				.setHeaderCaption("Arzi Status").setEditable(false);
		
		arziGrid.getColumn("conditionStartDate.gregorianCalDate")
				.setHeaderCaption("Condition Start Date")
				.setEditorField(new ArziDateField())
				.setRenderer(
						new DateRenderer("%1$tB %1$td, %1$tY", Locale.ENGLISH));
		
		//Setting the other condition values in the textfield editor on the grid for editing.
		arziGrid.getColumn("otherCondition").setEditorField(
				MedicalArziUtils.getTextFieldEditor(true));
		
		Container condContainer = MedicalArziUtils
				.getContainer(Condition.class);
		StringToConditionConverter condConv = new StringToConditionConverter(
				condContainer);
		arziGrid.getColumn("condition")
				.setConverter(condConv)
				.setEditorField(
						MedicalArziUtils.getComboBox("",
								"Please select a condition...",
								"Condition is required!",
								getLookupService().getListOfAllMedicalConditions(),
								"conditionName", condContainer, null));
		//Add the valueChange listener to the Condition combobox
		//arziGrid.getColumn("condition").getEditorField().addValueChangeListener(this);
		
		//Setting the arziType values in the combobox editor on the grid for editing.
		Container arziTypeContainer = MedicalArziUtils
				.getContainer(ArziType.class);
		StringToArziTypeConverter arziTypeConv = new StringToArziTypeConverter(
				arziTypeContainer);
		arziGrid.getColumn("arziType")
				.setConverter(arziTypeConv)
				.setEditorField(
						MedicalArziUtils.getComboBox("",
								"Please select a type...", "Type is required!",
								getLookupService().getListOfAllArziTypes(),
								"arziTypeName", arziTypeContainer, null));
		
		Container procContainer = MedicalArziUtils
				.getContainer(Procedure.class);
		StringToProcedureConverter procConv = new StringToProcedureConverter(
				procContainer);
		arziGrid.getColumn("procedure")
				.setConverter(procConv)
				.setEditorField(
						MedicalArziUtils.getComboBox("",
								"Please select a procedure...",
								"Procedure is required!",
								getLookupService().getListOfAllMedicalProcedures(),
								"procedureName", procContainer, null));

		Container bdyPrtContainer = MedicalArziUtils
				.getContainer(BodyPart.class);
		StringToBodyPartConverter bdyPrtConv = new StringToBodyPartConverter(
				bdyPrtContainer);
		arziGrid.getColumn("bodyPart")
				.setConverter(bdyPrtConv)
				.setEditorField(
						MedicalArziUtils.getComboBox("", "Please select a body part...",
								"Body Part is required!",
								getLookupService().getListOfAllBodyParts(),
								"bodyPartName", bdyPrtContainer, null));
		
		// This call prepends the header row to the existing grid
		HeaderRow ptntMedicalInfoHeader = arziGrid.prependHeaderRow();
		// Get hold of the columnID, mind you in my case this is a nestedID
		HeaderCell arziType = ptntMedicalInfoHeader.getCell("arziType");
		HeaderCell condition = ptntMedicalInfoHeader.getCell("condition");
		HeaderCell otherCondition = ptntMedicalInfoHeader.getCell("otherCondition");
		HeaderCell conditionStartDt = ptntMedicalInfoHeader
				.getCell("conditionStartDate.gregorianCalDate");
		HeaderCell procedure = ptntMedicalInfoHeader.getCell("procedure");
		HeaderCell bodyPart = ptntMedicalInfoHeader.getCell("bodyPart");

		// Now join all of these cells to form a logical block
		HeaderCell ptntMedicalInfoHeaderCell = ptntMedicalInfoHeader.join(
				arziType, condition, otherCondition, conditionStartDt,
				procedure, bodyPart);

		ptntMedicalInfoHeaderCell.setText("Medical Information");

	}
	
	@SuppressWarnings("unused")
	private void customizeEditDetails() {
		arziGrid.setDetailsGenerator(new DetailsGenerator() {
			
			private static final long serialVersionUID = -4523748503772126313L;

			@Override
			public Component getDetails(RowReference rowReference) {
				Arzi arzi = (Arzi) rowReference.getItemId();
				
				Button submitBtn_grid = new Button("Submit");
				//submitBtn_grid.setStyleName(Reindeer.BUTTON_LINK);
				
				Button deleteBtn_grid = new Button("Delete");
				deleteBtn_grid.setStyleName(Reindeer.BUTTON_LINK);
				
				HorizontalLayout hLayout = new HorizontalLayout(submitBtn_grid, deleteBtn_grid);
				hLayout.setMargin(true);
				hLayout.setSpacing(true);
				
				return hLayout;
			}
		});
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
	 * the grid items (row). Override this when the ItemClickListener is added
	 * to the grid
	 * 
	 * @param event
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		Arzi selectedArzi = (Arzi) event.getItemId();

		if (selectedArzi.getCurrentStatus().getStatusId().intValue() == MedicalArziConstants.ARZI_SUBMITTED_STATUS
				.intValue()) {
			arziGrid.setEditorEnabled(false);
		} else {
			arziGrid.setEditorEnabled(true);
		}
		
		logger.debug("The patient with ITS number:-> " + patient.getItsNumber()
				+ " selected arzi : " + selectedArzi.getArziId() + " for editing");

		// Toggle the visibility of the details
		/*
		 * arziGrid.setDetailsVisible(selectedArzi,
		 * !arziGrid.isDetailsVisible(selectedArzi));
		 */

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
			// The row should not be editable if the arzi is in the 'Submitted' status
			if (selectedArzi.getCurrentStatus().getStatusId().intValue() == MedicalArziConstants.ARZI_SUBMITTED_STATUS
					.intValue()) {
				arziGrid.setEditorEnabled(false);
			} else {
				arziGrid.setEditorEnabled(true);
			}
			
			//The other condition field should be editable if the 'Other' is selected as the condition.
			/*if (selectedArzi.getCondition().getConditionId().intValue() == MedicalArziConstants.MAP_OTHER_CONDITION_ID
					.intValue()) {
				arziGrid.getColumn("otherCondition").setEditable(true);
			} else {
				arziGrid.getColumn("otherCondition").setEditable(false);
			}*/
		}

		logger.debug("The patient with ITS number:-> " + patient.getItsNumber()
				+ " selected arzi : " + selectedArzi + " for editing");
	}	

}
