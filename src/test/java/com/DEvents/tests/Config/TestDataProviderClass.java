package com.DEvents.tests.Config;

import org.testng.annotations.DataProvider;
/**
 * This class provides Test Data to all the API during runtime through TestNG data provider functionality
 * @author shmurali
 * @since Dec 2019
 */
public class TestDataProviderClass {

	/**
	 * To provide Test Data for GetProfileInfo API
	 * @author shmurali
	 * @return Object [][]
	 */
	@DataProvider(name="GetProfileInfo_Input") /*To get all the participants Profile Info*/
	public static Object[][] GetProfileInfo_DataProvider(){

		Object[][] inputData=new Object[1][10];

		//Enter data into Object Array
		inputData[0][0]="/GetProfileInfo?emailId="; 		// Pass the path Query of Request URL
		inputData[0][1]="skumarmalina@deloitte.com";		// Pass the email id for which the request is posted
		inputData[0][2]="200"; 								// Pass the expected Status Code
		inputData[0][3]="application/json; charset=utf-8"; 	// Pass the expected Content Type
		inputData[0][4]="gzip"; 							// Pass the expected Content Encoding
		inputData[0][5]="5";						 		// Pass the expected Response Time
		inputData[0][6]="GetProfileInfo_Schema.json"; 		// Pass the expected Json Schema file name
		inputData[0][7]="FirstName;UserRole;LastName"; 		// Pass the expected data fields(delimited by ;)
		// Pass the SQL query for comparison
		inputData[0][8]="select distinct USER_FIRST_NAME as FirstName, USER_LAST_NAME as LastName, USER_ROLE as UserRole from de_event_users where USER_EMAIL_ID='@PARM'"; 		
		inputData[0][9]="skumarmalina@deloitte.com";		// Pass the email id as parameter for SQL query

		return inputData;
	}

	/**
	 * To provide Test Data for GetEvents API
	 * @author shmurali
	 * @return Object [][]
	 */
	@DataProvider(name="GetEvents_Input") /*To get all the Events Info*/
	public static Object[][] GetEvents_DataProvider(){

		Object[][] inputData=new Object[1][10];

		//Enter data into Object Array
		inputData[0][0]="/GetEvents?emailId="; 				// Pass the path Query of Request URL
		inputData[0][1]="shmurali@deloitte.com";			// Pass the email id for which the request is posted
		inputData[0][2]="200"; 								// Pass the expected Status Code
		inputData[0][3]="application/json; charset=utf-8"; 	// Pass the expected Content Type
		inputData[0][4]="gzip"; 							// Pass the expected Content Encoding
		inputData[0][5]="7";						 		// Pass the expected Response Time
		inputData[0][6]="GetEvents_Schema.json"; 		// Pass the expected Json Schema file name
		inputData[0][7]="EventGmfctnInd;EventCategory;EventType;RcrdCreatnDtm;RcrdCreatnUserId;EventLogo;EventStatus;EventCode;IsSponsor;EventEndDtm;EventApprvInd;EventId;LastUpdtDTM;EventDesc;EventLocation;EventStrtDtm;EventName;EventVenue;EventLogoType;LastUpdtUserId;EventQR;EventGmfctnATTDNCPNTS;EventTargetAudnc;EventVenueAddress;IsGlobal"; 		// Pass the expected data fields(delimited by ;)
		// Pass the SQL query for comparison
		inputData[0][8]="SELECT distinct d.EVENT_ID as EventId,d.EVENT_CODE as EventCode,d.event_name as EventName,d.EVENT_DESC as EventDesc,d.EVENT_CATEGORY as EventCategory,CONCAT(date(d.event_strt_dtm),\"T\",time(d.event_strt_dtm)) AS EventStrtDtm,CONCAT(date(d.EVENT_END_DTM),\"T\",time(d.EVENT_END_DTM)) AS EventEndDtm,d.EVENT_LOCATION as EventLocation,"
				+ "d.EVENT_TYPE as EventType,d.EVENT_VENUE as EventVenue,d.EVENT_VENUE_ADDRS as EventVenueAddress,d.EVENT_GMFCTN_IND as EventGmfctnInd,d.EVENT_GMFCTN_ATTDNC_PNTS as EventGmfctnATTDNCPNTS,d.event_apprv_ind as EventApprvInd,d.EVENT_LOGO as EventLogo,d.EVENT_LOGO_TYPE as EventLogoType,d.EVENT_QR as EventQR,CONCAT(date(d.RCRD_CREATN_DTM),\"T\",time(d.RCRD_CREATN_DTM),\"Z\") AS RcrdCreatnDtm,"
				+ "d.RCRD_CREATN_USER_ID as RcrdCreatnUserId,CONCAT(date(d.LAST_UPDT_DTM),\"T\",time(d.LAST_UPDT_DTM),\"Z\") AS LastUpdtDTM,d.LAST_UPDT_USER_ID as LastUpdtUserId,d.EVENT_TARGET_AUDNC as EventTargetAudnc,case when d.event_end_dtm<CURDATE() then 'COMPLETED' when d.EVENT_STRT_DTM>CURDATE() then 'UPCOMING' when (d.event_end_dtm>CURDATE() and d.EVENT_STRT_DTM<=CURDATE()) then 'ONGOING' END AS EventStatus, "
				+ "case when d.EVENT_TARGET_AUDNC='ALL' then 'true' else 'false' END AS IsGlobal, case when (u.EVENT_USER_ID is not null and u.USER_ROLE in ('PMO', 'SPONSOR')) then 'true' else 'false' end as IsSponsor FROM de_event_registrations r join de_event d left join de_event_users u on r.EVENT_ID=u.EVENT_ID and r.ATTENDEE_EMAIL=u.USER_EMAIL_ID where attendee_email='@PARM' and r.event_id=d.event_id order by EventStatus asc"; 		
		inputData[0][9]="shmurali@deloitte.com";		// Pass the email id as parameter for SQL query

		return inputData;
	}


