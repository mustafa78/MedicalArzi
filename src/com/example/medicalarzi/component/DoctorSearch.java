package com.example.medicalarzi.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.medicalarzi.model.Arzi;
import com.example.medicalarzi.model.ArziType;
import com.example.medicalarzi.model.BodyPart;
import com.example.medicalarzi.model.Condition;
import com.example.medicalarzi.model.Jamaat;
import com.example.medicalarzi.model.Procedure;
import com.example.medicalarzi.service.ServiceLocator;
import com.example.medicalarzi.util.MedicalArziConstants;
import com.example.medicalarzi.util.MedicalArziUtils;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

public class DoctorSearch extends CustomComponent {

	/**
	 *
	 */
	private static final long serialVersionUID = -2608386102633068228L;
	
	public static Logger logger = LogManager.getLogger(DoctorSearch.class);
	
	private VerticalLayout mainLayout;

	private VerticalSplitPanel splitPanel;

	private Grid arziGrid;

	private VerticalLayout topLayout;

	private Button search;

	private HorizontalLayout criteriaLayout;
	
	private VerticalLayout leftLayout;
	
	private FormLayout leftFormLayout;
	
	private VerticalLayout rightLayout;

	private FormLayout rightFormLayout;

	
	private ComboBox arziType;
	
	private ComboBox bodyPart;

	private ComboBox procedure;

	private ComboBox condition;

	private ComboBox jamaat;
	
	private BeanItemContainer<Arzi> myArziContainer = createMyArziDummyDatasource();
	

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 */
	public DoctorSearch() {
		//Build the mainLoayout
		buildMainLayout();
		
		//initArziList();
		
		setCompositionRoot(mainLayout);
	}

	private void buildMainLayout() {
		// top-level component properties
		setSizeFull();

		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setSizeFull();

		// splitPanel
		buildSplitPanel();
		mainLayout.addComponent(splitPanel);
	}

	private void buildSplitPanel() {
		// common part: create layout
		splitPanel = new VerticalSplitPanel();
		splitPanel.setImmediate(false);
		splitPanel.setLocked(true);
		splitPanel.setHeight("600px");
		splitPanel.setWidth("100%");		

		// topLayout
		buildTopLayout();
		topLayout.setSizeFull();
		splitPanel.addComponent(topLayout);
		
		// arziTable
		arziGrid = new Grid();
		arziGrid.setImmediate(false);
		arziGrid.setSizeFull();
		splitPanel.addComponent(arziGrid);
	}

	private void buildTopLayout() {
		// common part: create layout
		topLayout = new VerticalLayout();
		topLayout.setSizeFull();
		topLayout.setImmediate(false);
		topLayout.setMargin(false);

		// criteriaLayout
		buildCriteriaLayout();
		criteriaLayout.setSizeFull();
		topLayout.addComponent(criteriaLayout);

		// search
		search = new Button("Search");
		search.setImmediate(true);
		search.setStyleName(Reindeer.BUTTON_LINK);

		topLayout.addComponent(search);
		topLayout.setComponentAlignment(search, Alignment.MIDDLE_RIGHT);
	}

