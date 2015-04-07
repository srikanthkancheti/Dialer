package com.ielts.dialer.model;


public class CallHistoryData {
	
	
	private String callhistorytype, callhistoryname;
	 private String callhistorynumber;
	 private String callhistorydatetime;
	 private String callhistoryduration;
	 private String callhistoryId;

	  
//	 public CallHistoryData(String calltype, String callnumber, String contactName, String callDateTime, String callDuration, String call_id)
//	 {
//	  this.callhistorydatetime=callDateTime;
//	  this.callhistoryduration=callDuration;
//	  this.callhistorynumber=callnumber;
//	  this.callhistorytype=calltype;
//	  this.callhistoryname = contactName;
//	  this.callhistoryId = call_id;
//	 }
	 
//	public CallHistoryData() {
//		// TODO Auto-generated constructor stub
//	}

	public String getCall_ID(){
		 return callhistoryId;
	 }
	 
	 public void setCall_ID(String callid){
		 this.callhistoryId = callid;
	 }
	 
	 public String getCalltype() {
	  return callhistorytype;
	 }
	 
	 public void setCalltype(String calltype) {
	  this.callhistorytype = calltype;
	 }
	 
	 public String getCallnumber() {
	  return callhistorynumber;
	 }
	 
	 public void setCallnumber(String callnumber) {
	  this.callhistorynumber = callnumber;
	 }
	 
	 public String getContactName(){
		 return callhistoryname;
	 }
	 
	 public void setContactname(String conName){
		 this.callhistoryname = conName;
	 }
	 
	 public String getCalldatetime() {
	  return callhistorydatetime;
	 }
	 
	 public void setCalldatetime(String calldatetime) {
	  this.callhistorydatetime = calldatetime;
	 }
	 
	 public String getCallduration() {
	  return callhistoryduration;
	 }
	 
	 public void setCallduration(String callduration) {
	  this.callhistoryduration = callduration;
	 }

}