	/**
	 * To provide Test Data for GetParticipants API
	 * @author shmurali
	 * @return Object [][]
	 */
	@DataProvider(name="GetParticipants_Input") /*To get all the participants UserID*/
	public static Object[][] GetParticipants_DataProvider(){

		Object[][] inputData=new Object[1][8];

		//Enter data into Object Array
		inputData[0][0]="/GetParticipants"; 				// Pass the path Query of Request URL
		inputData[0][1]="200"; 								// Pass the expected Status Code
		inputData[0][2]="application/json; charset=utf-8"; 	// Pass the expected Content Type
		inputData[0][3]="gzip"; 							// Pass the expected Content Encoding
		inputData[0][4]="7";						 		// Pass the expected Response Time
		inputData[0][5]="GetParticipants_Schema.json"; 		// Pass the expected Json Schema file name
		inputData[0][6]="UserId"; 							// Pass the expected data fields(delimited by ;)
		// Pass the SQL query for comparison
		inputData[0][7]="select distinct replace(replace(USER_EMAIL_ID, '@deloitte',''),'.com','') as UserId from devents.de_event_users";
		
		return inputData;
	}
	
	/**
	 * To provide Test Data for GetSessions API
	 * @author shmurali
	 * @return Object [][]
	 */
	@DataProvider(name="GetSessions_Input") /*To get all the Sessions Info*/
	public static Object[][] GetSessions_DataProvider(){

		Object[][] inputData=new Object[1][11];

		//Enter data into Object Array
		inputData[0][0]="/GetSessions?eventId={eventId}&emailId={emailId}"; // Pass the path Query of Request URL
		inputData[0][1]="42";												// Pass the event id for which the request is posted
		inputData[0][2]="shmurali@deloitte.com";							// Pass the email id for which the request is posted
		inputData[0][3]="200"; 												// Pass the expected Status Code
		inputData[0][4]="application/json; charset=utf-8"; 					// Pass the expected Content Type
		inputData[0][5]="gzip"; 											// Pass the expected Content Encoding
		inputData[0][6]="12";						 						// Pass the expected Response Time
		inputData[0][7]="GetSessions_Schema.json"; 							// Pass the expected Json Schema file name
		inputData[0][8]="SessionStartDtm;FclttrName;SessionVenue;EventId;SessionDesc;FclttrDescription;SessionId;UserStatus;SessionName;SessionEndDtm"; 		// Pass the expected data fields(delimited by ;)
		// Pass the SQL query for comparison
		inputData[0][9]="SELECT DKIOSK.KIOSK_ID as SessionId,DKIOSK.EVENT_ID as EventId,DKIOSK.KIOSK_NAME as SessionName, DKIOSK.KIOSK_DESC as SessionDesc,DKIOSK.FCLTTR_NAME as FclttrName, DKIOSK.FCLTTR_DESC as FclttrDescription, CONCAT(DKIOSK.KIOSK_STRT_DT,\"T\",DKIOSK.KIOSK_STRT_TM) AS SessionStartDtm, CONCAT(DKIOSK.KIOSK_END_DT,\"T\",DKIOSK.KIOSK_END_TM) AS SessionEndDtm,DKIOSK.KIOSK_VENUE as SessionVenue, "
				+ "(CASE WHEN (DSESN_ATTD.PRTCPNT_ATTENDANCE_ID IS NOT NULL) THEN 'JOINED' WHEN (DFED.QUESTION_ID IS NOT NULL) THEN 'FEEDBACK SUBMITTED' WHEN (DSESN.SESSION_REGSTN_ID IS NOT NULL AND DSESN_ATTD.PRTCPNT_ATTENDANCE_ID IS NULL) THEN 'REGISTERED' ELSE 'NOT REGISTERED' END) AS UserStatus FROM DE_EVENT_KIOSK DKIOSK INNER JOIN DE_EVENT DEVENT ON DEVENT.EVENT_ID = DKIOSK.EVENT_ID "
				+ "LEFT OUTER JOIN DE_SESSION_REGISTRATIONS DSESN ON DSESN.KIOSK_ID = DKIOSK.KIOSK_ID AND DKIOSK.EVENT_ID = DSESN.EVENT_ID AND DSESN.ATTENDEE_EMAIL = '@PARM' LEFT OUTER JOIN DE_PRTCPNT_ATTENDANCE DSESN_ATTD ON DSESN_ATTD.KIOSK_ID = DKIOSK.KIOSK_ID AND DKIOSK.EVENT_ID = DSESN_ATTD.EVENT_ID AND DSESN_ATTD.ATTENDEE_EMAIL = DSESN.ATTENDEE_EMAIL AND DSESN_ATTD.KIOSK_TYPE_CD = DKIOSK.KIOSK_TYPE_CD "
				+ "LEFT OUTER JOIN DE_FEEDBACK_RESPONSE DFED ON DFED.SESSION_ID = DKIOSK.KIOSK_ID AND DFED.EVENT_ID= DKIOSK.EVENT_ID AND DFED.EMAIL_ID = DSESN.ATTENDEE_EMAIL WHERE DKIOSK.EVENT_ID = @PARM AND DKIOSK.KIOSK_TYPE_CD = 3 GROUP BY DKIOSK.KIOSK_ID"; 		
		inputData[0][10]="shmurali@deloitte.com;42";						// Pass the email-id,event-id as parameter for SQL query

		return inputData;
	}
	
