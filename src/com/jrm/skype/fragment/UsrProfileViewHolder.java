
package com.jrm.skype.fragment;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.jrm.skype.fragment.UserprofileFragment;
import com.jrm.skype.ui.R;
import com.jrm.skype.view.SKEditText;

/**
 * @author andy.liu 
 * the ViewHolder of UserprofileFragment
 */
public class UsrProfileViewHolder {
    public UserprofileFragment mVHUserprofileFragment;
    
    public TextView mVHPageTv;
    
    public RelativeLayout mVHPageOneRel;
    
    public ImageView mVHHeadIv;
    
    public TextView mVHDisplayNameTv;
    
    public SKEditText mVHFullNameEt;

    public Spinner mVHGenderSp;
    
    public SKEditText mVHBirthDEt;
    
    public SKEditText mVHBirthMEt;
    
    public SKEditText mVHBirthYEt;
    
    public SKEditText mVHSigEt;
    
    public SKEditText mVHEmailEt;
    
    public SKEditText mVHHomePageEt;
    
    public Button mVHPictureBtn;
    
    public RelativeLayout mVHPageTwoRel;
    
    public Spinner mVHCountrySp;
    
    public SKEditText mVHStateEt;
    
    public SKEditText mVHCityEt;
    
    public Spinner mVHLanguageSp;
    
    public SKEditText mVHHomePHEt;
    
    public SKEditText mVHOfficePHEt;
    
    public SKEditText mVHMobilePHEt;

    public TextView mVHHomePHCodeTv;

    public TextView mVHOfficePHCodeTv;

    public TextView mVHMobilePHCodeTv;
    
    public TextView mVHHomePHTv; //show the home number
    
    public TextView mVHOfficePHTv; //show the home number
    
    public TextView mVHMobilePHTv; //show the home number
    
    public Button mVHNextPageBtn;
    
    public Button mVHLastPageBtn;

    public UsrProfileViewHolder(UserprofileFragment mVHUserprofileFragment) {
        super();
        this.mVHUserprofileFragment = mVHUserprofileFragment;
    }

    public void findView() {
        mVHPageTv = (TextView)  mVHUserprofileFragment.getLayout().findViewById(
                R.id.tv_userprofile_fragment_page);
        mVHHeadIv = (ImageView) mVHUserprofileFragment.getLayout().findViewById(
                R.id.iv_userprofile_fragment_head);
        mVHDisplayNameTv = (TextView) mVHUserprofileFragment.getLayout().findViewById(
                R.id.tv_userprofile_fragment_name);
        mVHPageOneRel = (RelativeLayout) mVHUserprofileFragment.getLayout().findViewById(
                R.id.rel_userprofile_fragment_pageone);
        mVHFullNameEt = (SKEditText) mVHUserprofileFragment.getLayout().findViewById(
                R.id.et_userprofile_fragment_fullname);
        mVHGenderSp = (Spinner) mVHUserprofileFragment.getLayout().findViewById(
                R.id.sp_userprofile_fragment_gender);
        mVHBirthDEt = (SKEditText) mVHUserprofileFragment.getLayout().findViewById(
                R.id.et_userprofile_fragment_birthday_D);
        mVHBirthMEt = (SKEditText) mVHUserprofileFragment.getLayout().findViewById(
                R.id.et_userprofile_fragment_birthday_M);
        mVHBirthYEt = (SKEditText) mVHUserprofileFragment.getLayout().findViewById(
                R.id.et_userprofile_fragment_birthday_Y);
        mVHSigEt = (SKEditText) mVHUserprofileFragment.getLayout().findViewById(
                R.id.et_userprofile_fragment_sig);
        mVHEmailEt = (SKEditText) mVHUserprofileFragment.getLayout().findViewById(
                R.id.et_userprofile_fragment_email);
        mVHHomePageEt = (SKEditText) mVHUserprofileFragment.getLayout().findViewById(
                R.id.et_userprofile_fragment_homepage);
        mVHPictureBtn = (Button) mVHUserprofileFragment.getLayout().findViewById(
                R.id.btn_userprofile_fragment_picture);
        
        mVHPageTwoRel = (RelativeLayout) mVHUserprofileFragment.getLayout().findViewById(
                R.id.rel_userprofile_fragment_pagetwo);
        mVHCountrySp = (Spinner) mVHUserprofileFragment.getLayout().findViewById(
                R.id.sp_userprofile_fragment_country);
        mVHStateEt = (SKEditText) mVHUserprofileFragment.getLayout().findViewById(
                R.id.et_userprofile_fragment_state);
        mVHCityEt = (SKEditText) mVHUserprofileFragment.getLayout().findViewById(
                R.id.et_userprofile_fragment_city);
        mVHLanguageSp = (Spinner) mVHUserprofileFragment.getLayout().findViewById(
                R.id.sp_userprofile_fragment_language);
        mVHHomePHEt = (SKEditText) mVHUserprofileFragment.getLayout().findViewById(
                R.id.et_userprofile_fragment_homeph);
        mVHOfficePHEt = (SKEditText) mVHUserprofileFragment.getLayout().findViewById(
                R.id.et_userprofile_fragment_officeph);
        mVHMobilePHEt = (SKEditText) mVHUserprofileFragment.getLayout().findViewById(
                R.id.et_userprofile_fragment_mobileph);
        mVHHomePHCodeTv = (TextView) mVHUserprofileFragment.getLayout().findViewById(
                R.id.tv_userprofile_fragment_homeph_code);
        mVHOfficePHCodeTv = (TextView) mVHUserprofileFragment.getLayout().findViewById(
                R.id.tv_userprofile_fragment_officeph_code);
        mVHMobilePHCodeTv = (TextView) mVHUserprofileFragment.getLayout().findViewById(
                R.id.tv_userprofile_fragment_mobileph_code);
        mVHHomePHTv = (TextView) mVHUserprofileFragment.getLayout().findViewById(
                R.id.tv_userprofile_fragment_homeph);
        mVHOfficePHTv = (TextView) mVHUserprofileFragment.getLayout().findViewById(
                R.id.tv_userprofile_fragment_officeph);
        mVHMobilePHTv = (TextView) mVHUserprofileFragment.getLayout().findViewById(
                R.id.tv_userprofile_fragment_mobileph);
        
        mVHNextPageBtn = (Button) mVHUserprofileFragment.getLayout().findViewById(
                R.id.btn_userprofile_fragment_nextPage);
        mVHLastPageBtn = (Button) mVHUserprofileFragment.getLayout().findViewById(
                R.id.btn_userprofile_fragment_lastPage);
    }

}
