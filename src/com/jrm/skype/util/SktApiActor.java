
package com.jrm.skype.util;

import java.util.ArrayList;
import com.jrm.skype.util.Log;
import android.view.Surface;
import com.jrm.skype.api.SktAccount;
import com.jrm.skype.api.SktContact;
import com.jrm.skype.api.SktConversation;
import com.jrm.skype.api.SktOption;
import com.jrm.skype.api.SktOption.HISTORY_SAVE_POLICY;
import com.jrm.skype.api.SktSkypeApp;
import com.jrm.skype.api.SktContact.SktAuthItem;
import com.jrm.skype.api.SktContact.SktBuddy;
import com.skype.api.Account;
import com.skype.api.Contact;
import com.skype.api.ContactSearch;
import com.skype.api.Video;
import com.skype.api.Skype.VALIDATERESULT;
import com.skype.api.Skype.ValidateAvatarResult;
import com.skype.api.Skype.ValidateProfileStringResult;

/**
 * execute the api
 * 
 * @author andy.liu
 */
public class SktApiActor {
    private final static String TAG = "SktApiActor";
    
    private static SktSkypeApp skypeInstance;

    private static SktAccount skypeAccount;

    private static SktContact skypeContact;

    private static SktConversation skypeConversation;

    private static SktOption skypeOption;

    public static void initApi() {
        skypeInstance = SktSkypeApp.getInstance();

        if (null != skypeInstance) {
            skypeAccount = skypeInstance.getAccount();
            skypeContact = skypeInstance.getContact();
            skypeConversation = skypeInstance.getConversation();
            skypeOption = skypeInstance.getOption();
        }else{
            Log.e(TAG, "initApi-----failed!");
        }
    }
    
    public static void release(){
        skypeAccount = null;
        skypeContact = null;
        skypeConversation = null;
        skypeOption = null;
        skypeInstance = null;
    }

    // SktSkypeApp---------------------------------------------------------------->
    public static void setPlayerSurface(Surface playerSurface){
        if (null != skypeInstance && null != playerSurface) {
            skypeInstance.setPlayerSurface(playerSurface);
        }
    }
    
    public static void setPreviewSurface(Surface previewSurface){
        if (null != skypeInstance && null != previewSurface) {
            skypeInstance.setPreviewSurface(previewSurface);
        }
    }
    
    public static void updateSurface(){
        if (null != skypeInstance) {
            skypeInstance.updateSurface();
        }
    }
    
    public static void startAudioIn(){
        if (null != skypeInstance) {
            skypeInstance.startAudioIn();
        }
    }
    
    public static void stopAudioIn(){
        if (null != skypeInstance) {
            skypeInstance.stopAudioIn();
        }
    }
    
    public static void stopRemoteVideoJni(){
        if (null != skypeInstance) {
            skypeInstance.stopRemoteVideo();
        }
    }
    
    public static void stopLocalVideoJni(){
        if (null != skypeInstance) {
            skypeInstance.stopLocalVideo();
        }
        
    }
    
    public static boolean isLoggedIn() {
        if (null != skypeInstance) {
            return skypeInstance.isLoggedIn();
        }
        return false;
    }

    public static ValidateProfileStringResult checkSkypeName(String skypeName,
            boolean forRegistration) {
        if (null != skypeInstance) {
            return skypeInstance.checkSkypeName(skypeName, forRegistration);
        }
        return null;
    }

    public static VALIDATERESULT checkPassword(String actName, String pwd) {
        if (null != skypeInstance) {
            return skypeInstance.checkPassword(actName, pwd);
        }
        return null;
    }

    public static ValidateProfileStringResult checkEmail(String email, boolean forRegistration) {
        if (null != skypeInstance) {
            return skypeInstance.checkEmail(email, forRegistration);
        }
        return null;
    }