	/**
	 * To provide Test Data for MarkMyAttendance API for Positive Scenario-1
	 * @author shmurali
	 * @return Object [][]
	 */
	@DataProvider(name="MarkMyAttendanceP1_Input") /*To mark attendance for any participant*/
	public static Object[][] MarkMyAttendance_P1_DataProvider(){

		Object[][] inputData=new Object[1][9];

		//Enter data into Object Array
		inputData[0][0]="/MarkMyAttendance?eventId=#&attendeeEmail=APITest@deloitte.com&qrCode=EVNTKSK001"; 		// Pass the path Query of Request URL
		inputData[0][1]="200"; 								// Pass the expected Status Code
		inputData[0][2]="application/json; charset=utf-8"; 	// Pass the expected Content Type
		inputData[0][3]="gzip"; 							// Pass the expected Content Encoding
		inputData[0][4]="5";						 		// Pass the expected Response Time
		inputData[0][5]="MarkMyAttendance_Schema.json"; 		// Pass the expected Json Schema file name
		inputData[0][6]="AttendanceStatus;Id;IsSession"; 		// Pass the expected data fields(delimited by ;)
		// Pass the SQL query for comparison
		inputData[0][7]="SELECT  DISTINCT 'ATTENDENCE SUCCESSFULLY TAKEN' AS AttendanceStatus, EVENT_ID as Id, CASE WHEN (PRTCPNT_ATTENDANCE_ID is NOT  NULL AND KIOSK_TYPE_CD=1) THEN 'false' WHEN (PRTCPNT_ATTENDANCE_ID is NOT NULL AND KIOSK_TYPE_CD=3) THEN 'true' ELSE 'false' END AS IsSession FROM de_prtcpnt_attendance WHERE ATTENDEE_EMAIL='@PARM'"; 		
		inputData[0][8]="APITest@deloitte.com";		// Pass the email id as parameter for SQL query

		return inputData;
	}
	
