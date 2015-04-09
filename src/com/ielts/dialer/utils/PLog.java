package com.ielts.dialer.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class PLog {

	private PLog() {}

	private static final boolean DEBUG_FLAG = true;
	private static final boolean DEBUG = DEBUG_FLAG;
	private static final boolean ERROR = DEBUG_FLAG;
	private static final boolean WARN = DEBUG_FLAG;
	private static final boolean INFO = DEBUG_FLAG;
	private static final boolean VERB = DEBUG_FLAG;
	private static StringBuilder errorReport;

	public static void e(final String clazz, final Throwable ex) {
		if (ERROR) {
			errorReport = new StringBuilder();
			if (null != ex) {
				android.util.Log.e(clazz, ex.getMessage());
				android.util.Log.e(clazz, ex.getClass().getName());
				for (final StackTraceElement element : ex.getStackTrace()) {
					android.util.Log.e(clazz, "\t" + element.toString());
				}
				StringWriter stackTrace = new StringWriter();
				ex.printStackTrace(new PrintWriter(stackTrace));
				errorReport.append(stackTrace.toString());
				errorLoginDB(errorReport.toString());
			} else {
				errorLoginDB("null pointer exception");
			}
		}
	}

	public static int errorLoginDB(String errorMessage) {
		int state = 1;
		if (errorMessage != null) {
			errorMessage = errorMessage.replaceAll("'", "");
		}
		try {
//			InsertDataIntoDB.insertErrorLog("insert into log4net(date,thread,level,logger,message,exception,userid,serverUpdateStatus,exceptionat)values('" + UtilDeclarations.getDateTime() + "','','','','EXCEPTION','" + errorMessage + "','" + ConstantsPMC.getInstance().getDeviceID() + "','false','')");
		} catch (Exception e) {
		}
		return state;
	}

	public static void v(final String clazz, final String msg) {
		if (VERB) {
			android.util.Log.v(clazz, "" + msg);
		}
	}

	public static void i(final String clazz, final String msg) {
		if (INFO) {
			android.util.Log.i(clazz, "" + msg);
		}
	}

	public static void w(final String clazz, final String msg) {
		if (WARN) {
			android.util.Log.w(clazz, "" + msg);
		}
	}

	public static void e(final String clazz, final String msg) {
		if (ERROR) {
			android.util.Log.e(clazz, "" + msg);
		}
	}

	public static void d(final String clazz, final String msg) {
		if (DEBUG) {
			android.util.Log.d(clazz, "" + msg);
		}
	}
}