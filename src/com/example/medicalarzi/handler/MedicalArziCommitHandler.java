/**
 * 
 */
package com.example.medicalarzi.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.medicalarzi.model.Arzi;
import com.example.medicalarzi.service.ServiceLocator;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Notification;

/**
 * @author mkanchwa
 *
 */
public class MedicalArziCommitHandler implements CommitHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7680370403913163639L;
	
	public static Logger logger = LogManager
			.getLogger(MedicalArziCommitHandler.class);

	/**
	 * 
	 */
	public MedicalArziCommitHandler() {
		super();
	}


	@Override
	public void preCommit(CommitEvent commitEvent) throws CommitException {
		
	}

	@Override
	public void postCommit(CommitEvent commitEvent) throws CommitException {
		
		Item editedItem = commitEvent.getFieldBinder().getItemDataSource();
		
		if(editedItem instanceof BeanItem<?>) {
			BeanItem<?> editedBeanItem = (BeanItem<?>)editedItem;
			
			Arzi editedArzi = (Arzi)editedBeanItem.getBean();
			
			logger.debug("Updated arzi information: " + editedArzi);
			
			ServiceLocator.getInstance().getPatientService()
					.updateAnExistingArzi(editedArzi);
			
			Notification.show("Arzi updated successfully!!!");
		}
		
	}

}
