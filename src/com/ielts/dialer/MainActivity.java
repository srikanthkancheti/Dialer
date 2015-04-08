package com.ielts.dialer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract.PhoneLookup;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ielts.dialer.common.Common;
import com.ielts.dialer.contactdetails.ContactCallHistoryActivity;
import com.ielts.dialer.contactdetails.ContactDetailsActivity;
import com.ielts.dialer.contacts.ContactsListActivity;
import com.ielts.dialer.model.CallData;
import com.ielts.dialer.model.GridListDataModel;
import com.ielts.dialer.swipetocall.SwipeActionAdapter;
import com.ielts.dialer.swipetocall.SwipeDirections;


public class MainActivity extends Activity implements SwipeActionAdapter.SwipeActionListener {

		private ListView listview = null;
		private GridView gridView;
		private TextView grid_num_tv;
		private EditText phone_num_edt;
		//private Button btn_paste;
		private RelativeLayout gridView_collapse_rl, dialPad_person_rl, dialPad_num_edt_rl, home_call_rl;
		private ImageView dialPad_collapse_iv, backspace_iv;
		private Cursor managedCursor;
		//private Spinner spinner;
		private ArrayList<String> number = new ArrayList<String>();
		static final String[] GRID_NUM = new String[] {
			"1", "2", "3", 
			"4", "5", "6", 
			"7" ,"8", "9",
			"*", "0", "#"};
		static final String[] GRID_LETTERS = new String[] {
			" ", "abc", "def", 
			"ghi", "jkl", "mno", 
			"pqrs" ,"tuv", "wxyz" ,
			" ", "+", " "};
		ArrayList<GridListDataModel> myGridList = new ArrayList<GridListDataModel>();

	    private String callType = null;
	    private String phoneNumber = null;
	    private String callDate = null;
	    private String callDuration = null;
	    private String callDateTime = null;
	    private String _phoneNumber;
	    private String contactName = null;
	    private String call_id = null;
	    private String edit_contactNumber;
	    
	    public Context context=null;
	    //private List<CallData> subCallLogList=new ArrayList<CallData>();
	    protected SwipeActionAdapter mAdapter;
	    protected CustomAdapter callListAdapter;
	    
	    boolean isLoading=false;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setContentView(R.layout.activity_main);
	 context=this;
	 Common.calloglist = new ArrayList<CallData>();
	  InitializeUI();
	  
	  getCallDetails();
	  
	  //InitialCalLogList();
	  
	  callListAdapter=new CustomAdapter(MainActivity.this, R.layout.list_row, Common.calloglist);
	  
	  mAdapter = new SwipeActionAdapter(callListAdapter);
     
      //setListAdapter(mAdapter);
      listview.setAdapter(mAdapter);
      
      mAdapter.setSwipeActionListener(this).setListView(listview);

      mAdapter.addBackground(SwipeDirections.DIRECTION_FAR_LEFT,R.layout.row_bg_left_far)
              .addBackground(SwipeDirections.DIRECTION_NORMAL_LEFT,R.layout.row_bg_left)
              .addBackground(SwipeDirections.DIRECTION_FAR_RIGHT,R.layout.row_bg_right_far)
              .addBackground(SwipeDirections.DIRECTION_NORMAL_RIGHT,R.layout.row_bg_right);

      
      dialPad_num_edt_rl = (RelativeLayout) findViewById(R.id.dial_num_edt_rl);
      
      gridView.setVisibility(View.VISIBLE);
      
      gridView.setAdapter(new CustomGridAdapter(this, GRID_NUM));
      
      edit_contactNumber = getIntent().getStringExtra("edit_contact");
      
      if(null != edit_contactNumber){
    	  phone_num_edt.setText(edit_contactNumber);
    	  phone_num_edt.setSelection(phone_num_edt.getText().length());
      }
      
      
      gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				grid_num_tv = (TextView) view.findViewById(R.id.grid_num_textView);
				String num = phone_num_edt.getText().toString() + grid_num_tv.getText().toString();
				
