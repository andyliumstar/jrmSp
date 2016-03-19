
package com.jrm.skype.dialog;

import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.datebase.ChatTableActor;
import com.jrm.skype.datebase.ChatTableResourceHolder;
import com.jrm.skype.datebase.ChatTableActor.ChatTableInfoHolder;
import com.jrm.skype.datebase.ConvTableActor;
import com.jrm.skype.datebase.ConvTableActor.ConvTableInfoHolder;
import com.jrm.skype.dialog.ChatDialog;
import com.jrm.skype.listview.adapter.CursorListViewAdapter;
import com.jrm.skype.listview.adapter.ListViewAdapter;
import com.jrm.skype.listview.itemholder.MessageListItemHolder;
import com.jrm.skype.listview.resourceholder.NewMsgResourceHolder;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import com.jrm.skype.ui.R;
import com.jrm.skype.ui.UsrAccountActivity;
import com.jrm.skype.util.DateFormat;
import com.jrm.skype.util.SktApiActor;
import com.skype.api.Contact.AVAILABILITY;

/**
 * @author andy.liu 
 * the ViewListenner of ChatDialog
 */
public class ChatViewListenner implements OnClickListener, OnItemClickListener {
    private ChatDialog mVLChatDialog;

    private ChatViewHolder mVLChatViewHolder;

    private boolean mVLHistoryVisible;
    
    private String mVLChatConvName;
    
    private String mVLChatDisplayName;
    
    private boolean mVLIsConvConference;
    
    private byte[] mVLAvater;
    
    private AnimationDrawable mVLAnimationDrawable;

    /*
     * listview part
     */
    private MessageListItemHolder mVLMsgItemHolder; //new message and the history use the same item holder

    private ListViewAdapter mVLMsgListAdapter; // for new message list
    
    private NewMsgResourceHolder mVLMsgResourceHolder;
    
    private CursorListViewAdapter mVLHistoryListAdapter; // for history list

    /*
     *  database part,
     */
    private Cursor mVLHistoryCursor;

    private ChatTableResourceHolder mVLChatTableResourceHolder;
    
    private DateFormat mDateFormat;
    
    // page select
    private int mVLTotleLines; // the total lines in select results

    private int mVLNumberOfLine; // the number of line in one page of the history list which can change

    private int mVLTotlePages; // the total pages of the select results,which depend on mVLTotleLines and mVLNumberOfLine

    private int mVLStartLine; // for the page select which will change many times.

    private int mVLIndexOfPage;

    public ChatViewListenner(ChatDialog mVLChatDialog, ChatViewHolder mVLChatViewHolder,
            int numberOfLine) {
        super();
        this.mVLChatDialog = mVLChatDialog;
        this.mVLChatViewHolder = mVLChatViewHolder;
        this.mVLNumberOfLine = numberOfLine;

        mVLMsgItemHolder = new MessageListItemHolder(R.layout.message_list_items);
        mVLMsgResourceHolder = new NewMsgResourceHolder();
        mVLMsgListAdapter = new ListViewAdapter(mVLChatDialog.getOwnerActivity(), mVLMsgItemHolder);
        
        mDateFormat = new DateFormat();
    }

