/**
 * 
 */
package com.example.medicalarzi.component;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.medicalarzi.model.Arzi;
import com.example.medicalarzi.model.GregHijDate;
import com.example.medicalarzi.model.Status;
import com.example.medicalarzi.service.ServiceLocator;
import com.example.medicalarzi.util.MedicalArziConstants;
import com.example.medicalarzi.util.MedicalArziUtils;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author Mkanchwa
 *
 */
public class ReviewArziComponent extends CustomComponent implements
		ClickListener {

	private static final long serialVersionUID = -6040343713306953370L;
	
	public static Logger logger = LogManager.getLogger(ReviewArziComponent.class);
	
	// Main View Layout
	private VerticalLayout viewLayout;

	// Reviewer Section
	private Panel reviewerSection;
	
	private FormLayout reviewerForm;
	
	@PropertyId("doctorConsultNote")
	private TextArea reviewerComments;
	
	// Arzi Comments Section
	private Panel commentsSection;
	
	private FormLayout commentsForm;
	
	private TextArea comment;
	
	// Buttons Layout
	private HorizontalLayout buttonsLayout;
	
	private Button approveBtn;
	
	private Button requestInfoBtn;
	
	// Comments button
	private Button addCommentBtn;
	
	private Button removeCommentBtn;
	
	private BeanFieldGroup<Arzi> arziFieldsBinder;
	
	private Arzi arzi;
	
	/**
	 * Constructor - Responsible for building the layout with reviewer section
	 * and setting it as the composition root.
	 * 
	 */
	public ReviewArziComponent() {
		// Build the layout and elements
		buildViewlayout();
		
		// Bind the field components to the model object.
		bindFieldsToModel();
		
		// Set the composition root (or mainLayout)
		setCompositionRoot(viewLayout);
	}
	
	/**
	 * This constructor is responsible for adding the reviewer section to the
	 * layout passed in the argument.
	 * 
	 * @param viewLayout
	 */
	public ReviewArziComponent(Layout viewLayout, Arzi arziToBeReviewed) {

		this.arzi = arziToBeReviewed;
		
		if (viewLayout instanceof VerticalLayout) {
			VerticalLayout verticalLayout = (VerticalLayout) viewLayout;
			
			// Build the layout and elements
			buildCommentsSection();
			verticalLayout.addComponent(commentsSection);
			
			buildReviewerInfoSection();
			verticalLayout.addComponent(reviewerSection);

			buildButtonsLayout();
			verticalLayout.addComponent(buttonsLayout);
			verticalLayout.setComponentAlignment(buttonsLayout,
					Alignment.MIDDLE_CENTER);
			
			// Bind the field components to the model object.
			bindFieldsToModel();
		}
	}
	
	/**
	 * This method is responsible for binding the field components to model.
	 * 
	 */
	private void bindFieldsToModel() {
		logger.debug("Binding the patient and the arzi fields to their respective models.");

		/* Bind the arzi fields. */
		arziFieldsBinder = new BeanFieldGroup<Arzi>(Arzi.class);
		/**
		 * =======>IMPORTANT: Binding for nested bean will work by switching
		 * "setItemDataSource" and "bindMemberFields".
		 * 
		 * =======>First bindMembers then add dataSource.
		 * **/
		arziFieldsBinder.bindMemberFields(this);
		arziFieldsBinder.setItemDataSource(this.arzi);
		arziFieldsBinder.setBuffered(true);

	}	

	/**
	 * This method is responsible for building the view layout and add the
	 * reviewer section to it.
	 */
	private void buildViewlayout() {
		// viewLayout
		viewLayout = new VerticalLayout();
		viewLayout.setId(MedicalArziConstants.REVIEW_ARZI_COMPONENT_VIEW_LAYOUT_ID);
		viewLayout.setSizeFull();
		viewLayout.setImmediate(false);
		viewLayout.setMargin(true);
		viewLayout.setSpacing(true);
		
		buildCommentsSection();
		// adds commentsSection to the viewLayout
		viewLayout.addComponent(commentsSection);
		
		buildReviewerInfoSection();
		// adds reviewerSection to the viewLayout
		viewLayout.addComponent(reviewerSection);
		
		buildButtonsLayout();
		// adds buttonLayout to the viewLayout
		viewLayout.addComponent(buttonsLayout);
		viewLayout
				.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
	}
	
	/**
	 * This method is responsible for building the Reviewer section Panel with
	 * the Textarea for the reviewer to add his comments or consultation notes
	 * regarding the arzi in question.
	 */
	private void buildReviewerInfoSection() {
		reviewerSection = new Panel("Reviewer Section :");
		reviewerSection.setSizeFull();
		reviewerSection.addStyleName("arziContent");
		
		reviewerForm = new FormLayout();
		reviewerForm.setImmediate(false);
		reviewerForm.setSizeFull();
		reviewerForm.setStyleName("customForm");
		reviewerSection.setContent(reviewerForm);
		
		reviewerComments = new TextArea("Doctor's Summary:");
		reviewerComments.setImmediate(false);
		reviewerComments.setNullRepresentation("");
		reviewerComments.setWidth(70, Unit.PERCENTAGE);
		reviewerComments.setWordwrap(true);
		reviewerComments.setMaxLength(4000);
		reviewerComments.setRequired(true);
		reviewerForm.addComponent(reviewerComments);
	}
	
	/**
	 * This method is responsible for building the Reviewer section Panel with
	 * the Textarea for the reviewer to add his comments or consultation notes
	 * regarding the arzi in question.
	 */
	private void buildCommentsSection() {
		commentsSection = new Panel("Request Additional Information:");
		commentsSection.setSizeFull();
		commentsSection.setVisible(false);
		commentsSection.addStyleName("arziContent");
		
		commentsForm = new FormLayout();
		commentsForm.setImmediate(false);
		commentsForm.setSizeFull();
		commentsForm.setStyleName("customForm");
		commentsSection.setContent(commentsForm);
		
		comment = new TextArea("Question/Comment :");
		comment.setImmediate(false);
		comment.setNullRepresentation("");
		comment.setWidth(70, Unit.PERCENTAGE);
		comment.setWordwrap(true);
		comment.setMaxLength(4000);
		comment.setRequired(true);
		commentsForm.addComponent(comment);
	}	
	
	/**
	 * This method is responsible for building the buttons layout with the
	 * 'Approve' button (which approves and moves the arzi in the next stage )
	 * and 'Request Information' button (which sends the arzi back to the
	 * patient because the reviewer needs additional information for the arzi).
	 * 
	 */
	private void buildButtonsLayout() {

		buttonsLayout = new HorizontalLayout();
		buttonsLayout.setId(MedicalArziConstants.ARZI_FORM_COMPONENT_BUTTON_LAYOUT_ID);
		
		// addCommentBtn
		addCommentBtn = new Button(new ThemeResource("img/add-comment.png"));
		addCommentBtn.addClickListener(this);
		addCommentBtn.setStyleName(Reindeer.BUTTON_LINK);
		buttonsLayout.addComponent(addCommentBtn);
		buttonsLayout.setExpandRatio(addCommentBtn, 1.0f);
		
		// removeCommentBtn
		removeCommentBtn = new Button(new ThemeResource("img/remove-comment.png"));
		removeCommentBtn.addClickListener(this);
		removeCommentBtn.setStyleName(Reindeer.BUTTON_LINK);
		// Hide the button. Should only be visible when the reviewer adds
		// comment/note on the arzi requesting for more information.
		removeCommentBtn.setVisible(false);
		buttonsLayout.addComponent(removeCommentBtn);
		buttonsLayout.setExpandRatio(removeCommentBtn, 1.0f);		
		
		// requestInfoBtn
		requestInfoBtn = new Button(new ThemeResource("img/request-info.png"));
		requestInfoBtn.addClickListener(this);
		requestInfoBtn.setStyleName(Reindeer.BUTTON_LINK);
		// Hide the button. Should only be visible when the reviewer adds
		// comment/note on the arzi requesting for more information.
		requestInfoBtn.setVisible(false);
		buttonsLayout.addComponent(requestInfoBtn);
		buttonsLayout.setExpandRatio(requestInfoBtn, 1.0f);

		// approveBtn
		approveBtn = new Button(new ThemeResource("img/approve.png"));
		approveBtn.addClickListener(this);
		approveBtn.setStyleName(Reindeer.BUTTON_LINK);
		buttonsLayout.addComponent(approveBtn);
		buttonsLayout.setExpandRatio(approveBtn, 1.0f);

		buttonsLayout.setSpacing(true);
		buttonsLayout.setStyleName("ptntRegistrationBtn");

	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(approveBtn)) {
			try {
				// FieldGroup buffered mode is on, so commit() is required.
				// Throws CommitException
				arziFieldsBinder.commit();
				
				// For ArziHeader to update the arzi
				Status arziStatus = new Status();
				arziStatus
						.setStatusId(MedicalArziConstants.ARZI_DOCTOR_APPROVED_STATUS);
				arzi.setCurrentStatus(arziStatus);

				GregHijDate currentDate = ServiceLocator.getInstance()
						.getLookupService()
						.getRequestedGregorianHijriCalendar(new Date());
				arzi.setCurrentStatusDate(currentDate);

				// For Arzi to create a new record in the detail table.
				arzi.setStatusChangeDate(currentDate);
				arzi.setStatus(arziStatus);
				arzi.setReviewDate(currentDate);
				arzi.setUpdatedBy(String.valueOf(arzi.getReviewerItsNumber()));
				
				// Call the reviewer service to approve the arzi.
				ServiceLocator.getInstance().getReviewerService()
						.approveArzi(arzi);
				
				String successMsg = "Arzi \""
						+ arzi.getArziId()
						+ "\" is successfully reviewed and approved by reviewer: -> "
						+ arzi.getReviewerItsNumber();

				MedicalArziUtils.createAndShowNotification(null,
						successMsg, Type.HUMANIZED_MESSAGE,
						Position.TOP_LEFT, "userFriendlyMsg",
						MedicalArziConstants.USER_FRIENDLY_MSG_DELAY_MSEC);				
				
			} catch (CommitException ce) {
				logger.error(ce);

				String errorDescription = "Fields marked with asterisk (*) are required. "
						+ "Please enter the required values and fix the errors before proceeding further.";

				// Create an error notification if the required fields are not
				// entered correctly.
				MedicalArziUtils.createAndShowNotification(null,
						errorDescription, Type.ERROR_MESSAGE,
						Position.TOP_LEFT, "errorMsg", -1);
			}	
			
		} else if(event.getButton().equals(addCommentBtn)) {
			
			commentsSection.setVisible(true);
			reviewerSection.setVisible(false);
			
			// Hide the 'Approve' and 'Add Comments' button
			approveBtn.setVisible(false);
			addCommentBtn.setVisible(false);
			
			// Display the 'Request Info' and 'Remove Comments' button
			requestInfoBtn.setVisible(true);
			removeCommentBtn.setVisible(true);
				
		} else if(event.getButton().equals(removeCommentBtn)) {
			
			commentsSection.setVisible(false);
			reviewerSection.setVisible(true);
		
			// Hide the 'Request Info' and 'Remove Comments' button
			requestInfoBtn.setVisible(false);
			removeCommentBtn.setVisible(false);
			
			// Display the 'Approve' and 'Add Comments' button
			approveBtn.setVisible(true);
			addCommentBtn.setVisible(true);
		}
		
	}

}
