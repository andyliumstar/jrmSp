
package com.jrm.skype.fragment;

import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.datebase.ConvTableActor;
import com.jrm.skype.datebase.ConvTableActor.ConvTableInfoHolder;
import com.jrm.skype.listview.adapter.CursorListViewAdapter;
import com.jrm.skype.listview.itemholder.HistoryListItemHolder;
import com.jrm.skype.ui.R;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import com.jrm.skype.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author andy.liu 
 * a fragment of UserAccount , handle the functions of history list
 */

public class HistoryListFragment extends Fragment {
    private final static String TAG = "HistoryListFragment";
    
    private View mLayout;
    
    private ListView mHistoryLv;
    /*
     * HistoryListItemHolder :listview' item view 
     * CursorListViewAdapter:listview's adapter 
     * Cursor : the database's select results 
     * ConvTableActor : the executer of call table, mainly execute select and delete function here
     */
    private HistoryListItemHolder mHistoryLvItemHolder;

    private CursorListViewAdapter mCursorListViewAdapter;

    private Cursor mCursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.usraccount_historylist_fragment, container, false);
        return mLayout;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        mHistoryLvItemHolder = new HistoryListItemHolder(R.layout.history_list_items);
        
        mHistoryLv = (ListView) getLayout().findViewById(R.id.lv_historylist_history);
        
        mCursor = ConvTableActor.getInstance().selectAll();       
        mCursorListViewAdapter = new CursorListViewAdapter(getActivity(),mCursor, mHistoryLvItemHolder);
        mHistoryLv.setAdapter(mCursorListViewAdapter);
        mHistoryLv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View vv = (View) mHistoryLv.getChildAt(position
                        - mHistoryLv.getFirstVisiblePosition());

                Bundle bl = new Bundle();
                bl.putString(SKYPECONSTANT.SKYPESTRING.CONVNAME, ((HistoryListItemHolder) vv.getTag()).mConvNameStr);
                bl.putString(SKYPECONSTANT.SKYPESTRING.DISPLAYNAME, ((HistoryListItemHolder) vv.getTag()).mDisplayNameTv.getText().toString());
                bl.putInt(SKYPECONSTANT.SKYPESTRING.CONVTYPE, ((HistoryListItemHolder) vv.getTag()).mConvType);
                bl.putInt(SKYPECONSTANT.SKYPESTRING.VOICEMAILID, ((HistoryListItemHolder) vv.getTag()).mVMId);
                getActivity().showDialog(SKYPECONSTANT.USRACCOUNTDIALOG.HISTORYITEM, bl);         
            }
        });
    }

    public View getLayout() {
        return mLayout;
    }
    
    public void deleteItem() {
        try {
            ConvTableActor.getInstance().delete("_id=?", new String[] {
                mCursor.getString((mCursor.getColumnIndex("_id")))
            });
            updateHistory();
        }catch(Exception e){
            Log.e(TAG, "deleteItem error");
        }
    }

    public void updateVMType() {
        try{
            // update the history database,make the voicemail type to old voicemail
            ContentValues valuesConv = new ContentValues();
            valuesConv.put(ConvTableInfoHolder.CONVTYPE, SKYPECONSTANT.CONVTYPE.VOICEMAIL);

            ConvTableActor.getInstance().update(valuesConv, "_id=?", new String[] {
                mCursor.getString((mCursor.getColumnIndex("_id")))
            });
            updateHistory();
        }catch(Exception e){
            Log.e(TAG, "updateItem error");
        }
        
    }

    public void updateHistory() {
        try{
            mCursor = ConvTableActor.getInstance().selectAll();  
            mCursorListViewAdapter.changeCursor(mCursor);
            mCursorListViewAdapter.notifyDataSetChanged();
        }catch(Exception e){
            Log.e(TAG, "updateHistory error");
        }
    }

    @Override
    public void onDestroy() {
        if (null != mCursor && !mCursor.isClosed())	{
            mCursor.close();
		}
        
        mLayout = null;
        mHistoryLv = null;
        mHistoryLvItemHolder = null;
        mCursorListViewAdapter = null;
        mCursor = null;
        
        System.gc();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        /*
         * update history list
         */
        updateHistory();
    }
    
    public void setListFocus(){
        if (null != mHistoryLv){
            mHistoryLv.setSelection(mHistoryLv.getFirstVisiblePosition());
            mHistoryLv.requestFocus();
        }
        
    }

}
