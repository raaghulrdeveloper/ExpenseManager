

package com.raaghulr.expensemanager.utils;

public class Log  {
	
    private static String app = "Expense Manager";
    private static boolean DEBUG = true;
    
    public static final void d(Throwable throwable) {
    	if (DEBUG)
    		android.util.Log.d(app, "", throwable);
    }

    public static final void d(Object object) {
    	if (DEBUG)
    		android.util.Log.d(app, object!=null ? object.toString() : null);
    }

    public static final void d(Object object, Throwable throwable) {
    	if (DEBUG)
    		android.util.Log.d(app, object!=null ? object.toString() : null, throwable);
    }
}