				number.add(grid_num_tv.getText().toString());
				phone_num_edt.setText(num);
				// to place the edittext cursor always right side of the entered number
				phone_num_edt.setSelection(phone_num_edt.getText().length());
			}
		});
		
		gridView_collapse_rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// dont forget to put android:animateLayoutChanges="true" in your xml container layout
				if (gridView.getVisibility() == View.VISIBLE) {
					
					gridView.setVisibility(View.GONE);
					dialPad_collapse_iv.setImageResource(R.drawable.ic_action_collapse);
					
              }else{
              	
            	  	gridView.setVisibility(View.VISIBLE);
            	  	dialPad_collapse_iv.setImageResource(R.drawable.ic_action_expand);
              }
				
			}
		});
		
		phone_num_edt.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				//hideSoftKeyboard();
				if(gridView.getVisibility() != View.VISIBLE){
					
					gridView.setVisibility(View.VISIBLE);
            	  	dialPad_collapse_iv.setImageResource(R.drawable.ic_action_expand);
				}
				return true;
			}
		});
		
//		phone_num_edt.setOnLongClickListener(new OnLongClickListener() {
//			
//			@Override
//			public boolean onLongClick(View v) {
//				// TODO Auto-generated method stub
//				if(Common.clipDataValue.length() != 0){
//					
//					btn_paste.setVisibility(View.VISIBLE);
//				}
//				return false;
//			}
//		});
		
//		btn_paste.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//				phone_num_edt.setText(Common.clipDataValue);
//				btn_paste.setVisibility(View.GONE);
//			}
//		});
		
		backspace_iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(0 != phone_num_edt.getText().length()){
					// To delete the last character in edittext
					phone_num_edt.getText().delete(phone_num_edt.getText().length() - 1,
							phone_num_edt.getText().length());
				}
				
			}
		});
		
		backspace_iv.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				phone_num_edt.setText("");
				return true;
			}
		});
		
		/**
		 * This listener is used to hide the keypad when user scrollup the listview
		 */
		listview.setOnScrollListener(new OnScrollListener() {
			
			private int mLastFirstVisibleItem;
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				final ListView lw = (ListView) view.findViewById(R.id.listView_calldata);

			       if(scrollState == 0) 
			      Log.i("a", "scrolling stopped...");


			        if (view.getId() == lw.getId()) {
			        final int currentFirstVisibleItem = lw.getFirstVisiblePosition();
			         if (currentFirstVisibleItem > mLastFirstVisibleItem) {
			            //mIsScrollingUp = false;
			            Log.i("a", "scrolling down...");
			            
			            if (gridView.getVisibility() == View.VISIBLE) {
							
							gridView.setVisibility(View.GONE);
							dialPad_collapse_iv.setImageResource(R.drawable.ic_action_collapse);
			            }
			            
 
			        } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
			            //mIsScrollingUp = true;
			            Log.i("a", "scrolling up...");
			            
		              
			        }

			        mLastFirstVisibleItem = currentFirstVisibleItem;
			    } 
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
			     int lastIndexInScreen = visibleItemCount + firstVisibleItem;

			     if (lastIndexInScreen>= totalItemCount && !isLoading) {
			     // It is time to load more items
				     isLoading = true;
				     //loadMoreCallLogList();
			      }
			}
		});
		
		home_call_rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(phone_num_edt.getText().length() != 0){
					Intent callIntent = new Intent(Intent.ACTION_CALL);
				    callIntent.setData(Uri.parse("tel:" + phone_num_edt.getText().toString()));
				    startActivity(callIntent);
				}else{
					phone_num_edt.setText(Common.calloglist.get(0).getCallnumber());
					phone_num_edt.setSelection(phone_num_edt.getText().length());
				}
			}
		});
		
		dialPad_person_rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	             
				Intent contactsIntent = new Intent(MainActivity.this, ContactsListActivity.class);
				startActivity(contactsIntent);
			}
		});
		
		/**
         * Enabling Search Filter
         * */
		// Capture Text in EditText
		phone_num_edt.addTextChangedListener(new TextWatcher() {
 
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = phone_num_edt.getText().toString().toLowerCase(Locale.getDefault());
                callListAdapter.filter(text);
            }
 
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                    int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
 
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
                // TODO Auto-generated method stub
            }
        });
		
	}
	

