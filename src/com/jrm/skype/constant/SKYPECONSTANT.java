
package com.jrm.skype.constant;

public class SKYPECONSTANT {
    public static class SKYPEACTION {
        public final static String SKYPE_SERVICE = "skype_service";
        
        public final static String SKYPE_EXCEPTION_SERVICE = "skype_exception_service";
        
        public final static String USRACCOUNTACTIVITY = "UsrAccountActivity";
        
        public final static String SIGNINACTIVITY = "SigninActivity";
        
        public final static String INCOMINGCALLACTIVITY = "IncomingCallActivity";
        
        public final static String CREATING_ONLINESTATUS_CHANGE = "creatingonlineStatusChange";
        
        public final static String SIGNING_ONLINESTATUS_CHANGE = "signingonlineStatusChange";
        
        public final static String SIGNOUT_ONLINESTATUS_CHANGE = "signoutonlineStatusChange";
        
        public final static String LOADING_ONLINESTATUS_CHANGE = "loadingnlineStatusChange";

        public final static String MAINACTION = "main_action";
        
        public final static String SEARCHSTATUS_CHANGE = "searchstatuschange";
        
        public final static String CONTACT_VOICEMAILSTATUS_CHANGE = "contactvoicemailstatuschange"; 
        
        public final static String HISTORY_VOICEMAILSTATUS_CHANGE = "historyvoicemailstatuschange"; 
        
        public final static String CONTACT_CONVSTATUS_CHANGE = "contactconvstatuschange";
        
        public final static String HISTORY_CONVSTATUS_CHANGE = "historyconvstatuschange";
        
        public final static String SKYPEOUT_CONVSTATUS_CHANGE = "skypeoutconvstatuschange";
        
        public final static String INCALL_DIALOG_CONVSTATUS_CHANGE = "incalldialogconvstatuschange";
        
        public final static String INCALL_PSTN_CONVSTATUS_CHANGE = "incallpstnconvstatuschange";
        
        public final static String INCALL_CONFERENCE_CONVSTATUS_CHANGE = "incallconferenceconvstatuschange";
        
        public final static String INCOMINGCALL_CONVSTATUS_CHANGE = "incomingcallconvstatuschange";
        
        public final static String SKYPEKITCONNECT_CHANGE = "skypekitconnectchange";
        
        public final static String PASSWORD_CHANGE = "passwordchange";
        
        public final static String VIDEO_STATUS_CHANGE = "videostatuschange";
        
        public final static String CAMERA_IN = "com.mstar.android.intent.action.CAMERA_PLUG_IN";
        
        public final static String CAMERA_OUT = "com.mstar.android.intent.action.CAMERA_PLUG_OUT";
        
        public final static String MIC_IN = "com.mstar.android.intent.action.MICROPHONE_PLUG_IN";
        
        public final static String MIC_OUT = "com.mstar.android.intent.action.MICROPHONE_PLUG_OUT";
    }
    
    public static class MAINACTION {
        public final static int GETBUDDYLIST = 0;
        
        public final static int BUDDYPROPERTYCHANGE = 1;
        
        public final static int AUTHREQUEST = 2;
        
        public final static int VOICEMAIL = 3;
        
        public final static int MESSAGE = 4;
        
        public final static int AVATAR = 5;
        
        public final static int AVAILABILITY = 6;
        
        public final static int MOODTEXT = 7;
        
        public final static int FULLNAME = 8;
        
        public final static int BLANCE = 9;
        
        public final static int SYNCFAILED = 10;
        
        public final static int UPDATE = 11;
    }
    
    public static class USRACCOUNTFOCUS {
        public final static int CONTACTLIST = 0;

        public final static int HISTORYLIST = 1;

        public final static int SKYPEOUT = 2;

        public final static int OPTIONS = 3;
    }

    public static class USRACCOUNTDIALOG {
        public final static int USR_STATUS = 0;

        public final static int SIGN_OUT = 1;

        public final static int ADD_CONTACT_TYPE = 2;

        public final static int ADD_SKYPE_CONTACT = 3;

        public final static int SEARCH_RESULT = 4;

        public final static int ADD_PHONE_NUMBER = 5;

        public final static int COUNTRYCODE_ADD = 6;

        public final static int INVITATION = 7;

        public final static int CHAT = 8;
        
        public final static int CONTACTITEM = 9;
        
        public final static int HISTORYITEM = 10;
        
        public final static int SKYPEOUTCALLOUT = 11;
        
        public final static int COUNTRYCODE_CALL = 12;

        public final static int DIALPAD = 13;
    }

    public static class USRACCOUNTPOPDIALOG {
        public final static int MIDPROFILEREL = 1;

        public final static int MIDSENDVMREL = 2;

        public final static int MIDLISTENVMREL = 3;

        public final static int MIDADDTOCONTACTREL = 4;

        public final static int MIDDELETEITEMREL = 5;

        public final static int MIDVVCALLREL = 6;
        
        public final static int MIDBLOCK = 7;
        
        public final static int MIDREMOVE = 8;
    }

    public static class OPTIONSACTIVITYFOCUS {
        public final static int USERPROFILE = 0;

        public final static int CHANGEPASSWORD = 1;

        public final static int GENERALSETTINGS = 2;

        public final static int AUDIOSETTINGS = 3;

        public final static int PRIVACYSETTINGS = 4;

        public final static int BLOCKEDCONTACTS = 5;

        public final static int CALLSETTINGS = 6;

        public final static int ABOUTSKYPE = 7;
    }

    public static class OPTIONSDIALOG {
        public final static int USERPROFILE = 0;

