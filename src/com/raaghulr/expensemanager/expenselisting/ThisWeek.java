    

package com.raaghulr.expensemanager.expenselisting;

import com.raaghulr.expensemanager.helpers.DisplayDate;
import com.raaghulr.expensemanager.R;

public class ThisWeek extends TabLayoutListingAbstract {

	@Override
	protected boolean condition(DisplayDate mDisplayDate) {
		return mDisplayDate.isCurrentWeek();
	}
	
	@Override
	protected void setType() {
		type = R.string.sublist_thisweek;
	}

	@Override
	protected void setModifiedValues() {
		isModifiedThisYear = false;
		isModifiedThisMonth = false;
		isModifiedThisWeek = true;
		isModifiedAll = false;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!isModifiedThisWeek) {
			initListView();
		}
	}
	
}