//	protected void loadMoreCallLogList() {
//		// TODO Auto-generated method stub
//		 if(subCallLogList.size() <= Common.calloglist.size()){
//			 
//			 subCallLogList = new ArrayList<CallData>();
//			 subCallLogList.addAll(Common.calloglist.subList(subCallLogList.size(), Common.calloglist.size()));
//			 callListAdapter.notifyDataSetChanged();
//		 }
//	}
//
//
//	private void InitialCalLogList() {
//		// TODO Auto-generated method stub
//		
//		if(Common.calloglist.size() >= 10){
//			subCallLogList=new ArrayList<CallData>();
//			subCallLogList.addAll(Common.calloglist.subList(0, 10));
//			Log.i("InitialCallLogList", "Call log size is grater than 9: "+Common.calloglist.size());
//		}else{
//			subCallLogList.addAll(Common.calloglist);
//			Log.i("InitialCallLogList", "Call log size is less than 10: "+Common.calloglist.size());
//		}
//		
//	}


	private void InitializeUI() {
		// TODO Auto-generated method stub
		 listview=(ListView)findViewById(R.id.listView_calldata);
		 gridView = (GridView) findViewById(R.id.gridView1);
	     phone_num_edt = (EditText) findViewById(R.id.dial_pad_num_editText);
	     phone_num_edt.setGravity(Gravity.BOTTOM);
	     gridView_collapse_rl = (RelativeLayout) findViewById(R.id.dial_close_rl);
	     dialPad_person_rl = (RelativeLayout) findViewById(R.id.dial_person_rl);
	     dialPad_collapse_iv = (ImageView) findViewById(R.id.dialpad_collapse_imageView);
	     backspace_iv = (ImageView) findViewById(R.id.backspace_imageView);
	     home_call_rl = (RelativeLayout) findViewById(R.id.home_call_rl);
	     //btn_paste = (Button) findViewById(R.id.paste_button);
	     //btn_paste.setVisibility(View.GONE);
	     //spinner = (Spinner) findViewById(R.id.spinner1);
	}

	
	/**
	  * This method is used to get the call history from the calllog and store that data into a collection 	
	  */
	 public void getCallDetails()
	    {
		 
		 //Common.calloglist = new ArrayList<CallData>();
		 
	        @SuppressWarnings("deprecation")
	        String sortOrder = String.format("%s limit 100 ", CallLog.Calls.DATE + " DESC");
	        managedCursor = managedQuery( CallLog.Calls.CONTENT_URI, null, null, null, sortOrder);
	        int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER );
	        int type = managedCursor.getColumnIndex( CallLog.Calls.TYPE );
	        int date = managedCursor.getColumnIndex( CallLog.Calls.DATE);
	        int duration = managedCursor.getColumnIndex( CallLog.Calls.DURATION);
	        int id = managedCursor.getColumnIndex(CallLog.Calls._ID);
	        
	        try{
	        
		        while (managedCursor.moveToNext())
		        {
		          
		            phoneNumber = managedCursor.getString(number);
		            callType = managedCursor.getString(type);
		            callDate = managedCursor.getString(date);
		            contactName = getContactname(phoneNumber);  
		            call_id = managedCursor.getString(id);
		            //contactId = getContactId(phoneNumber);
		            
		            //callDateTime = new Date(Long.valueOf(callDate));
		            long seconds=Long.parseLong(callDate);
		            SimpleDateFormat format1 = new SimpleDateFormat("EEEE, dd MMM, hh:mm a");
		            callDateTime = format1.format(new Date(seconds));
		              
		            callDuration = managedCursor.getString(duration);
		            String convertedDuration = convertCallDuration(callDuration);  
		            String cType = null;
		              
		            int cTypeCode = Integer.parseInt(callType);
		              
		            switch(cTypeCode){
		            
		               case CallLog.Calls.OUTGOING_TYPE:
		                   cType = "OUTGOING";
		                   break;
		                  
		               case CallLog.Calls.INCOMING_TYPE:
		                   cType= "INCOMING";
		                   break;
		                  
		               case CallLog.Calls.MISSED_TYPE:
		                   cType = "MISSED";
		                   break;
		                }
		              
		            CallData calldata=new CallData(cType, phoneNumber, contactName, callDateTime, convertedDuration, call_id);
		            Common.calloglist.add(calldata);
		            
		            
		        }
	              
	        }finally{
	        	
	        	//managedCursor.close();
	        }
	        
	    }
	 
	private String convertCallDuration(String callDuration2) {
		// TODO Auto-generated method stub
		/**
		* First consider the following values:
		* 1 Minute = 60 Seconds
		* 1 Hour = 3600 Seconds ( 60 * 60 )
		* 1 Day = 86400 Second ( 24 * 3600 )
		*/
		int numberOfDays, numberOfHours = 0, numberOfMinutes = 0, numberOfSeconds = 0;
		String convertedDuration = null;
		

			try {
				numberOfDays = Integer.parseInt(callDuration2) / 86400;
				numberOfHours = (Integer.parseInt(callDuration2) % 86400 ) / 3600;
				numberOfMinutes = ((Integer.parseInt(callDuration2) % 86400 ) % 3600 ) / 60;
				numberOfSeconds = ((Integer.parseInt(callDuration2) % 86400 ) % 3600 ) % 60;
			
				} catch(NumberFormatException nfe) {
					   System.out.println("Could not parse " + nfe);
				}
				 
		if(numberOfSeconds != 0){
			
			convertedDuration = numberOfSeconds + " sec";
			
			if(numberOfMinutes != 0){
				
				convertedDuration = numberOfMinutes + " mins " + numberOfSeconds + " sec";
				
				if(numberOfHours != 0){
					
					convertedDuration = numberOfHours + " hrs " + numberOfMinutes + " mins " + numberOfSeconds + " sec";
				}
			}
		
		}else{
			convertedDuration = 0+"sec";
		}
		return convertedDuration;
	}


	/**
	  * this method is used to get the contact name by its phone number
	  * @param phoneNumber2
	  * @return contact name
	  */
	 private String getContactname(String phoneNumber2) {
		// TODO Auto-generated method stub
		 ContentResolver cr = context.getContentResolver();
		    Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
		    Cursor cursor = cr.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
		    if (cursor == null) {
		        return null;
		    }
		    String contactName = null;
		    if(cursor.moveToFirst()) {
		        contactName = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
		    }

		    if(cursor != null && !cursor.isClosed()) {
		        cursor.close();
		    }

		    return contactName;
	}

	@Override
	 public void onResume(){
	     super.onResume();
	     
	     //getCallDetails();
	     callListAdapter.notifyDataSetChanged();
	 }
	
	@Override
	 public void onPause(){
	     super.onPause();
	     
	     
	 }
	
	@Override
	 public void onStop(){
	     super.onStop();
	     
	     
	 }
	
	@Override
    public void onDestroy() {
        super.onDestroy();
        managedCursor.close();
    }
	 
	 
	 /**
	  * This Adapter os used to bind the call log collection data to listview 
	  * and giving the functionality of add comment, update comment and delete comment functionality.
	  * @author Srikanth
	  *
	  */
	 public class CustomAdapter extends ArrayAdapter<CallData> {
		 
		 //String _heading, _comm;
		 private Activity activity;
		 private List<CallData> listdata=null;
		 private LayoutInflater mInflater=null;
		 private ArrayList<CallData> arraylist;
		 private CallData objBean;
		 private int listRow;
		 
		 public CustomAdapter(Activity activity, int listRow, List<CallData> calldata) {
		  super(context, listRow, calldata);
		  
		  this.listdata=calldata;
		  this.listRow = listRow;
		  this.activity = activity;
		  mInflater = activity.getLayoutInflater();
		  this.arraylist = new ArrayList<CallData>();
		  this.arraylist.addAll(listdata);
		  
		 }
		 
		  
		public void filter(String charText) {
			// TODO Auto-generated method stub
			charText = charText.toLowerCase(Locale.getDefault());
			listdata.clear();
	        if (charText.length() == 0) {
	        	listdata.addAll(arraylist);
	        } 
	        else 
	        {
	            for (CallData ob : arraylist) 
	            {
	                if (ob.getCallnumber().toLowerCase(Locale.getDefault()).contains(charText)) 
	                {
	                	listdata.add(ob);
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
		  // holder.callduration = (TextView) convertView.findViewById(R.id.callDuration_tv);
		   //holder.heading = (TextView) convertView.findViewById(R.id.heading_tv);
		   //holder.addImage = (ImageView) convertView.findViewById(R.id.add_comment_imageView);
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
		  
		  if(calltype == "INCOMING"){
			  holder.calltype.setImageResource(R.drawable.incoming);
		  }if(calltype == "OUTGOING"){
			  holder.calltype.setImageResource(R.drawable.outgoing);
		  }if(calltype == "MISSED"){
			  holder.calltype.setImageResource(R.drawable.missed);
		  }
		   
		  if(null != contactname){
			  holder.callnumber.setText(contactname);
		  }else{
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
				Intent detailsActInt = new Intent(MainActivity.this, ContactDetailsActivity.class);
				detailsActInt.putExtra("key_contact_name", contactname);
				detailsActInt.putExtra("key_contact_number", callnumber);
				detailsActInt.putExtra("key_contact_call_id", contact_call_id);
				startActivity(detailsActInt);
				//addCommentDialog(number);
			}
		});
		  
		  holder.numRelLayout.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				String number = listdata.get(position).getCallnumber();
				Intent detailsActInt = new Intent(MainActivity.this, ContactDetailsActivity.class);
				detailsActInt.putExtra("key_contact_name", contactname);
				detailsActInt.putExtra("key_contact_number", callnumber);
				detailsActInt.putExtra("key_contact_call_id", contact_call_id);
				startActivity(detailsActInt);
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
				    startActivity(callIntent);
				}
			});

		  notifyDataSetChanged();
		  return convertView;
		  
		 }
		 
	}
	 
	 private static class ViewHolder {
		 
	     public ImageView optionsImage;
		 public TextView callnumber, calldate, callduration, heading;
	     public ImageView calltype, nextImage;
	     public RelativeLayout contactOptions_rl, numRelLayout;
	 }
	 

	@Override
	public boolean hasActions(int position) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean shouldDismiss(int position, int direction) {
		// TODO Auto-generated method stub
		return direction == SwipeDirections.DIRECTION_NORMAL_LEFT;
	}

	@Override
	public void onSwipe(int[] positionList, int[] directionList) {
		// TODO Auto-generated method stub
		
		for(int i=0;i<positionList.length;i++) {
            int direction = directionList[i];
            int position = positionList[i];
            String dir = "";

            switch (direction) {
                case SwipeDirections.DIRECTION_FAR_LEFT:
                    dir = "Far left";
                    break;
                case SwipeDirections.DIRECTION_NORMAL_LEFT:
                    dir = "Left";
                    break;
                case SwipeDirections.DIRECTION_FAR_RIGHT:
                    dir = "Far right";
                    break;
                case SwipeDirections.DIRECTION_NORMAL_RIGHT:
                    dir = "Right";
                    break;
            }
            
           // Toast.makeText(this,dir + " swipe Action triggered on " + list.get(position).getCallnumber(),Toast.LENGTH_SHORT).show();
            
            if(dir == "Far left" || dir == "left"){
            	
            	sendMessageToSwipedItem(Common.calloglist.get(position).getCallnumber());
            	
            }if(dir == "Far right" || dir == "right"){
            	
            	callContactHistory(Common.calloglist.get(position).getCallnumber(), Common.calloglist.get(position).getContactName());
            }
            
            mAdapter.notifyDataSetChanged();
        }
		
	}

	private void callContactHistory(String callnumber, String contactName) {
		// TODO Auto-generated method stub
		
		Intent callContactHistoryIntent = new Intent(MainActivity.this, ContactCallHistoryActivity.class);
		callContactHistoryIntent.putExtra("history_contact_number", callnumber);
		callContactHistoryIntent.putExtra("history_contact_name", contactName);
		startActivity(callContactHistoryIntent);
		
	}

	private void sendMessageToSwipedItem(String callnumber) {
		// TODO Auto-generated method stub
		Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
		smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
		smsIntent.setType("vnd.android-dir/mms-sms");
		smsIntent.setData(Uri.parse("sms:" + callnumber)); 
		startActivity(smsIntent);
	}
 
}