        public final static int CALLSETTINGS = 1;

        public final static int CHANGE_PICTURE = 2;
        
        public final static int UNBLOCK_CONTACTS = 3;
    }

    public static class OPTIONSPHONEFOCUS {
        public final static int HOMEPHONE = 0;

        public final static int OFFICEPHONE = 1;

        public final static int MOBILEPHONE = 2;

        public final static int FARWARDONE = 3;

        public final static int FARWARDTWO = 4;

        public final static int FARWARDTHREE = 5;
    }
    
    public static class OPTIONSPICTURE {
        public final static int FROM_CAMERA = 0;

        public final static int FROM_ALBUM = 1;
    }

    public static class CONVTYPE {
        public final static int VOICEMAILNEW = 0;

        public final static int VOICEMAIL = 1;

        public final static int CALLOUT = 2;

        public final static int CALLIN = 3;

        public final static int CALLMISSED = 4;

        public final static int CONFERENCEIN = 5;

        public final static int CONFERENCEMISSED = 6;
        
        public final static int NEWMESSAGE = 7;
        
        public final static int OLDMESSAGE = 8;
    }

    public static class USRACCOUNTNOTIFY {
        public final static int NONE = 0;

        public final static int VOICEMAIL = 1;

        public final static int INVITATION = 2;
    }

    public static class SKYPESTRING {
        public final static String SKYPEPREFRENCES = "skypeprefrences";
        
        public final static String HASSIGNIN = "hassignin";
        
        public final static String ISINCONV = "isinconv";
        
        public final static String FIRSTSTART = "firststart";
        
        public final static String RESTARTKIT = "restartkit";
        
        public final static String KITCONNECT = "kitconnect";
        
        public final static String DISPLAYNAME = "displayname";
        
        public final static String VOICEMAILID = "voicemailid";
        
        public final static String CONVNAME = "convname";
        
        public final static String ISVIDEOPLAYING = "isvideoplaying";
        
        public final static String LOCALVIDEO = "localvideo";
        
        public final static String ISPSTN = "ispstn";
        
        public final static String CONVPARTNER = "convpartner";
        
        public final static String CONVTIME = "convtime";
        
        public final static String CONVBODY = "convbody";
        
        public final static String CONVTYPE = "conversationtype";
        
        public final static String CALLTYPE = "calltype";
        
        public final static String CUSTOMERS = "customers";
        
        public final static String SKYPENAME = "skypename";
        
        public final static String PASSWORD = "password";
        
        public final static String SAVEPASSWORD = "savepassword";
        
        public final static String CREATEFULLNAME = "createfullname";
        
        public final static String CREATEPASSWORD = "createpassword";
        
        public final static String CREATESKYPENAME = "createskypename";

        public final static String CREATEEMAIL = "createemail";
        
        public final static String CREATEACCOUNT = "createaccount";
        
        public final static String SENDSKNEWS = "sendnews";

        public final static String SIGNINSKTSTART = "signinsktstart";

        public final static String STARTSKTTVSTART = "startskttvstart";
        
        public final static String REQUESTCONTACTLIST = "RequestContactList";

        public final static String ALREADSIGNIN = "alreadysignin";

        public final static String RINGINGVOLUME = "ringingvolume";
        
        public final static String LOGINTIP = "logintip";
        
        public final static String CREATETIP = "createtip";
        
        public final static String SEARCHSTRING = "searchstring";
        
        public final static String NOTIFY = "notify";
        
        public final static String VOICEMAILSTATUS = "voicemailstatus";
        
        public final static String PHONENUMBER = "phonenumber";
        
        public final static String VIDEO_ACTNAME = "videoactname";
    }
    
    public static class CALLTYPE{
        public final static int DIALOG = 0;
        
        public final static int CONFERENCE = 1;
    }
    
    public static class SEARCHSTATUS{
        public final static int PENDING = 0;
        
        public final static int FINISHED = 1;
        
        public final static int FAILED = 2;
    }
    
    public static class VOICEMAILSTATUS{
        public final static int INITIALIZE = 0;
        
        public final static int RECORDING = 1;
        
        public final static int SENDED = 2;
        
        public final static int RECORDING_PROGRESS = 3;
        
        public final static int PLAYING = 4;
        
        public final static int PLAYBACK_PROGRESS = 5;
        
        public final static int PLAYED = 6;
        
        public final static int CANCELLED_RECORD_WHEN_INCOMING_CALL = 7;
        
        public final static int CANCELLED_PLAYBACK_WHEN_INCOMING_CALL = 8;
        
        public final static int CANCELLED = 9;
        
        public final static int SENDFAILED = 10;
        
        public final static int RECFAILED = 11;
    }
    
    public static class CONVERSATIONSTATUS {
        public final static int IM_LIVE = 0;
        
        public final static int RECENTLY_LIVE = 1; 
        
        public final static int ON_HOLD_REMOTELY = 2;
        
        public final static int OTHERS_ARE_LIVE = 3;
        
        public final static int PARTICIPANTSCHANGE = 4;
    }
    
    public static class VIDEOSTATUS {
        public final static int RUNNING = 0;
        
        public final static int STOPPED = 1;
        
        public final static int PAUSED = 2;
        
        public final static int AVAILABLE = 3;
        
        public final static int STARTING = 4;
    }
    
    public static class HISTORYSAVEDAYS{
        public final static int TWO_WEEKS = 14;
        
        public final static int ONE_MONTH = 30;
        
        public final static int THREE_MONTHS = 90;
        
        public final static int FOREVER = -1;
    }
}
