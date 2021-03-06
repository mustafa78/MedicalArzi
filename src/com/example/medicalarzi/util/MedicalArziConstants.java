/**
 *
 */
package com.example.medicalarzi.util;

/**
 * @author Mkanchwa
 *
 */
public interface MedicalArziConstants {

	// Keys
	final static public String MAP_ENCODE_PASSWORD_KEY = "MUFADDAL_MOULA_53";
	
	// Session and Request Param Keys
	final static public String SESS_ATTR_PTNT_ITS_NUM = "patientITSNumber";
	
	final static public String SESS_ATTR_PTNT_INFO = "patientInfo";
	
	final static public String SESS_ATTR_IS_REGISTRATION_SUCCESSFUL = "isRegistrationSuccess";
	
	final static public String REQ_ATTR_PASSWD_EMAILED = "passwordEmailedSuccessfully";

	final static public String MAP_PROPERTIES = "com.example.medicalarzi.resources.MAPConf";
	
	/**Lookup types & value constants**/
	//  Lookup Types
	final static public String MAP_DAWAT_TITLE = "DAWAT TITLE";
	
	final static public String SEARCH_NUM_OF_DAYS = "NUM OF DAYS SEARCH";
	
	final static public String MAP_GENDER = "GENDER";
	
	// Lookup Vals
	final static public String MAP_DAWAT_TITLE_BHAI = "Bhai";
	
	final static public String MAP_DAWAT_TITLE_BEHEN = "Behen";
	
	// Database Lookup Ids
	final static public Long ARZI_DRAFT_STATUS = 1000L;
	
	final static public Long ARZI_SUBMITTED_STATUS = 1001L;
	
	final static public Long ARZI_IN_PROCESS_STATUS = 1002L;
	
	final static public Long ARZI_DOCTOR_APPROVED_STATUS = 1006L;
	
	final static public Integer MAP_GENDER_MALE_ID = 1005;
	
	final static public Integer MAP_GENDER_FEMALE_ID = 1006;
	
	final static public Integer SEARCH_PERIOD_LAST_3_DAYS_ID = 1007;
	
	final static public Integer SEARCH_PERIOD_3_TO_7_DAYS_ID = 1008;
	
	final static public Integer SEARCH_PERIOD_7_TO_15_DAYS_ID = 1009;
	
	final static public Integer SEARCH_PERIOD_15_TO_30_DAYS_ID = 1010;
	
	final static public Integer SEARCH_PERIOD_30_DAYS_OR_OLDER_ID = 1011;
	
	// For sending email.
	final static public String EMAIL_HOST = "EmailHost";

	final static public String FORGOT_EMAIL_FROM = "EmailFrom";

	final static public String FORGOT_EMAIL_REPLY_TO = "EmailReplyTo";

	final static public String FORGOT_PSWD_EMAIL_TO = "FP_EmailTo";

	final static public String FORGOT_PSWD_EMAIL_BCC = "FP_EmailBcc";

	final static public String FORGOT_PSWD_EMAIL_SUBJECT = "FP_EmailSubject";

	final static public String FORGOT_PSWD_EMAIL_BODY = "FP_EmailBody";
	
	// Component Ids
	final static public String ARZI_LANDING_VIEW_TABSHEET = "arziLandingView";
	
	final static public String HEADER_LOGOUT_BUTTON_ID = "header_logoutBtn";
	
	final static public String HEADER_LOGGED_IN_PTNT_NAME = "header_loggedInPtntName";
	
	final static public String CUSTOM_FORM_LEFTFORM_LAYOUT_ID = "customForm_leftFormLayout";
	
	final static public String CUSTOM_FORM_RIGHTFORM_LAYOUT_ID = "customForm_rightFormLayout";
	
	final static public String NEW_ARZI_TAB_CAPTION = "New Arzi";
	
	final static public String NEW_ARZI_TAB_COMPONENT_ID = "newArziTabComponent";
	
	final static public String ARZI_FORM_COMPONENT_VIEW_LAYOUT_ID = "arziFormComponentViewLayout";
	
	final static public String ARZI_FORM_COMPONENT_BUTTON_LAYOUT_ID = "arziFormComponentButtonsLayout";
	
