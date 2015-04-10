package com.ielts.dialer.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ielts.dialer.R;
import com.ielts.dialer.activities.ContactDetailsActivity;
import com.ielts.dialer.model.CallHistoryData;

public class CallHistoryCustomAdapter extends BaseAdapter{

	private List<CallHistoryData> callListData=null;
	private LayoutInflater inflater;
	private Context context;
	public CallHistoryCustomAdapter(Context context, List<CallHistoryData> historyArrayList) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
		this.callListData = historyArrayList;
		this.context = context;
	}

	@Override
	 public int getCount() {
		return callListData == null ? 0 : callListData.size();
	 }

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return callListData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/* Following code view the list view in holded surface */
	@Override
	public View getView(final int position, View convertView,ViewGroup parent) {
		final ViewHolder holder = convertView == null ? new ViewHolder(): (ViewHolder) convertView.getTag();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.call_history_list_item, null);
			
			 holder.callhistorytype = (ImageView) convertView.findViewById(R.id.call_history_type_imageView);
			 holder.callhistorynumber = (TextView) convertView.findViewById(R.id.call_history_num_textView); 		   
			 holder.callhistorydate = (TextView) convertView.findViewById(R.id.call_history_dateTime_textView);
			 holder.callhistoryduration = (TextView) convertView.findViewById(R.id.call_history_duration_textView);
			 holder.callHistoryOptions_iv =(ImageView) convertView.findViewById(R.id.call_history_options_imageView);
			
			 convertView.setTag(holder);
		}

		  final String callHistoryNumber = callListData.get(position).getCallnumber();
		  //final String contactname = historyArrayList.get(position).getContactName();
		  String callHistoryType = callListData.get(position).getCalltype();
		  String callHistoryDate = callListData.get(position).getCalldatetime(); 
		  String callHistoryDuration = callListData.get(position).getCallduration();
		  final String callHistoryName = callListData.get(position).getContactName();
		  String callHistoryContactID = callListData.get(position).getCall_ID();
		  
		  if(callHistoryType == "INCOMING"){
			  holder.callhistorytype.setImageResource(R.drawable.incoming);
		  }if(callHistoryType == "OUTGOING"){
			  holder.callhistorytype.setImageResource(R.drawable.outgoing);
		  }if(callHistoryType == "MISSED"){
			  holder.callhistorytype.setImageResource(R.drawable.missed);
		  }
		  
		  holder.callhistorynumber.setText(callHistoryNumber);
		  holder.callhistorydate.setText(String.valueOf(callHistoryDate));
		  holder.callhistoryduration.setText(callHistoryDuration);
		  
		  holder.callHistoryOptions_iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String number = callListData.get(position).getCallnumber();
				Intent detailsActInt = new Intent(context, ContactDetailsActivity.class);
				detailsActInt.putExtra("key_contact_name", callHistoryName);
				detailsActInt.putExtra("key_contact_number", callHistoryNumber);
				detailsActInt.putExtra("key_contact_call_id", callHistoryNumber);
				context.startActivity(detailsActInt);
			}
		});
		
	    notifyDataSetChanged();

	return convertView;

}
	
	class ViewHolder {
		 
	     public TextView callhistorynumber, callhistorydate, callhistoryduration;
	     public ImageView callhistorytype, callHistoryOptions_iv;
	 }

}

 