package com.ielts.dialer.contactdetails;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ielts.dialer.R;
import com.ielts.dialer.common.Common;
import com.ielts.dialer.model.CallHistoryData;


public class ContactCallHistoryActivity extends ActionBarActivity{
	
	private Context context = ContactCallHistoryActivity.this;
	private TextView history_contact_name_tv, history_contact_iv_background_tv;
	private String historyContactName, historyContactNumber;
	private String firstLetter  = "";
	private ListView callHistory_lv;
	
	private String cType = null;
	private String phNumber = null;
	private String conName = null;
	private String dateTime = null;
	private String duration = null;
	private String call_ID = null;
	
	CallHistoryCustomAdapter callhistoryAdapter = null;
	
	private List<CallHistoryData> historyArrayList = new ArrayList<CallHistoryData>();; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setContentView(R.layout.activity_contact_call_history);
	 
	 InitializeUI();
	 
	 historyContactName = getIntent().getExtras().getString("history_contact_name");
	 historyContactNumber = getIntent().getStringExtra("history_contact_number");
	 
	 setContactname(historyContactName, historyContactNumber);
	 
	 getContactHistory(historyContactNumber);
	 
	 
	}

	private void getContactHistory(String contactNumber) {
		// TODO Auto-generated method stub
		
		historyArrayList = new ArrayList<CallHistoryData>();
		
		try{
		
			for(int i = 0; i < Common.calloglist.size(); i++){
				
				if(Common.calloglist.get(i).getCallnumber().contains(contactNumber)){
					
					//String cType, phNumber, conName, dateTime, duration, call_ID = null;
					CallHistoryData historyCallData = new CallHistoryData();
					cType = Common.calloglist.get(i).getCalltype();
					phNumber = Common.calloglist.get(i).getCallnumber();
					conName = Common.calloglist.get(i).getContactName();
					dateTime = Common.calloglist.get(i).getCalldatetime();
					duration = Common.calloglist.get(i).getCallduration();
					call_ID = Common.calloglist.get(i).getCall_ID();
					
					historyCallData.setCalltype(cType);
					historyCallData.setCallnumber(phNumber);
					historyCallData.setContactname(conName);
					historyCallData.setCalldatetime(dateTime);
					historyCallData.setCallduration(duration);
					historyCallData.setCall_ID(call_ID);
					
					historyArrayList.add(historyCallData);
					historyCallData = null;
					
				}
				
			}
			
			callhistoryAdapter = new CallHistoryCustomAdapter(context, historyArrayList);
			historyArrayList.size();
			 
			callHistory_lv.setAdapter(callhistoryAdapter);
			callhistoryAdapter.notifyDataSetChanged();
		
		}finally{
			//historyArrayList.clear();
		}
	}

	private void setContactname(String historyContactName2, String historyContactNumber2) {
		// TODO Auto-generated method stub
		
		if(null != historyContactName2){
			
			history_contact_name_tv.setText(historyContactName2);
			firstLetter = historyContactName2.substring(0, 1);
			history_contact_iv_background_tv.setText(firstLetter);
			
		}else{
			
			history_contact_name_tv.setText(historyContactNumber2);
			history_contact_iv_background_tv.setText("?");
		}
		
		
	}

	private void InitializeUI() {
		// TODO Auto-generated method stub
		
		history_contact_name_tv = (TextView) findViewById(R.id.history_contact_name_tv);
		history_contact_iv_background_tv = (TextView) findViewById(R.id.history_contact_iv_textView);
		callHistory_lv = (ListView) findViewById(R.id.call_history_listView);
	}

	public class CallHistoryCustomAdapter extends BaseAdapter{

		private List<CallHistoryData> callListData=null;
		private LayoutInflater inflater;
		
		public CallHistoryCustomAdapter(Context context, List<CallHistoryData> historyArrayList) {
			// TODO Auto-generated constructor stub
			this.inflater = LayoutInflater.from(ContactCallHistoryActivity.this);
			this.callListData = historyArrayList;
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
			  holder.callhistoryduration.setText(callHistoryDuration+" sec");
			  
			  holder.callHistoryOptions_iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String number = callListData.get(position).getCallnumber();
					Intent detailsActInt = new Intent(ContactCallHistoryActivity.this, ContactDetailsActivity.class);
					detailsActInt.putExtra("key_contact_name", callHistoryName);
					detailsActInt.putExtra("key_contact_number", callHistoryNumber);
					detailsActInt.putExtra("key_contact_call_id", callHistoryNumber);
					startActivity(detailsActInt);
				}
			});
			
		    notifyDataSetChanged();

		return convertView;

	}
	
}
	
	 class ViewHolder {
			 
		     public TextView callhistorynumber, callhistorydate, callhistoryduration;
		     public ImageView callhistorytype, callHistoryOptions_iv;
		 }
}
