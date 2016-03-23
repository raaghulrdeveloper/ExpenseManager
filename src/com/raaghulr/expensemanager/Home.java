

package com.raaghulr.expensemanager;

import java.util.Calendar;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.raaghulr.expensemanager.entry.CameraEntry;
import com.raaghulr.expensemanager.entry.FavoriteEntry;
import com.raaghulr.expensemanager.entry.Text;
import com.raaghulr.expensemanager.entry.Voice;
import com.raaghulr.expensemanager.expenselisting.ExpenseListing;
import com.raaghulr.expensemanager.helpers.ConvertCursorToListString;
import com.raaghulr.expensemanager.helpers.DatabaseAdapter;
import com.raaghulr.expensemanager.helpers.GraphHelper;
import com.raaghulr.expensemanager.helpers.LocationHelper;
import com.raaghulr.expensemanager.helpers.UnfinishedEntryCount;
import com.raaghulr.expensemanager.models.Entry;
import com.raaghulr.expensemanager.sync.SyncHelper;
import com.raaghulr.expensemanager.R;

public class Home extends BaseActivity implements OnClickListener {
	
	private Bundle bundle;
	private GraphHelper mHandleGraph;
	private ProgressBar graphProgressBar;
	private UnfinishedEntryCount unfinishedEntryCount;
	private ConvertCursorToListString mConvertCursorToListString;
	
	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, getString(R.string.flurry_key));
		FlurryAgent.onEvent(getString(R.string.home_screen));
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return true;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		bundle = new Bundle();
		mConvertCursorToListString = new ConvertCursorToListString(this);
		////// ********* Adding Click Listeners to HomeActivity ********** /////////
		
		((Button) findViewById(R.id.home_text)).setOnClickListener(this);
		((Button) findViewById(R.id.home_voice)).setOnClickListener(this);
		((Button) findViewById(R.id.home_camera)).setOnClickListener(this);
		((Button) findViewById(R.id.home_favorite)).setOnClickListener(this);
		((Button) findViewById(R.id.home_save_reminder)).setOnClickListener(this);
		((ImageView) findViewById(R.id.home_listview)).setOnClickListener(this);
		
		ImageView mainGenerateReport = (ImageView) findViewById(R.id.home_generate_report);
		mainGenerateReport.setVisibility(View.VISIBLE);
		mainGenerateReport.setOnClickListener(this);
		
		graphProgressBar = (ProgressBar) findViewById(R.id.graph_progress_bar);
		graphProgressBar.setVisibility(View.VISIBLE);
		
		if(ExpenseManagerApplication.toSync) {
			SyncHelper.syncHelper = new SyncHelper(this);
        	SyncHelper.syncHelper.execute();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		//finding current location
		LocationHelper mLocationHelper = new LocationHelper();
		Location location = mLocationHelper.getBestAvailableLocation();
		if(location == null) {
			mLocationHelper.requestLocationUpdate();
		}
		mHandleGraph = new GraphHelper(this,graphProgressBar);
		unfinishedEntryCount = new UnfinishedEntryCount(mConvertCursorToListString.getEntryList(false,""),null,null,null,((TextView)findViewById(R.id.home_unfinished_entry_count)));
		unfinishedEntryCount.execute();
		mHandleGraph.execute();
	}
	
	@Override
	public void onClick(View clickedView) {
		boolean isMediaMounted = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if(isMediaMounted) {
			if(!ExpenseManagerApplication.isInitialized){ExpenseManagerApplication.Initialize();}
		}
		int idOfClickedView = clickedView.getId();
		cancelHandleGraphTask();
		cancelUnfinishedEntryTask();
		switch (idOfClickedView) {
			case R.id.home_text:
				Intent intentTextEntry = new Intent(this, Text.class);
				intentTextEntry.putExtras(bundle);
				startActivity(intentTextEntry);
				break;
				
			case R.id.home_voice:
				if (isMediaMounted) {
					Intent intentVoice = new Intent(this, Voice.class);
					intentVoice.putExtras(bundle);
					startActivity(intentVoice);
				} else {
					Toast.makeText(this, "sdcard not available", Toast.LENGTH_SHORT).show();
				}
				break;
	
			case R.id.home_camera:
				if (isMediaMounted) {
					Intent intentCamera = new Intent(this, CameraEntry.class);
					intentCamera.putExtras(bundle);
					startActivity(intentCamera);
				} else {
					Toast.makeText(this, "sdcard not available", Toast.LENGTH_SHORT).show();
				}
				break;
				
			case R.id.home_favorite:
				Intent intentFavorite = new Intent(this, FavoriteEntry.class);
				intentFavorite.putExtras(bundle);
				startActivity(intentFavorite);	
				break;
				
			case R.id.home_save_reminder:
				FlurryAgent.onEvent(getString(R.string.save_reminder));
				insertToDatabase(R.string.unknown);
				Intent intentListView = new Intent(this, ExpenseListing.class);
				startActivity(intentListView);
				SyncHelper.startSync();
				break;
			
			case R.id.home_listview:
				Intent intentListView2 = new Intent(this, ExpenseListing.class);
				startActivity(intentListView2);
				break;
				
			case R.id.home_generate_report:
				startGenerateReportActivity();
				break;
		}//end switch
	}//end onClick
	
//	private void createDatabaseEntry(int typeOfEntry) {
//		bundle.putLong(Constants.KEY_ID, Long.parseLong(insertToDatabase(typeOfEntry).toString()));
//		
//		if(LocationHelper.currentAddress != null && !LocationHelper.currentAddress.trim().equals("")) {
//			bundle.putBoolean(Constants.KEY_SET_LOCATION, false);
//		} else {
//			bundle.putBoolean(Constants.KEY_SET_LOCATION, true);
//		}
//	}

	///////// ******** function to mark entry into the database and returns the id of the new entry ***** //////
	private Long insertToDatabase(int type) {
		Entry list = new Entry();
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.setFirstDayOfWeek(Calendar.MONDAY);
		
		list.timeInMillis = mCalendar.getTimeInMillis();

		if (LocationHelper.currentAddress != null && !LocationHelper.currentAddress.trim().equals("")) {
			list.location = LocationHelper.currentAddress;
		}
		list.type = getString(type);
		DatabaseAdapter mDatabaseAdapter = new DatabaseAdapter(this);
		mDatabaseAdapter.open();
		long id = mDatabaseAdapter.insertToEntryTable(list);
		mDatabaseAdapter.close();
		return id;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		cancelHandleGraphTask();
		cancelUnfinishedEntryTask();
	}
	
	private void cancelUnfinishedEntryTask() {
		if(unfinishedEntryCount != null && !unfinishedEntryCount.isCancelled()) {
			unfinishedEntryCount.cancel(true);
		}
	}
	
	private void cancelHandleGraphTask() {
		if(mHandleGraph != null && !mHandleGraph.isCancelled()) {
			mHandleGraph.cancel(true);
		}
	}
	
}