	final static public String ARZI_FORM_COMPONENT_SAVE_BUTTON_ID = "arziFormComponentSaveBtn";
	
	final static public String ARZI_FORM_COMPONENT_SUBMIT_BUTTON_ID = "arziFormComponentSubmitBtn";
	
	final static public String ARZI_FORM_COMPONENT_DELETE_BUTTON_ID = "arziFormComponentDeleteBtn";
	
	final static public String ARZI_FORM_COMPONENT_COUNTRY_COMBOBOX_ID = "arziFormComponentCountryCombobox";
	
	final static public String ARZI_FORM_COMPONENT_STATE_COMBOBOX_ID = "arziFormComponentStateCombobox";
	
	final static public String ARZI_FORM_COMPONENT_CITY_COMBOBOX_ID = "arziFormComponentCityCombobox";
	
	final static public String ARZI_FORM_COMPONENT_MED_HIST_DIABETES_ID = "arziFormComponentMedHistDiabetes";
	
	final static public String ARZI_FORM_COMPONENT_MED_HIST_CANCER_ID = "arziFormComponentMedHistCancer";
	
	final static public String ARZI_FORM_COMPONENT_MED_HIST_HEART_DISEASE_ID = "arziFormComponentMedHistHeartDisease";
	
	final static public String REVIEW_ARZI_COMPONENT_ID = "reviewArziComponent";
	
	final static public String REVIEW_ARZI_COMPONENT_VIEW_LAYOUT_ID = "reviewArziComponentViewLayout";
	
	final static public String INBOX_TAB_CAPTION = "Inbox";
	
	final static public String INBOX_TAB_COMPONENT_ID = "inboxTabComponent";
	
	final static public String INBOX_COMPONENT_VIEW_LAYOUT_ID = "inboxComponentViewLayout";
	
	final static public String INBOX_COMPONENT_DELETE_ARZI_BUTTON = "inboxComponentDeleteArziBtn";
	
	final static public String INBOX_COMPONENT_SUBMIT_ARZI_BUTTON = "inboxComponentSubmitArziBtn";
	
	final static public String SEARCH_TAB_CAPTION = "Search";
	
	final static public String SEARCH_TAB_COMPONENT_ID = "searchTabComponent";
	
	final static public String ADMIN_TAB_CAPTION = "Administration";
	
	final static public String ADMIN_TAB_COMPONENT_ID = "adminTabComponent";
	
	final static public String REVIEW_ARZI_TAB_CAPTION = "Review Arzi";
	
	final static public String REVIEW_ARZI_TAB_COMPONENT_ID = "Review_Arzi_Component";
	
	final static public String EDIT_ARZI_TAB_CAPTION = "Edit Arzi";
	
	final static public String EDIT_ARZI_TAB_COMPONENT_ID = "Edit_Arzi_Component";
	
	final static public String VIEW_ARZI_TAB_CAPTION = "View Arzi";
	
	final static public String VIEW_ARZI_TAB_COMPONENT_ID = "View_Arzi_Component";		
	
	final static public String PENDING_TASKS_TAB_CAPTION = "Pending Tasks";
	
	final static public String PENDING_TASKS_TAB_COMPONENT_ID = "pendingTaskComponent";
	
	final static public String PENDING_TASKS_COMPONENT_VIEW_LAYOUT_ID = "pendingTasksComponentViewLayout";
	
	// Hint texts, descriptions or any static texts
	final static public String PASSWORD_HINT_TEXT = "Password must be at least 8 characters long and contain at least 1 number.";
	
	final static public Integer USER_FRIENDLY_MSG_DELAY_MSEC =  4000;
	
	final static public Long SEC_ROLE_ID_DOCTOR = 1001L;
	
	final static public Long SEC_ROLE_ID_QUEUE_MGR = 1002L;

	final static public Long SEC_ROLE_ID_ADMIN = 1004L;
	
	// Database ids
	final static public Long CONDITION_OTHER_ID = -3L;
	
	final static public Long PROCEDURE_OTHER_ID = -3L;
	
	final static public Long BODY_PART_OTHER_ID = -3L;
}
