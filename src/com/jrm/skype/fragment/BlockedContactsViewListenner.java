
package com.jrm.skype.fragment;

import java.util.ArrayList;

import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.fragment.BlockedContactsFragment;
import com.jrm.skype.listview.adapter.ListViewAdapter;
import com.jrm.skype.listview.itemholder.BlockContactItemHolder;
import com.jrm.skype.listview.resourceholder.BlockContactResourceHolder;
import com.jrm.skype.ui.R;
import com.jrm.skype.util.SktApiActor;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author andy.liu 
 * the ViewListenner of BlockedContactsFragment
 */
public class BlockedContactsViewListenner implements OnClickListener,OnItemClickListener {
    private BlockedContactsFragment mVLBlockedContactsFragment;

    private BlockedContactsViewHolder mVLBlockedContactsViewHolder;

    private BlockContactResourceHolder mVLBlockContactResourceHolder;
    
    private BlockContactItemHolder mVLContactsItemHolder;
    
    private ListViewAdapter mVLBlockContactAdapter;
    
    private ArrayList<String> mVLBlockedLs;
    
    private String mVLBlockNameStr;
    
    private String mVLUnBlockNameStr;

    public BlockedContactsViewListenner(BlockedContactsFragment mVLBlockedContactsFragment,
            BlockedContactsViewHolder mVLBlockedContactsViewHolder) {
        super();
        this.mVLBlockedContactsFragment = mVLBlockedContactsFragment;
        this.mVLBlockedContactsViewHolder = mVLBlockedContactsViewHolder;

        mVLBlockContactResourceHolder = new BlockContactResourceHolder();
        
        mVLContactsItemHolder = new BlockContactItemHolder(R.layout.block_contact_item);
      
        mVLBlockContactAdapter  = new ListViewAdapter(mVLBlockedContactsFragment.getActivity(), mVLContactsItemHolder);
        
        mVLBlockContactResourceHolder.setResourceList(SktApiActor.getBlockedContactList());
        mVLBlockContactAdapter.setListItem(mVLBlockContactResourceHolder.getResourceList());
    }

    public void setViewListenner() {
        mVLBlockedContactsViewHolder.mVHContactsTv.setOnClickListener(this);
        mVLBlockedContactsViewHolder.mVHBlockedContactsLv.setOnItemClickListener(this);
        mVLBlockedContactsViewHolder.mVHBlockBtn.setOnClickListener(this);
        mVLBlockedContactsViewHolder.mVHUnBlockBtn.setOnClickListener(this);
        
        mVLBlockedContactsViewHolder.mVHContactsTv.setOnKeyListener(new OnKeyListener() {
            
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                            && mVLBlockedContactsViewHolder.mVHBlockedContactsLv.getChildCount()<=0)
                        return true;
                }
                return false;
            }
        });
    }

    public void initVar() {
        mVLBlockedContactsViewHolder.mVHBlockedContactsLv.setAdapter(mVLBlockContactAdapter);
        
        mVLBlockedContactsViewHolder.mVHBlockBtn.setEnabled(false);
        mVLBlockedContactsViewHolder.mVHBlockBtn.setFocusable(false);
        mVLBlockedContactsViewHolder.mVHUnBlockBtn.setEnabled(false);
        mVLBlockedContactsViewHolder.mVHUnBlockBtn.setFocusable(false);
    }
    
    public void refreshList(){
        mVLBlockContactResourceHolder.setResourceList(SktApiActor.getBlockedContactList());
        mVLBlockContactAdapter.setListItem(mVLBlockContactResourceHolder.getResourceList());
        mVLBlockContactAdapter.notifyDataSetChanged();
    }
    
    public void selectBlockContact(String contactName){
        mVLBlockedContactsViewHolder.mVHContactsTv.setText(contactName);
        mVLBlockedContactsViewHolder.mVHBlockBtn.setEnabled(true);
        mVLBlockedContactsViewHolder.mVHBlockBtn.setFocusable(true);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_blockedcontacts_fragment_block:
                mVLBlockNameStr = mVLBlockedContactsViewHolder.mVHContactsTv.getText().toString();
                if (null != mVLBlockNameStr){
                    SktApiActor.blockContact(mVLBlockNameStr);
                    mVLBlockedContactsViewHolder.mVHBlockBtn.setEnabled(false);
                    mVLBlockedContactsViewHolder.mVHBlockBtn.setFocusable(false);
                    mVLBlockedContactsViewHolder.mVHContactsTv.setText("");
                    mVLBlockedContactsViewHolder.mVHContactsTv.requestFocus();
                    mVLBlockedContactsViewHolder.mVHContactsTv.requestFocusFromTouch();
                }
                    
                mVLBlockNameStr = null;
                break;
            case R.id.btn_blockedcontacts_fragment_unblock:
                if (null != mVLUnBlockNameStr){
                    SktApiActor.unBlockContact(mVLUnBlockNameStr);
                    mVLBlockedContactsViewHolder.mVHUnBlockBtn.setEnabled(false);
                    mVLBlockedContactsViewHolder.mVHUnBlockBtn.setFocusable(false);
                    mVLBlockedContactsViewHolder.mVHBlockContactSelectedTv.setText("");
                    mVLBlockedLs = SktApiActor.getBlockedContactList();
                    
                    if(null != mVLBlockedLs && mVLBlockedLs.size()>0){
                        mVLBlockedContactsViewHolder.mVHBlockedContactsLv.requestFocus();
                        mVLBlockedContactsViewHolder.mVHBlockedContactsLv.requestFocusFromTouch(); 
                    }else{
                        mVLBlockedContactsViewHolder.mVHContactsTv.requestFocus();
                        mVLBlockedContactsViewHolder.mVHContactsTv.requestFocusFromTouch();
                    }
                    
                }
                    
                mVLUnBlockNameStr = null;
                break;
            case R.id.tv_blockedcontacts_fragment_unblock:
                mVLBlockedContactsFragment.getActivity().showDialog(SKYPECONSTANT.OPTIONSDIALOG.UNBLOCK_CONTACTS);
                break;
        }
        refreshList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mVLUnBlockNameStr = (String) mVLBlockContactResourceHolder.getResourceList().get(position);
        mVLBlockedContactsViewHolder.mVHBlockContactSelectedTv.setText(mVLUnBlockNameStr);
        mVLBlockedContactsViewHolder.mVHUnBlockBtn.setEnabled(true);
        mVLBlockedContactsViewHolder.mVHUnBlockBtn.setFocusable(true);
    }

}
