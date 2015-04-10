package com.ielts.dialer.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ielts.dialer.R;
import com.ielts.dialer.activities.ContactDetailsActivity;
import com.ielts.dialer.model.CallData;

/**
 * This Adapter os used to bind the call log collection data to listview 
 * and giving the functionality of add comment, update comment and delete comment functionality.
 * @author Srikanth
 *
 */
public class CustomAdapter extends ArrayAdapter<CallData> implements Filterable{
	 
	 //String _heading, _comm;
	 private Activity activity;
	 private List<CallData> listdata=null;
	 private LayoutInflater mInflater=null;
	 private ArrayList<CallData> arraylist;
	 private CallData objBean;
	 private int listRow;
	 
	 public CustomAdapter(Activity context, int listRow, List<CallData> calldata) {
	  super(context, listRow, calldata);
	  
	  this.listdata=calldata;
	  this.listRow = listRow;
	  this.activity = context;
	  mInflater = LayoutInflater.from(context);
	  this.arraylist = new ArrayList<CallData>();
	  this.arraylist.addAll(listdata);
	  
	 }
	  
	public void filter(String charText) {
		// TODO Auto-generated method stub
		
		charText = charText.toLowerCase(Locale.getDefault());
		listdata.clear();
       if (charText.length() == 0) {
       	
       	listdata.addAll(arraylist);
       	
       }else {
       	
           for (CallData ob : arraylist) {
           	
//               if (ob.getCallnumber().toLowerCase(Locale.getDefault()).contains(charText)){
//               	
//               	listdata.add(ob);
//               }
        	   if(charText.contains("1")){
            	   
            	   if (ob.getCallnumber().toLowerCase(Locale.getDefault()).contains(charText)){
            		   
            		   listdata.add(ob);
            	   }
            	   
               }
        	   
        	   if(charText.contains("2")){
            	   
            	   if (ob.getCallnumber().toLowerCase(Locale.getDefault()).contains(charText)
               			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("a") 
            			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("b")
            			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("c")){
            		   
            		   listdata.add(ob);
            	   }
            	   
               }
        	   
        	   if(charText.contains("3")){
            	   
            	   if (ob.getCallnumber().toLowerCase(Locale.getDefault()).contains(charText)
               			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("d") 
            			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("e")
            			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("f")){
            		   
            		   listdata.add(ob);
            	   }
            	   
               }
        	   
        	   if(charText.contains("4")){
            	   
            	   if (ob.getCallnumber().toLowerCase(Locale.getDefault()).contains(charText)
               			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("g") 
            			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("h")
            			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("i")){
            		   
            		   listdata.add(ob);
            	   }
            	   
               }
        	   
        	   if(charText.contains("5")){
            	   
            	   if (ob.getCallnumber().toLowerCase(Locale.getDefault()).contains(charText)
               			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("j") 
            			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("k")
            			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("l")){
            		   
            		   listdata.add(ob);
            	   }
            	   
               }
        	   
        	   if(charText.contains("6")){
            	   
            	   if (ob.getCallnumber().toLowerCase(Locale.getDefault()).contains(charText)
               			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("m") 
            			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("n")
            			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("o")){
            		   
            		   listdata.add(ob);
            	   }
            	   
               }

               if(charText.contains("7")){
            	   
            	   if (ob.getCallnumber().toLowerCase(Locale.getDefault()).contains(charText)
               			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("p") 
            			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("q")
            			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("r")
            			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("s")){
            		   
            		   listdata.add(ob);
            	   }
            	   
               }
               
               if(charText.contains("8")){
            	   
            	   if (ob.getCallnumber().toLowerCase(Locale.getDefault()).contains(charText)
               			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("t") 
            			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("u")
            			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("v")){
            		   
            		   listdata.add(ob);
            	   }
            	   
               }
               
               if(charText.contains("9")){
            	   
            	   if (ob.getCallnumber().toLowerCase(Locale.getDefault()).contains(charText)
               			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("w") 
            			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("x")
            			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("y")
            			   || ob.getContactName().toLowerCase(Locale.getDefault()).contains("z")){
            		   
            		   listdata.add(ob);
            	   }
            	   
               }
           }
       }
       
       notifyDataSetChanged();
	}