    public static ValidateAvatarResult checkAvatar(byte[] avatar) {
        if (null != skypeInstance && null != avatar) {
            return skypeInstance.checkAvatar(avatar);
        }
        return null;
    }
    
    public static boolean checkPSTNNumberBool(String number, String code){
        if (null != skypeInstance && null != number && null != code) {
            return skypeInstance.checkPSTNNumberBool(number,code);
        }
        return false;
    }

    // SktAccount------------------------------------------------------------------>
    public static boolean login(String actName, String password, boolean savePwd) {
        if (null != skypeAccount) {
            return skypeAccount.login(actName, password, savePwd);
        }
        return false;
    }

    public static void logout(boolean clearPwd) {
        if (null != skypeAccount) {
            skypeAccount.logout(clearPwd);
        }
    }

    public static boolean createNewAccount(String actName, String password, boolean savePwd,
            String email, boolean allowSpam) {

        if (null != skypeAccount) {
            return skypeAccount.createNewAccount(actName, password, true, email, allowSpam);
        }
        return false;
    }

    public static void cancelCreateAccount() {
        if (null != skypeAccount) {
            skypeAccount.cancelCreateNewAccount();
        }
    }
    
    public static void changePassword(String oldPwd, String newPwd){
        if (null != skypeAccount && null != oldPwd && null != newPwd) {
            skypeAccount.changePassword(oldPwd,newPwd);
        }
    }

    public static String getMoodText() {
        if (null != skypeAccount) {
            return skypeAccount.getMoodText();
        }
        return "";
    }
    
    public static void setMoodText(String text) {
        if (null != skypeAccount && null != text) {
            skypeAccount.setMoodText(text);
        }
    }
    
    public static String getEmail(){
        if (null != skypeAccount) {
            return skypeAccount.getEmail();
        }
        return "";
    }
    
    public static void setEmail(String email) {
        if (null != skypeAccount && null != email) {
            skypeAccount.setEmail(email);
        }
    }
    
    public static String getHomePage(){
        if (null != skypeAccount) {
            return skypeAccount.getHomePage();
        }
        return "";
    }
    
    public static void setHomePage(String homePage) {
        if (null != skypeAccount && null != homePage) {
            skypeAccount.setHomePage(homePage);
        }
    }
    
    public static String getLanguage(){
        if (null != skypeAccount) {
            return skypeAccount.getLanguage();
        }
        return "";
    }
    
    public static void setLanguage(String language) {
        if (null != skypeAccount && null != language) {
            skypeAccount.setLanguage(language);
        }
    }
    
    public static String getBirthday(){
        if (null != skypeAccount) {
            return skypeAccount.getBirthday()+"";
        }
        return "";
    }
    
    public static void setBirthday(int birthday) {
        if (null != skypeAccount) {
            skypeAccount.setBirthday(birthday);
        }
    }
    
    public static int getGender(){
        if (null != skypeAccount) {
            return skypeAccount.getGender();
        }
        return 0;
    }
    
    public static void setGender(int gender) {
        if (null != skypeAccount) {
            skypeAccount.setGender(gender);
        }
    }
    
    public static String getCountry(){
        if (null != skypeAccount) {
            return skypeAccount.getCountry();
        }
        return "";
    }
    
    public static void setCountry(String country) {
        if (null != skypeAccount && null != country) {
            skypeAccount.setCountry(country);
        }
    }
    
    public static String getProvice(){
        if (null != skypeAccount) {
            return skypeAccount.getProvice();
        }
        return "";
    }
    
    public static void setProvice(String provice) {
        if (null != skypeAccount && null != provice) {
            skypeAccount.setProvice(provice);
        }
    }
    
    public static String getCity(){
        if (null != skypeAccount) {
            return skypeAccount.getCity();
        }
        return "";
    }
    
    public static void setCity(String city) {
        if (null != skypeAccount && null != city) {
            skypeAccount.setCity(city);
        }
    }

