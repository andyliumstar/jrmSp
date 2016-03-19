package com.jrm.skype.dialog;


import com.jrm.skype.listview.adapter.ListViewAdapter;
import com.jrm.skype.listview.itemholder.CountryInfoListItemHolder;
import com.jrm.skype.listview.resourceholder.CountryResourceHolder;
import com.jrm.skype.listview.resourceholder.CountryResourceHolder.CountryResource;
import com.jrm.skype.ui.R;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
 
/**
 * 
 * @author andy.liu
 * a Dialog to show the country code
 */
public class CountrycodeDialog extends MainDialog {
	public static String mCountryNameSelected = null;
	public static String mCountryCodeSelected = null;
	/*
	 * CountryResourceHolder : list item data resource
	 * CountryInfoListItemHolder  : list item view
	 * ListViewAdapter : list adapter
	 */
    private ListView mCountryCodeLs;
	private ListViewAdapter mCountryInfoAdapter;
 	private CountryInfoListItemHolder mCountryInfoListItemHolder;
 	private CountryResourceHolder mCountryResourceHolder;
	
 
	public CountrycodeDialog(Context context, int theme) {
		super(context, theme);
		
        mCountryInfoListItemHolder = new CountryInfoListItemHolder(R.layout.countryinfo_list_items);		
		mCountryInfoAdapter = new ListViewAdapter(getOwnerActivity(),mCountryInfoListItemHolder);
		mCountryResourceHolder = new CountryResourceHolder();	 
	}

	@Override
	public void findView() {		 
		mCountryCodeLs = (ListView) getLayout().findViewById(R.id.listview);			
	}

	@Override
	public void setViewListenner() {				
		mCountryCodeLs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			    mCountryNameSelected = ((CountryResource)mCountryInfoAdapter.getItem(arg2)).mCountryName;		 
			    mCountryCodeSelected = ((CountryResource)mCountryInfoAdapter.getItem(arg2)).mCountryCode;
				CountrycodeDialog.this.dismiss();				
			}
		});
	}
	
	@Override
	public void initVar(Bundle args) {
		mCountryResourceHolder.readCountryInfoResource(getOwnerActivity());		
		mCountryInfoAdapter.setListItem(mCountryResourceHolder.getResourceListItem());
		mCountryCodeLs.setAdapter(mCountryInfoAdapter);	
	}

}