	@Override
	 public int getCount() {
	  return listdata.size();
	 }
	  
	  
	 public View getView(final int position, View convertView, ViewGroup parent) {
	   
	  final ViewHolder holder;
	   
	  if (convertView == null || convertView.getTag() == null) {
	   holder = new ViewHolder();
	   convertView = mInflater.inflate(listRow, null);
	   
	   holder.calltype = (ImageView) convertView.findViewById(R.id.call_logo_imageView);
	   holder.callnumber = (TextView) convertView.findViewById(R.id.callNumber_tv); 		   
	   holder.calldate = (TextView) convertView.findViewById(R.id.callDate_tv);
	   //holder.heading = (TextView) convertView.findViewById(R.id.heading_tv);
	   holder.contactOptions_rl = (RelativeLayout) convertView.findViewById(R.id.options_rv);
	   holder.numRelLayout = (RelativeLayout) convertView.findViewById(R.id.number_rl);
	   holder.optionsImage = (ImageView) convertView.findViewById(R.id.option_imageView);
	         convertView.setTag(holder);
	  }
	  else {
	   holder = (ViewHolder) convertView.getTag();
	  }
	   
	  CallData calldatalist=listdata.get(position);
	  
	  final String callnumber = calldatalist.getCallnumber();
	  final String contactname = calldatalist.getContactName();
	  String calltype = calldatalist.getCalltype();
	  String calldate = calldatalist.getCalldatetime(); 
	  final String contact_call_id = calldatalist.getCall_ID();
	  //String callduration=calldatalist.getCallduration();
	  
	  if(calltype == "INCOMING") {
		  holder.calltype.setImageResource(R.drawable.incoming);
	  }if(calltype == "OUTGOING") {
		  holder.calltype.setImageResource(R.drawable.outgoing);
	  }if(calltype == "MISSED") {
		  holder.calltype.setImageResource(R.drawable.missed);
	  }
	   
	  if(null != contactname) {
		  holder.callnumber.setText(contactname);
	  }else {
		  holder.callnumber.setText(callnumber);
	  }
	 // holder.calltype.setText(calltype);
	  holder.calldate.setText(String.valueOf(calldate));
	  //holder.callduration.setText(callduration+" sec");
	  
	  
	  holder.optionsImage.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String number = listdata.get(position).getCallnumber();
			Intent detailsActInt = new Intent(activity, ContactDetailsActivity.class);
			detailsActInt.putExtra("key_contact_name", contactname);
			detailsActInt.putExtra("key_contact_number", callnumber);
			detailsActInt.putExtra("key_contact_call_id", contact_call_id);
			activity.startActivity(detailsActInt);
			//addCommentDialog(number);
		}
	});
	  
	  holder.numRelLayout.setOnLongClickListener(new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			String number = listdata.get(position).getCallnumber();
			Intent detailsActInt = new Intent(activity, ContactDetailsActivity.class);
			detailsActInt.putExtra("key_contact_name", contactname);
			detailsActInt.putExtra("key_contact_number", callnumber);
			detailsActInt.putExtra("key_contact_call_id", contact_call_id);
			activity.startActivity(detailsActInt);
			return true;
		}
	});
	  
	  holder.numRelLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String number = listdata.get(position).getCallnumber();
				Intent callIntent = new Intent(Intent.ACTION_CALL);
			    callIntent.setData(Uri.parse("tel:" + number));
			    activity.startActivity(callIntent);
			}
		});

	  notifyDataSetChanged();
	  return convertView;
	  
	 }
	 
}

class ViewHolder {
	 
    public ImageView optionsImage;
	 public TextView callnumber, calldate, callduration, heading;
    public ImageView calltype, nextImage;
    public RelativeLayout contactOptions_rl, numRelLayout;
}