    public static String getHomePhone(){
        if (null != skypeAccount) {
            return skypeAccount.getHomePhone();
        }
        return "";
    }
    
    public static void setHomePhone(String homePhone) {
        if (null != skypeAccount && null != homePhone) {
            skypeAccount.setHomePhone(homePhone);
        }
    }
    
    public static String getOfficePhone(){
        if (null != skypeAccount) {
            return skypeAccount.getOfficePhone();
        }
        return "";
    }
    
    public static void setOfficePhone(String officePhone) {
        if (null != skypeAccount && null != officePhone) {
            skypeAccount.setOfficePhone(officePhone);
        }
    }
    
    public static String getMobilePhone(){
        if (null != skypeAccount) {
            return skypeAccount.getMobilePhone();
        }
        return "";
    }
    
    public static void setMobilePhone(String mobilePhone) {
        if (null != skypeAccount && null != mobilePhone) {
            skypeAccount.setMobilePhone(mobilePhone);
        }
    }
    
    public static void setFullName(String fname) {
        if (null != skypeAccount && null != fname) {
            skypeAccount.setFullName(fname);
        }
    }

    public static String getFullName() {
        if (null != skypeAccount) {
            return skypeAccount.getFullName();
        }
        return "";
    }

    public static void setAccountAvatar(byte[] avatar) {
        if (null != skypeAccount && null != avatar) {
            skypeAccount.setAvatar(avatar);
        }
    }

    public static byte[] getAccountAvatar() {
        if (null != skypeAccount) {
            return skypeAccount.getAvatar();
        }
        return null;
    }

    public static String getAccountName() {
        if (null != skypeAccount) {
            return skypeAccount.getAccountName();
        }
        return "";
    }

    public static String getCurrency() {
        if (null != skypeAccount) {
            return skypeAccount.getCurrency();
        }
        return "";
    }

    public static float getBalance() {
        if (null != skypeAccount) {
            return skypeAccount.getBalance();
        }
        return 0.0f;
    }

    public static Contact.AVAILABILITY getAccountAvailability() {
        if (null != skypeAccount) {
            return skypeAccount.getAvailability();
        }
        return Contact.AVAILABILITY.OFFLINE;
    }
    
    public static void setAccountAvailability(Contact.AVAILABILITY availability){
        if (null != skypeAccount && null != availability) {
            skypeAccount.setAvailability(availability);
        }
    }
    
    public static boolean haveVoicemailCapabilityOwn(){
        if (null != skypeAccount ) {
            return skypeAccount.haveVoicemailCapability();
        }
        return false;
    }

    // SktContact--------------------------------------------
    public static ArrayList<SktAuthItem> getAuthRequestList(){
        if (null != skypeContact) {
            return skypeContact.getAuthRequestList();
        }
        return null;
    }
    public static SktBuddy getBuddy(String skypeName){
        if (null != skypeContact && null != skypeName) {
            return skypeContact.getBuddy(skypeName);
        }
        return null;
    }
    public static boolean isMyBuddy(String actName){
        if (null != skypeContact && null != actName ) {
            return skypeContact.isMyBuddy(actName);
        }
        return false;
    }
    
    public static int getBuddyNums(){
        if (null != skypeContact) {
            return skypeContact.getBuddyNums();
        }
        return 0;
    }
    
    public static void addIncomingContactRequest(String actName){
        if (null != skypeContact && null != actName) {
            skypeContact.addIncomingContactRequest(actName);
        }
    }
    
    public static void blockIncomingContactRequest(String actName){
        if (null != skypeContact && null != actName) {
            skypeContact.blockIncomingContactRequest(actName);
        }
    }
    
    public static void ignoreIncomingContactRequest(String actName){
        if (null != skypeContact && null != actName) {
            skypeContact.ignoreIncomingContactRequest(actName);
        }
    }
     
