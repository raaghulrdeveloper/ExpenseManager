    

package com.raaghulr.expensemanager.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.raaghulr.expensemanager.ExpenseManagerApplication;
import com.raaghulr.expensemanager.models.User;
import com.raaghulr.expensemanager.R;

public class SharedPreferencesHelper {

	private static Context context = ExpenseManagerApplication.getContext();
	
	public static SharedPreferences getSharedPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public static void setDeletePrefs(Boolean isChecked) {
		SharedPreferences sharedPreferences = getSharedPreferences();
		Editor prefEditor = sharedPreferences.edit();
		prefEditor.putBoolean(context.getString(R.string.pref_key_delete_dialog), isChecked);
		prefEditor.commit();	
	}
	
	public static void setFlashPrefs(int resId) {
		SharedPreferences sharedPreferences = getSharedPreferences();
		Editor prefEditor = sharedPreferences.edit();
		prefEditor.putInt(context.getString(R.string.pref_key_flash_res_id), resId);
		prefEditor.commit();	
	}
	
	public static void setBooleanPrefs(int keyId, boolean isFirstTime) {
		SharedPreferences sharedPreferences = getSharedPreferences();
		Editor prefEditor = sharedPreferences.edit();
		prefEditor.putBoolean(context.getString(keyId), isFirstTime);
		prefEditor.commit();	
	}
	
	public static void setUserDetails(User user) {
		SharedPreferences sharedPreferences = getSharedPreferences();
		Editor prefEditor = sharedPreferences.edit();
		prefEditor.putString(context.getString(R.string.pref_key_sync_user_id), user.idFromServer);
		prefEditor.putString(context.getString(R.string.pref_key_token), user.token);
		prefEditor.putString(context.getString(R.string.pref_key_sync_name), user.name);
		prefEditor.putString(context.getString(R.string.pref_key_sync_email), user.email);
		prefEditor.commit();	
	}
	
	public static void setSyncTimeStamp(String timestamp) {
		SharedPreferences sharedPreferences = getSharedPreferences();
		Editor prefEditor = sharedPreferences.edit();
		prefEditor.putString(context.getString(R.string.pref_key_sync_timestamp), timestamp);
		prefEditor.commit();	
	}
	
}
