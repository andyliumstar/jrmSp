
package com.jrm.skype.dialog;

import com.jrm.skype.dialog.InvitationDialog;
import com.jrm.skype.listview.adapter.ListViewAdapter;
import com.jrm.skype.listview.itemholder.InvitationListItemHolder;
import com.jrm.skype.listview.resourceholder.InvitationResourceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.jrm.skype.ui.R;
import com.jrm.skype.ui.UsrAccountActivity;
import com.jrm.skype.util.SktApiActor;
/**
 * @author andy.liu
 *  the ViewListenner of InvitationDialog
 */
public class InvitationViewListenner implements OnClickListener {
    private InvitationDialog mVLInvitationDialog;

    private InvitationViewHolder mVLInvitationViewHolder;
    
    private String mSkypeName;

    private String mText;
    /*
     * InvitationResourceHolder : list item data resource InvitationListItemHolder : list item view ListViewAdapter :
     * list adapter
     */
    private InvitationResourceHolder mVLInvitationResourceHolder;

    private InvitationListItemHolder mVLListItemHolder;

    private ListViewAdapter mVLListViewAdapter;

    public InvitationViewListenner(InvitationDialog mVLInvitationDialog,
            InvitationViewHolder mVLInvitationViewHolder) {
        super();
        this.mVLInvitationDialog = mVLInvitationDialog;
        this.mVLInvitationViewHolder = mVLInvitationViewHolder;

        mVLInvitationResourceHolder = new InvitationResourceHolder();
        mVLListItemHolder = new InvitationListItemHolder(R.layout.search_result_list_items);
        mVLListViewAdapter = new ListViewAdapter(mVLInvitationDialog.getOwnerActivity(),
                mVLListItemHolder);
    }

    public void initVar() {
        mVLInvitationViewHolder.mVHRequestRel.setVisibility(View.VISIBLE);
        mVLInvitationViewHolder.mVHAcceptRel.setVisibility(View.GONE);

        mVLInvitationResourceHolder.setListItem(SktApiActor.getAuthRequestList());
        mVLListViewAdapter.setListItem(mVLInvitationResourceHolder.getResourceListItem());
        mVLInvitationViewHolder.mVHRequestLv.setAdapter(mVLListViewAdapter);
    }

    public void setViewListenner() {
        mVLInvitationViewHolder.mVHBlockBtn.setOnClickListener(this);
        mVLInvitationViewHolder.mVHAcceptBtn.setOnClickListener(this);
        mVLInvitationViewHolder.mVHDeclineBtn.setOnClickListener(this);

        mVLInvitationViewHolder.mVHBackTv.setOnClickListener(this);

        mVLInvitationViewHolder.mVHRequestLv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                mVLListViewAdapter.setSelected(arg2);
                

                View vv = (View) mVLInvitationViewHolder.mVHRequestLv.getChildAt(arg2
                        - mVLInvitationViewHolder.mVHRequestLv.getFirstVisiblePosition());

                mVLInvitationViewHolder.mVHRequestRel.setVisibility(View.GONE);
                mVLInvitationViewHolder.mVHAcceptRel.setVisibility(View.VISIBLE);
                mVLInvitationViewHolder.mVHAcceptBtn.requestFocus();
                mSkypeName = ((InvitationListItemHolder) vv.getTag()).mTextView.getText().toString();
                mText = ((InvitationListItemHolder) vv.getTag()).mText;
                mVLInvitationViewHolder.mVHRequestTv.setText(mText);
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_invitation_accept:
                SktApiActor.addIncomingContactRequest(mSkypeName);
                break;
            case R.id.btn_invitation_block:
                SktApiActor.blockIncomingContactRequest(mSkypeName);
                break;
            case R.id.btn_invitation_decline:
                SktApiActor.ignoreIncomingContactRequest(mSkypeName);
                break;
            case R.id.tv_invitation_back:
                if (mVLListViewAdapter.getCount() == 0) {
                    ((UsrAccountActivity) mVLInvitationDialog.getOwnerActivity()).getViewHolder().mVHRequestTv
                            .setVisibility(View.GONE);
                }
                mVLInvitationDialog.dismiss();
                return;
            default:
                break;
        }

        // after deal with the request, update the UI
        mVLInvitationResourceHolder.setListItem(SktApiActor.getAuthRequestList());
        mVLListViewAdapter.setListItem(mVLInvitationResourceHolder.getResourceListItem());
        mVLListViewAdapter.notifyDataSetChanged();

        if (mVLListViewAdapter.getCount() == 0) {
            ((UsrAccountActivity) mVLInvitationDialog.getOwnerActivity()).getViewHolder().mVHRequestTv
                    .setVisibility(View.GONE);
            mVLInvitationDialog.dismiss();
        }

        mVLInvitationViewHolder.mVHRequestRel.setVisibility(View.VISIBLE);
        mVLInvitationViewHolder.mVHAcceptRel.setVisibility(View.GONE);
    }
}
