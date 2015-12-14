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
	
	//Session and Request Param Keys
	final static public String SESS_ATTR_PTNT_ITS_NUM = "patientITSNumber";
	
	final static public String SESS_ATTR_PTNT_INFO = "patientInfo";
	
	final static public String SESS_ATTR_IS_REGISTRATION_SUCCESSFUL = "isRegistrationSuccess";
	
	final static public String REQ_ATTR_PASSWD_EMAILED = "passwordEmailedSuccessfully";

	final static public String MAP_PROPERTIES = "com.example.medicalarzi.resources.MAPConf";
	
	//Lookup types & value constants
	final static public String MAP_DAWAT_TITLE = "DAWAT TITLE";
	
	final static public String MAP_GENDER = "GENDER";
	
	final static public String MAP_DAWAT_TITLE_BHAI = "Bhai";
	
	final static public String MAP_DAWAT_TITLE_BEHEN = "Behen";
	
	final static public Long ARZI_DRAFT_STATUS = 1000L;
	
	final static public Long ARZI_SUBMITTED_STATUS = 1001L;
	
	final static public Integer MAP_GENDER_MALE_ID = 1005;
	
	final static public Integer MAP_GENDER_FEMALE_ID = 1006;
	
	final static public Integer MAP_OTHER_CONDITION_ID = 3016;

	// For sending email.
	final static public String EMAIL_HOST = "EmailHost";

	final static public String FORGOT_PSWD_EMAIL_FROM = "FP_EmailFrom";

	final static public String FORGOT_PSWD_EMAIL_REPLY_TO = "FP_EmailReplyTo";

	final static public String FORGOT_PSWD_EMAIL_TO = "FP_EmailTo";

	final static public String FORGOT_PSWD_EMAIL_BCC = "FP_EmailBcc";

	final static public String FORGOT_PSWD_EMAIL_SUBJECT = "FP_EmailSubject";

	final static public String FORGOT_PSWD_EMAIL_BODY = "FP_EmailBody";
	
	//Component Ids
	final static public String HEADER_LOGOUT_BUTTON_ID = "header_logoutBtn";
	
	final static public String HEADER_LOGGED_IN_PTNT_NAME = "header_loggedInPtntName";
	
	final static public String CUSTOM_FORM_LEFTFORM_LAYOUT_ID = "customForm_leftFormLayout";
	
	final static public String CUSTOM_FORM_RIGHTFORM_LAYOUT_ID = "customForm_rightFormLayout";
	
	final static public String NEW_ARI_TAB_COMPONENT_ID = "newArziTabComponent";
	
	final static public String INBOX_TAB_COMPONENT_ID = "inboxTabCompoent";
	
	final static public String SEARCH_TAB_COMPONENT_ID = "searchTabCompoent";
	
	final static public String NEW_ARZI_TAB_CAPTION = "New Arzi";
	
	final static public String INBOX_TAB_CAPTION = "Inbox";
	
	final static public String SEARCH_TAB_CAPTION = "Search";
	
	//Hint texts, descriptions or any static texts
	final static public String PASSWORD_HINT_TEXT = "Password must be at least 8 characters long and contain at least 1 number.";
	
	final static public Integer USER_FRIENDLY_MSG_DELAY_MSEC =  4000;

}
