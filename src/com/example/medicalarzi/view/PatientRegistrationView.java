package com.example.medicalarzi.view;

import java.text.MessageFormat;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.example.medicalarzi.component.ArziDateField;
import com.example.medicalarzi.component.ArziFooterComponent;
import com.example.medicalarzi.component.ArziHeaderComponent;
import com.example.medicalarzi.model.Lookup;
import com.example.medicalarzi.model.Patient;
import com.example.medicalarzi.service.LookupService;
import com.example.medicalarzi.service.PatientService;
import com.example.medicalarzi.util.InstallPatientValidatorBlurListener;
import com.example.medicalarzi.util.MAPMail;
import com.example.medicalarzi.util.MedicalArziConstants;
import com.example.medicalarzi.util.MedicalArziUtils;
import com.example.medicalarzi.util.PropertyLoader;
import com.example.medicalarzi.validator.PasswordValidator;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author Mustafa Kanchwala
 *
 */
@Theme("medicalarzi")
@UIScope
@SpringView(name = PatientRegistrationView.NAME)
public class PatientRegistrationView extends CustomComponent implements View,
		ClickListener {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */
	/**
	 *
	 */
	private static final long serialVersionUID = 4718010552839868752L;

	public static Logger logger = LogManager
			.getLogger(PatientRegistrationView.class);

	public static final String NAME = "register";
	
	private static Properties properties = PropertyLoader
			.loadProperties(MedicalArziConstants.MAP_PROPERTIES);

	// View Components
	private VerticalLayout mainLayout;

	private FormLayout viewLayout;

	private Button registerBtn;

	private Button backBtn;

	private PasswordField confirmPassword;

	@PropertyId("password")
	private PasswordField newPassword;

	@PropertyId("lastName")
	private TextField lastName;

	@PropertyId("ptntMiddleNmTitle")
	private ComboBox mdlNameTitle;

	@PropertyId("middleName")
	private TextField middleName;

	@PropertyId("ptntTitle")
	private ComboBox title;

	@PropertyId("firstName")
	private TextField firstName;

	@PropertyId("itsNumber")
	private TextField itsNumber;

	@PropertyId("dob")
	private PopupDateField dob;

	@PropertyId("emailAddress")
	private TextField emailAddress;
	
	@PropertyId("gender")
	private OptionGroup gender;

	private ArziHeaderComponent header;

	private HorizontalLayout buttonsLayout;

	private ArziFooterComponent footer;

	private BeanFieldGroup<Patient> ptntRegFormFieldsBinder;

	@Autowired
	@Qualifier("service.PatientService")
	private PatientService patientService;

	@Autowired
	@Qualifier("service.LookupService")
	private LookupService lookupService;

	/**
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 */
	public PatientRegistrationView() {
		logger.debug("Inside the Patient Registration View.");
	}

	@Override
	public void enter(ViewChangeEvent event) {
		init();

		Notification.show("Welcome to the Registration View!!!");

		// Get the logout button from the Header component and hide it
		Component logoutBtn = MedicalArziUtils.findById(header,
				MedicalArziConstants.HEADER_LOGOUT_BUTTON_ID);
		if (logoutBtn != null) {
			logoutBtn.setVisible(false);
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
		// mainLayout.setExpandRatio(header, 0.5f);

		// viewLayout
		viewLayout = buildViewLayout();
		mainLayout.addComponent(viewLayout);
		mainLayout.setExpandRatio(viewLayout, 4.0f);
		mainLayout.setComponentAlignment(viewLayout, Alignment.MIDDLE_CENTER);

		Patient patient = new Patient();
		ptntRegFormFieldsBinder = new BeanFieldGroup<Patient>(Patient.class);
		// Add bean item that is bound.
		ptntRegFormFieldsBinder.setItemDataSource(patient);
		// Set the buffered mode on, if using the bean validation JSR-3.0 with
		// vaadin. Does not work when fields are immediately updated with the
		// bean.
		ptntRegFormFieldsBinder.setBuffered(true);
		// Bind all the member fields whose type extends Field to the property
		// id of the item.
		// The mapping is done based on the @PropertyId annotation
		ptntRegFormFieldsBinder.bindMemberFields(this);

		// footer
		footer = new ArziFooterComponent();
		footer.setImmediate(false);
		footer.setWidthUndefined();
		footer.setHeight("50px");
		mainLayout.addComponent(footer);
		mainLayout.setComponentAlignment(footer, Alignment.MIDDLE_CENTER);

		return mainLayout;
	}

	private FormLayout buildViewLayout() {
		// common part: create layout
		viewLayout = new FormLayout();
		viewLayout.setImmediate(true);
		viewLayout.setCaption("Registration Form:");
		viewLayout.setSpacing(true);
		viewLayout.setStyleName("ptntRegForm");
		viewLayout.setSizeUndefined();

		// itsNumFld
		itsNumber = new TextField("ITS Number:");
		itsNumber.setNullRepresentation("");
		itsNumber.setImmediate(true);
		itsNumber.setRequired(true);
		itsNumber.setMaxLength(8);
		itsNumber.setConverter(MedicalArziUtils.itsNumberConverter());
		viewLayout.addComponent(itsNumber);
		itsNumber.addBlurListener(new InstallPatientValidatorBlurListener(
				itsNumber, "itsNumber"));

		// title
		title = new ComboBox("Title:");
		title.setContainerDataSource(MedicalArziUtils
				.getContainer(Lookup.class));
		title.addItems(lookupService
				.getByLookupType(MedicalArziConstants.MAP_DAWAT_TITLE));
		title.setInputPrompt("Please select a title.");
		title.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		title.setItemCaptionPropertyId("lookupValue");
		viewLayout.addComponent(title);

		// fNameFld
		firstName = new TextField("First Name:");
		firstName.setNullRepresentation("");
		firstName.setImmediate(true);
		firstName.setRequired(true);
		firstName.setWidth("300px");
		viewLayout.addComponent(firstName);
		firstName.addBlurListener(new InstallPatientValidatorBlurListener(
				firstName, "firstName"));

		// mdlNameTitle
		mdlNameTitle = new ComboBox("Middle Name Title:");
		mdlNameTitle.setContainerDataSource(MedicalArziUtils
				.getContainer(Lookup.class));
		mdlNameTitle.addItems(lookupService
				.getByLookupType(MedicalArziConstants.MAP_DAWAT_TITLE));
		mdlNameTitle.setInputPrompt("Please select a title.");
		mdlNameTitle.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		mdlNameTitle.setItemCaptionPropertyId("lookupValue");
		viewLayout.addComponent(mdlNameTitle);

		// mNameFld
		middleName = new TextField("Middle Name:");
		middleName.setNullRepresentation("");
		middleName.setImmediate(true);
		middleName.setWidth("300px");
		viewLayout.addComponent(middleName);

		// lNameFld
		lastName = new TextField("Last Name:");
		lastName.setNullRepresentation("");
		lastName.setImmediate(true);
		lastName.setRequired(true);
		lastName.setWidth("300px");
		lastName.addBlurListener(new InstallPatientValidatorBlurListener(
				lastName, "lastName"));
		viewLayout.addComponent(lastName);
		
		// gender
		gender = new OptionGroup("Gender:");
		gender.setContainerDataSource(MedicalArziUtils
				.getContainer(Lookup.class));
		gender.addItems(lookupService
				.getByLookupType(MedicalArziConstants.MAP_GENDER));
		gender.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		gender.setItemCaptionPropertyId("description");
		gender.setRequired(true);
		gender.setStyleName("horizontal");
		gender.addBlurListener(new InstallPatientValidatorBlurListener(gender, "gender"));
		viewLayout.addComponent(gender);

		// dob
		dob = new ArziDateField("Date of Birth:") ;
		dob.setImmediate(true);
		dob.setRequired(true);
		dob.addBlurListener(new InstallPatientValidatorBlurListener(dob, "dob"));
		viewLayout.addComponent(dob);

		emailAddress = new TextField("Email Address:");
		emailAddress.setNullRepresentation("");
		emailAddress.setImmediate(true);
		emailAddress.setRequired(true);
		emailAddress.setWidth("300px");
		emailAddress.addBlurListener(new InstallPatientValidatorBlurListener(
				emailAddress, "emailAddress"));
		viewLayout.addComponent(emailAddress);

		// newPsswdFld
		newPassword = new PasswordField("New Password:");
		newPassword.setNullRepresentation("");
		newPassword.setDescription(MedicalArziConstants.PASSWORD_HINT_TEXT);
		newPassword.setImmediate(true);
		newPassword.setRequired(true);
		newPassword.setWidth("300px");
		newPassword.addValidator(new PasswordValidator());
		viewLayout.addComponent(newPassword);

		// confirmPsswdFld
		confirmPassword = new PasswordField("Confirm Password:");
		confirmPassword.setNullRepresentation("");
		confirmPassword.setDescription(MedicalArziConstants.PASSWORD_HINT_TEXT);
		confirmPassword.setRequired(true);
		confirmPassword.setWidth("300px");
		confirmPassword.addValidator(new Validator() {

			private static final long serialVersionUID = -1981709421233991335L;

			@Override
			public void validate(Object value) throws InvalidValueException {
				String password = (String) value;
				if (!password.equals(newPassword.getValue())) {
					throw new InvalidValueException("Passwords must match");
				}
			}
		});
		viewLayout.addComponent(confirmPassword);

		// buttonsLayout
		buttonsLayout = buildButtonsLayout();
		viewLayout.addComponent(buttonsLayout);

		return viewLayout;
	}

	private HorizontalLayout buildButtonsLayout() {

		buttonsLayout = new HorizontalLayout();

		// clearBtn
		backBtn = new Button(new ThemeResource("img/back-button.png"));
		backBtn.setStyleName(Reindeer.BUTTON_LINK);
		backBtn.addClickListener(this);
		backBtn.setImmediate(true);
		buttonsLayout.addComponent(backBtn);
		buttonsLayout.setComponentAlignment(backBtn, Alignment.MIDDLE_RIGHT);

		// registerBtn
		registerBtn = new Button("", this);
		registerBtn.setIcon(new ThemeResource("img/register-button.png"));
		registerBtn.setStyleName(Reindeer.BUTTON_LINK);
		buttonsLayout.addComponent(registerBtn);
		buttonsLayout.setComponentAlignment(registerBtn, Alignment.MIDDLE_LEFT);

		buttonsLayout.setSpacing(true);
		buttonsLayout.setStyleName("ptntRegistrationBtn");

		return buttonsLayout;

	}
	
	/**
	 * This method extracts the properties for the email config and emails the
	 * registration information to the newly registered patient.
	 * 
	 */
	private void emailRegistrationInfoToPatient() {
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


		String formattedSubject = MessageFormat.format(subject,
				itsNumber.getValue());

		String formattedBody = MessageFormat.format(body, new Object[] {
				itsNumber.getValue(), newPassword.getValue() });

		String[] toList = { emailAddress.getValue() };

		// email password
		MAPMail mail = new MAPMail();
		
		logger.debug("Sending registration info. email to email address --> "
				+ emailAddress.getValue());

		mail.sendMail(toList, from, replyTo, host, formattedBody,
				formattedSubject, "Medical Arzi Admin");

		logger.debug("Email successfully sent to email address --> "
				+ emailAddress.getValue());
	}
	

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(backBtn)) {
			MedicalArziUtils.setSessionAttribute(
					"isRegistrationSuccess", false);
			
			getUI().getNavigator().navigateTo(SimpleLoginView.NAME);
		}
		if (event.getButton().equals(registerBtn)) {

			try {
				logger.debug("Validating the user entered passwords.");

				// This is important to avoid the validation error when the page
				// first loads up.
				MedicalArziUtils.installSingleValidator(itsNumber, "itsNumber");
				MedicalArziUtils.installSingleValidator(firstName, "firstName");
				MedicalArziUtils.installSingleValidator(lastName, "lastName");
				MedicalArziUtils.installSingleValidator(dob, "dob");
				MedicalArziUtils.installSingleValidator(emailAddress,
						"emailAddress");

				// FieldGroup buffered mode is on, so commit() is required.
				// Throws CommitException
				ptntRegFormFieldsBinder.commit();

				confirmPassword.validate();

				logger.debug("Passwords are valid. Proceed with registration.");

				Patient patient = ptntRegFormFieldsBinder.getItemDataSource()
						.getBean();
				patient.setPassString(MedicalArziConstants.MAP_ENCODE_PASSWORD_KEY);

				// Check if the patient is already registered
				logger.debug("Check if the patient with ITS number, "
						+ patient.getItsNumber() + " is already registered.");

				if (!patientService.checkIfPtntAlreadyRegistered(patient
						.getItsNumber())) {

					logger.debug("New registration. Calling the registerPatient() on the Patient Service "
							+ "for the patient with ITS number: "
							+ patient.getItsNumber());

					patientService.registerPatient(patient);

					logger.debug("The patient with ITS number: "
							+ patient.getItsNumber()
							+ " is successfully registered.");

					MedicalArziUtils
							.setSessionAttribute(
									MedicalArziConstants.SESS_ATTR_IS_REGISTRATION_SUCCESSFUL,
									true);
					
					emailRegistrationInfoToPatient();

					getUI().getNavigator().navigateTo(SimpleLoginView.NAME);

				} else {
					String errorDescription = "The ITS number, " + patient.getItsNumber()
							+ " is already registered.";
					// Create an error notification if the user is already
					// registered.
					MedicalArziUtils.createAndShowNotification(null,
							errorDescription,
							Type.ERROR_MESSAGE, Position.TOP_LEFT,
							"errorMsg", -1);
				}

			} catch (InvalidValueException e) {
				this.confirmPassword.setValue(null);

				this.confirmPassword.focus();

			} catch (CommitException ce) {
				logger.error(ce);
				
				String errorDescription = "Fields marked with asterisk (*) are required. "
						+ "Please enter the required values and fix the errors before proceeding further.";
				
				// Create an error notification if the required fields are not
				// entered correctly.
				MedicalArziUtils.createAndShowNotification(null,
						errorDescription,
						Type.ERROR_MESSAGE, Position.TOP_LEFT,
						"errorMsg", -1);
				
			}
		}

	}

}
