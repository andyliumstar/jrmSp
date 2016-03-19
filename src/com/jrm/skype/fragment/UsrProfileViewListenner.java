
package com.jrm.skype.fragment;

import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.fragment.UserprofileFragment;
import com.jrm.skype.ui.OptionsActivity;
import android.graphics.BitmapFactory;
import com.jrm.skype.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;
import com.jrm.skype.ui.R;
import com.jrm.skype.util.CountryInfoHelper;
import com.jrm.skype.util.DateFormat;
import com.jrm.skype.util.SktApiActor;
import com.jrm.skype.view.SKEditText.OnEditBackListenner;
import com.skype.api.Skype.VALIDATERESULT;
import com.skype.api.Skype.ValidateProfileStringResult;
/**
 * @author andy.liu 
 * the ViewListenner of UserprofileFragment
 */
public class UsrProfileViewListenner implements OnClickListener, OnEditorActionListener,
        OnEditBackListenner, OnItemSelectedListener {
    private UserprofileFragment mVLUserprofileFragment;

    private UsrProfileViewHolder mVLUsrProfileViewHolder;
    
    private DateFormat mDateFormat;
    
    private String mVLBirthDay;
    
    private String mVLFullName;
    
    private byte[] mVLAvater;
    
    public UsrProfileViewListenner(UserprofileFragment mVLUserprofileFragment,
            UsrProfileViewHolder mVLUsrProfileViewHolder) {
        super();
        this.mVLUserprofileFragment = mVLUserprofileFragment;
        this.mVLUsrProfileViewHolder = mVLUsrProfileViewHolder;
        mDateFormat = new DateFormat();
    }

    public void setViewListenner() {
        mVLUsrProfileViewHolder.mVHPictureBtn.setOnClickListener(this);
        mVLUsrProfileViewHolder.mVHNextPageBtn.setOnClickListener(this);
        mVLUsrProfileViewHolder.mVHLastPageBtn.setOnClickListener(this);
        
        mVLUsrProfileViewHolder.mVHHomePHCodeTv.setOnClickListener(this);
        mVLUsrProfileViewHolder.mVHMobilePHCodeTv.setOnClickListener(this);
        mVLUsrProfileViewHolder.mVHOfficePHCodeTv.setOnClickListener(this);
        mVLUsrProfileViewHolder.mVHHomePHTv.setOnClickListener(this);
        mVLUsrProfileViewHolder.mVHOfficePHTv.setOnClickListener(this);
        mVLUsrProfileViewHolder.mVHMobilePHTv.setOnClickListener(this);
        
        mVLUsrProfileViewHolder.mVHFullNameEt.setOnEditBackListenner(this);
        mVLUsrProfileViewHolder.mVHBirthDEt.setOnEditBackListenner(this);
        mVLUsrProfileViewHolder.mVHBirthMEt.setOnEditBackListenner(this);
        mVLUsrProfileViewHolder.mVHBirthYEt.setOnEditBackListenner(this);
        mVLUsrProfileViewHolder.mVHSigEt.setOnEditBackListenner(this);
        mVLUsrProfileViewHolder.mVHEmailEt.setOnEditBackListenner(this);
        mVLUsrProfileViewHolder.mVHHomePageEt.setOnEditBackListenner(this);
        mVLUsrProfileViewHolder.mVHStateEt.setOnEditBackListenner(this);
        mVLUsrProfileViewHolder.mVHCityEt.setOnEditBackListenner(this);
        mVLUsrProfileViewHolder.mVHHomePHEt.setOnEditBackListenner(this);
        mVLUsrProfileViewHolder.mVHOfficePHEt.setOnEditBackListenner(this);
        mVLUsrProfileViewHolder.mVHMobilePHEt.setOnEditBackListenner(this);
        
        mVLUsrProfileViewHolder.mVHFullNameEt.setOnEditorActionListener(this);
        mVLUsrProfileViewHolder.mVHBirthDEt.setOnEditorActionListener(this);
        mVLUsrProfileViewHolder.mVHBirthMEt.setOnEditorActionListener(this);
        mVLUsrProfileViewHolder.mVHBirthYEt.setOnEditorActionListener(this);
        mVLUsrProfileViewHolder.mVHSigEt.setOnEditorActionListener(this);
        mVLUsrProfileViewHolder.mVHEmailEt.setOnEditorActionListener(this);
        mVLUsrProfileViewHolder.mVHHomePageEt.setOnEditorActionListener(this);
        mVLUsrProfileViewHolder.mVHStateEt.setOnEditorActionListener(this);
        mVLUsrProfileViewHolder.mVHCityEt.setOnEditorActionListener(this);
        mVLUsrProfileViewHolder.mVHHomePHEt.setOnEditorActionListener(this);
        mVLUsrProfileViewHolder.mVHOfficePHEt.setOnEditorActionListener(this);
        mVLUsrProfileViewHolder.mVHMobilePHEt.setOnEditorActionListener(this);
        
        mVLUsrProfileViewHolder.mVHGenderSp.setOnItemSelectedListener(this);
        mVLUsrProfileViewHolder.mVHCountrySp.setOnItemSelectedListener(this);
        mVLUsrProfileViewHolder.mVHLanguageSp.setOnItemSelectedListener(this);
    }

    public void initVar() {
        Log.v("UsrProfileViewListenner", "initVar--------1--"+System.currentTimeMillis());
        mVLFullName = SktApiActor.getFullName();
        if (null != mVLFullName && mVLFullName.length() > 0) {
            mVLUsrProfileViewHolder.mVHDisplayNameTv.setText(mVLFullName);
        } else {
            mVLUsrProfileViewHolder.mVHDisplayNameTv.setText(SktApiActor.getAccountName());
        }
        
        updateAvater();
        
        mVLUsrProfileViewHolder.mVHFullNameEt.setText(mVLFullName);
        mVLUsrProfileViewHolder.mVHSigEt.setText(SktApiActor.getMoodText());
        mVLUsrProfileViewHolder.mVHEmailEt.setText(SktApiActor.getEmail());
        mVLUsrProfileViewHolder.mVHHomePageEt.setText(SktApiActor.getHomePage());
        mVLUsrProfileViewHolder.mVHStateEt.setText(SktApiActor.getProvice());
        mVLUsrProfileViewHolder.mVHCityEt.setText(SktApiActor.getCity());
        
        mVLBirthDay = SktApiActor.getBirthday();
        if ( 8 == mVLBirthDay.length()){
            mVLUsrProfileViewHolder.mVHBirthYEt.setText(mVLBirthDay.substring(0, 4));
            mVLUsrProfileViewHolder.mVHBirthMEt.setText(mVLBirthDay.substring(4, 6));
            mVLUsrProfileViewHolder.mVHBirthDEt.setText(mVLBirthDay.substring(6, 8));
        }
        
        
        mVLUsrProfileViewHolder.mVHGenderSp.setSelection(SktApiActor.getGender());
        
        if (!SktApiActor.getHomePhone().trim().equals("")){
            mVLUsrProfileViewHolder.mVHHomePHTv.setText(SktApiActor.getHomePhone().trim()) ;
            mVLUsrProfileViewHolder.mVHHomePHCodeTv.setVisibility(View.GONE);
            mVLUsrProfileViewHolder.mVHHomePHEt.setVisibility(View.GONE);
        }
        else
            mVLUsrProfileViewHolder.mVHHomePHTv.setVisibility(View.GONE);
        
        if (!SktApiActor.getOfficePhone().trim().equals("")){
            mVLUsrProfileViewHolder.mVHOfficePHTv.setText(SktApiActor.getOfficePhone().trim()) ;
            mVLUsrProfileViewHolder.mVHOfficePHCodeTv.setVisibility(View.GONE);
            mVLUsrProfileViewHolder.mVHOfficePHEt.setVisibility(View.GONE);
        }
        else
            mVLUsrProfileViewHolder.mVHOfficePHTv.setVisibility(View.GONE);
        
        if (!SktApiActor.getMobilePhone().trim().equals("")){
            mVLUsrProfileViewHolder.mVHMobilePHTv.setText(SktApiActor.getMobilePhone().trim()) ;
            mVLUsrProfileViewHolder.mVHMobilePHCodeTv.setVisibility(View.GONE);
            mVLUsrProfileViewHolder.mVHMobilePHEt.setVisibility(View.GONE);
        }
        else
            mVLUsrProfileViewHolder.mVHMobilePHTv.setVisibility(View.GONE);
       
        initCountryAndLanguage();
        Log.v("UsrProfileViewListenner", "initVar--------2--"+System.currentTimeMillis());
    }
    
    public void updateAvater() {
        mVLAvater = SktApiActor.getAccountAvatar();
        if (null != mVLAvater && mVLAvater.length > 0)
            mVLUsrProfileViewHolder.mVHHeadIv.setImageBitmap(BitmapFactory.decodeByteArray(
                    mVLAvater, 0, mVLAvater.length));
        else {
            mVLUsrProfileViewHolder.mVHHeadIv
                    .setImageResource(R.drawable.skype_img_login_user_head_default);
        }
    }
    
    private void initCountryAndLanguage(){
        String shortName = SktApiActor.getCountry();
        int pos = CountryInfoHelper.getCountryNameIdByShortName(mVLUserprofileFragment.getActivity(), shortName);
        mVLUsrProfileViewHolder.mVHCountrySp.setSelection(pos);
        
        shortName = SktApiActor.getLanguage();
        pos = CountryInfoHelper.getLanguageNameIdByShortName(mVLUserprofileFragment.getActivity(), shortName);
        mVLUsrProfileViewHolder.mVHLanguageSp.setSelection(pos);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_userprofile_fragment_picture:
                mVLUserprofileFragment.getActivity().showDialog(SKYPECONSTANT.OPTIONSDIALOG.CHANGE_PICTURE);
                break;
            case R.id.tv_userprofile_fragment_homeph_code:
                //tell the OptionsActivity to set the country code after select
                ((OptionsActivity) mVLUserprofileFragment.getActivity()).mPhoneFocus = SKYPECONSTANT.OPTIONSPHONEFOCUS.HOMEPHONE;
                mVLUserprofileFragment.getActivity().showDialog(SKYPECONSTANT.OPTIONSDIALOG.USERPROFILE);
                break;
            case R.id.tv_userprofile_fragment_officeph_code:
              //tell the OptionsActivity to set the country code after select
                ((OptionsActivity) mVLUserprofileFragment.getActivity()).mPhoneFocus = SKYPECONSTANT.OPTIONSPHONEFOCUS.OFFICEPHONE;
                mVLUserprofileFragment.getActivity().showDialog(SKYPECONSTANT.OPTIONSDIALOG.USERPROFILE);
                break;
            case R.id.tv_userprofile_fragment_mobileph_code:
              //tell the OptionsActivity to set the country code after select
                ((OptionsActivity) mVLUserprofileFragment.getActivity()).mPhoneFocus = SKYPECONSTANT.OPTIONSPHONEFOCUS.MOBILEPHONE;
                mVLUserprofileFragment.getActivity().showDialog(SKYPECONSTANT.OPTIONSDIALOG.USERPROFILE);
                break;
            case R.id.tv_userprofile_fragment_homeph:
                mVLUsrProfileViewHolder.mVHHomePHCodeTv.setVisibility(View.VISIBLE);
                mVLUsrProfileViewHolder.mVHHomePHEt.setVisibility(View.VISIBLE);
                mVLUsrProfileViewHolder.mVHHomePHTv.setVisibility(View.GONE);
                mVLUsrProfileViewHolder.mVHHomePHCodeTv.requestFocus();
                mVLUsrProfileViewHolder.mVHHomePHCodeTv.requestFocusFromTouch();
                break;
            case R.id.tv_userprofile_fragment_officeph:
                mVLUsrProfileViewHolder.mVHOfficePHCodeTv.setVisibility(View.VISIBLE);
                mVLUsrProfileViewHolder.mVHOfficePHEt.setVisibility(View.VISIBLE);
                mVLUsrProfileViewHolder.mVHOfficePHTv.setVisibility(View.GONE);
                mVLUsrProfileViewHolder.mVHOfficePHCodeTv.requestFocus();
                mVLUsrProfileViewHolder.mVHOfficePHCodeTv.requestFocusFromTouch();
                break;
            case R.id.tv_userprofile_fragment_mobileph:
                mVLUsrProfileViewHolder.mVHMobilePHCodeTv.setVisibility(View.VISIBLE);
                mVLUsrProfileViewHolder.mVHMobilePHEt.setVisibility(View.VISIBLE);
                mVLUsrProfileViewHolder.mVHMobilePHTv.setVisibility(View.GONE);
                mVLUsrProfileViewHolder.mVHMobilePHCodeTv.requestFocus();
                mVLUsrProfileViewHolder.mVHMobilePHCodeTv.requestFocusFromTouch();
                break;
            case R.id.btn_userprofile_fragment_nextPage:
                mVLUserprofileFragment.pageDown();
                break;
            case R.id.btn_userprofile_fragment_lastPage:
                mVLUserprofileFragment.pageUp();
                break;
            default:
                break;
        }
        
    }

    @Override
    public void onEditBack(View v) {
        switch (v.getId()) {
            case R.id.et_userprofile_fragment_fullname:
                fullNameEditDone();
                break;
            case R.id.et_userprofile_fragment_birthday_D:
            case R.id.et_userprofile_fragment_birthday_M:
            case R.id.et_userprofile_fragment_birthday_Y:
                birthdayEditDone();
                break;
            case R.id.et_userprofile_fragment_sig:
                SktApiActor.setMoodText(mVLUsrProfileViewHolder.mVHSigEt.getText().toString().trim());
                break;
            case R.id.et_userprofile_fragment_email:
                emailEditDone();
                break;
            case R.id.et_userprofile_fragment_homepage:
                SktApiActor.setHomePage(mVLUsrProfileViewHolder.mVHHomePageEt.getText().toString().trim());
                break;
            case R.id.et_userprofile_fragment_state:
                SktApiActor.setProvice(mVLUsrProfileViewHolder.mVHStateEt.getText().toString().trim());
                break;
            case R.id.et_userprofile_fragment_city:
                SktApiActor.setCity(mVLUsrProfileViewHolder.mVHCityEt.getText().toString().trim());
                break;
            case R.id.et_userprofile_fragment_homeph:
                homephEditDone();
                break;
            case R.id.et_userprofile_fragment_officeph:
                officephEditDone();
                break;
            case R.id.et_userprofile_fragment_mobileph:
                mobilephEditDone();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId != EditorInfo.IME_ACTION_DONE)
            return false;
        switch (v.getId()) {
            case R.id.et_userprofile_fragment_fullname:
                fullNameEditDone();
                break;
            case R.id.et_userprofile_fragment_birthday_D:
            case R.id.et_userprofile_fragment_birthday_M:
            case R.id.et_userprofile_fragment_birthday_Y:
                birthdayEditDone();
                break;
            case R.id.et_userprofile_fragment_sig:
                SktApiActor.setMoodText(mVLUsrProfileViewHolder.mVHSigEt.getText().toString().trim());
                break;
            case R.id.et_userprofile_fragment_email:
                emailEditDone();
                break;
            case R.id.et_userprofile_fragment_homepage:
                SktApiActor.setHomePage(mVLUsrProfileViewHolder.mVHHomePageEt.getText().toString().trim());
                break;
            case R.id.et_userprofile_fragment_state:
                SktApiActor.setProvice(mVLUsrProfileViewHolder.mVHStateEt.getText().toString().trim());
                break;
            case R.id.et_userprofile_fragment_city:
                SktApiActor.setCity(mVLUsrProfileViewHolder.mVHCityEt.getText().toString().trim());
                break;
            case R.id.et_userprofile_fragment_homeph:
                homephEditDone();
                break;
            case R.id.et_userprofile_fragment_officeph:
                officephEditDone();
                break;
            case R.id.et_userprofile_fragment_mobileph:
                mobilephEditDone();
                break;
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_userprofile_fragment_gender:
                SktApiActor.setGender(position);
                break;
            case R.id.sp_userprofile_fragment_language:
                SktApiActor.setLanguage(CountryInfoHelper.getLanguageShortNameById(
                        mVLUserprofileFragment.getActivity(), position));
                break;
            case R.id.sp_userprofile_fragment_country:
                SktApiActor.setCountry(CountryInfoHelper.getCountryShortNameById(
                        mVLUserprofileFragment.getActivity(), position));
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        
    }

    public void fullNameEditDone(){
        mVLFullName = mVLUsrProfileViewHolder.mVHFullNameEt.getText().toString();
        SktApiActor.setFullName(mVLFullName);
        if (null != mVLFullName && mVLFullName.length() > 0) {
            mVLUsrProfileViewHolder.mVHDisplayNameTv.setText(mVLFullName);
        } else {
            mVLUsrProfileViewHolder.mVHDisplayNameTv.setText(SktApiActor
                    .getAccountName());
        }
    }
    
    public void birthdayEditDone(){
        String d = mVLUsrProfileViewHolder.mVHBirthDEt.getText().toString().trim();
        String m = mVLUsrProfileViewHolder.mVHBirthMEt.getText().toString().trim();
        String y = mVLUsrProfileViewHolder.mVHBirthYEt.getText().toString().trim();
        
        if (d.equals("") ||m.equals("")||y.equals(""))
            return ;
        else if (d.length()!=2 ||m.length()!=2||y.length()!=4){
            Toast.makeText(
                    mVLUserprofileFragment.getActivity(),
                    mVLUserprofileFragment.getActivity().getResources()
                            .getString(R.string.date_format_invalid), Toast.LENGTH_SHORT).show();
            return;
        }
            
        mVLBirthDay = y+m+d;
        
        if (mDateFormat.parseStr(mVLBirthDay, false, "yyyyMMdd")){
            int bd = Integer.parseInt(mVLBirthDay);
            SktApiActor.setBirthday(bd);
        }
        else
            Toast.makeText(
                    mVLUserprofileFragment.getActivity(),
                    mVLUserprofileFragment.getActivity().getResources()
                            .getString(R.string.date_invalid), Toast.LENGTH_SHORT).show();
    }
    
    public void emailEditDone() {
        String emailStr = mVLUsrProfileViewHolder.mVHEmailEt.getText().toString();

        ValidateProfileStringResult vpsResult;
        vpsResult = SktApiActor.checkEmail(emailStr, true);

        if (vpsResult.result != VALIDATERESULT.VALIDATED_OK) {
            Toast.makeText(
                    mVLUserprofileFragment.getActivity(),
                    mVLUserprofileFragment.getActivity().getResources()
                            .getString(R.string.email_invalid), Toast.LENGTH_SHORT).show();
        } else {
            SktApiActor.setEmail(emailStr);
        }
    }
    
    public void phonenumberEditDone(){
        
    }
    public void homephEditDone() {
        String code = mVLUsrProfileViewHolder.mVHHomePHCodeTv.getText().toString();
        String number = mVLUsrProfileViewHolder.mVHHomePHEt.getText().toString().replaceAll(" ", "");
        
        if (SktApiActor.checkPSTNNumberBool(number, code)){
            SktApiActor.setHomePhone(code+number);
            mVLUsrProfileViewHolder.mVHHomePHCodeTv.setVisibility(View.GONE);
            mVLUsrProfileViewHolder.mVHHomePHEt.setVisibility(View.GONE);
            mVLUsrProfileViewHolder.mVHHomePHTv.setVisibility(View.VISIBLE);
            mVLUsrProfileViewHolder.mVHHomePHTv.setText(code+number);
            mVLUsrProfileViewHolder.mVHHomePHTv.requestFocus();
            mVLUsrProfileViewHolder.mVHHomePHTv.requestFocusFromTouch();
        }
    }
    
    public void officephEditDone() {
        String code = mVLUsrProfileViewHolder.mVHOfficePHCodeTv.getText().toString();
        String number = mVLUsrProfileViewHolder.mVHOfficePHEt.getText().toString().replaceAll(" ", "");
        
        if (SktApiActor.checkPSTNNumberBool(number, code)){
            SktApiActor.setOfficePhone(code+number);
            mVLUsrProfileViewHolder.mVHOfficePHCodeTv.setVisibility(View.GONE);
            mVLUsrProfileViewHolder.mVHOfficePHEt.setVisibility(View.GONE);
            mVLUsrProfileViewHolder.mVHOfficePHTv.setVisibility(View.VISIBLE);
            mVLUsrProfileViewHolder.mVHOfficePHTv.setText(code+number);
            mVLUsrProfileViewHolder.mVHOfficePHTv.requestFocus();
            mVLUsrProfileViewHolder.mVHOfficePHTv.requestFocusFromTouch();
        }
    }
    
    public void mobilephEditDone() {
        String code = mVLUsrProfileViewHolder.mVHMobilePHCodeTv.getText().toString();
        String number = mVLUsrProfileViewHolder.mVHMobilePHEt.getText().toString().replaceAll(" ", "");
        
        if (SktApiActor.checkPSTNNumberBool(number, code)){
            SktApiActor.setMobilePhone(code+number);
            mVLUsrProfileViewHolder.mVHMobilePHCodeTv.setVisibility(View.GONE);
            mVLUsrProfileViewHolder.mVHMobilePHEt.setVisibility(View.GONE);
            mVLUsrProfileViewHolder.mVHMobilePHTv.setVisibility(View.VISIBLE);
            mVLUsrProfileViewHolder.mVHMobilePHTv.setText(code+number);
            mVLUsrProfileViewHolder.mVHMobilePHTv.requestFocus();
            mVLUsrProfileViewHolder.mVHMobilePHTv.requestFocusFromTouch();
        }
    }
    
    
    

}
