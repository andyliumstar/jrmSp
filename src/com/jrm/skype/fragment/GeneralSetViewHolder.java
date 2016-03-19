
package com.jrm.skype.fragment;

import com.jrm.skype.fragment.GeneralSetFragment;
import com.jrm.skype.ui.R;

import android.widget.Button;
import android.widget.Spinner;

/**
 * @author andy.liu 
 * the ViewHolder of GeneralSetFragment
 */
public class GeneralSetViewHolder {
    public GeneralSetFragment mVHGeneralSetFragment;

    public Spinner mVHStartSkTVSp;

    public Spinner mVHSaveDaysSp;

    public Button mVHClearBtn;
    public GeneralSetViewHolder(GeneralSetFragment mVHGeneralSetFragment) {
        super();
        this.mVHGeneralSetFragment = mVHGeneralSetFragment;
    }

    public void findView() {
        mVHStartSkTVSp = (Spinner) mVHGeneralSetFragment.GetLayout().findViewById(
                R.id.sp_generalset_fragment_startTVon);
        mVHSaveDaysSp = (Spinner) mVHGeneralSetFragment.GetLayout().findViewById(
                R.id.sp_generalset_fragment_savedays);
        mVHClearBtn = (Button) mVHGeneralSetFragment.GetLayout().findViewById(
                R.id.btn_generalset_fragment_clear);
    }

}
