
package com.jrm.skype.fragment;

import java.util.ArrayList;
import com.jrm.skype.api.SktContact.SktBuddy;
import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.listview.adapter.ListViewAdapter;
import com.jrm.skype.listview.itemholder.ContactListItemHolder;
import com.jrm.skype.listview.resourceholder.ContactResourceHolder;
import com.jrm.skype.ui.R;
import com.jrm.skype.util.SktApiActor;
import com.jrm.skype.util.SkypePrefrences;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author andy.liu 
 * a fragment of UserAccount , handle the functions of contactlist
 */

public class ContactListFragment extends Fragment {
    public static String TAG ="ContactListFragment";
    
    private View mLayout;

    private ListView mContactLv;
    /*
     * ContactResourceHolder : listview item data resource 
     * ContactListItemHolder : listview item view 
     * ListViewAdapter :listview adapter
     */
    private ContactResourceHolder mContactResourceHolder;

    private ContactListItemHolder mContactLvItemHolder;

    private ListViewAdapter mListViewAdapter;

    private ProgressDialog progressDialog;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.usraccount_contactlist_fragment, container, false);
        return mLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(R.string.loading);
        progressDialog.setMessage(getResources().getText(R.string.waiting));
        progressDialog.setCancelable(false);
        progressDialog.show();
        
        mContactLv = (ListView) getLayout().findViewById( R.id.lv_contactlist_fragment_contact); 
        mContactLv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View vv = (View) mContactLv.getChildAt(position - mContactLv.getFirstVisiblePosition());

                Bundle bl = new Bundle();
                bl.putString(SKYPECONSTANT.SKYPESTRING.SKYPENAME, ((ContactListItemHolder) vv.getTag()).mSkypeNameStr);
                bl.putString(SKYPECONSTANT.SKYPESTRING.DISPLAYNAME, ((ContactListItemHolder) vv.getTag()).mDisPlayNameTv.getText().toString());
                getActivity().showDialog(SKYPECONSTANT.USRACCOUNTDIALOG.CONTACTITEM, bl);
            }
        });
        
        //only request contact list when sign in
        if(SkypePrefrences.getRequestContactListBool(getActivity())){
            SktApiActor.requestBuddyList();
            SkypePrefrences.setRequestContactListBool(getActivity(), false);
        }else{
            initList(SktApiActor.getBuddyList());
            progressDialog.dismiss();
        }
    }

    
    public View getLayout() {
        return mLayout;
    }
    
    public void initList(ArrayList<SktBuddy> buddies) {
        mContactResourceHolder = new ContactResourceHolder();
        mContactLvItemHolder = new ContactListItemHolder(R.layout.contact_list_items);
        mListViewAdapter = new ListViewAdapter(getActivity(),mContactLvItemHolder);
        mContactResourceHolder.setResourceListItem(buddies);
        mListViewAdapter.setListItem(mContactResourceHolder.getResourceListItem());
        mContactLv.setAdapter(mListViewAdapter);
    }

    public void refreshList(ArrayList<SktBuddy> buddies) {
        if (null == mContactResourceHolder) {
            return;
        }

        mContactResourceHolder.setResourceListItem(buddies);
        mListViewAdapter.setListItem(mContactResourceHolder.getResourceListItem());
        mListViewAdapter.notifyDataSetChanged();
    }

    public void stopLoadingDialog() {
        if (null != progressDialog) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        stopLoadingDialog();
        
        mLayout = null;
        mContactLv = null;
        mContactResourceHolder = null;
        mContactLvItemHolder = null;
        mListViewAdapter = null;
        progressDialog = null;
        System.gc();
        
        super.onDestroy();
    }
    
}