    public void initVar(Bundle args) {
        // update the contact
        if (null == args){
           return;
        }
        
        mVLChatConvName = args.getString(SKYPECONSTANT.SKYPESTRING.SKYPENAME, "");
        mVLChatDisplayName = args.getString(SKYPECONSTANT.SKYPESTRING.DISPLAYNAME, "");
        
        
        mVLHistoryVisible = false; // the History view is invisible every time when the dialog show
        mVLIndexOfPage = -1; // and the History will start from the first page

        // mVLChatTableActor need update for different chatContact,
        ChatTableActor.getInstance().setChatContact(mVLChatConvName);
        
        mVLChatViewHolder.mVHWaitIv.setBackgroundResource(R.drawable.signinganimation);
        mVLAnimationDrawable = (AnimationDrawable)mVLChatViewHolder.mVHWaitIv.getBackground();
        mVLAnimationDrawable.start();
        
        // init the name and other view for different contact
        mVLChatViewHolder.mVHDisplayNameTv.setText(mVLChatDisplayName);
        mVLChatViewHolder.mVHHistoryBtn.setText(R.string.history1);
        mVLChatViewHolder.mVHChatHistoryLil.setVisibility(View.GONE);
        
        updateStatus();
        updateAvater();
        /*
         *  a adapter with the null list;
         */
        mVLMsgResourceHolder.clearList();
        mVLMsgListAdapter.setListItem(mVLMsgResourceHolder.getResourceList());
        mVLChatViewHolder.mVHMessageLv.setAdapter(mVLMsgListAdapter);
        
        getUnReadMsgs(mVLChatConvName);
      
        // mVLChatDialog.setDialogSize(560, 415);
        mVLChatDialog.setDialogSizeF(0.780, 0.324);
    }

    public void setViewListenner() {
        mVLChatViewHolder.mVHEnterBtn.setOnClickListener(this);
        mVLChatViewHolder.mVHCloseBtn.setOnClickListener(this);

        mVLChatViewHolder.mVHChatHistoryLv.setOnItemClickListener(this);
        mVLChatViewHolder.mVHHistoryBtn.setOnClickListener(this);

        mVLChatViewHolder.mVHLastPageBtn.setOnClickListener(this);
        mVLChatViewHolder.mVHNextPageBtn.setOnClickListener(this);
        mVLChatViewHolder.mVHDeleteAllBtn.setOnClickListener(this);
    }

    public boolean isSameConv(String name){
        if (null == name)
            return false;
        return mVLChatConvName.equals(name);
    }
    
    //do after get the mVLChatConvName
    public void updateStatus( ){
        mVLIsConvConference = SktApiActor.isConferenceConv(mVLChatConvName);
        if (mVLIsConvConference){
            mVLChatViewHolder.mVHStatusIv.setImageResource(R.drawable.callconferencein_32x32);
            return;
        }
        int id;
        switch (SktApiActor.getContactAvailability(mVLChatConvName)){
            case ONLINE:
                id = R.drawable.presenceonline_32x32;
                break;
            case AWAY:
                id = R.drawable.presenceaway_32x32;
                break;
            case DO_NOT_DISTURB:
                id = R.drawable.presencedonotdisturb_32x32;
                break;
            case BLOCKED:
                id = R.drawable.presenceblocked_32x32;
                break;
            case SKYPEOUT:
                id = R.drawable.presencephone_32x32;
                break;
            case UNKNOWN:
                id = R.drawable.presenceunknown_32x32;
                break;
            default:
                id = R.drawable.presenceoffline_32x32;
                break;
        }
        mVLChatViewHolder.mVHStatusIv.setImageResource(id);
    }
    