    public static boolean isPstnUser(String actName) {
        if (null != skypeContact && null != actName) {
            return skypeContact.isPstnUser(actName);
        }
        return true;
    }
    public static Contact.AVAILABILITY getContactAvailability(String actName) {
        if (null != skypeContact) {
            return skypeContact.getAvailability(actName);
        }
        return Contact.AVAILABILITY.OFFLINE;
    }
    
    public static byte[] getContactAvatar(String actName) {
        if (null != skypeContact && null != actName) {
            return skypeContact.getAvatar(actName);
        }
        return null;
    }
    
    public static String getContactDisplayName(String actName) {
        if (null != skypeContact && null != actName) {
            return skypeContact.getDisplayName(actName);
        }
        return "";
    }
    
    public static void requestBuddyList(){
        if (null != skypeContact) {
            skypeContact.requestBuddies();
        }
    }

    public static ArrayList<SktBuddy> getBuddyList() {
        if (null != skypeContact) {
            return skypeContact.getBuddyList();
        }
        return null;
    }
    
    public static ArrayList<String> getUnBlockedContactList() {
        if (null != skypeContact) {
            return skypeContact.getUnBlockedContactList();
        }
        return null;
    }

    public static void unBlockContact(String actName) {
        if (null != skypeContact && null != actName) {
            skypeContact.unBlockContact(actName);
        }
    }

    public static void blockContact(String actName) {
        if (null != skypeContact && null != actName) {
            skypeContact.blockContact(actName);
        }
    }

    public static ArrayList<String> getBlockedContactList() {
        if (null != skypeContact) {
            return skypeContact.getBlockedContactList();
        }
        return null;
    }
    
    public static ArrayList<SktBuddy>getSearchList(){
        if (null != skypeContact) {
            return skypeContact.getSearchList();
        }
        return null;
    }
    
    public static boolean addContact(String actName, String msg){
        if (null != skypeContact && null != actName && null != msg) {
           return skypeContact.addContact(actName,msg);
        }
        return false;
    }
    
    public static void cancelContactSearch(){
        if (null != skypeContact) {
            skypeContact.cancelContactSearch();
        }  
    }
    
    public static ContactSearch getCurrentContactSearch(){
        if (null != skypeContact) {
            return skypeContact.getCurrentContactSearch();
        }
        return null;
    }
    
    public static boolean haveVoicemailCapability(String actName){
        if (null != skypeContact && null != actName) {
            return skypeContact.haveVoicemailCapability(actName);
        }
        return false;
    }
    
    public static boolean haveChatCapability(String actName){
        if (null != skypeContact && null != actName) {
            return skypeContact.haveChatCapability(actName);
        }
        return false;
    }
    
    public static boolean canBlocked(String actName){
        if (null != skypeContact && null != actName) {
            return skypeContact.canBlocked(actName);
        }
        return false;
    }
    
    public static boolean havaVoiceCallCapabilityContact(String actName){
        if (null != skypeContact && null != actName) {
            return skypeContact.havaVoiceCallCapability(actName);
        }
        return false;
    }
    
    public static boolean havaVideoCallCapabilityContact(String actName){
        if (null != skypeContact && null != actName) {
            return skypeContact.havaVideoCallCapability(actName);
        }
        return false;
    }
    
    public static void removeContact(String actName){
        if (null != skypeContact && null != actName) {
            skypeContact.removeContact(actName);
        }
    }
    
    public static void refreshContact(){
        if (null != skypeContact ) {
            skypeContact.requestBuddies();
        }
    }
    
    public static String getContactDisplayname(String actName){
        if (null != skypeContact && null != actName) {
            return skypeContact.getDisplayName(actName);
        }
        
        return "";
    }
    
    
    // SktConversation-------------------------------------------------------------->
    public static boolean isConferenceConv(String convName){
        if (null != skypeConversation && null != convName) {
            return skypeConversation.isConferenceConv(convName);
        }
        return false;
    }
    
