
package com.jrm.skype.listview.itemholder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.datebase.ConvTableActor.ConvTableInfoHolder;
import android.database.Cursor;
import com.jrm.skype.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jrm.skype.ui.R;
import com.jrm.skype.util.SktApiActor;
/**
 * @author andy.liu 
 * the ItemHolder of HistoryList
 */
public class HistoryListItemHolder extends BaseListItemHolder {
    private java.util.Date mDate;

    private Calendar mCalendar;

    private java.text.SimpleDateFormat mFormat;

    private static final String[] WEEKDAY = {
            "Sun", "Mon", "Tue", "Wen", "Thu", "Fri", "Sat"
    };

    private static final String[] MONTH = {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private int mLayout;

    public ImageView mConvTypeIv;
    
    public int mConvType;
    
    public String mConvNameStr;
    
    public int mVMId;

    public TextView mDisplayNameTv;

    public TextView mWeekTv;

    public TextView mMonthDayTv;

    public TextView mTimeTv;

    public TextView mYearTv;

    public TextView mDurationTv;
    
    public TextView mDurationTip;

    public HistoryListItemHolder(int layout) {
        this.mLayout = layout;
        mFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalendar = Calendar.getInstance();
    }

    @Override
    public BaseListItemHolder createNewViewHolder() {
        return new HistoryListItemHolder(mLayout);
    }

    @Override
    public int getLayoutId() {
        return mLayout;
    }

    @Override
    public void findView(View v) {
        mConvTypeIv = (ImageView) v.findViewById(R.id.iv_history_listitem_convType);
        mDisplayNameTv = (TextView) v.findViewById(R.id.tv_history_listitem_name);
        mWeekTv = (TextView) v.findViewById(R.id.tv_history_listitem_week);
        mMonthDayTv = (TextView) v.findViewById(R.id.tv_history_listitem_monthday);
        mTimeTv = (TextView) v.findViewById(R.id.tv_history_listitem_time);
        mYearTv = (TextView) v.findViewById(R.id.tv_history_listitem_year);
        mDurationTv = (TextView) v.findViewById(R.id.tv_history_listitem_duration);
        mDurationTip = (TextView) v.findViewById(R.id.tv_history_listitem_durationTip);
    }

    @Override
    public void setViewResource(Object obj) {

        if (obj instanceof Cursor) {
            try {
                mDate = mFormat.parse(((Cursor) obj).getString(((Cursor) obj)
                        .getColumnIndex(ConvTableInfoHolder.CONVDATE)));
            } catch (ParseException e) {
                Log.w("Call History", "set view resource failed!");
                return;
            }

            mCalendar.setTime(mDate);

            mConvNameStr = ((Cursor) obj).getString(((Cursor) obj)
                    .getColumnIndex(ConvTableInfoHolder.CONVNAME));
            mDisplayNameTv.setText(SktApiActor.getConvDisplayName(mConvNameStr));
            mDurationTv.setText(((Cursor) obj).getString(((Cursor) obj)
                    .getColumnIndex(ConvTableInfoHolder.CONVDURATION)));
            mTimeTv.setText(((Cursor) obj).getString(((Cursor) obj)
                    .getColumnIndex(ConvTableInfoHolder.CONVTIME)));

            mYearTv.setText(mCalendar.get(Calendar.YEAR) + "");
            mWeekTv.setText(WEEKDAY[mCalendar.get(Calendar.DAY_OF_WEEK) - 1]);
            mMonthDayTv.setText(MONTH[mCalendar.get(Calendar.MONTH)] + "   "
                    + mCalendar.get(Calendar.DAY_OF_MONTH));
            mVMId = ((Cursor) obj).getInt(((Cursor) obj)
                    .getColumnIndex(ConvTableInfoHolder.VOICEMAILID));
            mConvType = ((Cursor) obj).getInt(((Cursor) obj)
                    .getColumnIndex(ConvTableInfoHolder.CONVTYPE));
            
            mDurationTip.setText(R.string.duration);
            
            switch (mConvType) {
                case SKYPECONSTANT.CONVTYPE.CALLIN:
                    mConvTypeIv.setBackgroundResource(R.drawable.callin_32x32);
                    break;
                case SKYPECONSTANT.CONVTYPE.CALLMISSED:
                    mConvTypeIv.setBackgroundResource(R.drawable.callmissed_32x32);
                    break;
                case SKYPECONSTANT.CONVTYPE.CALLOUT:
                    mConvTypeIv.setBackgroundResource(R.drawable.callout_32x32);
                    break;
                case SKYPECONSTANT.CONVTYPE.CONFERENCEIN:
                    mConvTypeIv.setBackgroundResource(R.drawable.callconferencein_32x32);
                    break;
                case SKYPECONSTANT.CONVTYPE.CONFERENCEMISSED:
                    mConvTypeIv.setBackgroundResource(R.drawable.callconferencemissed_32x32);
                    break;
                case SKYPECONSTANT.CONVTYPE.NEWMESSAGE:
                    mConvTypeIv.setBackgroundResource(R.drawable.new_msg);
                    mDurationTip.setText(R.string.message);
                    break;
                case SKYPECONSTANT.CONVTYPE.OLDMESSAGE:
                    mConvTypeIv.setBackgroundResource(R.drawable.old_msg);
                    mDurationTip.setText(R.string.message);
                    break;
                case SKYPECONSTANT.CONVTYPE.VOICEMAILNEW:
                    mConvTypeIv.setBackgroundResource(R.drawable.callvoicemailnew_32x32);
                    break;
                case SKYPECONSTANT.CONVTYPE.VOICEMAIL:
                    mConvTypeIv.setBackgroundResource(R.drawable.callvoicemail_32x32);
                    break;
                default:
                    mConvTypeIv.setBackgroundResource(R.drawable.callin_32x32);
            }
        } else
            Log.w("Call History", "set view resource failed!");
    }
}
