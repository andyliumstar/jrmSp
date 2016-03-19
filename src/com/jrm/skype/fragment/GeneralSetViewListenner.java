
package com.jrm.skype.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.jrm.skype.api.SktOption.HISTORY_SAVE_POLICY;
import com.jrm.skype.datebase.ChatTableActor;
import com.jrm.skype.datebase.ConvTableActor;
import com.jrm.skype.fragment.GeneralSetFragment;
import com.jrm.skype.ui.R;
import com.jrm.skype.util.SktApiActor;
import com.jrm.skype.util.SkypePrefrences;
/**
 * @author andy.liu 
 * the ViewListenner of GeneralSetFragment
 */
public class GeneralSetViewListenner implements OnItemSelectedListener {
    private GeneralSetViewHolder mVLGeneralSetViewHolder;

    private GeneralSetFragment mVLGeneralSetFragment;
    
    public GeneralSetViewListenner(GeneralSetViewHolder mVLGeneralSetViewHolder,
            GeneralSetFragment mVLGeneralSetFragment) {
        super();
        this.mVLGeneralSetViewHolder = mVLGeneralSetViewHolder;
        this.mVLGeneralSetFragment = mVLGeneralSetFragment;
    }

    public void setViewListenner() {
        mVLGeneralSetViewHolder.mVHStartSkTVSp.setOnItemSelectedListener(this);
        mVLGeneralSetViewHolder.mVHSaveDaysSp.setOnItemSelectedListener(this);
        mVLGeneralSetViewHolder.mVHClearBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new Builder(mVLGeneralSetFragment.getActivity());
                builder.setMessage(R.string.sure_to_clear_history);
                builder.setTitle(R.string.clear_history);
                builder.setPositiveButton(R.string.Yes,new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(){

                            @Override
                            public void run() {
                                super.run();
                                SktApiActor.clearHistory();
                                ChatTableActor.getInstance().delete(null, null);
                                ConvTableActor.getInstance().delete(null, null);
                            }
                            
                        }.start();
                       
                        dialog.dismiss();
                    }
                }); 
                builder.setNegativeButton(R.string.No,  new DialogInterface.OnClickListener() {
                   
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();              
                   }
                }); 

                builder.create().show();
                
            }
        });
    }
    
    public void initVar(){
        if(SkypePrefrences.isStartSKTWhenTVStart(mVLGeneralSetFragment.getActivity()))
            mVLGeneralSetViewHolder.mVHStartSkTVSp.setSelection(1);//on
        else
            mVLGeneralSetViewHolder.mVHStartSkTVSp.setSelection(0);//off
        
        switch(SktApiActor.getHistoryDays()){
            case NO_HISTORY:
                mVLGeneralSetViewHolder.mVHSaveDaysSp.setSelection(0);
                break;
            case TWO_WEEKS:
                mVLGeneralSetViewHolder.mVHSaveDaysSp.setSelection(1);
                break;
            case ONE_MONTH:
                mVLGeneralSetViewHolder.mVHSaveDaysSp.setSelection(2);
                break;
            case THREE_MONTHS:
                mVLGeneralSetViewHolder.mVHSaveDaysSp.setSelection(3);
                break;
            case FOREVER:
                mVLGeneralSetViewHolder.mVHSaveDaysSp.setSelection(4);
                break;
            default:
                break;
        }
        
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.sp_generalset_fragment_startTVon:
                if (0 == position)//off
                    SkypePrefrences.setStartSKTWhenTVStart(mVLGeneralSetFragment.getActivity(), false);
                else
                    SkypePrefrences.setStartSKTWhenTVStart(mVLGeneralSetFragment.getActivity(), true);
                break;
            case R.id.sp_generalset_fragment_savedays:
                switch(position){
                    case 0:
                        SktApiActor.setHistoryDays(HISTORY_SAVE_POLICY.NO_HISTORY);
                        break;
                    case 1:
                        SktApiActor.setHistoryDays(HISTORY_SAVE_POLICY.TWO_WEEKS);
                        break;
                    case 2:
                        SktApiActor.setHistoryDays(HISTORY_SAVE_POLICY.ONE_MONTH);
                        break;
                    case 3:
                        SktApiActor.setHistoryDays(HISTORY_SAVE_POLICY.THREE_MONTHS);
                        break;
                    case 4:
                        SktApiActor.setHistoryDays(HISTORY_SAVE_POLICY.FOREVER);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        
    }

}