	private void buildCriteriaLayout() {
		// common part: create layout
		criteriaLayout = new HorizontalLayout();
		criteriaLayout.setImmediate(false);
		criteriaLayout.setSizeFull();
		criteriaLayout.setMargin(false);
		
		// leftLayout
		leftLayout = buildLeftLayout();
		criteriaLayout.addComponent(leftLayout);
		criteriaLayout.setExpandRatio(leftLayout, 1.0f);
		
		// rightLayout
		rightLayout = buildRightLayout();
		criteriaLayout.addComponent(rightLayout);
		criteriaLayout.setExpandRatio(rightLayout, 1.0f);

		// region
		loadJamaats();
		leftLayout.addComponent(jamaat);
		
		// bodyPart
		loadArziTypes();
		rightLayout.addComponent(arziType);

		// bodyPart
		loadBodyParts();
		leftLayout.addComponent(bodyPart);

		// condition
		loadConditions();
		rightLayout.addComponent(condition);

		// procedure
		loadProcedures();;
		leftLayout.addComponent(procedure);
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

	private void initArziList() {
		/*arziGrid.setContainerDataSource(myArziContainer);
		arziGrid.setSelectable(true);
		arziGrid.setImmediate(true);
		arziGrid.setColumnHeaderMode(ColumnHeaderMode.EXPLICIT);
		arziGrid.setVisibleColumns(new Object[]{"arziId", "arziCategory", "region", "country", "status"});
		arziGrid.setColumnHeaders(new String[]{"ID", "CATEGORY", "REGION", "COUNTRY", "STATUS"});*/
	}

	private static BeanItemContainer<Arzi> createMyArziDummyDatasource() {
		BeanItemContainer<Arzi> bc = new BeanItemContainer<Arzi>(Arzi.class);

		 /*ArrayList<Arzi> arziList = new ArrayList<Arzi>();
		 for (int i = 1; i < 50; i++) {
			 Arzi arzi = new Arzi();
			 Status status = new Status();
			 arzi.setArziId(new Long((long)(i* Math.random() + 1000)));
			 if(i%2 == 0)
				 arzi.setArziCategory("Doa");
			 else
				 arzi.setArziCategory("Raza");

			 if(i%3 == 0) {
				 status.setStatusDesc("Inprocess");
				 arzi.setStatus(status);
			 }
			 else if(i%7 == 0) {
				 status.setStatusDesc("Completed");
				 arzi.setStatus(status);
			 }
			 else if(i%5 == 0) {
				 status.setStatusDesc("Waiting");
				 arzi.setStatus(status);
			 }
			 else {
				 status.setStatusDesc("Submitted");
				 arzi.setStatus(status);
			 }

			 long randomNum = (long)(i* Math.random() + 1000);
			 if(randomNum % 2 == 0) {
				 arzi.setRegion("North America");
				 arzi.setCountry("USA");
			 }
			 else if(randomNum % 3 == 0) {
				 arzi.setRegion("North America");
				 arzi.setCountry("Canada");
			 }
			 if(randomNum % 5 == 0) {
				 arzi.setRegion("Asia");
				 arzi.setCountry("India");
			 }
			 if(randomNum % 7 == 0) {
				 arzi.setRegion("Asia");
				 arzi.setCountry("Pakistan");
			 }
			 if(randomNum % 9 == 0) {
				 arzi.setRegion("Europe");
				 arzi.setCountry("United Kingdom");
			 }
			 if(randomNum % 2 == 1) {
				 arzi.setRegion("Europe");
				 arzi.setCountry("France");
			 }
			 if(randomNum % 3 == 1) {
				 arzi.setRegion("Africa");
				 arzi.setCountry("Kenya");
			 }
			 if(randomNum % 5 == 1) {
				 arzi.setRegion("Africa");
				 arzi.setCountry("Tanzania");
			 }
			 arziList.add(arzi);
		 }

		 bc.addAll(arziList);*/

		return bc;
	}	
	
	private VerticalLayout buildLeftLayout() {
		// common part: create layout
		leftLayout = new VerticalLayout();
		leftLayout.setImmediate(false);
		leftLayout.setWidth("100.0%");
		leftLayout.setHeight("100.0%");
		leftLayout.setMargin(false);
		
		// leftFormLayout
		leftFormLayout = new FormLayout();
		leftFormLayout.setId(MedicalArziConstants.CUSTOM_FORM_LEFTFORM_LAYOUT_ID);
		leftLayout.addComponent(leftFormLayout);
		
		return leftLayout;
	}

	private VerticalLayout buildRightLayout() {
		// common part: create layout
		rightLayout = new VerticalLayout();
		rightLayout.setImmediate(false);
		rightLayout.setWidth("100.0%");
		rightLayout.setHeight("100.0%");
		rightLayout.setMargin(false);
		
		// rightFormLayout
		rightFormLayout = new FormLayout();
		rightFormLayout.setId(MedicalArziConstants.CUSTOM_FORM_RIGHTFORM_LAYOUT_ID);
		rightLayout.addComponent(rightFormLayout);
		
		return rightLayout;
	}	

}