    public static boolean haveVoicemailCapabilityConv(String convName){
        if (null != skypeConversation && null != convName) {
            return skypeConversation.haveVoicemailCapability(convName);
        }
        return false;
    }
    
    public static boolean havaVoiceCallCapabilityConv(String convName){
        if (null != skypeConversation && null != convName) {
            return skypeConversation.havaVoiceCallCapability(convName);
        }
        return false;
    }
    
    public static boolean havaVideoCallCapabilityConv(String convName){
        if (null != skypeConversation && null != convName) {
            return skypeConversation.havaVideoCallCapability(convName);
        }
        return false;
    }
    
    public static boolean canAddtoBuddyList(String convName){
        if (null != skypeConversation && null != convName) {
            return skypeConversation.canAddtoBuddyList(convName);
        }
        return false;
    }
    
    /**
     * 
     * @param code 1: send video; 2: recv video
     * @return Video.STATUS
     */
    public static Video.STATUS getVideoStatus(int code){
        if (null != skypeConversation) {
            return skypeConversation.getVideoStatus(code);
        }
        return Video.STATUS.NOT_AVAILABLE;
    }

    public static byte[] getConvAvatar(String convName) {
        if (null != skypeConversation && null != convName) {
            return skypeConversation.getAvatar(convName);
        }
        return null;
    }
    
    public static int getParticipantsSize(String convName) {
        if (null != skypeConversation && null != convName) {
            return skypeConversation.getParticipants(convName).size();
        }
        return 0;
    }
    
    public static void startRecvVideo() {
        if (null != skypeConversation) {
            skypeConversation.startRecvVideo();
        }
    }
    
    public static void stopRecvVideo(){
        if (null != skypeConversation) {
            skypeConversation.stopRecvVideo();
        }
    }
    
    public static boolean startSendVideo() {
        if (null != skypeConversation) {
            return skypeConversation.startSendVideo();
        }
        return false;
    }

    /**
     * not check the video status
     */
    public static void startSendVideoForce() {
        if (null != skypeConversation) {
            skypeConversation.startSendVideoForce();
        }
    }

    public static void stopSendVideo() {
        if (null != skypeConversation) {
            skypeConversation.stopSendVideo();
        }
    }

    public static void holdResumeConversation(boolean hold) {
        if (null != skypeConversation) {
            skypeConversation.holdConversationCall(hold);
        }
    }

    public static void muteConversation(boolean mute) {
        if (null != skypeConversation) {
            skypeConversation.muteMicroPhone(mute);
        }
    }

    public static void anwserIncomingCall(boolean enableVideo) {
        if (null != skypeConversation) {
            skypeConversation.anwserIncomingCall(enableVideo);
        }
    }
    
    public static boolean startConversationCall(String actName, boolean enableVideo){
        if (null != skypeConversation && null != actName) {
            return skypeConversation.startConversationCall(actName,enableVideo);
        }
        return false;
    }

    public static void leaveConversation() {
        if (null != skypeConversation) {
            skypeConversation.leaveConversationCall();
        }
    }

    public static String getConvDisplayName(String convName) {
        if (null != skypeConversation && null != convName) {
            return skypeConversation.getDisplayName(convName);
        }
        return "";
    }
    
    public static String getConvCallName() {
        if (null != skypeConversation ) {
            return skypeConversation.getConvCallName();
        }
        return "";
    }

    public static boolean startPSTBCall(String pstnNumber){
        if (null != skypeConversation && null != pstnNumber) {
            return skypeConversation.startPSTBCall(pstnNumber);
        }
        return false;
    }
    
    public static void sendDTMF(char ch){
        if (null != skypeConversation) {
            skypeConversation.sendDTMF(ch);
        }
    }
    
    public static void sendChatMessage(String convName, String text){
        if (null != skypeConversation && null != convName && null != text) {
            skypeConversation.sendChatMessage(convName,text);
        }
    }
    
