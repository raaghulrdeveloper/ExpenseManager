

package com.raaghulr.expensemanager.show;

import com.raaghulr.expensemanager.R;

import android.app.Activity;
import android.widget.TextView;

public class ShowLocationHandler {

	private TextView showLocation;

	public ShowLocationHandler(Activity activity, String location) {
		showLocation = (TextView) activity.findViewById(R.id.show_location);
		showLocation.setText(location);
	}

}