	/**
	 * To provide Test Data for MarkMyAttendance API for Positive Scenario-2
	 * @author shmurali
	 * @return Object [][]
	 */
	@DataProvider(name="MarkMyAttendanceP2_Input") /*To check attendance if already marked*/
	public static Object[][] MarkMyAttendance_P2_DataProvider(){

		Object[][] inputData=new Object[1][9];

		//Enter data into Object Array
		inputData[0][0]="/MarkMyAttendance?eventId=#&attendeeEmail=APITest@deloitte.com&qrCode=EVNTKSK001"; 		// Pass the path Query of Request URL
		inputData[0][1]="200"; 								// Pass the expected Status Code
		inputData[0][2]="application/json; charset=utf-8"; 	// Pass the expected Content Type
		inputData[0][3]="gzip"; 							// Pass the expected Content Encoding
		inputData[0][4]="5";						 		// Pass the expected Response Time
		inputData[0][5]="MarkMyAttendance_Schema.json"; 		// Pass the expected Json Schema file name
		inputData[0][6]="AttendanceStatus;Id;IsSession"; 		// Pass the expected data fields(delimited by ;)
		// Pass the SQL query for comparison
		inputData[0][7]="SELECT  DISTINCT 'ALREADY ATTENDED' AS AttendanceStatus, EVENT_ID as Id, CASE WHEN (PRTCPNT_ATTENDANCE_ID is NOT  NULL AND KIOSK_TYPE_CD=1) THEN 'false' WHEN (PRTCPNT_ATTENDANCE_ID is NOT NULL AND KIOSK_TYPE_CD=3) THEN 'true' ELSE 'false' END AS IsSession FROM de_prtcpnt_attendance WHERE ATTENDEE_EMAIL='@PARM'"; 		
		inputData[0][8]="APITest@deloitte.com";		// Pass the email id as parameter for SQL query

		return inputData;
	}
	
	/**
	 * To provide Test Data for MarkMyAttendance API for Negative Scenario-1
	 * @author shmurali
	 * @return Object [][]
	 */
	@DataProvider(name="MarkMyAttendanceN1_Input") /*To check when incorrect QR code/Event id is passed*/
	public static Object[][] MarkMyAttendance_N1_DataProvider(){

		Object[][] inputData=new Object[1][9];

		//Enter data into Object Array
		inputData[0][0]="/MarkMyAttendance?eventId=#&attendeeEmail=APITest@deloitte.com&qrCode=EVNTKSK001"; 		// Pass the path Query of Request URL
		inputData[0][1]="200"; 								// Pass the expected Status Code
		inputData[0][2]="application/json; charset=utf-8"; 	// Pass the expected Content Type
		inputData[0][3]="gzip"; 							// Pass the expected Content Encoding
		inputData[0][4]="5";						 		// Pass the expected Response Time
		inputData[0][5]="MarkMyAttendance_Schema.json"; 		// Pass the expected Json Schema file name
		inputData[0][6]="AttendanceStatus;Id;IsSession"; 		// Pass the expected data fields(delimited by ;)
		// Pass the SQL query for comparison
		inputData[0][7]="SELECT  DISTINCT 'INVALID QR CODE' AS AttendanceStatus, a.EVENT_ID as Id, CASE WHEN (a.PRTCPNT_ATTENDANCE_ID is NOT  NULL AND a.KIOSK_TYPE_CD=1) THEN 'false' WHEN (a.PRTCPNT_ATTENDANCE_ID is NOT NULL AND a.KIOSK_TYPE_CD=3 and b.PRTCPNT_ATTENDANCE_ID is NOT NULL) THEN 'true' ELSE 'false' END AS IsSession FROM de_prtcpnt_attendance a left join de_prtcpnt_attendance b on a.EVENT_ID=b.EVENT_ID and b.ATTENDEE_EMAIL='APITest@deloitte.com' WHERE a.EVENT_ID=@PARM"; 		
		inputData[0][8]="40";		// Pass the email id as parameter for SQL query

		return inputData;
	}
	