    public static void startRecordingVoicemail(String actName){
        if (null != skypeConversation && null != actName ) {
            skypeConversation.startRecordingVoicemail(actName);
        }
    }
    
    public static void sendRecordVoicemail(){
        if (null != skypeConversation) {
            skypeConversation.sendRecordVoicemail();
        }
    }
    
    public static void cancelRecordingVoicemail(){
        if (null != skypeConversation) {
            skypeConversation.cancelRecordingVoicemail();
        }
    }
    
    public static void playbackVoicemail(int id){
        if (null != skypeConversation && id > 0) {
            skypeConversation.playbackVoicemail(id);
        }
    }
    
    public static void stopPlaybackVoicemail(){
        if (null != skypeConversation) {
            skypeConversation.stopPlaybackVoicemail();
        }
    }
    
    // SktOption------------------------------------------------------>
    public static void setSpeakerVolume(int val) {
        if (null != skypeOption) {
            skypeOption.setSpeakerVolume(val);
        }
    }

    public static int getSpeakerVolume() {
        if (null != skypeOption) {
            return skypeOption.getSpeakerVolume();
        }
        return 0;
    }

    public static int getCallForwarding() {
        if (null != skypeOption) {
            return skypeOption.getCallForwarding();
        }
        return -1;
    }

    public static String getTimeoutOfCallStr() {
        if (null != skypeOption) {
            return skypeOption.getTimeoutOfCalling() + "";
        }
        return "";
    }

    public static int getAutoforwardingToVoicemail() {
        if (null != skypeOption) {
            return skypeOption.getAutoforwardingToVoicemail();
        }
        return -1;
    }

    public static void setCallForwarding(int policy) {
        if (null != skypeOption) {
            skypeOption.setCallForwarding(policy);
        }
    }
    
    public static void setAutoforwardingToVoicemail(int policy){
        if (null != skypeOption) {
            skypeOption.setAutoforwardingToVoicemail(policy);
        }
    }
    
    public static ArrayList<String> getCallForwardList(){
        if (null != skypeOption) {
            return skypeOption.getCallForwardList();
        }
        return null;
    }
    
    public static void setCallForwardList(ArrayList<String> list ){
        if (null != skypeOption && null != list) {
            skypeOption.setCallForwardList(list);
        }
    }
    
    public static void setTimeoutOfCalling(int seconds){
        if (null != skypeOption ) {
            skypeOption.setTimeoutOfCalling(seconds);
        }
    }
    
    public static HISTORY_SAVE_POLICY getHistoryDays(){
        if (null != skypeOption ) {
           return skypeOption.getHistoryDays();
        }
        return HISTORY_SAVE_POLICY.FOREVER;
    }
    
    public static void setHistoryDays(HISTORY_SAVE_POLICY policy){
        if (null != skypeOption && null != policy) {
            skypeOption.setHistoryDays(policy);
        }
    }
    
    public static Account.SKYPECALLPOLICY getSkypeCallPolicy(){
        if (null != skypeOption) {
            return skypeOption.getSkypeCallPolicy();
        }
        return Account.SKYPECALLPOLICY.BUDDIES_OR_AUTHORIZED_CAN_CALL;
    }
    
    public static void setSkypeCallPolicy(Account.SKYPECALLPOLICY policy){
        if (null != skypeOption && null != policy) {
            skypeOption.setSkypeCallPolicy(policy);
        }
    }
    
    public static int getVideoRecvPolicy(){
        if (null != skypeOption) {
            return skypeOption.getVideoRecvPolicy();
        }
        return -1;
    }
    
    public static void setVideoRecvPolicy(int policy){
        if (null != skypeOption) {
            skypeOption.setVideoRecvPolicy(policy);
        }
    }

    public static void clearHistory(){
        if (null != skypeOption) {
            skypeOption.clearHistory();
        }
    }
}
