/**
 *
 */
package com.example.medicalarzi.view;

import com.example.medicalarzi.component.UserList;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.VaadinUIScope;
import com.vaadin.spring.navigator.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.VerticalLayout;

/**
 * @author mkanchwa
 *
 */
@Theme("medicalarzi")
@VaadinUIScope
@SpringView(name = MyFormView.NAME)
public class MyFormView extends CustomComponent implements View {

	/**
	 *
	 */
	private static final long serialVersionUID = -8757177872653251494L;
	public static final String NAME = "myForm";

	/**
	 *
	 */
	public MyFormView() {
		setSizeFull();

		VerticalLayout layout = new VerticalLayout();
		TabSheet tabsheet = new TabSheet();
		layout.addComponent(tabsheet);
		setCompositionRoot(layout);

		// Create the first tab
		VerticalLayout tab1 = new VerticalLayout();
		tab1.addComponent(new Label("My Arzi"));
		tab1.setCaption("My Arzi");
		tabsheet.addTab(tab1);

		// Create the second tab
		VerticalLayout tab2 = new VerticalLayout();
		tab2.addComponent(new Label("New Arzi"));
		tab2.setCaption("New Arzi");
		tabsheet.addTab(tab2);

		handleTabChange(tabsheet);
	}

	private void handleTabChange(final TabSheet tabsheet) {
		// Handling tab changes
        tabsheet.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				TabSheet tabsheet = event.getTabSheet();

				// Find the tab (here we know it's a layout)
		        Layout tab = (Layout) tabsheet.getSelectedTab();

		     // Get the tab caption from the tab object
		        String caption = tabsheet.getTab(tab).getCaption();

		        if(caption.equals("My Arzi")) {
		        	getUI().getNavigator().navigateTo(UserList.NAME);
		        }
		        else if(caption.equals("New Arzi")) {
		        	//getUI().getNavigator().navigateTo(UserListView.NAME);
		        }
			}
		});
	}

	/**
	 * @param compositionRoot
	 */
	public MyFormView(Component compositionRoot) {
		super(compositionRoot);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
