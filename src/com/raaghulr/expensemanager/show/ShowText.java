    


package com.raaghulr.expensemanager.show;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.raaghulr.expensemanager.Constants;
import com.raaghulr.expensemanager.entry.Text;
import com.raaghulr.expensemanager.helpers.FavoriteHelper;
import com.raaghulr.expensemanager.R;

public class ShowText extends ShowAbstract {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		typeOfEntry = R.string.text;
		typeOfEntryFinished = R.string.finished_textentry;
		typeOfEntryUnfinished = R.string.unfinished_textentry;
		showHelper();
		if (intentExtras.containsKey(Constants.KEY_ENTRY_LIST_EXTRA)) {
			mFavoriteHelper = new FavoriteHelper(this,mDatabaseAdapter,fileHelper,mShowList);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (SHOW_RESULT == requestCode) {
			intentExtras = data.getExtras();
			if(Activity.RESULT_OK == resultCode) {
				doTaskOnActivityResult();
				mShowList = intentExtras.getParcelable(Constants.KEY_ENTRY_LIST_EXTRA);
				mFavoriteHelper = new FavoriteHelper(this,mDatabaseAdapter,fileHelper,mShowList);
				showDelete.setOnClickListener(this);
				showEdit.setOnClickListener(this);
			}
			if(resultCode == Activity.RESULT_CANCELED) {
				finish();
			}
		}
	}

	@Override
	protected void editAction() {
		Intent editIntent = new Intent(this, Text.class);
		editIntent.putExtras(intentExtras);
		startActivityForResult(editIntent,SHOW_RESULT);
	}
}