    public void updateAvater() {
        mVLAvater = SktApiActor.getContactAvatar(mVLChatConvName);
        if (null != mVLAvater && mVLAvater.length > 0)
            mVLChatViewHolder.mVHHeadIv.setImageBitmap(BitmapFactory.decodeByteArray(mVLAvater, 0,
                    mVLAvater.length));
        else {
            if (AVAILABILITY.SKYPEOUT == SktApiActor.getContactAvailability(mVLChatConvName)) {
                mVLChatViewHolder.mVHHeadIv
                        .setImageResource(R.drawable.skype_img_contact_list_buddy_phone);
            } else {
                mVLChatViewHolder.mVHHeadIv
                        .setImageResource(R.drawable.skype_img_contact_list_buddy_unknown);
            }
        }
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btn_chat_enter:
                sendMsg();
                break;
            case R.id.btn_chat_close:
                mVLChatDialog.dismiss();
                break;
            case R.id.btn_chat_history:
                showHistory();
                break;
            case R.id.btn_chat_history_last:
                if (0 == mVLTotlePages)
                    return;
                mVLIndexOfPage = mVLIndexOfPage - 1 > 1 ? mVLIndexOfPage - 1 : 1;
                mVLStartLine = (mVLIndexOfPage - 1) * mVLNumberOfLine;
                readDataToListPage(mVLStartLine, mVLNumberOfLine);
                mVLChatViewHolder.mVHPageIndexTv.setText(mVLIndexOfPage + "/" + mVLTotlePages);
                break;
            case R.id.btn_chat_history_next:
                mVLIndexOfPage = mVLIndexOfPage + 1 > mVLTotlePages ? mVLTotlePages : mVLIndexOfPage + 1;
                mVLStartLine = (mVLIndexOfPage - 1) * mVLNumberOfLine;
                readDataToListPage(mVLStartLine, mVLNumberOfLine);
                mVLChatViewHolder.mVHPageIndexTv.setText(mVLIndexOfPage + "/" + mVLTotlePages);
                break;
            case R.id.btn_chat_history_delete:
                if ( 0 == mVLTotlePages )
                    return;
                AlertDialog.Builder builder = new Builder(mVLChatDialog.getOwnerActivity());
                builder.setMessage(R.string.sure_to_delete_all);
                builder.setTitle(R.string.notification);
                builder.setPositiveButton(R.string.Yes,new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        
                        deleteAll();
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
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder builder = new Builder(mVLChatDialog.getOwnerActivity());
        builder.setMessage(R.string.sure_to_delete);
        builder.setTitle(R.string.notification);
        builder.setPositiveButton(R.string.Yes,new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    ChatTableActor.getInstance().delete("_id=?", new String[] {
                        mVLHistoryCursor.getString((mVLHistoryCursor.getColumnIndex("_id")))
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                // init the page select var
                initPageSelectVar();
                // read a page to the list view
                readDataToListPage(mVLStartLine, mVLNumberOfLine);
                mVLChatViewHolder.mVHPageIndexTv.setText(mVLIndexOfPage + "/" + mVLTotlePages);
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
    
    /*
     * send the message and insert into database
     */

    public void sendMsg() {
        if (mVLChatViewHolder.mVHMessageEt.getText().toString().equals("")) {
            Toast.makeText(mVLChatDialog.getOwnerActivity(), R.string.msg_is_null,
                    Toast.LENGTH_SHORT).show();
        } else {
            /*
             * add to database
             */
            // set the date to insert
            mVLChatTableResourceHolder = new ChatTableResourceHolder();
            mVLChatTableResourceHolder.setChatConvName(mVLChatConvName);
            mVLChatTableResourceHolder.setChatTime(mDateFormat.getFormatString(
                    System.currentTimeMillis(), "HH:mm:ss"));
            mVLChatTableResourceHolder.setChatDate(mDateFormat.getFormatString(
                    System.currentTimeMillis(), "yyyy-MM-dd"));
            mVLChatTableResourceHolder.setChatString(mVLChatViewHolder.mVHMessageEt.getText()
                    .toString());
            mVLChatTableResourceHolder.setChatDisplayName("ME");
            mVLChatTableResourceHolder.setChatRead("yes");
            // insert the constructed data into database
            ChatTableActor.getInstance().insert(mVLChatTableResourceHolder);
            /*
             * notify the message list
             */
            mVLMsgListAdapter.addListItem(mVLChatTableResourceHolder);
            mVLMsgListAdapter.notifyDataSetChanged();

            SktApiActor.sendChatMessage(mVLChatConvName, mVLChatViewHolder.mVHMessageEt.getText()
                    .toString());
            mVLChatViewHolder.mVHMessageEt.setText("");
        }
    }
    
    /*
     * receive unread msgs, mostly need seconds
     */
    public void getUnReadMsgs(final String convName){
        final String colums = ChatTableInfoHolder.CHATCONVNAME+"=? and "+ChatTableInfoHolder.CHATREAD+"=?";
        final String [] whereValue = {convName,"no"};
        
        new Thread() {
            private Cursor mUnReadCursor;
            @Override
            public void run() {
                //get the unread msgs
                mUnReadCursor = ChatTableActor.getInstance().select(colums, whereValue);
                // update the history database,make the msg type to old message,and message numer to 0.
                ContentValues valuesConv = new ContentValues();
                valuesConv.put(ConvTableInfoHolder.CONVDURATION, 0);
                valuesConv.put(ConvTableInfoHolder.CONVTYPE,SKYPECONSTANT.CONVTYPE.OLDMESSAGE);
               
                ConvTableActor.getInstance().update(
                        valuesConv,
                        ConvTableInfoHolder.CONVNAME + "=? and " + ConvTableInfoHolder.CONVTYPE
                                + "=?", new String[] {
                                mVLChatConvName, SKYPECONSTANT.CONVTYPE.NEWMESSAGE + ""
                        });
                
                //let these run on ui thread
                if (null != mVLChatDialog)
                    mVLChatDialog.getOwnerActivity().runOnUiThread(new Runnable() {
                        
                        @Override
                        public void run() {
                            if (mVLChatDialog.isShowing()){
                             // change the listview's data and notify
                                mVLMsgResourceHolder.setList(mUnReadCursor);
                                mVLMsgListAdapter.setListItem(mVLMsgResourceHolder.getResourceList());
                                mVLMsgListAdapter.notifyDataSetChanged();
                               
                                // update the history list
                                if (SKYPECONSTANT.USRACCOUNTFOCUS.HISTORYLIST == ((UsrAccountActivity) mVLChatDialog
                                        .getOwnerActivity()).getViewListenner().getFocus())
                                    ((UsrAccountActivity) mVLChatDialog.getOwnerActivity()).getViewHolder().mVHHistoryListFragment
                                           .updateHistory();
                                
                                //update the chat database,make the new msg to readed.
                                //this must run after the notify or we will get null form database ,cause it is update.
                                ContentValues valuesChat = new ContentValues();
                                valuesChat.put(ChatTableInfoHolder.CHATREAD,"yes");
                                ChatTableActor.getInstance().update(valuesChat, colums, whereValue);
                            }
                            
                            mVLAnimationDrawable.stop();
                            mVLChatViewHolder.mVHWaitIv.setBackgroundResource(0);
                            
                            //close the cursor
                            if (null != mUnReadCursor && !mUnReadCursor.isClosed())
                                mUnReadCursor.isClosed();
                        }
                    });
            }

        }.start();
    }
    /*
     * receive new msg 
     */
    public void receiveNewMsg(String convName, String fromName, long timestamp, String body) {
        if (null == convName || null == fromName || null == body) {
            return;
        }
        //update the new msg
        mVLMsgResourceHolder.addList(convName, fromName, timestamp, body);
        mVLMsgListAdapter.setListItem(mVLMsgResourceHolder.getResourceList());
        mVLMsgListAdapter.notifyDataSetChanged();
        
        // update the history database,make the msg type to old message,and message number to 0.
        ContentValues valuesConv = new ContentValues();
        valuesConv.put(ConvTableInfoHolder.CONVDURATION, 0);
        valuesConv.put(ConvTableInfoHolder.CONVTYPE,SKYPECONSTANT.CONVTYPE.OLDMESSAGE);
       
        ConvTableActor.getInstance().update(
                valuesConv,
                ConvTableInfoHolder.CONVNAME + "=? and " + ConvTableInfoHolder.CONVTYPE
                        + "=?", new String[] {
                        convName, SKYPECONSTANT.CONVTYPE.NEWMESSAGE + ""
                });
        
        if (SKYPECONSTANT.USRACCOUNTFOCUS.HISTORYLIST == ((UsrAccountActivity) mVLChatDialog
                .getOwnerActivity()).getViewListenner().getFocus())
            ((UsrAccountActivity) mVLChatDialog.getOwnerActivity()).getViewHolder().mVHHistoryListFragment
                   .updateHistory();
        //update the chat database,make the new msg to readed.
        final String colums = ChatTableInfoHolder.CHATCONVNAME+"=? and "+ChatTableInfoHolder.CHATREAD+"=?";
        final String [] whereValue = {convName,"no"};
        ContentValues valuesChat = new ContentValues();
        valuesChat.put(ChatTableInfoHolder.CHATREAD,"yes");
        ChatTableActor.getInstance().update(valuesChat, colums, whereValue);
    }

    /*
     *show the history view
     */
    public void showHistory() {
        if (!mVLHistoryVisible) {
            // mVLChatDialog.setDialogSize(560, 850);
            mVLChatDialog.setDialogSizeF(0.780, 0.664);
            mVLChatViewHolder.mVHChatHistoryLil.setVisibility(View.VISIBLE);
            mVLChatViewHolder.mVHHistoryBtn.setText(R.string.history2);
            // init the page select var
            initPageSelectVar();
            // read a page to the list view
            readDataToListPage(mVLStartLine, mVLNumberOfLine);
            mVLChatViewHolder.mVHPageIndexTv.setText(mVLIndexOfPage + "/" + mVLTotlePages);
        } else {
            // mVLChatDialog.setDialogSize(560, 415);
            mVLChatDialog.setDialogSizeF(0.780, 0.324);
            mVLChatViewHolder.mVHChatHistoryLil.setVisibility(View.GONE);
            mVLChatViewHolder.mVHHistoryBtn.setText(R.string.history1);
        }

        mVLHistoryVisible = !mVLHistoryVisible;
        mVLChatDialog.show();
    }

    public void initPageSelectVar() {
        /*
         * get the total lines of data in database that select
         */
        mVLTotleLines = ChatTableActor.getInstance().getNumberOfSelect();

        if (0 == mVLTotleLines) {
            mVLTotlePages = 0;
            mVLIndexOfPage = 0;
            return;
        }

        if (mVLNumberOfLine > 0) {
            // get the number of page
            if (mVLTotleLines % mVLNumberOfLine == 0) {
                mVLTotlePages = mVLTotleLines / mVLNumberOfLine;
            } else {
                mVLTotlePages = mVLTotleLines / mVLNumberOfLine + 1;
            }

            /*
             * the newer message are at the tail of the results, so we select the date from the last page to the first
             * page.
             */
            if (-1 == mVLIndexOfPage || (0 == mVLIndexOfPage && mVLTotlePages > 0)) {
                // we should set the start line, when it is the first time to open the history message
                // or it is 0 but the mVLTotlePages is not.
                mVLIndexOfPage = mVLTotlePages;
            } else {
                if (mVLIndexOfPage > mVLTotlePages) {
                    mVLIndexOfPage = mVLTotlePages;
                }
            }
            mVLStartLine = (mVLIndexOfPage - 1) * mVLNumberOfLine;
        }
    }

    /**
     * read a page of data from database to listview
     * @param startLine  the index of the first data we want in the database
     * @param numberOfLine the number of data for one page
     */
    public void readDataToListPage(int startLine, int numberOfLine) {
        mVLHistoryCursor = ChatTableActor.getInstance().selectPage(startLine, numberOfLine);
        if (null == mVLHistoryListAdapter) {
            mVLHistoryListAdapter = new CursorListViewAdapter(mVLChatDialog.getOwnerActivity(),
                    mVLHistoryCursor, mVLMsgItemHolder);
            mVLChatViewHolder.mVHChatHistoryLv.setAdapter(mVLHistoryListAdapter);
        } else {
            mVLHistoryListAdapter.changeCursor(mVLHistoryCursor);
            mVLHistoryListAdapter.notifyDataSetChanged();
        }
    }
    
    public void deleteAll()
    {
        ChatTableActor.getInstance().delete(ChatTableInfoHolder.CHATCONVNAME+"=?", new String[]{mVLChatConvName});
        initPageSelectVar();
        readDataToListPage(mVLStartLine, mVLNumberOfLine);
        mVLChatViewHolder.mVHPageIndexTv.setText(mVLIndexOfPage + "/" + mVLTotlePages);
    }
    
    public void closeCursor()
    {
        if (null != mVLHistoryCursor && !mVLHistoryCursor.isClosed()){
            mVLHistoryCursor.close();
        }
        
    }

}