	/**
	 * To provide Test Data for Register Or Cancel Sessions API for Negative Scenario-1
	 * @author shmurali
	 * @return Object [][]
	 */
	@DataProvider(name="RegisterOrCancelSessionsP1_Input") /*To check Registration for Sessions */
	public static Object[][] RegisterOrCancelSessions_P1_DataProvider(){

		Object[][] inputData=new Object[1][5];

		//Enter data into Object Array
		inputData[0][0]="/RegisterOrCancelSessions?eventId=e#&kioskId=k#&attendeeEmail=APITest@deloitte.com&isRegister=1"; 		// Pass the path Query of Request URL
		inputData[0][1]="200"; 								// Pass the expected Status Code
		inputData[0][2]="5";						 		// Pass the expected Response Time
		// Pass the SQL query for comparison
		inputData[0][3]="select ATTENDEE_EMAIL, SESSION_REGSTN_ID from de_session_registrations where session_regstn_id in (select max(session_regstn_id) from de_session_registrations)";
		inputData[0][4]="APITest@deloitte.com";									// Pass the SQL parameters
		
		return inputData;
	}
	
	/**
	 * To provide Test Data for Register Or Cancel Sessions API for Negative Scenario-1
	 * @author shmurali
	 * @return Object [][]
	 */
	@DataProvider(name="RegisterOrCancelSessionsN1_Input") /*To check Cancellations of Session registration*/
	public static Object[][] RegisterOrCancelSessions_N1_DataProvider(){

		Object[][] inputData=new Object[1][5];

		//Enter data into Object Array
		inputData[0][0]="/RegisterOrCancelSessions?eventId=e#&kioskId=k#&attendeeEmail=APITest@deloitte.com&isRegister=0"; 		// Pass the path Query of Request URL
		inputData[0][1]="200"; 								// Pass the expected Status Code
		inputData[0][2]="5";						 		// Pass the expected Response Time
		// Pass the SQL query for comparison
		inputData[0][3]="select ATTENDEE_EMAIL, SESSION_REGSTN_ID from de_session_registrations where session_regstn_id in (select max(session_regstn_id) from de_session_registrations)";
		inputData[0][4]="APITest@deloitte.com";									// Pass the SQL parameters
		
		return inputData;
	}
	
	/**
	 * To provide Test Data for GetFeedbackQuestions API
	 * @author shmurali
	 * @return Object [][]
	 */
	@DataProvider(name="GetFeedbackQuestions_Input") /*To get all the feedback questions for a session*/
	public static Object[][] GetFeedbackQuestions_DataProvider(){

		Object[][] inputData=new Object[1][9];

		//Enter data into Object Array
		inputData[0][0]="GetFeedbackQuestions?eventId={eventId}&sessionId={sessionId}"; // Pass the path Query of Request URL
		inputData[0][1]="200"; 															// Pass the expected Status Code
		inputData[0][2]="application/json; charset=utf-8"; 								// Pass the expected Content Type
		inputData[0][3]="gzip"; 														// Pass the expected Content Encoding
		inputData[0][4]="7";						 									// Pass the expected Response Time
		inputData[0][5]="GetFeedbackQuestions_Schema.json"; 							// Pass the expected Json Schema file name
		inputData[0][6]="FeedBackQuestionId;FeedBackQuestion"; 							// Pass the expected data fields(delimited by ;)
		// Pass the SQL query for comparison
		inputData[0][7]="select distinct QUESTION_ID as FeedBackQuestionId, QUESTION_TEXT as FeedBackQuestion from de_feedback_questions where EVENT_ID=@PARM and SESSION_ID=@PARM order by QUESTION_ID asc";
		inputData[0][8]="44;26";														// Pass the SQL parameters
		return inputData;
	}
}
