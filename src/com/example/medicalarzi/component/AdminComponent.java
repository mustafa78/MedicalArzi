/**
 * 
 */
package com.example.medicalarzi.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

/**
 * @author mkanchwa
 *
 */
public class AdminComponent extends CustomComponent {

	private static final long serialVersionUID = 9091061703248511542L;
	
	private static Logger logger = LogManager.getLogger(AdminComponent.class);
	
	// Layout and components
	private HorizontalSplitPanel hSplitPanel;
	
	private VerticalLayout vLayout;
	
	private CustomFormComponent formComponent;

	/**
	 * 
	 */
	public AdminComponent() {
		logger.debug("Initializing Admin Component");
	}

	/**
	 * @param compositionRoot
	 */
	public AdminComponent(Component compositionRoot) {
		super(compositionRoot);
	}
	
	private void buildSplitPanel() {
		// top-level component properties
		setSizeFull();

		// common part: create layout
		hSplitPanel = new HorizontalSplitPanel();
		hSplitPanel.setImmediate(false);
		hSplitPanel.setLocked(true);
		hSplitPanel.setSizeFull();
		hSplitPanel.setSplitPosition(20, Unit.PERCENTAGE);
		// Use a custom style
		hSplitPanel.addStyleName("invisiblesplitter");

	}

}
