package com.example.medicalarzi.view;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.example.medicalarzi.component.ArziFooterComponent;
import com.example.medicalarzi.component.ArziHeaderComponent;
import com.example.medicalarzi.model.Patient;
import com.example.medicalarzi.service.PatientService;
import com.example.medicalarzi.util.MAPMail;
import com.example.medicalarzi.util.MedicalArziConstants;
import com.example.medicalarzi.util.MedicalArziUtils;
import com.example.medicalarzi.util.PropertyLoader;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.converter.StringToLongConverter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

@Theme("medicalarzi")
@UIScope
@SpringView(name = ForgotPasswordView.NAME)
public class ForgotPasswordView extends CustomComponent implements View,
		ClickListener, ValueChangeListener {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	/**
	 *
	 */
	private static final long serialVersionUID = -5351880145337365828L;

	public static Logger logger = LogManager
			.getLogger(ForgotPasswordView.class);

	public static final String NAME = "forgotPassword";

	private VerticalLayout mainLayout;

	private HorizontalLayout buttonLayout;

	private ArziFooterComponent footer;

	private FormLayout viewLayout;

	private ArziHeaderComponent header;

	private TextField itsNumber;

	private TextField emailAddress;

	private Button backToLoginBtn;

	private Button resetPasswdBtn;

	private static Properties properties = PropertyLoader
			.loadProperties(MedicalArziConstants.MAP_PROPERTIES);

	@Autowired
	@Qualifier("service.PatientService")
	private PatientService patientService;

	/**
	 * Constructor
	 */
	public ForgotPasswordView() {
		logger.debug("Inside the Forgot Password View.");
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

	@Override
	public void enter(ViewChangeEvent event) {
		init();

		Notification.show("Welcome to the Forgot Password View!!!");

		// Get the logout button from the Header component and hide it
		Component logoutBtn = MedicalArziUtils.findById(header,
				MedicalArziConstants.HEADER_LOGOUT_BUTTON_ID);
		if (logoutBtn != null) {
			logoutBtn.setVisible(false);
		}
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// top-level component properties
		setSizeFull();

		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setSizeFull();

		// header
		header = new ArziHeaderComponent();
		header.setImmediate(false);
		header.setHeight("108px");
		mainLayout.addComponent(header);

		// viewLayout
		buildViewLayout();
		mainLayout.addComponent(viewLayout);
		//mainLayout.setExpandRatio(viewLayout, 4.0f);
		mainLayout.setComponentAlignment(viewLayout, Alignment.MIDDLE_CENTER);

		buildButtonLayout();
		mainLayout.addComponent(buttonLayout);
		mainLayout.setComponentAlignment(buttonLayout, Alignment.TOP_CENTER);		

		// footer
		footer = new ArziFooterComponent();
		footer.setImmediate(false);
		footer.setWidthUndefined();
		footer.setHeight("70px");
		mainLayout.addComponent(footer);
		mainLayout.setComponentAlignment(footer, Alignment.MIDDLE_CENTER);

		return mainLayout;
	}

	private void buildButtonLayout() {
		buttonLayout = new HorizontalLayout();
		buttonLayout.setImmediate(false);
		buttonLayout.setSpacing(true);
		
		// backToLoginBtn
		backToLoginBtn = new Button("", this);
		backToLoginBtn.setIcon(new ThemeResource("img/back-button.png"));
		backToLoginBtn.setStyleName(Reindeer.BUTTON_LINK);
		buttonLayout.addComponent(backToLoginBtn);
		buttonLayout.setComponentAlignment(backToLoginBtn, Alignment.MIDDLE_RIGHT);

		// resetPasswdBtn
		resetPasswdBtn = new Button("", this);
		resetPasswdBtn.setIcon(new ThemeResource(
				"img/reset-password-button.png"));
		resetPasswdBtn.setStyleName(Reindeer.BUTTON_LINK);
		buttonLayout.addComponent(resetPasswdBtn);
		buttonLayout.setComponentAlignment(backToLoginBtn, Alignment.MIDDLE_LEFT);
	}

	/**
	 *
	 */
	private void buildViewLayout() {
		viewLayout = new FormLayout();
		viewLayout.setImmediate(true);
		viewLayout.setCaption("Forgot Password:");
		viewLayout.setSpacing(true);
		viewLayout.setStyleName("ptntRegForm");
		viewLayout.setSizeUndefined();

		// itsNumFld
		itsNumber = new TextField("ITS Number:");
		itsNumber.setNullRepresentation("");
		itsNumber.setImmediate(true);
		itsNumber.setRequired(true);
		itsNumber.addValueChangeListener(this);
		itsNumber.setMaxLength(8);
		itsNumber.setConverter(new StringToLongConverter() {
			private static final long serialVersionUID = 1L;

			@Override
			protected NumberFormat getFormat(Locale locale) {
				NumberFormat format = super.getFormat(locale);
				format.setGroupingUsed(false);
				return format;
			};
		});
		viewLayout.addComponent(itsNumber);

		// emailAddress
		emailAddress = new TextField("Email Address");
		emailAddress.setNullRepresentation("");
		emailAddress.setImmediate(false);
		emailAddress.setReadOnly(true);
		emailAddress.setWidth("300px");
		viewLayout.addComponent(emailAddress);

	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(backToLoginBtn)) {
			getUI().getNavigator().navigateTo(SimpleLoginView.NAME);
		}
		else if (event.getButton().equals(resetPasswdBtn)) {

			if (StringUtils.isEmpty(emailAddress.getValue())) {
				setEmailAddressField();
			}
			// Retrieve the patient's (or user's) password and send an email
			// with the retrieved password.
			String password = patientService.emailForgotPassword(
					Long.valueOf(itsNumber.getValue()),
					MedicalArziConstants.MAP_ENCODE_PASSWORD_KEY);

			// Email the password
			emailPasswordToPatient(password);
			
			MedicalArziUtils.setRequestAttribute(
					MedicalArziConstants.REQ_ATTR_PASSWD_EMAILED, true);
			
			getUI().getNavigator().navigateTo(SimpleLoginView.NAME);

		}

	}

	/**
	 * This method extracts the properties for the email config and emails the
	 * extracted password from the dbase to the registered email address.
	 * 
	 * @param password
	 */
	private void emailPasswordToPatient(String password) {
		// Get the email host server from the properties file.
		String host = properties.getProperty(MedicalArziConstants.EMAIL_HOST);

		// Get the from name from the properties file.
		String from = properties
				.getProperty(MedicalArziConstants.FORGOT_EMAIL_FROM);

		// Get the replyTo name from the properties file.
		String replyTo = properties
				.getProperty(MedicalArziConstants.FORGOT_EMAIL_REPLY_TO);

		// Get the subject name from the properties file.
		String subject = properties
				.getProperty(MedicalArziConstants.FORGOT_PSWD_EMAIL_SUBJECT);

		// Get the body name from the properties file.
		String body = properties
				.getProperty(MedicalArziConstants.FORGOT_PSWD_EMAIL_BODY);

		//Fetch the patient information based on the ITS number.
		Patient ptInfo = patientService.getPatientInfo(Long.valueOf(itsNumber
				.getValue()));

		String formattedSubject = MessageFormat.format(subject,
				itsNumber.getValue());

		String formattedBody = MessageFormat.format(body, new Object[] {
				MedicalArziUtils.constructPtntFullName(ptInfo), password });

		String[] toList = { emailAddress.getValue() };

		// email password
		MAPMail mail = new MAPMail();
		
		logger.debug("Sending password reset email to email address --> "
				+ emailAddress.getValue());

		mail.sendMail(toList, from, replyTo, host, formattedBody,
				formattedSubject, "Medical Arzi Admin");

		logger.debug("Email successfully sent to email address --> "
				+ emailAddress.getValue());
	}

	@Override
	public void valueChange(ValueChangeEvent event) {

		if (event.getProperty().equals(itsNumber)) {
			try {

				/*
				 * // This is important to avoid the validation error when the
				 * page // first loads up.
				 * MedicalArziUtils.installSingleValidator(itsNumber,
				 * "itsNumber");
				 * MedicalArziUtils.installSingleValidator(emailAddress,
				 * "emailAddress");
				 */

				logger.debug("Retreiving information for patient with ITS number -> "
						+ itsNumber.getValue());

				setEmailAddressField();

			} catch (NumberFormatException ne) {
				this.itsNumber.setValue(null);

				this.itsNumber.focus();
			}
		}

	}

	/**
	 * 
	 */
	private void setEmailAddressField() {

		Patient patient = patientService.getPatientInfo(Long.valueOf(itsNumber
				.getValue()));

		if (patient != null) {
			logger.debug("Get the email address and set it in the field. The email address for ITS number -> "
					+ patient.getItsNumber()
					+ " is -> "
					+ patient.getEmailAddress());

			emailAddress.setReadOnly(false);

			emailAddress.setValue(patient.getEmailAddress());

			emailAddress.setReadOnly(true);

			logger.debug("Email address -> " + patient.getEmailAddress()
					+ " successfully set.");
		}
	}

}
