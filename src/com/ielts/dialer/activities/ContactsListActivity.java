package com.ielts.dialer.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.andraskindler.quickscroll.QuickScroll;
import com.ielts.dialer.R;
import com.ielts.dialer.adapters.ContanctAdapter;
import com.ielts.dialer.model.ContactBean;

public class ContactsListActivity extends ActionBarActivity {
	
	private ListView contact_listview;
	private List<ContactBean> list = new ArrayList<ContactBean>();
	private ContanctAdapter objAdapter;
	Context context = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts_list);
		context = this;
		InitializeUi();
		LoadContacts();
		
		final QuickScroll quickscroll = (QuickScroll)findViewById(R.id.quickscroll);
		quickscroll.init(QuickScroll.TYPE_INDICATOR_WITH_HANDLE, contact_listview, objAdapter, QuickScroll.STYLE_HOLO);
		quickscroll.setFixedSize(1);
		quickscroll.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 48);
		
		objAdapter.notifyDataSetChanged();
		invalidateOptionsMenu();

		if (null != list && list.size() != 0) {
			Collections.sort(list, new Comparator<ContactBean>() {

				@Override
				public int compare(ContactBean lhs, ContactBean rhs) {
					return lhs.getName().compareTo(rhs.getName());
				}
			});

		} else {
			showToast("No Contact Found!!!");
		}
		
		contact_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ContactBean bean = (ContactBean) contact_listview.getItemAtPosition(position);
				String phoneNumber = bean.getPhoneNo();	
				String name = bean.getName();
				String firstContactId = null;
				
				if(name == null){
					
					Intent i = new Intent();     
					i.setAction(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT);
					i.setData(Uri.fromParts("tel", phoneNumber, null)); 
					startActivity(i);
				}else{
					String contactId = getContactRowIDLookupList(phoneNumber, ContactsListActivity.this);
					if(contactId.contains(",")){
						StringTokenizer tokens = new StringTokenizer(contactId, ",");
						firstContactId = tokens.nextToken();
					}else{
						firstContactId = contactId;
					}
					
					//String second = tokens.nextToken();
					Intent intent = new Intent(Intent.ACTION_VIEW);
					Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(firstContactId));
					intent.setData(uri);
					context.startActivity(intent);
				}
			}
		});

	}
	
	private void LoadContacts() {
		// TODO Auto-generated method stub
		
		Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,null, null);
		
		while (phones.moveToNext()) {

			String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

			String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

			ContactBean objContact = new ContactBean();
			objContact.setName(name);
			objContact.setPhoneNo(phoneNumber);
			list.add(objContact);
		}
		phones.close();
		//Astat.contactCheckArray = new boolean[list.size()];
		objAdapter = new ContanctAdapter(ContactsListActivity.this, R.layout.contacts_list_item, list);
		contact_listview.setAdapter(objAdapter);
		
	}

	private void InitializeUi() {
		// TODO Auto-generated method stub
		contact_listview = (ListView) findViewById(R.id.select_contacts_listView);
	}

	protected String getContactRowIDLookupList(String phoneNumber,
			ContactsListActivity cxt) {
		// TODO Auto-generated method stub
		String contactNumber = Uri.encode(phoneNumber);
	     String contactIdList = new String();
	     if (contactNumber != null) {
	         Cursor contactLookupCursor = cxt.getContentResolver()
	        		 .query(Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, 
	        		 Uri.encode(contactNumber)),
	                 new String[] { PhoneLookup.DISPLAY_NAME, PhoneLookup._ID },
	                 null, null, null);
	         if (contactLookupCursor != null) {
	             while (contactLookupCursor.moveToNext()) {
	                 int phoneContactID = contactLookupCursor
	                         .getInt(contactLookupCursor
	                                 .getColumnIndexOrThrow(PhoneLookup._ID));
	                 if (phoneContactID > 0) {
	                     contactIdList += "" + phoneContactID + ",";
	                 }
	             }
	             if (contactIdList.endsWith(",")) {
	                 contactIdList = contactIdList.substring(0,
	                         contactIdList.length() - 1);
	             }
	         }
	         contactLookupCursor.close();
	     }
	     return contactIdList;
	}

	private void showToast(String msg) {
		// TODO Auto-generated method stub
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_contacts, menu);
		invalidateOptionsMenu();
		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		SearchView.OnQueryTextListener queryTextWatcher = new SearchView.OnQueryTextListener(){

			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				
				objAdapter.filter(newText);
				return true;
			}
			
		};
		
		searchView.setOnQueryTextListener(queryTextWatcher);

		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * On selecting action bar icons
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		
		case R.id.action_search:
			// search action
			return true;
		
		case R.id.action_add_new:
			
			Intent i = new Intent(ContactsContract.Intents.Insert.ACTION);

	        // Sets the MIME type to the one expected by the insertion activity
	        i.setType(ContactsContract.RawContacts.CONTENT_TYPE);
	        startActivity(i);
	        
            return true;
            
		default:
			return super.onOptionsItemSelected(item);
		}
	}



}
