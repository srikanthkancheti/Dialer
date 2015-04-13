package com.ielts.dialer.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andraskindler.quickscroll.Scrollable;
import com.ielts.dialer.R;
import com.ielts.dialer.model.ContactBean;

public class ContanctAdapter extends ArrayAdapter<ContactBean> implements Scrollable{
	
	private Activity activity;
	private List<ContactBean> items;
	private int row;
	private ContactBean objBean;
	private boolean isEdit;
	private ArrayList<ContactBean> arraylist;

	public ContanctAdapter(Activity act, int row, List<ContactBean> items) {
		super(act, row, items);

		this.activity = act;
		this.row = row;
		this.items = items;
		this.arraylist = new ArrayList<ContactBean>();
        this.arraylist.addAll(items);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = convertView == null ? new ViewHolder(): (ViewHolder) convertView.getTag();
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(row, null);
			holder.tvname = (TextView) convertView.findViewById(R.id.tvname);
			holder.tvPhoneNo = (TextView) convertView.findViewById(R.id.tvphone);
			holder.contact_iv_tv = (TextView) convertView.findViewById(R.id.contact_iv_textView);
			holder.contact_iv = (ImageView) convertView.findViewById(R.id.contact_imageView);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			
		}
		

		if ((items == null) || ((position + 1) > items.size()))
			return convertView;

		objBean = items.get(position);

		
		if (holder.tvname != null && null != objBean.getName()
				&& objBean.getName().trim().length() > 0) {
			holder.tvname.setText(Html.fromHtml(objBean.getName()));
		}
		if (holder.tvPhoneNo != null && null != objBean.getPhoneNo()
				&& objBean.getPhoneNo().trim().length() > 0) {
			holder.tvPhoneNo.setText(Html.fromHtml(objBean.getPhoneNo()));
		}
		
		String contactName = objBean.getName();
		String firstLetter = contactName.substring(0, 1);
		holder.contact_iv_tv.setText(firstLetter);
		
		 Uri uri = getPhotoUri(Long.parseLong(fetchContactIdFromPhoneNumber(objBean.getPhoneNo())));

        if (uri != null) {
            holder.contact_iv.setImageURI(uri);
            //holder.contact_iv_tv.setVisibility(View.GONE);
        } else {
            holder.contact_iv.setImageResource(R.drawable.round_transparent);
        }

		return convertView;
	}
	
	private Uri getPhotoUri(long contactId) {
		// TODO Auto-generated method stub
		ContentResolver contentResolver = activity.getContentResolver();

        try {
            Cursor cursor = contentResolver
                    .query(ContactsContract.Data.CONTENT_URI,
                            null,
                            ContactsContract.Data.CONTACT_ID
                                    + "="
                                    + contactId
                                    + " AND "

                                    + ContactsContract.Data.MIMETYPE
                                    + "='"
                                    + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                                    + "'", null, null);

            if (cursor != null) {
                if (!cursor.moveToFirst()) {
                    return null; // no photo
                }
            } else {
                return null; // error in cursor process
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Uri person = ContentUris.withAppendedId(
                ContactsContract.Contacts.CONTENT_URI, contactId);
        return Uri.withAppendedPath(person,
                ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
	}

	private String fetchContactIdFromPhoneNumber(String phoneNo) {
		// TODO Auto-generated method stub
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNo));
        Cursor cFetch = activity.getContentResolver().query(uri,
                new String[] { PhoneLookup.DISPLAY_NAME, PhoneLookup._ID },
                null, null, null);

        String contactId = "";


        if (cFetch.moveToFirst()) {

            cFetch.moveToFirst();

                contactId = cFetch.getString(cFetch
                        .getColumnIndex(PhoneLookup._ID));

        }

        System.out.println(contactId);

        return contactId;       
	}

	public void setEditMode(boolean isEdit) {
		this.isEdit = isEdit;
	}

	public class ViewHolder {
		public TextView tvname, tvPhoneNo, contact_iv_tv;
		private ImageView contact_iv;
	}

	public void filter(String charText ) {
		// TODO Auto-generated method stub
		charText = charText.toLowerCase(Locale.getDefault());
		//charText = charText.replace(" ", "");
		items.clear();
        if (charText.length() == 0) {
        	items.addAll(arraylist);
        } 
        else {
        	
            for (ContactBean ob : arraylist){
            	
                if (ob.getName().toLowerCase(Locale.getDefault()).contains(charText) 
                		|| ob.getPhoneNo().toLowerCase(Locale.getDefault()).contains(charText)){
                	
                	items.add(ob);
                }
            }
        }
        notifyDataSetChanged();
    
	}

	@Override
	public String getIndicatorForPosition(int childposition, int groupposition) {
		// TODO Auto-generated method stub
		return Character.toString(items.get(childposition).getName().charAt(0));
	}

	@Override
	public int getScrollPosition(int childposition, int groupposition) {
		// TODO Auto-generated method stub
		return childposition;
	}

}
