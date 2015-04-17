package com.example.medicalarzi.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Title("Medical Arzi Project")
@Theme("medicalarzi")
public class MedicalarziUI extends UI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Table contactList = new Table();
	private TextField searchField = new TextField();
	private Button addNewContactButton = new Button("New");
	private Button removeContactButton = new Button("Remove this contact");
	private FormLayout editorLayout = new FormLayout();
	private FieldGroup editorFields = new FieldGroup();

	IndexedContainer contactContainer = createDummyDatasource();

	private static final String FNAME = "First Name";
	private static final String LNAME = "Last Name";
	private static final String ITS_ID = "ITS Number";
	private static final String[] fieldNames = new String[] { FNAME, LNAME,
			ITS_ID, "Mobile Phone", "Work Phone", "Home Phone", "Work Email",
			"Home Email", "Street", "City", "Zip", "State", "Country",
			"Condition", "Undergoing Treatment", "Doctor's Name", "Arzi" };

	/**
	 * 
	 */
	public MedicalarziUI() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param content
	 */
	public MedicalarziUI(Component content) {
		super(content);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init(VaadinRequest request) {
		 initLayout();
         initContactList();
         initEditor();
         initSearch();
         initAddRemoveButtons();
	}

	private void initLayout() {

		HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
		setContent(splitPanel);

		VerticalLayout leftLayout = new VerticalLayout();
		splitPanel.addComponent(leftLayout);
		splitPanel.addComponent(editorLayout);
		leftLayout.addComponent(contactList);
		HorizontalLayout bottomLeftLayout = new HorizontalLayout();
		leftLayout.addComponent(bottomLeftLayout);
		bottomLeftLayout.addComponent(searchField);
		bottomLeftLayout.addComponent(addNewContactButton);

		leftLayout.setSizeFull();

		leftLayout.setExpandRatio(contactList, 1);
		contactList.setSizeFull();

		bottomLeftLayout.setWidth("100%");
		searchField.setWidth("100%");
		bottomLeftLayout.setExpandRatio(searchField, 1);

		editorLayout.setMargin(true);
		editorLayout.setVisible(false);
	}

	private void initEditor() {

		for (String fieldName : fieldNames) {
			TextField field = new TextField(fieldName);
			editorLayout.addComponent(field);
			field.setWidth("100%");

			editorFields.bind(field, fieldName);
		}
		editorLayout.addComponent(removeContactButton);

		editorFields.setBuffered(false);
	}

	private void initSearch() {

		searchField.setInputPrompt("Search contacts");

		searchField.setTextChangeEventMode(TextChangeEventMode.LAZY);

		searchField.addTextChangeListener(new TextChangeListener() {

			public void textChange(final TextChangeEvent event) {
				contactContainer.removeAllContainerFilters();
				contactContainer.addContainerFilter(new ContactFilter(event
						.getText()));
			}
		});
	}

	private class ContactFilter implements Filter {
		private String needle;

		public ContactFilter(String needle) {
			this.needle = needle.toLowerCase();
		}

		public boolean passesFilter(Object itemId, Item item) {
			String haystack = ("" + item.getItemProperty(FNAME).getValue()
					+ " " + item.getItemProperty(LNAME).getValue() + " " + item
					.getItemProperty(ITS_ID).getValue()).toLowerCase();
			return haystack.contains(needle);
		}

		public boolean appliesToProperty(Object id) {
			return true;
		}
	}

	private void initAddRemoveButtons() {
		addNewContactButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {

				contactContainer.removeAllContainerFilters();
				Object contactId = contactContainer.addItemAt(0);

				contactList.getContainerProperty(contactId, FNAME).setValue(
						"New");
				contactList.getContainerProperty(contactId, LNAME).setValue(
						"Contact");

				contactList.select(contactId);
			}
		});

		removeContactButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				Object contactId = contactList.getValue();
				contactList.removeItem(contactId);
			}
		});
	}

	private void initContactList() {
		contactList.setContainerDataSource(contactContainer);
		contactList.setVisibleColumns(new String[] { FNAME, LNAME, ITS_ID });
		contactList.setSelectable(true);
		contactList.setImmediate(true);

		contactList.addValueChangeListener(new Property.ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				Object contactId = contactList.getValue();

				editorFields.setItemDataSource(contactList.getItem(contactId));

				editorLayout.setVisible(contactId != null);
			}
		});
	}

	private static IndexedContainer createDummyDatasource() {
		IndexedContainer ic = new IndexedContainer();

		for (String p : fieldNames) {
			ic.addContainerProperty(p, String.class, "");
		}

		String[] fnames = { "Mustafa", "Murtaza", "Shabbir", "Qusai",
				"Tasneem", "Sakina", "Ahmed", "Aliasgar", "Burhanuddin",
				"Mufaddal", "Samina", "Farida", "Yakuta", "Khuzaima" };
		String[] lnames = { "Jamali", "Kanchwala", "Shahmalak", "Badri",
				"Rangwala", "Shahmalak", "Haji", "Shakir", "Karimji",
				"Shajapurwala", "Poonawala", "Pardawala", "Manasawala",
				"Kagalwala", "Bootwala" };
		for (int i = 0; i < 1000; i++) {
			Object id = ic.addItem();
			ic.getContainerProperty(id, FNAME).setValue(
					fnames[(int) (fnames.length * Math.random())]);
			ic.getContainerProperty(id, LNAME).setValue(
					lnames[(int) (lnames.length * Math.random())]);
			ic.getContainerProperty(id, ITS_ID).setValue(String.valueOf((long)(Math.random()*90000000 + 10000000)));
		}

		return ic;
	}

}