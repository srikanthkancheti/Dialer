package com.ielts.dialer.contactdetails;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.ielts.dialer.R;
import com.ielts.dialer.adapters.CallHistoryCustomAdapter;
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
		
		String searchNum = null;
		historyArrayList = new ArrayList<CallHistoryData>();

		try{
		
			for(int i = 0; i < Common.calloglist.size(); i++){
				
				if(Common.calloglist.get(i).getCallnumber().contains(contactNumber) || Common.calloglist.get(i).getCallnumber() == contactNumber){
					
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

	
}
